package com.codigo04.shorturl.integration;

import com.codigo04.shorturl.model.ShortUrl;
import com.codigo04.shorturl.repository.ShortUrlRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ShortUrlIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ShortUrlRepository repository;

    @Test
    @Order(1)
    void testShortenAndRedirect_Success() throws Exception {
        String jsonRequest = """
            {
              "name": "mytest",
              "prefix": null,
              "originalUrl": "http://example.com"
            }
        """;

        // POST para crear el short URL
        mockMvc.perform(post("/api/shorten")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("mytest"));

        // Verifica que se guardó en la base de datos
        Optional<ShortUrl> saved = repository.findByPrefixIsNullAndName("mytest");
        assertTrue(saved.isPresent());
        assertEquals("http://example.com", saved.get().getOriginalUrl());

        // GET para redirigir
        mockMvc.perform(get("/mytest"))
                .andExpect(status().isFound())
                .andExpect(header().string("Location", "http://example.com"));
    }

    @Test
    @Order(2)
    void testRedirect_NotFound() throws Exception {
        mockMvc.perform(get("/notexist"))
                .andExpect(status().isNotFound());
    }
}

