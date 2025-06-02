package com.codigo04.shorturl.service;

import com.codigo04.shorturl.dto.ShortUrlRequest;
import com.codigo04.shorturl.dto.ShortUrlResponse;
import com.codigo04.shorturl.exception.ShortUrlArgumentException;
import com.codigo04.shorturl.model.ShortUrl;
import com.codigo04.shorturl.repository.ShortUrlRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ShortUrlServiceImplTest {

    @Mock
    private ShortUrlRepository repository;

    @InjectMocks
    private ShortUrlServiceImpl service;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testShorten_Success_WithProvidedName() {
        ShortUrlRequest request = new ShortUrlRequest("myname", null, "http://example.com");

        when(repository.findByPrefixIsNullAndName("myname")).thenReturn(Optional.empty());
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        ResponseEntity<ShortUrlResponse> response = service.shorten(request);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("myname", response.getBody().name());
    }

    @Test
    void testShorten_Success_WithoutProvidedName() {
        ShortUrlRequest request = new ShortUrlRequest(null, null, "http://example.com");

        when(repository.findByPrefixIsNullAndName(null)).thenReturn(Optional.empty());
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        ResponseEntity<ShortUrlResponse> response = service.shorten(request);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
    }

    @Test
    void testShorten_Throws_WhenNameTaken() {
        ShortUrlRequest request = new ShortUrlRequest("myname", null, "http://example.com");
        ShortUrl existing = new ShortUrl("myname", null, "http://other.com");

        when(repository.findByPrefixIsNullAndName("myname")).thenReturn(Optional.of(existing));

        ShortUrlArgumentException ex = assertThrows(ShortUrlArgumentException.class, () -> {
            service.shorten(request);
        });

        assertEquals("name.taken", ex.getMessage());
    }

    @Test
    void testResolveOriginalUrl_Found() {
        String name = "abc123";
        ShortUrl shortUrl = new ShortUrl(name, null, "http://example.com");

        when(repository.findByPrefixIsNullAndName(name)).thenReturn(Optional.of(shortUrl));

        Optional<String> result = service.resolveOriginalUrl(null, name);

        assertTrue(result.isPresent());
        assertEquals("http://example.com", result.get());
    }

    @Test
    void testResolveOriginalUrl_NotFound() {
        when(repository.findByPrefixIsNullAndName("notfound")).thenReturn(Optional.empty());

        Optional<String> result = service.resolveOriginalUrl(null, "notfound");

        assertFalse(result.isPresent());
    }

    @Test
    void testResolveOriginalUrl_IncrementsUsageCount() {
        String name = "abc123";
        ShortUrl shortUrl = new ShortUrl(name, null, "http://example.com");

        shortUrl.setUsageCount(3);

        when(repository.findByPrefixIsNullAndName(name)).thenReturn(Optional.of(shortUrl));
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Optional<String> result = service.resolveOriginalUrl(null, name);

        assertTrue(result.isPresent());
        assertEquals("http://example.com", result.get());

        assertEquals(4, shortUrl.getUsageCount());

        verify(repository).save(shortUrl);
    }


}
