package com.sh.threesentences.book.service;

import static com.sh.threesentences.book.exception.BookErrorCode.BOOK_NOT_FOUND;
import static com.sh.threesentences.book.exception.BookErrorCode.SEARCH_DUPLICATE_BOOK;

import com.sh.threesentences.book.dto.Book;
import com.sh.threesentences.book.dto.NaverBookResponseDto;
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
public class NaverBookProvider implements BookProvider {

    @Value("${api.naver.url}")
    private String naverApiUrl;

    @Value("${api.naver.id}")
    private String naverApiId;

    @Value("${api.naver.secret}")
    private String naverApiSecret;

    @Override
    public List<Book> searchBooksByTitle(String title, int display, int start) {

        HttpEntity<Void> httpEntityWithHeaders = getHttpEntityWithHeaders();

        URI uri = UriComponentsBuilder
            .fromUriString(naverApiUrl)
            .queryParam("d_titl", title)
            .queryParam("display", display)
            .queryParam("start", start)
            .encode()
            .build()
            .toUri();

        NaverBookResponseDto naverBookResponseDto = getBooks(uri, httpEntityWithHeaders);

        return naverBookResponseDto.getItems();
    }


    @Override
    public Book findBookByISBN(String isbn) {

        HttpEntity<Void> httpEntityWithHeaders = getHttpEntityWithHeaders();

        URI uri = UriComponentsBuilder
            .fromUriString(naverApiUrl)
            .queryParam("d_isbn", isbn)
            .encode()
            .build()
            .toUri();

        NaverBookResponseDto naverBookResponseDto = getBooks(uri, httpEntityWithHeaders);

        List<Book> items = naverBookResponseDto.getItems();
        validateResult(items);

        return items.get(0);
    }

    private NaverBookResponseDto getBooks(URI uri, HttpEntity<Void> requestWithHeaders) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<NaverBookResponseDto> responseEntity = restTemplate.exchange(uri, HttpMethod.GET,
            requestWithHeaders, NaverBookResponseDto.class);

        return responseEntity.getBody();
    }

    private HttpEntity<Void> getHttpEntityWithHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("X-Naver-Client-Id", naverApiId);
        httpHeaders.set("X-Naver-Client-Secret", naverApiSecret);
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
