package com.evereading.best.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler{

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        String requestURI = request.getHeader("Referer");
        if(requestURI.equals("http://localhost:8888/member/signout")){
            log.info("회원탈퇴");
            response.sendRedirect("/security/successSignout");
        }else{
            log.info("로그아웃");
            response.sendRedirect("/home");
        }
    }
    
}
