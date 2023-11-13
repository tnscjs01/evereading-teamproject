package com.evereading.best.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class QuestionDTO {

    private Long qno;
    private String title;
    private String content;
    private String answer;
    private LocalDateTime regdate, ansdate;
    private String nickname;
    private String email;
    
}
