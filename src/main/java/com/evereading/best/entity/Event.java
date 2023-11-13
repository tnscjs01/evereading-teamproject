package com.evereading.best.entity;

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
import lombok.Setter;
import lombok.ToString;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class Event extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long evno;
    
    @Column(length = 200, nullable = false)
    private String title;
    
    @Column(length = 6000, nullable = false)
    private String content;

    @Column(nullable = false)
    private String carouselImg;

    @Column(nullable = false)
    private String eventImg;
    
    @ManyToOne(fetch = FetchType.LAZY)
    public Member member;


    public void changeTitle(String title){
        this.title = title;
    }

    public void changeContent(String content){
        this.content = content;
    }

    public void changeCarouselImg(String carouselImg){
        this.carouselImg = carouselImg;
    }

    public void changeEventImg(String eventImg){
        this.eventImg = eventImg;
    }

}
