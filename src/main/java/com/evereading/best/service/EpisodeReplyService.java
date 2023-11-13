package com.evereading.best.service;

import com.evereading.best.dto.EpisodeReplyDTO;
import com.evereading.best.dto.PageRequestDTO;
import com.evereading.best.dto.PageResultDTO;
import com.evereading.best.entity.Episode;
import com.evereading.best.entity.EpisodeReply;

public interface EpisodeReplyService {

    PageResultDTO<EpisodeReplyDTO,EpisodeReply> getListEpisodeReply(Long eno, PageRequestDTO requestDTO);
    Long register(EpisodeReplyDTO episodeReplyDTO);
    void modify(EpisodeReplyDTO episodeReplyDTO);
    void remove(Long erno);

    //?
    String findNickName(EpisodeReply episodeReply);
    
    default EpisodeReplyDTO entityToDto(EpisodeReply entity){
        EpisodeReplyDTO dto = EpisodeReplyDTO.builder()
                        .erno(entity.getErno())
                        .content(entity.getContent())
                        .regdate(entity.getRegdate())
                        .eno(entity.getEpisode().getEno())
                        .email(entity.getEmail())
                        .nickName(findNickName(entity))
                        .build();
        return dto;
    }

    default EpisodeReply dtoToEntity(EpisodeReplyDTO dto){
        EpisodeReply entity = EpisodeReply.builder()
                        .erno(dto.getErno())
                        .content(dto.getContent())
                        .episode(Episode.builder().eno(dto.getEno()).build())
                        .email(dto.getEmail())
                        .build();
        return entity;
    }
}
