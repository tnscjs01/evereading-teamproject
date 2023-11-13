package com.evereading.best.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.evereading.best.entity.Notice;

public interface NoticeRepository extends JpaRepository<Notice, Long>  {
    
}
