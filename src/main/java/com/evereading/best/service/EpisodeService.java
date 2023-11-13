package com.evereading.best.service;

import com.evereading.best.dto.EpisodeDTO;
import com.evereading.best.dto.PageRequestDTO;
import com.evereading.best.dto.PageResultDTO;
import com.evereading.best.entity.Episode;
import com.evereading.best.entity.Novel;

public interface EpisodeService {
    
    //목록
    PageResultDTO<EpisodeDTO, Episode> getList(Long nno,PageRequestDTO requestDTO);
    Long getFirstEpisode(Long nno); //첫회
    Long getTotalCount(Long nno);//총 조회수
    Long getTotalEpisode(Long nno); //총 회차수 

    Long epiNum(Long nno,Long eno);//회차 리네이밍용 ㅎ..
    Long replyCount(Long eno);

    //등록
    Long register(EpisodeDTO episodeDTO);//eno번호 출력 안하면 void로...

    //읽기
    EpisodeDTO read(Long eno);
    EpisodeDTO readNoCount(Long eno);

    //수정
    void modify(EpisodeDTO episodeDTO);

    //삭제
    void delete(Long eno);

    //이전 화 찾기
    Long findPreviousEpisode(Long eno, Long nno);
    //다음 화 찾기
    Long findNextEpisode(Long eno, Long nno);
    

    default EpisodeDTO entityToDto(Episode entity){
        EpisodeDTO dto = EpisodeDTO.builder()
                    .eno(entity.getEno())
                    .title(entity.getTitle())
                    .content(entity.getContent())
                    .count(entity.getCount())
                    .note(entity.getNote())
                    .regdate(entity.getRegdate())
                    .nno(entity.getNovel().getNno())
                    .episodeNum(epiNum(entity.getNovel().getNno(),entity.getEno()))//오류시 지우기
                    .replyCount(replyCount(entity.getEno()))
                    .email(entity.getNovel().getMember().getEmail())
                    .build();
        return dto;
    }

    default Episode dtoToEntity(EpisodeDTO dto){
        Episode entity = Episode.builder()
                    .eno(dto.getEno())
                    .title(dto.getTitle())
                    .content(dto.getContent())
                    .count(dto.getCount())
                    .note(dto.getNote())
                    .novel(Novel.builder().nno(dto.getNno()).build())
                    .build();
        return entity;
    }

    // 총조회수 업데이트
    void updateTotalCount(Long nno);

    // 월간 조회수 업데이트
    void updateMonthlyCount(Long nno);

    // 주간 조회수 업데이트
    void updateWeeklyCount(Long nno);

    // 일일 조회수 업데이트
    void updateDailyCount(Long nno);
    
}
