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
public class NoticeDTO {

    private Long nono;
    private String title;
    private String content;
    private LocalDateTime regdate;
    private String email;
    
}
