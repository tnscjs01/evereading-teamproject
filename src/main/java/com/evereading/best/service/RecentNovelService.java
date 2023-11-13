package com.evereading.best.service;

import java.util.List;

import com.evereading.best.dto.BookmarkDTO;
import com.evereading.best.dto.PageRequestDTO;
import com.evereading.best.dto.PageResultDTO;
import com.evereading.best.dto.RecentNovelDTO;
import com.evereading.best.entity.Member;
import com.evereading.best.entity.Novel;
import com.evereading.best.entity.RecentNovel;

public interface RecentNovelService {

    //최근 본 작품 목록
    // List<RecentNovelDTO> getRecentList(String email);
    PageResultDTO<RecentNovelDTO, RecentNovel> getRecentList(String email, PageRequestDTO requestDTO);
    //최근 작품 삭제
    void removeRecent(Long nno);

    //최근작품 생성
    Long addRecent(RecentNovelDTO recentNovelDTO);

    //최근본작품 정보
    RecentNovelDTO findRecent(String email, Long nno);

    //최근 본 회차 타이틀
    String findLastEpisodeTitle(Long eno);

  
    default RecentNovelDTO entityToDto(RecentNovel entity){
        RecentNovelDTO dto = RecentNovelDTO.builder()
                        .rnno(entity.getRnno())
                        .lastEpisode(entity.getLastEpisode())
                        .lastEno(entity.getLastEno())
                        .nno(entity.getNovel().getNno())
                        .title(entity.getNovel().getTitle())
                        .thumbnail(entity.getNovel().getThumbnail())
                        .nickname(entity.getNovel().getMember().getNickname())
                        .email(entity.getMember().getEmail())
                        .date(entity.getDate())
                        .build();
        return dto;
    }


    //임시로 a member에게 저장됨
    default RecentNovel dtoToEntity(RecentNovelDTO dto){
        RecentNovel entity = RecentNovel.builder()
                        .rnno(dto.getRnno())
                        .lastEpisode(dto.getLastEpisode())
                        .lastEno(dto.getLastEno())
                        .novel(Novel.builder().nno(dto.getNno()).build())
                        .member(Member.builder().email(dto.getEmail()).build())
                        .date(dto.getDate())
                        .build();
        return entity;
    }
}
