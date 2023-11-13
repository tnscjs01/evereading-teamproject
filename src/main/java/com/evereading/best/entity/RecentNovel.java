package com.evereading.best.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class RecentNovel{ 

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rnno;

    @ManyToOne(fetch = FetchType.LAZY)
    private Novel novel;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    //마지막으로 본 회차
    @Column
    private Long lastEpisode;
    private Long lastEno;

    private LocalDateTime date;


    public void changeLastEpisode(Long lastEpisode){
        this.lastEpisode = lastEpisode;
    }
    public void changeLastEno(Long lastEno){
        this.lastEno = lastEno;
    }

    //현재 시간으로 바뀜(다른 필드값이 안변해도 시간은 변함)
    public void changeDate(){
        this.date = LocalDateTime.now();
    }
}
