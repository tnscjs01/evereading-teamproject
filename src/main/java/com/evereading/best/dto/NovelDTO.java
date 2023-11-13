package com.evereading.best.dto;

import java.net.URLEncoder;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NovelDTO {

    private Long nno;
    private String title;
    private String content;
    private String thumbnail;
    private String category;
    private boolean completed;
    private LocalDateTime regdate;
    private String nickname;
    private String email;
    private Long totalCount = 0L;
    private Long monthlyCount = 0L;
    private Long weeklyCount = 0L;
    private Long dailyCount = 0L;
    public LocalDateTime mostRecentReg;

    public String getImageUrl(){
        try {
            return URLEncoder.encode(thumbnail,"UTF-8");
        } catch(Exception e){
            e.printStackTrace();
        }
        return "";
    }
       
}
