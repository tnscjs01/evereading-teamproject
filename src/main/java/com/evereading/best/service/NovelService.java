package com.evereading.best.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

import com.evereading.best.dto.NovelDTO;
import com.evereading.best.dto.PageRequestDTO;
import com.evereading.best.dto.PageResultDTO;
import com.evereading.best.entity.Member;
import com.evereading.best.entity.Novel;

public interface NovelService {

    NovelDTO read(Long nno);

    PageResultDTO<NovelDTO,Novel> myNovelList(String email, PageRequestDTO pageRequestDTO);    
    
    default NovelDTO entityToDto(Novel entity){
        NovelDTO dto = NovelDTO.builder()
                    .nno(entity.getNno())
                    .title(entity.getTitle())
                    .content(entity.getContent())
                    .thumbnail(entity.getThumbnail())
                    .category(entity.getCategory())
                    .completed(entity.isCompleted())
                    .regdate(entity.getRegdate())
                    .nickname(entity.getMember().getNickname())
                    .email(entity.getMember().getEmail())
                    .totalCount(entity.getTotalCount())
                    .monthlyCount(entity.getMonthlyCount())
                    .weeklyCount(entity.getWeeklyCount())
                    .dailyCount(entity.getDailyCount())
                    .mostRecentReg(entity.getMostRecentReg())
                    .build();
        return dto;
    }

    default Novel dtoToEntity(NovelDTO dto){
        Novel entity = Novel.builder()
                .nno(dto.getNno())
                .title(dto.getTitle())
                .content(dto.getContent())
                .thumbnail(dto.getThumbnail())
                .category(dto.getCategory())
                .completed(dto.isCompleted())
                .member(Member.builder()
                        .email(dto.getEmail())                        
                        .build())
                .totalCount(dto.getTotalCount())
                .monthlyCount(dto.getMonthlyCount())
                .weeklyCount(dto.getWeeklyCount())
                .dailyCount(dto.getDailyCount())
                .build();
        return entity;
    }

    PageResultDTO<NovelDTO, Novel> getList(PageRequestDTO pageRequestDTO);
    
    PageResultDTO<NovelDTO, Novel> search(PageRequestDTO pageRequestDTO, String keyword);

    Long register(NovelDTO novelDTO, String email, MultipartFile imageFile);

    NovelDTO readForModify(Long nno, UserDetails userDetails);

    void modify(NovelDTO novelDTO, MultipartFile imageFile, UserDetails userDetails);

    void delete(Long nno, UserDetails userDetails);
    
    PageResultDTO<NovelDTO, Novel> getListByTotalCount(PageRequestDTO pageRequestDTO);

    PageResultDTO<NovelDTO, Novel> getListByCategory(PageRequestDTO pageRequestDTO, String category);

    PageResultDTO<NovelDTO, Novel> searchByCategory(String keyword, String category, PageRequestDTO pageRequestDTO);

    PageResultDTO<NovelDTO, Novel> getCompletedNovelList(PageRequestDTO pageRequestDTO);

    PageResultDTO<NovelDTO, Novel> getListByMonthlyCount(PageRequestDTO pageRequestDTO);
    PageResultDTO<NovelDTO, Novel> getListByWeeklyCount(PageRequestDTO pageRequestDTO);
    PageResultDTO<NovelDTO, Novel> getListByDailyCount(PageRequestDTO pageRequestDTO);
    
}
