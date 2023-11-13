package com.evereading.best.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.evereading.best.entity.FreeReply;

@Repository
public interface FreeReplyRepository extends JpaRepository<FreeReply,Long>{

    @Query("SELECT r FROM FreeReply r WHERE r.freeBoard.fno = :fno")
    Page<FreeReply> findByFreeBoard(@Param("fno") Long fno, Pageable pageable);
    
}