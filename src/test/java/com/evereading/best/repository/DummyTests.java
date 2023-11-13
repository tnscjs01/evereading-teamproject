package com.evereading.best.repository;

import java.util.stream.IntStream;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import com.evereading.best.entity.Episode;
import com.evereading.best.entity.Member;
import com.evereading.best.entity.Novel;


@SpringBootTest
public class DummyTests {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private NovelRepository novelRepository;
    @Autowired
    private EpisodeRepository episodeRepository;

    @Commit
    @Transactional
    @Test
    public void insertMember(){
        IntStream.rangeClosed(1, 3).forEach(i->{
            Member member = Member.builder()
                            .email("Email"+i)                            
                            .nickname("작가"+i)
                            .pw("1111")
                            .social(false)
                            .build();
            memberRepository.save(member);
        });     

    };

    @Commit
    @Transactional
    @Test
    public void insertNovelAndEpisode(){
        IntStream.rangeClosed(1,30).forEach(i -> {
            int count = (int)(Math.random() * 3) + 1;
            
            Novel novel = Novel.builder()
                        .category("카테고리")
                        .completed(false)
                        .content("소설소개"+i)
                        .thumbnail("이미지주소")
                        .title("소설제목"+i)
                        .member(Member.builder().email("Email"+count).build())
                        .build();
            novelRepository.save(novel);

            //각 소설별 에피소드 50개씩 추가..
            for(int j = 0; j < 50; j++){
                Episode episode = Episode.builder()
                                .content("소설내용")
                                .count(0L)
                                .note("작가의말")
                                .title("에피소드"+j)
                                .novel(novel)
                                .build();
                episodeRepository.save(episode);
            }

        });
    };

    @Commit
    @Transactional
    @Test
    public void insertNovel(){
            Novel novel = Novel.builder()
                        .category("카테고리")
                        .completed(false)
                        .content("소설소개")
                        .thumbnail("이미지주소")
                        .title("소설제목")
                        .member(Member.builder().email("Email").build())
                        .build();
            novelRepository.save(novel);

    };


}
