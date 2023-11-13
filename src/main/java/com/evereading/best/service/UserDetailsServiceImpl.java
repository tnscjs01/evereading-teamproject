
package com.evereading.best.service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.evereading.best.dto.MemberDTO;
import com.evereading.best.entity.Member;
import com.evereading.best.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService{

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info(username);
        Optional<Member> memberResult = memberRepository.findByEmail(username);
        if(memberResult.isEmpty()){
            log.info("없는 회원");
            throw new BadCredentialsException("존재하지 않는 회원");
        }
        Member member = memberResult.get();

        LocalDate result = memberRepository.getSignouttime(member.getEmail());
        if(result !=null){
            log.info("탈퇴회원");
            throw new InternalAuthenticationServiceException("탈퇴회원");
        }
        
        MemberDTO dto = new MemberDTO(
            member.getEmail(),
            member.getNickname(),
            member.getPw(),
            member.isSocial(),
            member.getRoleSet().stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_"+role.name()))
                        .collect(Collectors.toSet())
        );
        return dto;
    }

    
    
}
