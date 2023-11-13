package com.evereading.best.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.evereading.best.handler.CustomAuthFailureHandler;
import com.evereading.best.handler.CustomLogoutSuccessHandler;
import com.evereading.best.handler.LoginSuccessHandler;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig {
    

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private CustomAuthFailureHandler customFailureHandler;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeRequests()
        // 공지사항        
        .antMatchers("/notice/write", "/notice/modify", "/notice/delete").hasRole("ADMIN")

        // 자주 묻는 질문        
        .antMatchers("/faq/write", "/faq/modify", "/faq/delete").hasRole("ADMIN")                

        // 1:1 문의
        .antMatchers("/question/**").access("hasRole('USER') or hasRole('ADMIN')")
        
        // 이벤트        
        .antMatchers("/event/register", "/event/modify", "/event/delete").hasRole("ADMIN")                
        
        // 소설게시판 등록, 수정, 삭제 접근 권한
        .antMatchers("/novel/register", "/novel/modify", "/novel/delete").access("hasRole('USER') or hasRole('ADMIN')")
        
        //내 서재
        .antMatchers("/library/**").access("hasRole('USER') or hasRole('ADMIN')");


        http.formLogin()
            .loginPage("/security/login")
            .loginProcessingUrl("/login")
            .defaultSuccessUrl("/home")
            .usernameParameter("email")
            .passwordParameter("password")
            .failureHandler(customFailureHandler);
        http.oauth2Login().loginPage("/security/login").successHandler(successHandler()).failureHandler(customFailureHandler);
        http.rememberMe().rememberMeParameter("remember-me").tokenValiditySeconds(60*60*24*7).userDetailsService(userDetailsService);
        http.logout()
            .logoutUrl("/successLogout")
            .logoutSuccessHandler(new CustomLogoutSuccessHandler());

        http.csrf().disable();

        return http.build();
    }

    @Bean
    public LoginSuccessHandler successHandler(){
        return new LoginSuccessHandler(passwordEncoder());
    }
}

