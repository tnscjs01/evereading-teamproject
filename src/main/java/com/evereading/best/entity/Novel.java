package com.evereading.best.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Formula;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class Novel extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long nno;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "text", nullable = false)
    private String content;

    @Column(nullable = false)
    private String thumbnail;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private boolean completed;
    
    @ManyToOne(fetch = FetchType.LAZY)
    public Member member;

    @Column(nullable = false)
    private Long totalCount = 0L;

    @Column(nullable = false)
    private Long monthlyCount = 0L;
    
    @Column(nullable = false)
    private Long weeklyCount = 0L;
    
    @Column(nullable = false)
    private Long dailyCount = 0L;

    @Formula("(SELECT MAX(e.regdate) FROM episode e WHERE e.novel_nno = nno)")
    public LocalDateTime mostRecentReg;


    public void changeTitle(String title){
        this.title = title;
    }

    public void changeContent(String content){
        this.content = content;
    }

    public void changeCategory(String category){
        this.category = category;
    }

    public void changeCompleted(Boolean completed){
        this.completed = completed;
    }

    public void changeThumbnail(String thumbnail){
        this.thumbnail = thumbnail;
    }
        
}