package com.evereading.best.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.evereading.best.entity.Novel;

public interface NovelRepository extends JpaRepository<Novel, Long>{
    
    // 내 소설 목록 불러오기
    @Query("SELECT n FROM Novel n WHERE n.member.email = :email")
    Page<Novel> myNovelList(@Param("email") String email, Pageable pageable);
    
    // JPQL 쿼리를 사용하여 제목(title)과 작가명(nickname)으로 검색합니다.
    @Query("SELECT n FROM Novel n LEFT JOIN n.member m " +
            "WHERE n.title LIKE %:keyword% OR m.nickname LIKE %:keyword%")
    Page<Novel> search(@Param("keyword") String keyword, Pageable pageable);    
    
    Page<Novel> findAllByCategory(String category, Pageable pageable);    

    @Query("SELECT n FROM Novel n LEFT JOIN n.member m " +
        "WHERE (n.category = :category OR :category IS NULL) " +
        "AND (n.title LIKE %:keyword% OR m.nickname LIKE %:keyword%)")
    Page<Novel> searchByCategory(@Param("category") String category, @Param("keyword") String keyword, Pageable pageable);

    // 완결된 소설을 검색하는 JPQL 쿼리 추가
    @Query("SELECT n FROM Novel n WHERE n.completed = true")
    Page<Novel> findAllByCompleted(@Param("completed") boolean completed, Pageable pageable);

}
