package com.evereading.best.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.evereading.best.entity.FreeBoard;
import com.evereading.best.entity.FreeRecommend;
import com.evereading.best.entity.Member;

@Repository
public interface FreeRecommendRepository extends JpaRepository<FreeRecommend, Long> {
    boolean existsByMemberAndFreeBoard(Member member, FreeBoard freeBoard);
    void deleteByMemberAndFreeBoard(Member member, FreeBoard freeBoard);
}

