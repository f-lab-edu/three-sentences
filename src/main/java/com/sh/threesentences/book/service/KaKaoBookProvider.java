package com.sh.threesentences.book.service;

import static com.sh.threesentences.book.exception.BookErrorCode.BOOK_NOT_FOUND;
import static com.sh.threesentences.book.exception.BookErrorCode.SEARCH_DUPLICATE_BOOK;

import com.sh.threesentences.book.dto.Book;
import com.sh.threesentences.book.dto.KaKaoBookResponseDto;
import java.net.URI;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class KaKaoBookProvider implements BookProvider {

    @Value("${api.kakao.url}")
    private String kakaoApiUrl;

    @Value("${api.kakao.rest-api-key}")
    private String kakaoApiKey;

    @Value("${api.kakao.authorization-header-prefix}")
    private String kakaoHeaderPrefix;

    @Override
    public List<Book> searchBooksByTitle(String title, int size, int page) {

        HttpEntity<Void> httpEntityWithHeaders = getHttpEntityWithHeaders();

        URI uri = UriComponentsBuilder
            .fromUriString(kakaoApiUrl)
            .queryParam("target", "title")
            .queryParam("query", title)
            .queryParam("page", page)
            .queryParam("size", size)
            .encode()
            .build()
            .toUri();

        KaKaoBookResponseDto kakaoBookResponseDto = getBooks(uri, httpEntityWithHeaders);

        return kakaoBookResponseDto.toBooks();
    }


    @Override
    public Book findBookByISBN(String isbn) {

        HttpEntity<Void> httpEntityWithHeaders = getHttpEntityWithHeaders();

        URI uri = UriComponentsBuilder
            .fromUriString(kakaoApiUrl)
            .queryParam("target", "isbn")
            .queryParam("query", isbn)
            .encode()
            .build()
            .toUri();

        KaKaoBookResponseDto kakaoBookResponseDto = getBooks(uri, httpEntityWithHeaders);

        List<Book> books = kakaoBookResponseDto.toBooks();
        validateResult(books);

        return books.get(0);
    }

    private KaKaoBookResponseDto getBooks(URI uri, HttpEntity<Void> requestWithHeaders) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<KaKaoBookResponseDto> responseEntity = restTemplate.exchange(uri, HttpMethod.GET,
            requestWithHeaders, KaKaoBookResponseDto.class);

        return responseEntity.getBody();
    }

    private HttpEntity<Void> getHttpEntityWithHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", kakaoHeaderPrefix + " " + kakaoApiKey);
        return new HttpEntity<>(httpHeaders);
    }

    private void validateResult(List<Book> items) {
        if (items.isEmpty()) {
            throw new IllegalStateException(BOOK_NOT_FOUND.getMessage());
        } else if (items.size() > 1) {
            throw new IllegalStateException(SEARCH_DUPLICATE_BOOK.getMessage());
        }
    }

}
