package com.evereading.best.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.evereading.best.entity.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
    
}
