package com.evereading.best.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.evereading.best.entity.Member;
import com.evereading.best.entity.Review;
import com.evereading.best.entity.ReviewRecommend;

@Repository
public interface ReviewRecommendRepository extends JpaRepository<ReviewRecommend, Long>{
    boolean existsByMemberAndReview(Member member, Review review);
    void deleteByMemberAndReview(Member member, Review reivew);
}
