package com.evereading.best.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.evereading.best.entity.FreeBoard;
import com.evereading.best.entity.FreeRecommend;
import com.evereading.best.entity.Member;
import com.evereading.best.repository.FreeBoardRepository;
import com.evereading.best.repository.FreeRecommendRepository;
import com.evereading.best.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class FreeRecommendService {

    private final FreeRecommendRepository freeRecommendRepository;
    private final FreeBoardService freeBoardService;

    public void addRecommend(Long fno, Member member){
        FreeBoard board = freeBoardService.findById(fno);
        if(!freeRecommendRepository.existsByMemberAndFreeBoard(member,board)){
            board.setRecommendCnt(board.getRecommendCnt()+1);
            freeRecommendRepository.save(new FreeRecommend(member, board));
        } else {
            board.setRecommendCnt(board.getRecommendCnt()-1);
            freeRecommendRepository.deleteByMemberAndFreeBoard(member, board);
        }
    }
}

