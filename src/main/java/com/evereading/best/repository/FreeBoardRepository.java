package com.evereading.best.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.evereading.best.entity.FreeBoard;

public interface FreeBoardRepository extends JpaRepository<FreeBoard,Long>{

    Page<FreeBoard> findBytitleContaining(String searchKeyword, Pageable pageable);

    @Modifying
    @Query("UPDATE FreeBoard fb SET fb.replyCnt = (SELECT COUNT(fr) FROM FreeReply fr WHERE fr.freeBoard = fb) WHERE fb.fno = :fno")
    void updateReplyCount(@Param("fno") Long fno);
}
