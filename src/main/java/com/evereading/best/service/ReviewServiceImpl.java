package com.evereading.best.service;


import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.evereading.best.entity.Review;
import com.evereading.best.repository.ReviewRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService{

    private final ReviewRepository reviewRepository;

    @Override
    public void register(Review vo) {
        reviewRepository.save(vo);
    }

    @Override
    public Review read(long rvno) {
        return reviewRepository.findById(rvno).get();
    }

    @Override
    public void modify(Review vo) {
        reviewRepository.save(vo);
    }

    @Override
    public void delete(long rvno) {
        reviewRepository.deleteById(rvno);
    }

    @Override
    public Page<Review> getList(Pageable pageable) {
        
        return reviewRepository.findAll(pageable);
    }

    @Override
    public Page<Review> boardSearchList(String searchKeyword, Pageable pageable) {
       return reviewRepository.findBytitleContaining(searchKeyword, pageable);
    }

    @Transactional
    @Override
    public Review findById(Long rvno) {
        Optional<Review> optionalReviewEntity = reviewRepository.findById(rvno);
        if(optionalReviewEntity.isPresent()){
            Review review = optionalReviewEntity.get();
            return review;
        } else {
            return null;
        }
    }

    @Override
    public Review ReplyCount(Long boardId) {
       Review review = reviewRepository.findById(boardId).orElse(null);
       if (review != null){
           reviewRepository.updateReplyCount(boardId);
           return review;
       }
       return null;
    }

    @Override
    public int getRecommendCnt(Long rvno) {
        return reviewRepository.findById(rvno).map(Review::getRecommendCnt).orElse(0);
    }
    
}
