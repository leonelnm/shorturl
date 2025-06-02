package com.codigo04.shorturl.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ShortUrlRequest(

        @Size(max = 255)
        String name,

        @Size(max = 100)
        String prefix,

        @NotBlank(message = "original.url.mandatory")
        @Size(max = 2048)
        String originalUrl

) {}

