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
import com.evereading.best.service.ReviewRecommendService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/review/recommend")
@RequiredArgsConstructor
public class ReviewRecommendController {
    
    private final ReviewRecommendService reviewRecommendService;

    @PostMapping("up/{rvno}")
    public ResponseEntity addRecommend(@PathVariable("rvno") Long rvno,
                                       @AuthenticationPrincipal UserDetails userDetails){
        String email = userDetails.getUsername();

        Member member = new Member();
        member.setEmail(email);

        reviewRecommendService.addRecommend(rvno, member);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
