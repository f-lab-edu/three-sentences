package com.sh.threesentences.book.service;

import static com.sh.threesentences.book.exception.BookErrorCode.BOOK_NOT_FOUND;
import static com.sh.threesentences.book.exception.BookErrorCode.SEARCH_DUPLICATE_BOOK;

import com.sh.threesentences.book.dto.Book;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public interface BookProvider {

    /**
     * 책 제목을 이용해 도서 검색
     *
     * @param title 책 제목
     * @param size  페이징 사이즈
     * @param page  페이지 수
     * @return 검색된 책 리스트
     */
    List<Book> searchBooksByTitle(String title, int size, int page);

    /**
     * 책 ISBN 번호를 이용해 도서 검색
     *
     * @param isbn ISBN 번호
     * @return 검색된 책
     */
    Book findBookByISBN(String isbn);

    /**
     * API 요청에 필요한 헤더 세팅
     *
     * @return 헤더가 정의된 Map
     */
    HashMap<String, String> createHeaderMap();

    /**
     * ISBN으로 검색하는 경우 유효성을 검증한다.
     * <p>
     * 0개 이면 책을 찾지 못했다는 예외를 던진다. 1개 이면 정상 동작으로 간주한다. 2개 이상이면 검색 결과에 문제가 있다고 간주해 예외를 던진다.
     *
     * @param items 검색된 책 리스트
     */
    default void validateResultByIsbn(List<Book> items) {
        if (items.isEmpty()) {
            throw new IllegalStateException(BOOK_NOT_FOUND.getMessage());
        } else if (items.size() > 1) {
            throw new IllegalStateException(SEARCH_DUPLICATE_BOOK.getMessage());
        }
    }

    /**
     * Header가 세팅된 HttpEntity를 반환한다.
     *
     * @param headerMap Header가 정의된 Map
     * @return HttpEntity
     */
    default HttpEntity<Void> getHttpEntityWithHeaders(HashMap<String, String> headerMap) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAll(headerMap);
        return new HttpEntity<>(httpHeaders);
    }

    /**
     * 책을 조회한다.
     *
     * @param uri                API 요청 주소
     * @param requestWithHeaders 헤더
     * @param responseType       반환 타입
     * @param <T>                반환 타입 T
     * @return 요청 응답값
     */
    default <T> T getBooks(URI uri, HttpEntity<Void> requestWithHeaders, Class<T> responseType) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<T> responseEntity = restTemplate.exchange(uri, HttpMethod.GET,
            requestWithHeaders, responseType);

        return responseEntity.getBody();
    }
}
