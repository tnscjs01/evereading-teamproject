package com.evereading.best.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.evereading.best.dto.MemberDTO;
import com.evereading.best.service.MemberService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequestMapping("/member")
@Controller
@RequiredArgsConstructor
public class MemberContorller {

    private final MemberService memberService;


    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("pwCheckView")
    public String pwCheckView(@RequestParam("view") String view,@AuthenticationPrincipal MemberDTO memberDTO, Model model){
        MemberDTO member = memberService.info(memberDTO.getEmail());
        model.addAttribute("info", member);
        model.addAttribute("view", view);
        return "/member/pwCheck";
    }

    @PostMapping("pwCheck")
    public void pwCheck(@AuthenticationPrincipal MemberDTO memberDTO, String view ,Model model, HttpServletRequest request, HttpServletResponse response){
        if(true){
            
            if (view != null && !view.isEmpty()) {
                try {
                    HttpSession session = request.getSession();
                    session.setAttribute("pwValidated", true);
                    MemberDTO member = memberService.info(memberDTO.getEmail());
                    model.addAttribute("info", member);
                    if(view.equals("info")){
                        response.sendRedirect("/member/infoView");
                    }else if(view.equals("pwModify")){
                        response.sendRedirect("/member/pwModify");
                    }else{
                        response.sendRedirect("/member/signout");
                    }
                } catch (IOException e) {
                }
            } else {
                throw new BadCredentialsException("비정상적인 접근입니니다.");
            }
        }

        
    }
    
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/infoView")
    public String info(@AuthenticationPrincipal MemberDTO memberDTO, Model model,RedirectAttributes attr, HttpServletRequest request){
        HttpSession session = request.getSession();
        Boolean pwValidated = (Boolean) session.getAttribute("pwValidated");
        
        if(pwValidated != null){
            MemberDTO member = memberService.info(memberDTO.getEmail());
            model.addAttribute("info", member);
            session.removeAttribute("pwValidated");
            return "/member/personalInfo";
        }else{
            log.info("비밀번호 확인 안됨");
            attr.addAttribute("view", "info");
            return "redirect:/member/pwCheckView";
        }
        
    }

    @PostMapping("/personalInfo")
    public String infoModify(@AuthenticationPrincipal MemberDTO memberDTO, @RequestParam("nickname") String nickname) {
        log.info("확인");
        memberService.infoModify(memberDTO.getEmail(),nickname);
        return "redirect:/home";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/pwModify")
    public String pw(@AuthenticationPrincipal MemberDTO memberDTO, Model model,RedirectAttributes attr, HttpServletRequest request){
        HttpSession session = request.getSession();
        Boolean pwValidated = (Boolean) session.getAttribute("pwValidated");
        boolean social = memberDTO.isSocial();
        boolean pwCheck = memberService.pwCheck(memberDTO.getEmail(), "1111");
        
        if(pwValidated != null || (pwCheck && social)){
            MemberDTO member = memberService.info(memberDTO.getEmail());
            model.addAttribute("info", member);
            session.removeAttribute("pwValidated");
            if(pwCheck && social){
                model.addAttribute("firstVisit", "yes");
            }
            return "/member/pwModify";
        }else{
            log.info("비밀번호 확인 안됨");
            attr.addAttribute("view", "pwModify");
            return "redirect:/member/pwCheckView";
        }
        
    }

    @PostMapping("/pwModify")
    public String pwModify(@AuthenticationPrincipal MemberDTO memberDTO, @RequestParam("pw") String pw) {
        log.info("확인용"+memberDTO.getEmail()+pw);
        memberService.pwModify(memberDTO.getEmail(),pw);
        return "redirect:/home";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/signout")
    public String signout(@AuthenticationPrincipal MemberDTO memberDTO, Model model, RedirectAttributes attr, HttpServletRequest request){
        HttpSession session = request.getSession();
        Boolean pwValidated = (Boolean) session.getAttribute("pwValidated");
        
        if(pwValidated != null){
            MemberDTO member = memberService.info(memberDTO.getEmail());
            model.addAttribute("info", member);
            session.removeAttribute("pwValidated");
            return "/member/signout";
        }else{
            log.info("비밀번호 확인 안됨");
            attr.addAttribute("view", "signout");
            return "redirect:/member/pwCheckView";
        }
        
    }

    @PostMapping("/signout")
    public String signoutresult(String email){
        memberService.signoutTime(email);
        return "redirect:/successLogout";
    }
}
