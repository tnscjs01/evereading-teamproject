package com.evereading.best.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.evereading.best.entity.Review;

public interface ReviewRepository extends JpaRepository<Review,Long>{

    Page<Review> findBytitleContaining(String searchKeyword, Pageable pageable);

    @Modifying
    @Query("UPDATE Review rv SET rv.replyCnt = (SELECT COUNT(rr) FROM ReviewReply rr WHERE rr.review = rv) WHERE rv.rvno = :rvno")
    void updateReplyCount(@Param("rvno") Long rvno);
}
