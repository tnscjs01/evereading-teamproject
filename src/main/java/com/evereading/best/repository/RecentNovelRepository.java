package com.evereading.best.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.evereading.best.entity.RecentNovel;

public interface RecentNovelRepository extends JpaRepository<RecentNovel, Long>{

    //최근 목록 데이터 가져오기 - 최근 본 순서대로 출력됨
    // @Query(value = " select * from recent_novel where member_email =:email order by date desc ", nativeQuery = true)
    // List<RecentNovel> findRecentByEmail(@Param("email") String email);
    @Query(value = " select * from recent_novel where member_email =:email order by date desc ", nativeQuery = true)
    Page<RecentNovel> findRecentByEmail(@Param("email") String email, Pageable pageable);

    //최근목록 삭제
    @Query(value=" delete from recent_novel where novel_nno =:nno ", nativeQuery=true)
    void deleteByNno(@Param("nno")Long nno);

    //중복 데이터 확인, 소설별 마지막 회차 찾는데도 사용
    @Query(value=" select * from recent_novel where member_email =:email and novel_nno =:nno ", nativeQuery=true)
    Optional<RecentNovel> findByEmailAndNno(@Param("email") String email, @Param("nno")Long nno);

    //로그인 계정과 소설 작가 비교
    @Query(value=" select member_email from novel where nno=:nno ", nativeQuery = true)
    String novelWriter(@Param("nno") Long nno);

    //eno로 소설 제목 찾기-최근 본 회차에 이용
    @Query(value=" select title from episode where eno =:eno", nativeQuery = true)
    String findLastEpisodeTitle(@Param("eno") Long eno);
}
