package com.evereading.best.repository;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.evereading.best.entity.Member;

import java.time.LocalDate;
import java.util.List;

public interface MemberRepository extends JpaRepository<Member, String> {
    
    @EntityGraph(attributePaths = {"roleSet"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("select m from Member m where m.social = :social and m.email =:email") 
    Optional<Member> findByEmail(@Param("email") String email, @Param("social") boolean social);

    @EntityGraph(attributePaths = {"roleSet"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("select m from Member m where m.email =:email") 
    Optional<Member> findByEmail(@Param("email") String email);

    @Query("select count(m) from Member m where m.nickname like :nickname")
    Long duplicateCheck(@Param("nickname") String nickname);

    @Query("select count(m) from Member m where m.email like :email")
    Long emailCheck(@Param("email") String email);

    @Query("select m.pw from Member m where m.email like :email")
    String pwCheck(@Param("email") String email);

    @Transactional
    @Modifying
    @Query("update Member m set m.nickname = :nickname where m.email like :email")
    void infoModify(@Param("email") String email, @Param("nickname") String nickname);

    @Transactional
    @Modifying
    @Query("update Member m set m.pw = :pw where m.email like :email")
    void pwModify(@Param("email") String email, @Param("pw") String pw);

    @Transactional
    @Modifying
    @Query("update Member m set m.signoutTime = sysdate() where m.email like :email")
    void signOut(@Param("email") String email);

    @Query("select m.signoutTime from Member m where m.email like :email")
    LocalDate getSignouttime(@Param("email") String email);
   
}
