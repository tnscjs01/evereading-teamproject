package com.evereading.best.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.evereading.best.entity.Faq;

public interface FaqRepository extends JpaRepository<Faq, Long>  {
    
}
