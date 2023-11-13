package com.evereading.best.dto;

import java.time.LocalDateTime;

import com.evereading.best.entity.FreeReply;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FreeReplyDTO {
    // reply frno
    private Long frno;
    // free fno
    private Long boardId;

    private String email;

    private String nickname;
    
    private String content;
    
    private LocalDateTime regdate;
}
