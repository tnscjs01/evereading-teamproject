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
public class EventDTO {

    private Long evno;
    private String title;
    private String content;
    private LocalDateTime regdate;
    private String carouselImg;
    private String eventImg;
    private String email; 
       
}
