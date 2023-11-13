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
public class RecentNovelDTO {
    private Long rnno;
    private LocalDateTime date;

    private Long lastEpisode;
    private Long lastEno;
    
    private Long nno;
    private String title;
    private String thumbnail;

    private String nickname; // member로 찾으면 안되고 novel로 찾아들어가야됨...

    private String email;

    public String getImage(){
        try{
            return URLEncoder.encode(thumbnail,"UTF-8");
        } catch(Exception e){
            e.printStackTrace();
        }
        return "";
    }
}
