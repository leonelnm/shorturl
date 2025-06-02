package com.codigo04.shorturl.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

@Entity
@Table(name = "short_urls", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"prefix", "name"})
})
public class ShortUrl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    private String prefix;

    @NotBlank
    @Column(name = "original_url", nullable = false, length = 2048)
    private String originalUrl;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public ShortUrl() {}

    public ShortUrl(String name, String prefix, String originalUrl) {
        this.name = name;
        this.prefix = prefix;
        this.originalUrl = originalUrl;
        this.createdAt = LocalDateTime.now();
    }

    public String getFullShortUrl() {
        return prefix != null ? prefix + "/" + name : name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

}


