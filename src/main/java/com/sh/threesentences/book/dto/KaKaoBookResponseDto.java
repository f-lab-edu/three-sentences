package com.sh.threesentences.book.dto;

import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class KaKaoBookResponseDto {

    private KakaoMeta meta;

    private List<KakaoDocument> documents;

    public List<Book> toBooks() {
        return documents.stream()
            .map(b -> new Book(b.getTitle(), b.getThumbnail(), String.join(", ", b.getAuthors()),
                b.getPublisher(), null, b.getIsbn(), b.getContents()))
            .collect(Collectors.toList());
    }

}
