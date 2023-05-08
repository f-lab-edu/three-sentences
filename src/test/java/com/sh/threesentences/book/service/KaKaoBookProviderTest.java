package com.sh.threesentences.book.service;

import static com.sh.threesentences.book.fixture.KakaoBookFixture.RESULT_TITLE_IS_REACT;
import static com.sh.threesentences.book.fixture.KakaoBookFixture.author;
import static com.sh.threesentences.book.fixture.KakaoBookFixture.duplicate_book_isbn;
import static com.sh.threesentences.book.fixture.KakaoBookFixture.invalid_isbn;
import static com.sh.threesentences.book.fixture.KakaoBookFixture.isbn;
import static com.sh.threesentences.book.fixture.KakaoBookFixture.publisher;
import static com.sh.threesentences.book.fixture.KakaoBookFixture.thumbnail;
import static com.sh.threesentences.book.fixture.KakaoBookFixture.title;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import com.sh.threesentences.book.dto.Book;
import com.sh.threesentences.book.exception.BookErrorCode;
import com.sh.threesentences.book.fixture.KakaoBookFixture;
import java.net.URI;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.util.UriComponentsBuilder;

@RestClientTest(value = {KaKaoBookProvider.class})
@MockBean(JpaMetamodelMappingContext.class)
@DisplayName("* KakaoBookProvider")
class KaKaoBookProviderTest {

    @Value("${api.kakao.url}")
    private String kakaoApiUrl;

    @Autowired
    private KaKaoBookProvider kaKaoBookProvider;

    @Autowired
    private MockRestServiceServer mockServer;

    @Nested
    @DisplayName("** searchBooksByTitle method")
    class ContextSearchBookByTitleMethod {

        @DisplayName("제목과 관련된 책 리스트를 리턴한다.")
        @Test
        void searchBookByTitle() {

            String title = "리액트";
            int size = 10;
            int page = 1;

            URI uri = UriComponentsBuilder
                .fromUriString(kakaoApiUrl)
                .queryParam("target", "title")
                .queryParam("query", title)
                .queryParam("page", page)
                .queryParam("size", size)
                .encode()
                .build()
                .toUri();

            mockServer
                .expect(requestTo(uri))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(RESULT_TITLE_IS_REACT, MediaType.APPLICATION_JSON));

            List<Book> books = kaKaoBookProvider.searchBooksByTitle(title, size, page);

            assertThat(books).hasSizeLessThanOrEqualTo(size);

        }
    }

    @Nested
    @DisplayName("** findBookByISBN method")
    class ContextFindBookByIsbnMethod {

        @DisplayName("ISBN으로 책을 검색한다.")
        @Test
        void searchBookByIsbn() {

            URI uri = UriComponentsBuilder
                .fromUriString(kakaoApiUrl)
                .queryParam("target", "isbn")
                .queryParam("query", isbn)
                .encode()
                .build()
                .toUri();

            mockServer
                .expect(requestTo(uri))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(KakaoBookFixture.BOOK_BY_ISBN, MediaType.APPLICATION_JSON));

            Book book = kaKaoBookProvider.findBookByISBN(isbn);

            assertThat(book.getAuthor()).isEqualTo(author);
            assertThat(book.getTitle()).isEqualTo(title);
            assertThat(book.getImage()).isEqualTo(thumbnail);
            assertThat(book.getPublisher()).isEqualTo(publisher);
            assertThat(book.getIsbn()).isEqualTo(isbn);

        }

        @DisplayName("ISBN에 대한 검색결과가 없으면 예외를 던진다.")
        @Test
        void noBookByIsbn() {

            URI uri = UriComponentsBuilder
                .fromUriString(kakaoApiUrl)
                .queryParam("target", "isbn")
                .queryParam("query", invalid_isbn)
                .encode()
                .build()
                .toUri();

            mockServer
                .expect(requestTo(uri))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(KakaoBookFixture.NO_BOOK_BY_ISBN, MediaType.APPLICATION_JSON));

            assertThatThrownBy(() -> kaKaoBookProvider.findBookByISBN(invalid_isbn))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining(BookErrorCode.BOOK_NOT_FOUND.getMessage());
        }

        @DisplayName("ISBN에 대한 검색결과가 2개 이상이면 예외를 던진다.")
        @Test
        void duplicateBookByIsbn() {

            URI uri = UriComponentsBuilder
                .fromUriString(kakaoApiUrl)
                .queryParam("target", "isbn")
                .queryParam("query", duplicate_book_isbn)
                .encode()
                .build()
                .toUri();

            mockServer
                .expect(requestTo(uri))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(KakaoBookFixture.DUPLICATE_RESULT_BY_ISBN, MediaType.APPLICATION_JSON));

            assertThatThrownBy(() -> kaKaoBookProvider.findBookByISBN(duplicate_book_isbn))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining(BookErrorCode.SEARCH_DUPLICATE_BOOK.getMessage());
        }
    }
}
