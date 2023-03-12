package com.sh.threesentences.book.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    private String title;

    private String image;

    private String author;

    private String publisher;

    private String pubdate;

    private String isbn;

    private String description;

}
