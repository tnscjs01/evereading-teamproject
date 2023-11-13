package com.evereading.best.service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.evereading.best.dto.MemberDTO;
import com.evereading.best.entity.Member;
import com.evereading.best.entity.MemberRole;
import com.evereading.best.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
public class OAuth2UserDetailsService extends DefaultOAuth2UserService{

    private final MemberRepository repository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        String clientName = userRequest.getClientRegistration().getClientName();

        OAuth2User oAuth2User =  super.loadUser(userRequest);

        String email = null;

        if(clientName.equals("Google")){
            email = oAuth2User.getAttribute("email");
        }
        log.info(email);

        Member member = saveSocialMember(email);
        log.info("확인용"+member);

        MemberDTO dto = new MemberDTO(
                member.getEmail(),
                member.getNickname(),
                member.getPw(),
                true,   //fromSocial
                member.getRoleSet().stream().map(
                                role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                        .collect(Collectors.toList()),
                oAuth2User.getAttributes()
        );
        dto.setNickname(member.getNickname());
        log.info("확인"+dto);
        return dto;
        
    }


    private Member saveSocialMember(String email){

        Optional<Member> result = repository.findByEmail(email, true);
        if(result.isPresent()){
            LocalDate signoutResult = repository.getSignouttime(result.get().getEmail());
            if(signoutResult !=null){
                throw new InternalAuthenticationServiceException("탈퇴회원.");
            }
            return result.get();
        }

        // double num = Math.random();
        // String randomPw = String.valueOf(num);

        Member member = Member.builder()
                        .email(email)
                        .nickname(email)
                        .pw( passwordEncoder.encode("1111") )
                        .social(true)
                        .roleSet(new HashSet<MemberRole>())
                        .build();

        member.addMemberRole(MemberRole.USER);
        repository.save(member);
        return member;
        
       
    }

}
