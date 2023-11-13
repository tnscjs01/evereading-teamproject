package com.evereading.best.repository;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import com.evereading.best.entity.Novel;

@SpringBootTest
public class NovelRepositoryTests {

    @Autowired
    private NovelRepository novelRepository;

    @Commit
    @Transactional
    @Test
    public void dummy(){

        Novel novel = Novel.builder()
                .category("공포")
                .completed(true)
                .content("무서운이야기임")
                .thumbnail("이미지주소")
                .title("무서운게딱좋아")
                .build();

        novelRepository.save(novel);

    }
    
}
