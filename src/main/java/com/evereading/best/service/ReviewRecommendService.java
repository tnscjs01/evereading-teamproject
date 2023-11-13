package com.evereading.best.service;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.evereading.best.entity.Member;
import com.evereading.best.entity.Review;
import com.evereading.best.entity.ReviewRecommend;
import com.evereading.best.repository.ReviewRecommendRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewRecommendService {
    
    private final ReviewRecommendRepository reviewRecommendRepository;
    private final ReviewService reviewService;

    public void addRecommend(Long rvno, Member member){
        Review review = reviewService.findById(rvno);
        if(!reviewRecommendRepository.existsByMemberAndReview(member, review)){
            review.setRecommendCnt(review.getRecommendCnt()+1);
            reviewRecommendRepository.save(new ReviewRecommend(member, review));
        } else {
            review.setRecommendCnt(review.getRecommendCnt()-1);
            reviewRecommendRepository.deleteByMemberAndReview(member, review);
        }
    }
}
