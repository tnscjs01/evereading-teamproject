package com.evereading.best.entity;

import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity		// Getter Setter
@Builder		// DTO -> Entity화
@AllArgsConstructor	// 모든 컬럼 생성자 생성
@NoArgsConstructor	// 기본 생성자
@Getter
@Setter
@ToString
public class Member {

    @Id
    @Column(nullable = false, unique = true)
    private String email;

    private String nickname;

    private String pw;

    private boolean social;

    @Column(nullable = true)
    private LocalDateTime signoutTime;

    @ElementCollection(fetch = FetchType.LAZY) //onetomany와 비슷함
    private Set<MemberRole> roleSet;

    public void addMemberRole(MemberRole memberRole){
        roleSet.add(memberRole);
    }
}