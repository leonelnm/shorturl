package com.codigo04.shorturl.controller;

import com.codigo04.shorturl.dto.ShortUrlRequest;
import com.codigo04.shorturl.dto.ShortUrlResponse;
import com.codigo04.shorturl.model.ShortUrl;
import com.codigo04.shorturl.service.ShortUrlService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ShortUrlController.class)
@Import(ShortUrlControllerTest.TestConfig.class)
public class ShortUrlControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ShortUrlService shortUrlService;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public ShortUrlService shortUrlService() {
            return Mockito.mock(ShortUrlService.class);
        }
    }

    @Test
    void testCreateShortUrl_Success() throws Exception {
        ShortUrlRequest request = new ShortUrlRequest("myname", null, "http://example.com");
        ShortUrl shortUrl = new ShortUrl("myname", null, "http://example.com");
        ShortUrlResponse response = new ShortUrlResponse(shortUrl);

        when(shortUrlService.shorten(request)).thenReturn(ResponseEntity.ok(response));

        String jsonRequest = """
            {
              "name": "myname",
              "originalUrl": "http://example.com"
            }
        """;

        mockMvc.perform(post("/api/shorten")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("myname"));
    }

    @Test
    void testRedirectWithPrefix_Found() throws Exception {
        String prefix = "abc";
        String name = "123";
        String originalUrl = "http://example.com";

        when(shortUrlService.resolveOriginalUrl(prefix, name))
                .thenReturn(Optional.of(originalUrl));

        mockMvc.perform(get("/" + prefix + "/" + name))
                .andExpect(status().isFound())
                .andExpect(header().string("Location", originalUrl));
    }

    @Test
    void testRedirectWithPrefix_NotFound() throws Exception {
        when(shortUrlService.resolveOriginalUrl("abc", "notfound"))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/abc/notfound"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testRedirectUrl_Found() throws Exception {
        String name = "short123";
        String originalUrl = "http://example.com";

        when(shortUrlService.resolveOriginalUrl(null, name))
                .thenReturn(Optional.of(originalUrl));

        mockMvc.perform(get("/" + name))
                .andExpect(status().isFound())
                .andExpect(header().string("Location", originalUrl));
    }

    @Test
    void testRedirectUrl_NotFound() throws Exception {
        when(shortUrlService.resolveOriginalUrl(null, "notfound"))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/notfound"))
                .andExpect(status().isNotFound());
    }

}
