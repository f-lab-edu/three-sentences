package com.sh.threesentences.book.controller;

import com.sh.threesentences.book.dto.Book;
import com.sh.threesentences.book.service.KaKaoBookProvider;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
public class BookController {

    private final KaKaoBookProvider bookProvider;

    @GetMapping("search")
    public List<Book> searchBooksByTitle(@Param("title") String title, @Param("size") int size,
        @Param("page") int page) {
        return bookProvider.searchBooksByTitle(title, size, page);
    }

    @GetMapping()
    public Book getBooksByISBN(@Param("isbn") String isbn) {
        return bookProvider.findBookByISBN(isbn);
    }
}
