package com.evereading.best.dto;

import java.net.URLEncoder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookmarkDTO {
    private Long bno;

    //novel
    private Long nno;
    private String title;
    private String nickname;
    private String thumbnail;

    //member
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
