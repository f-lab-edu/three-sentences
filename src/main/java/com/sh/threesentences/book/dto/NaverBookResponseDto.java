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

    /**
     * 전체 검색 갯수
     */
    private int total;

    /**
     * 검색 시작 위치
     */
    private int start;

    /**
     * 한 번에 표시할 검색 결과 갯수
     */
    private int display;

    private List<Book> items;

}
