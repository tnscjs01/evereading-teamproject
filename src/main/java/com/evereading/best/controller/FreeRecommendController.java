package com.evereading.best.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.evereading.best.entity.Member;
import com.evereading.best.service.FreeRecommendService;
import com.evereading.best.service.MemberService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/free/recommend")
@RequiredArgsConstructor
public class FreeRecommendController {
    
    private final FreeRecommendService freeRecommendService;

    @PostMapping("up/{fno}")
    public ResponseEntity addRecommend(@PathVariable("fno") Long fno, 
                                       @AuthenticationPrincipal UserDetails userDetails){
        String email = userDetails.getUsername();

        Member member = new Member();
        member.setEmail(email);

        freeRecommendService.addRecommend(fno,member);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
