package com.evereading.best.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.evereading.best.entity.EpisodeReply;

public interface EpisodeReplyRepository extends JpaRepository<EpisodeReply, Long>{

    @Query(value=" delete from episode_reply where episode_eno =:eno ", nativeQuery = true)
    void deleteByEno(@Param("eno") Long eno);

    //댓글 페이징 처리
    @Query(value= " select * from episode_reply where episode_eno =:eno ", nativeQuery = true)
    Page<EpisodeReply> findByEpisode(@Param("eno") Long eno, Pageable pageable);

    //한 에피소드 당 댓글 개수
    @Query(value = " select count(erno) from episode_reply where episode_eno=:eno ", nativeQuery = true)
    Long replyCount(@Param("eno") Long eno);

    // 소설 삭제시 모든 회차에 달린 댓글 모두 삭제
    @Query(value=" delete from episode_reply where episode_eno in (select eno from episode where novel_nno =:nno)", nativeQuery = true)
    void deleteByNovel(@Param("nno") Long nno);

    //댓글 수정
    @Query(value=" select * from episode_reply where episode_eno =:eno and erno =:erno", nativeQuery = true)
    Optional<EpisodeReply> modifyEpisodeReply(@Param("eno") Long eno,@Param("erno") Long erno);
}
