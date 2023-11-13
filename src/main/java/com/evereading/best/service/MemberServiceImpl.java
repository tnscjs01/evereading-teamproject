package com.evereading.best.service;

import java.util.HashSet;
import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.evereading.best.dto.MemberDTO;
import com.evereading.best.entity.Member;
import com.evereading.best.entity.MemberRole;
import com.evereading.best.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service	
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public String join(String nickname, String email, String pw) {
        Member member = Member.builder()
                .email(email)
                .nickname(nickname)
                .pw(passwordEncoder.encode(pw))
                .roleSet(new HashSet<MemberRole>())
                .social(false)
                .build();

        member.addMemberRole(MemberRole.USER);

        return memberRepository.save(member).getEmail();
    }
    
    @Override
    public void adminAuthorization(String email) {
        Member member = memberRepository.findById(email).get();
        member.addMemberRole(MemberRole.ADMIN);
        memberRepository.save(member);
    }

    @Override
    public MemberDTO info(String email) {
        Optional<Member> member = memberRepository.findById(email);
        Member memberinfo = member.get();
        return entityToDto(memberinfo);
    }

    @Override
    public void infoModify(String email, String nickname) {
        memberRepository.infoModify(email,nickname);
    }

    @Override
    public void pwModify(String email, String pw) {
        memberRepository.pwModify(email,passwordEncoder.encode(pw));
    }

    @Override
    public Long duplicateCheck(String nickname) {
        return memberRepository.duplicateCheck(nickname);
    }

    @Override
    public Long emailCheck(String email) {
        return memberRepository.emailCheck(email);
    }

    @Override
    public boolean pwCheck(String email, String pw) {
        boolean pwck = passwordEncoder.matches(pw, memberRepository.pwCheck(email));
        return pwck;
    }

    @Override
    public void signoutTime(String email) {
        memberRepository.signOut(email);
    }

    
}