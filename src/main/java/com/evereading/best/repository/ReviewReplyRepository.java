package com.evereading.best.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.evereading.best.entity.ReviewReply;

@Repository
public interface ReviewReplyRepository extends JpaRepository<ReviewReply,Long>{

    @Query("SELECT r FROM ReviewReply r WHERE r.review.rvno = :rvno")
    Page<ReviewReply> findByReview(@Param("rvno") Long rvno, Pageable pageable);

}