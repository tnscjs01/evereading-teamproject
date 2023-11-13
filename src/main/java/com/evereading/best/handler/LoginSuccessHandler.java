package com.evereading.best.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.evereading.best.dto.MemberDTO;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class LoginSuccessHandler implements AuthenticationSuccessHandler{
    
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    private PasswordEncoder passwordEncoder;

    public LoginSuccessHandler(){
        passwordEncoder = new BCryptPasswordEncoder();
    }

    public LoginSuccessHandler(PasswordEncoder passwordEncoder){
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        MemberDTO dto = (MemberDTO)authentication.getPrincipal();
 
        boolean social = dto.isSocial();

        log.info(dto.getPw());
        boolean passwordResult = passwordEncoder.matches("1111", dto.getPw());

        log.info(passwordResult);
        if(social && passwordResult) {
            redirectStrategy.sendRedirect(request, response, "/member/pwModify");
        }else{
            redirectStrategy.sendRedirect(request, response, "/home");
        }
        
    }

    

}
