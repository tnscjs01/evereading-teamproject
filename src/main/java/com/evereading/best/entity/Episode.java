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
import lombok.ToString;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString

public class Episode extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eno;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "text", nullable = false)
    private String content;

    //조회수
    @Column(nullable = false)
    private Long count;

    //작가의 말
    @Column(columnDefinition = "varchar(600)")
    private String note;

    // @ManyToOne(fetch = FetchType.LAZY) - hibernate에러생김
    @ManyToOne(fetch = FetchType.EAGER)
    private Novel novel;
    
    //setter
    public void changeTitle(String title){
        this.title = title;
    }

    public void changeContent(String content){
        this.content = content;
    }

    public void changeCount(Long count){
        this.count = count;
    }

    public void changeNote(String note){
        this.note = note;
    }

}
