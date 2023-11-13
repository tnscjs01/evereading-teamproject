package com.evereading.best.service;

import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.evereading.best.dto.MemberDTO;
import com.evereading.best.entity.Member;

import lombok.RequiredArgsConstructor;

public interface MemberService {

    //회원가입
    String join(String nicname, String email, String pw);

    //관리자 권한 부여
    void adminAuthorization(String email);

    //회원정보
    MemberDTO info(String email);

    //회원 정보 수정
    void infoModify(String email, String nickname);

    void pwModify(String email, String pw);

    //닉네임확인
    Long duplicateCheck(String nickname);

    //이메일확인
    Long emailCheck(String nickname);

    boolean pwCheck(String email, String pw);
    
    //회원탈퇴날짜등록
    void signoutTime(String email);

    default MemberDTO entityToDto(Member entity){
        MemberDTO dto = new MemberDTO(
                        entity.getEmail(),
                        entity.getNickname(),
                        entity.getPw(),
                        entity.isSocial(),
                        entity.getRoleSet().stream()
                                    .map(role -> new SimpleGrantedAuthority("ROLE_"+role.name()))
                                    .collect(Collectors.toSet())
        );
        return dto;
    }

    

}
