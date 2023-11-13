package com.evereading.best.dto;

import java.time.LocalDateTime;

import com.evereading.best.entity.ReviewReply;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewReplyDTO {

    private Long rrno;

    private Long boardId;

    private String email;

    private String nickname;

    private String content;
    
    private LocalDateTime regdate;
}
