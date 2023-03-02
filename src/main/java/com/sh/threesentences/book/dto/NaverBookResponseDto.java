package com.sh.threesentences.book.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NaverBookResponseDto {

    private String lastBuildDate;

    private int total;

    private int start;

    private int display;

    private List<Book> items;

}
