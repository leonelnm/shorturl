package com.codigo04.shorturl.service;

import com.codigo04.shorturl.dto.ShortUrlRequest;
import com.codigo04.shorturl.dto.ShortUrlResponse;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface ShortUrlService {
    ResponseEntity<ShortUrlResponse> shorten(ShortUrlRequest request);
    Optional<String> resolveOriginalUrl(String prefix, String name);
}
