package com.evereading.best.service;


import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.evereading.best.entity.FreeBoard;
import com.evereading.best.repository.FreeBoardRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FreeBoardServiceImpl implements FreeBoardService{

    private final FreeBoardRepository freeBoardRepository;

    @Override
    public void register(FreeBoard vo) {
        freeBoardRepository.save(vo);
    }

    @Override
    public FreeBoard read(long fno) {
        return freeBoardRepository.findById(fno).get();
    }

    @Override
    public void modify(FreeBoard vo) {
        freeBoardRepository.save(vo);
    }

    @Override
    public void delete(long fno) {
        freeBoardRepository.deleteById(fno);
    }

    @Override
    public Page<FreeBoard> getList(Pageable pageable) {
        
        return freeBoardRepository.findAll(pageable);
    }

    @Override
    public Page<FreeBoard> boardSearchList(String searchKeyword, Pageable pageable) {
       return freeBoardRepository.findBytitleContaining(searchKeyword, pageable);
    }

    @Transactional
    @Override
    public FreeBoard findById(Long fno) {
        Optional<FreeBoard> optionalBoardEntity = freeBoardRepository.findById(fno);
        if(optionalBoardEntity.isPresent()){
            FreeBoard board = optionalBoardEntity.get();
            return board;
        } else {
            return null;
        }
    }

    @Override
    public FreeBoard ReplyCount(Long boardId) {
        FreeBoard freeBoard = freeBoardRepository.findById(boardId).orElse(null);
        if (freeBoard != null){
            freeBoardRepository.updateReplyCount(boardId);
            return freeBoard;
        }
        return null;
    }

    @Override
    public int getRecommendCnt(Long fno) {
        return freeBoardRepository.findById(fno).map(FreeBoard::getRecommendCnt).orElse(0);
    }
}
