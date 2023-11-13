package com.evereading.best.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rvno;
    
    @Column(length = 50)
    private String title;

    @Column(length = 5000)
    private String content;

    @Column(name = "regdate",insertable = false, updatable = false, columnDefinition = "datetime default now()")
    private LocalDateTime regDate;

    @Column(nullable = false)
    private String category;

    @Column(name = "count" , nullable = false)
    private int count = 0;

    @Column(name = "replyCnt" , nullable = false)
    private int replyCnt = 0;

    @Column(name = "recommendCnt" , nullable = false)
    private int recommendCnt = 0;

    @OneToMany(mappedBy = "review", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ReviewRecommend> reviewRecommend;

    @OneToMany(mappedBy = "review", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ReviewReply> commentEntityList = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "email", referencedColumnName = "email", updatable = false)
    private Member email;

    public void getMember(String username){
    }

    public void setMember(String username){
    }

}
