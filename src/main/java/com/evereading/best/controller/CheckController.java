package com.evereading.best.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.evereading.best.dto.FreeReplyDTO;
import com.evereading.best.dto.MemberDTO;
import com.evereading.best.service.EmailService;
import com.evereading.best.service.MemberService;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
public class CheckController {

    @Autowired
    private MemberService service;

    @Autowired
    private EmailService emailService;

    @GetMapping("/duplicateCheck/{nickname}")
	public Long nicknameCheck( @PathVariable("nickname") String nickname ) {
        Long result = service.duplicateCheck(nickname);
        return result;
	}

    @GetMapping("/emailCheck/{email}")
	public String emailCheck(@PathVariable("email") String email ) {
        Long result = service.emailCheck(email);
        if(result == 0){
            String num = emailService.sendMail(email);
            return num;
        }

        return null;
	}

    // @GetMapping("/pwCheck/{pw}")
	// public Long pwCheck(@AuthenticationPrincipal MemberDTO memberDTO, @PathVariable("pw") String pw ) {
    //     log.info("비밀번호"+pw);
    //     boolean result = service.pwCheck(memberDTO.getEmail(), pw);
    //     log.info("결과"+result);
    //     Long pwCheck;
    //     if(result){
    //         pwCheck = (long) 1;
    //     }else{
    //         pwCheck = (long) 0;
    //     }
        
    //     log.info(pwCheck);
    //     return pwCheck;
	// }

    @GetMapping("/pwCheck/{pw}")
    public ResponseEntity<Long> pwCheck(@AuthenticationPrincipal MemberDTO memberDTO, @PathVariable("pw") String pw ){

        boolean result = service.pwCheck(memberDTO.getEmail(), pw);
        Long pwCheck;
        if(result){
            pwCheck = (long) 1;
        }else{
            pwCheck = (long) 0;
        }

        return new ResponseEntity<>(pwCheck, HttpStatus.OK);
    }

}
