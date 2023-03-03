package com.sh.threesentences.book.service;

import com.sh.threesentences.book.dto.Book;
import java.util.List;

public interface BookProvider {

    List<Book> searchBooksByTitle(String title, int size, int page);

    Book findBookByISBN(String isbn);
}
