package com.codigo04.shorturl.dto;

import com.codigo04.shorturl.model.ShortUrl;

public record ShortUrlResponse(
        Long id,
        String name,
        String prefix,
        String originalUrl,
        String fullShortUrl

) {
    public ShortUrlResponse(ShortUrl shortUrl) {
        this(
                shortUrl.getId(),
                shortUrl.getName(),
                shortUrl.getPrefix(),
                shortUrl.getOriginalUrl(),
                shortUrl.getFullShortUrl()
        );
    }
}
