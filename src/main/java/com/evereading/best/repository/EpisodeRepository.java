package com.evereading.best.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.evereading.best.entity.Episode;

public interface EpisodeRepository extends JpaRepository<Episode, Long>{
    //novel별 검색 - pageing 처리 위해서 page<>로 리턴
    @Query(value=" select * from episode where novel_nno =:nno ", nativeQuery=true )
    Page<Episode> findByNno(@Param("nno") Long nno, Pageable pageable);

    //첫화 검색
    @Query(value=" select eno from episode where novel_nno =:nno order by eno limit 1 ", nativeQuery=true )
    Long findFirstEpisode(@Param("nno") Long nno);

    //조회수 합계
    @Query(value=" select sum(count) from episode where novel_nno =:nno ", nativeQuery=true)
    Long getTotalCount(@Param("nno") Long nno);

    //총 회차 수
    @Query(value=" select count(eno) from episode where novel_nno=:nno ", nativeQuery = true)
    Long getTotalEpisode(@Param("nno") Long nno);

    //회차 번호 정리~
    @Query(value=" SELECT s.num FROM ( " + 
            " select eno,rank() over (order by eno)AS num from episode where novel_nno=:nno) AS s " + 
            " WHERE eno =:eno ; ", nativeQuery = true)
    Long episodeNum(@Param("nno") Long nno, @Param("eno")Long eno);

    // 소설 삭제시 소설 모든 회차 삭제
    @Query(value=" delete from episode where novel_nno=:nno ", nativeQuery = true)
    void deleteByNovel(@Param("nno") Long nno);


    //뷰페이지용
    //이전 화 찾기
    @Query(value = " select eno from episode where eno <:eno and novel_nno=:nno order by eno desc limit 1", nativeQuery = true)
    Long findPreviousEpisode(@Param("eno")Long eno, @Param("nno")Long nno);

    //다음 화 찾기
    @Query(value = " select eno from episode where eno >:eno and novel_nno=:nno limit 1", nativeQuery = true)
    Long findNextEpisode(@Param("eno")Long eno, @Param("nno")Long nno);

    // 월간 조회수를 계산하기 위한 쿼리를 추가합니다.
    @Query(value = "SELECT SUM(count) FROM episode " +
            "WHERE novel_nno = :nno " +
            "AND regdate >= DATE_SUB(NOW(), INTERVAL 1 MONTH)",
            nativeQuery = true)
    Long getMonthlyCount(@Param("nno") Long nno);

    // 주간 조회수를 계산하기 위한 쿼리를 추가합니다.
    @Query(value = "SELECT SUM(count) FROM episode " +
            "WHERE novel_nno = :nno " +
            // regdate를 1주일 전까지로 제한
            "AND regdate >= DATE_SUB(NOW(), INTERVAL 1 WEEK)",
            nativeQuery = true)
    Long getWeeklyCount(@Param("nno") Long nno);

    // 일일 조회수를 계산하기 위한 쿼리를 추가합니다.
    @Query(value = "SELECT SUM(count) FROM episode " +
            "WHERE novel_nno = :nno " +
            // regdate를 1일 전까지로 제한
            "AND regdate >= DATE_SUB(NOW(), INTERVAL 1 DAY)",
            nativeQuery = true)
    Long getDailyCount(@Param("nno") Long nno);


}
