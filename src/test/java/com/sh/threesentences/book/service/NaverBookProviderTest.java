package com.sh.threesentences.book.service;

import static com.sh.threesentences.book.fixture.NaverBookFixture.BOOK_BY_ISBN;
import static com.sh.threesentences.book.fixture.NaverBookFixture.DUPLICATE_RESULT_BY_ISBN;
import static com.sh.threesentences.book.fixture.NaverBookFixture.NO_BOOK_BY_ISBN;
import static com.sh.threesentences.book.fixture.NaverBookFixture.RESULT_TITLE_IS_SPRING_BOOT;
import static com.sh.threesentences.book.fixture.NaverBookFixture.author;
import static com.sh.threesentences.book.fixture.NaverBookFixture.description;
import static com.sh.threesentences.book.fixture.NaverBookFixture.duplicate_book_isbn;
import static com.sh.threesentences.book.fixture.NaverBookFixture.image;
import static com.sh.threesentences.book.fixture.NaverBookFixture.invalid_isbn;
import static com.sh.threesentences.book.fixture.NaverBookFixture.isbn;
import static com.sh.threesentences.book.fixture.NaverBookFixture.pubdate;
import static com.sh.threesentences.book.fixture.NaverBookFixture.publisher;
import static com.sh.threesentences.book.fixture.NaverBookFixture.title;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import com.sh.threesentences.book.dto.Book;
import com.sh.threesentences.book.exception.BookErrorCode;
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

@RestClientTest(value = {NaverBookProvider.class})
@MockBean(JpaMetamodelMappingContext.class)
@DisplayName("* NaverBookProvider")
class NaverBookProviderTest {

    @Value("${api.naver.url}")
    private String naverApiUrl;

    @Autowired
    private NaverBookProvider naverBookProvider;

    @Autowired
    private MockRestServiceServer mockServer;

    @Nested
    @DisplayName("** searchBooksByTitle method")
    class ContextSearchBookByTitleMethod {

        @DisplayName("제목과 관련된 책 리스트를 리턴한다.")
        @Test
        void searchBookByTitle() {

            String title = "스프링부트";
            int size = 10;
            int page = 1;

            URI uri = UriComponentsBuilder
                .fromUriString(naverApiUrl)
                .queryParam("d_titl", title)
                .queryParam("display", size)
                .queryParam("start", page)
                .encode()
                .build()
                .toUri();

            mockServer
                .expect(requestTo(uri))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(RESULT_TITLE_IS_SPRING_BOOT, MediaType.APPLICATION_JSON));

            List<Book> books = naverBookProvider.searchBooksByTitle(title, size, page);

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
                .fromUriString(naverApiUrl)
                .queryParam("d_isbn", isbn)
                .encode()
                .build()
                .toUri();

            mockServer
                .expect(requestTo(uri))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(BOOK_BY_ISBN, MediaType.APPLICATION_JSON));

            Book book = naverBookProvider.findBookByISBN(isbn);

            assertThat(book.getAuthor()).isEqualTo(author);
            assertThat(book.getTitle()).isEqualTo(title);
            assertThat(book.getImage()).isEqualTo(image);
            assertThat(book.getPubdate()).isEqualTo(pubdate);
            assertThat(book.getPublisher()).isEqualTo(publisher);
            assertThat(book.getIsbn()).isEqualTo(isbn);
            assertThat(book.getDescription()).isEqualTo(description);

        }

        @DisplayName("ISBN에 대한 검색결과가 없으면 예외를 던진다.")
        @Test
        void noBookByIsbn() {

            URI uri = UriComponentsBuilder
                .fromUriString(naverApiUrl)
                .queryParam("d_isbn", invalid_isbn)
                .encode()
                .build()
                .toUri();

            mockServer
                .expect(requestTo(uri))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(NO_BOOK_BY_ISBN, MediaType.APPLICATION_JSON));

            assertThatThrownBy(() -> naverBookProvider.findBookByISBN(invalid_isbn))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining(BookErrorCode.BOOK_NOT_FOUND.getMessage());
        }

        @DisplayName("ISBN에 대한 검색결과가 2개 이상이면 예외를 던진다.")
        @Test
        void duplicateBookByIsbn() {

            URI uri = UriComponentsBuilder
                .fromUriString(naverApiUrl)
                .queryParam("d_isbn", duplicate_book_isbn)
                .encode()
                .build()
                .toUri();

            mockServer
                .expect(requestTo(uri))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(DUPLICATE_RESULT_BY_ISBN, MediaType.APPLICATION_JSON));

            assertThatThrownBy(() -> naverBookProvider.findBookByISBN(duplicate_book_isbn))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining(BookErrorCode.SEARCH_DUPLICATE_BOOK.getMessage());
        }
    }
}
