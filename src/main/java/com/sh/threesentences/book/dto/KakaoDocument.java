package com.sh.threesentences.book.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class KakaoDocument {

    private String[] authors;

    private String contents;

    private String datetime;

    private String isbn;

    private int price;

    private String publisher;

    private int sale_price;

    private String status;

    private String thumbnail;

    private String title;

    private String[] translators;

    private String url;

}
