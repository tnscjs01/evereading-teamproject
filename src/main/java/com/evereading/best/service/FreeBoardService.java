package com.evereading.best.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.evereading.best.entity.FreeBoard;

public interface FreeBoardService {
    
    public Page<FreeBoard> getList(Pageable pageable); // 전체 리스트
    public void register(FreeBoard vo); // 글 등록
    public FreeBoard read(long fno); // 상세보기
    public void modify(FreeBoard vo);
    public void delete(long fno);
    public Page<FreeBoard> boardSearchList(String searchKeyword, Pageable pageable);
    public FreeBoard findById(Long fno);

    public FreeBoard ReplyCount(Long boardId);
    public int getRecommendCnt(Long fno);
}
