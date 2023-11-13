package com.evereading.best.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.evereading.best.dto.MemberDTO;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
@RequestMapping("/view")
public class testController {

    @GetMapping("/home")
    public void home(){}

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/user")
    public void user(@AuthenticationPrincipal MemberDTO member){}

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin")
    public void admin(@AuthenticationPrincipal MemberDTO member){
        log.info("exAdmin..........");
    }

    
}
