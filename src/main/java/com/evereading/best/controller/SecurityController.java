package com.evereading.best.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.evereading.best.service.MemberService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
@Controller
@RequestMapping("/security")
public class SecurityController {

    private final MemberService memberService;

    // @GetMapping("/login")
    // public String loginForm(){
    //     return "/security/loginForm";
    // }
    
    @GetMapping("/login")
    public String loginForm(@RequestParam(value = "exception", required = false)String exception,
                            Model model){
        model.addAttribute("exception", exception);
        return "/security/loginForm";
    }

    @GetMapping("/signup")
    public void createMemberForm() {}

    @PostMapping("/signup")
    public String createMember(@RequestParam("nickname") String nickname,@RequestParam("email") String email,@RequestParam("pw") String pw) {
        memberService.join(nickname,email,pw);
        return "redirect:/security/login";
    }
    
    @GetMapping("/memberList")
    public void memberList(){}

    @PostMapping("/memberList")
    public void adminAuthorization(String email) {
        log.info("회원 이메일========================="+email);
        memberService.adminAuthorization(email);
    }

    @GetMapping("/successSignout")
    public void successSignout(){}

}
