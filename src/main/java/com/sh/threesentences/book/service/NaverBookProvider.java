package com.sh.threesentences.book.service;

import com.sh.threesentences.book.dto.Book;
import com.sh.threesentences.book.dto.NaverBookResponseDto;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
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
    public List<Book> searchBooksByTitle(String title, int size, int page) {

        HashMap<String, String> headerMap = createHeaderMap();
        HttpEntity<Void> httpEntityWithHeaders = getHttpEntityWithHeaders(headerMap);

        URI uri = UriComponentsBuilder
            .fromUriString(naverApiUrl)
            .queryParam("d_titl", title)
            .queryParam("display", size)
            .queryParam("start", page)
            .encode()
            .build()
            .toUri();

        NaverBookResponseDto naverBookResponseDto = getBooks(uri, httpEntityWithHeaders, NaverBookResponseDto.class);

        return naverBookResponseDto.getItems();
    }

    @Override
    public Book findBookByISBN(String isbn) {

        HashMap<String, String> headerMap = createHeaderMap();
        HttpEntity<Void> httpEntityWithHeaders = getHttpEntityWithHeaders(headerMap);

        URI uri = UriComponentsBuilder
            .fromUriString(naverApiUrl)
            .queryParam("d_isbn", isbn)
            .encode()
            .build()
            .toUri();

        NaverBookResponseDto naverBookResponseDto = getBooks(uri, httpEntityWithHeaders, NaverBookResponseDto.class);

        List<Book> items = naverBookResponseDto.getItems();
        validateResultByIsbn(items);

        return items.get(0);
    }

    @Override
    public HashMap<String, String> createHeaderMap() {
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("X-Naver-Client-Id", naverApiId);
        headerMap.put("X-Naver-Client-Secret", naverApiSecret);
        return headerMap;
    }

}
