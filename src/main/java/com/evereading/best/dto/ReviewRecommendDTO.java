package com.evereading.best.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewRecommendDTO {
    
    private Long memberId;
    private Long boardId;

    public ReviewRecommendDTO(Long memberId, Long boardId){
        this.memberId = memberId;
        this.boardId = boardId;
    }
}
