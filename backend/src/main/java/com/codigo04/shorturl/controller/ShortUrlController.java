package com.codigo04.shorturl.controller;

import com.codigo04.shorturl.dto.ShortUrlRequest;
import com.codigo04.shorturl.service.ShortUrlService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
public class ShortUrlController {

    private final ShortUrlService shortUrlService;

    public ShortUrlController(ShortUrlService shortUrlService) {
        this.shortUrlService = shortUrlService;
    }

    @PostMapping("/api/shorten")
    public ResponseEntity<?> createShortUrl(@Valid @RequestBody ShortUrlRequest request) {
        return this.shortUrlService.shorten(request);
    }

    @GetMapping("/{prefix}/{name}")
    public ResponseEntity<?> redirectWithPrefix(@PathVariable String prefix, @PathVariable String name) {
        return shortUrlService.resolveOriginalUrl(prefix, name)
                .map(url -> ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).location(URI.create(url)).build())
                .orElse(ResponseEntity.<Void>notFound().build());
    }

    @GetMapping("/{name}")
    public ResponseEntity<?> redirectUrl(@PathVariable String name) {
        return shortUrlService.resolveOriginalUrl(null, name)
                .map(url -> ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).location(URI.create(url)).build())
                .orElse(ResponseEntity.<Void>notFound().build());
    }

}
