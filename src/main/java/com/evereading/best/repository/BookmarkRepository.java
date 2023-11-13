package com.evereading.best.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.evereading.best.entity.Bookmark;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long>{
    //email로 novel 번호 찾기
    // @Query(value = " select * from bookmark where member_email =:email ", nativeQuery = true)
    // List<Bookmark> findByEmail(@Param("email")String email);
    @Query(value = " select * from bookmark where member_email =:email ", nativeQuery = true)
    Page<Bookmark> findByEmail(@Param("email")String email, Pageable Pageable);

    //북마크 했는지 아닌지 확인용
    @Query(value = " select bno from bookmark where novel_nno =:nno and member_email=:email ", nativeQuery = true)
    Long findByNnoAndEmail(@Param("nno")Long nno, @Param("email")String email);

    //북마크 삭제
    @Query(value = " delete from bookmark where novel_nno =:nno and member_email=:email ", nativeQuery = true)
    void deleteByNnoAndEmail(@Param("nno") Long nno, @Param("email")String email);

    // 소설 삭제시 북마크 삭제
    @Query(value = " delete from bookmark where novel_nno =:nno ", nativeQuery = true)
    void deleteByNovel(@Param("nno") Long nno);

    //북마크 수
    @Query(value = " select count(bno) from bookmark where novel_nno=:nno ", nativeQuery = true)
    Long totalBookmarkByNno(@Param("nno")Long nno);
}
