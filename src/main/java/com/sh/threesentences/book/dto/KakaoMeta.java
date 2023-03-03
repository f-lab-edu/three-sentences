package com.sh.threesentences.book.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter

public class KakaoMeta {

    private boolean is_end;

    private int pageable_count;

    private int total_count;

}
