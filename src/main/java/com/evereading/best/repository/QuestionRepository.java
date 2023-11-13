package com.evereading.best.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.evereading.best.entity.Question;

public interface QuestionRepository extends JpaRepository<Question, Long>  {
    // 작성자의 사용자 이름으로 질문을 검색하는 사용자 정의 쿼리 메서드를 추가합니다.
    Page<Question> findByMember_Email(String email, Pageable pageable);
    
}
