package com.evereading.best.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.evereading.best.entity.Review;

public interface ReviewService {
    
    public Page<Review> getList(Pageable pageable); // 전체 리스트
    public void register(Review vo); // 글 등록
    public Review read(long rvno); // 상세보기
    public void modify(Review vo);
    public void delete(long rvno);
    public Page<Review> boardSearchList(String searchKeyword, Pageable pageable);
    public Review findById(Long rvno);

    public Review ReplyCount(Long boardId);
    public int getRecommendCnt(Long rvno);
}
