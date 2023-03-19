package com.sh.threesentences.book.service;

import com.sh.threesentences.book.dto.Book;
import com.sh.threesentences.book.dto.KaKaoBookResponseDto;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@Qualifier("kakaoBookProvider")
public class KaKaoBookProvider implements BookProvider {

    @Value("${api.kakao.url}")
    private String kakaoApiUrl;

    @Value("${api.kakao.rest-api-key}")
    private String kakaoApiKey;

    @Value("${api.kakao.authorization-header-prefix}")
    private String kakaoHeaderPrefix;

    private final RestTemplate restTemplate;

    public KaKaoBookProvider(RestTemplateBuilder restTemplateBuilder) {
        restTemplate = restTemplateBuilder.build();
    }

    @Override
    public List<Book> searchBooksByTitle(String title, int size, int page) {

        HashMap<String, String> headerMap = createHeaderMap();
        HttpEntity<Void> httpEntityWithHeaders = getHttpEntityWithHeaders(headerMap);

        URI uri = UriComponentsBuilder
            .fromUriString(kakaoApiUrl)
            .queryParam("target", "title")
            .queryParam("query", title)
            .queryParam("page", page)
            .queryParam("size", size)
            .encode()
            .build()
            .toUri();

        KaKaoBookResponseDto kakaoBookResponseDto = getBooks(uri, httpEntityWithHeaders, KaKaoBookResponseDto.class,
            restTemplate);

        return kakaoBookResponseDto.toBooks();
    }

    @Override
    public Book findBookByISBN(String isbn) {

        HashMap<String, String> headerMap = createHeaderMap();
        HttpEntity<Void> httpEntityWithHeaders = getHttpEntityWithHeaders(headerMap);

        URI uri = UriComponentsBuilder
            .fromUriString(kakaoApiUrl)
            .queryParam("target", "isbn")
            .queryParam("query", isbn)
            .encode()
            .build()
            .toUri();

        KaKaoBookResponseDto kakaoBookResponseDto = getBooks(uri, httpEntityWithHeaders, KaKaoBookResponseDto.class,
            restTemplate);

        List<Book> books = kakaoBookResponseDto.toBooks();
        validateResultByIsbn(books);

        return books.get(0);
    }

    @Override
    public HashMap<String, String> createHeaderMap() {
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", kakaoHeaderPrefix + " " + kakaoApiKey);
        return headerMap;
    }

}
