package com.codigo04.shorturl.repository;

import com.codigo04.shorturl.model.ShortUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShortUrlRepository extends JpaRepository<ShortUrl, Long> {

    boolean existsByPrefixIsNullAndName(String name);
    boolean existsByPrefixAndName(String prefix, String name);
    Optional<ShortUrl> findByPrefixIsNullAndName(String name);
    Optional<ShortUrl> findByPrefixAndName(String prefix, String name);

}
