package com.evereading.best.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FreeRecommendDTO {
    
    private Long memberId;
    private Long boardId;

    public FreeRecommendDTO(Long memberId, Long boardId){
        this.memberId = memberId;
        this.boardId = boardId;
    }
}
