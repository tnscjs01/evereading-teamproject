package com.evereading.best.service;

import com.evereading.best.dto.FreeReplyDTO;
import com.evereading.best.dto.PageRequestDTO;
import com.evereading.best.dto.PageResultDTO;
import com.evereading.best.entity.FreeBoard;
import com.evereading.best.entity.FreeReply;
import com.evereading.best.entity.Member;

public interface FreeReplyService {
     PageResultDTO<FreeReplyDTO,FreeReply> getListofReply(Long fno, PageRequestDTO pageRequestDTO);

    Long register(FreeReplyDTO freeReplyDTO);

    void modify(FreeReplyDTO freeReplyDTO);

    void remove(Long frno);

    default FreeReply dtoToEntity(FreeReplyDTO freeReplyDTO){

        FreeReply freeReply = FreeReply.builder()
                    .frno(freeReplyDTO.getFrno())
                    .content(freeReplyDTO.getContent())
                    .freeBoard(FreeBoard.builder().fno(freeReplyDTO.getBoardId()).build())
                    .member(Member.builder().email(freeReplyDTO.getEmail()).build())
                    .build();
        
        return freeReply;
    }
    default FreeReplyDTO entityToDto(FreeReply freeReply){

        FreeReplyDTO freeReplyDTO = FreeReplyDTO.builder()
                    .frno(freeReply.getFrno())
                    .boardId(freeReply.getFreeBoard().getFno())
                    .email(freeReply.getMember().getEmail())
                    .nickname(freeReply.getMember().getNickname())
                    .content(freeReply.getContent())
                    .regdate(freeReply.getRegdate())
                    .build();
        return freeReplyDTO;
    }
}
