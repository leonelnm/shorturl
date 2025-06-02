package com.codigo04.shorturl.service;

import com.codigo04.shorturl.dto.ShortUrlRequest;
import com.codigo04.shorturl.dto.ShortUrlResponse;
import com.codigo04.shorturl.exception.ShortUrlArgumentException;
import com.codigo04.shorturl.model.ShortUrl;
import com.codigo04.shorturl.repository.ShortUrlRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ShortUrlServiceImpl implements ShortUrlService {
    private static final int LENGTH_SHORT_URL = 7;
    private final ShortUrlRepository repository;

    public ShortUrlServiceImpl(ShortUrlRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public ResponseEntity<ShortUrlResponse> shorten(ShortUrlRequest request) {
        String name = request.name();
        String prefix = request.prefix();

        Optional<ShortUrl> shortUrlFound = findByPrefixNullableAndName(prefix, name);

        if (shortUrlFound.isPresent()) {
            String codeError = name.equals(shortUrlFound.get().getName()) ? "name.taken" : "prefix.taken";
            throw new ShortUrlArgumentException(codeError);
        }

        ShortUrl saved = repository.save(composeNewShorUrl(request));

        return ResponseEntity.ok(new ShortUrlResponse(saved));
    }

    @Override
    public Optional<String> resolveOriginalUrl(String prefix, String name) {
        return findByPrefixNullableAndName(prefix, name)
                .map(shortUrl -> {
                    shortUrl.incrementUsageCount();
                    repository.save(shortUrl);
                    return shortUrl.getOriginalUrl();
                });

    }

    private ShortUrl composeNewShorUrl(ShortUrlRequest request) {
        ShortUrl shortUrl = new ShortUrl(request.name(), request.prefix(), request.originalUrl());
        if (shortUrl.getName() == null || shortUrl.getName().isBlank()) {
            shortUrl.setName(getUniqueRandomName());
        }
        return shortUrl;
    }

    private Optional<ShortUrl> findByPrefixNullableAndName(String prefix, String name) {
        return (prefix == null || prefix.isBlank())
                ? repository.findByPrefixIsNullAndName(name)
                : repository.findByPrefixAndName(prefix, name);
    }

    private String getUniqueRandomName() {
        String name = Base62Shortener.generateRandomBase62(LENGTH_SHORT_URL);
        boolean unique;
        do {
            unique = repository.findByPrefixAndName(null, name).isEmpty();
        } while (!unique);

        return name;
    }


}
