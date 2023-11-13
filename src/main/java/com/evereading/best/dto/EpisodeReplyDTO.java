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
public class EpisodeReplyDTO {
    private Long erno;
    private String content;
    
    //episode
    private Long eno;

    private LocalDateTime regdate;

    //member
    private String email;
    private String nickName;
    
}
