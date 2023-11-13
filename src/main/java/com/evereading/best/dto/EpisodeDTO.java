package com.evereading.best.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EpisodeDTO {
    private Long eno;
    private String title;
    private String content;
    private Long count;
    private String note;
    private LocalDateTime regdate;

    //각 소설별 회차 번호...
    private Long episodeNum;
    private Long replyCount; //댓글 개수

    //novel
    private Long nno;

    //member
    private String email;
    
}
