package com.sparta.springusersetting.domain.boards.dto.responseDto;

import com.sparta.springusersetting.domain.boards.entity.Boards;
import lombok.Getter;

@Getter
public class BoardsDetailResponseDto {

    private final Long id;
    private final String title;
    private final String contents;
    private final String images;

    public BoardsDetailResponseDto(Boards boards) {
        this.id = boards.getId();
        this.title = boards.getTitle();
        this.contents = boards.getContent();
        this.images = boards.getImage();
    }
}
