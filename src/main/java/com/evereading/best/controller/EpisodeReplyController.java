package com.evereading.best.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.evereading.best.dto.EpisodeReplyDTO;
import com.evereading.best.service.EpisodeReplyService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController//json 사용
@RequestMapping("/episodeReply")
@RequiredArgsConstructor
@Log4j2
public class EpisodeReplyController {
    private final EpisodeReplyService episodeReplyService;

    //댓글 작성
    @PostMapping("/{eno}")
    public ResponseEntity<Long> addEpisodeReply(@RequestBody EpisodeReplyDTO episodeReplyDTO){
        Long erno = episodeReplyService.register(episodeReplyDTO); 

        return new ResponseEntity<>(erno, HttpStatus.OK);
    }
    //댓글 삭제
    @DeleteMapping("/{eno}/{erno}")
    public ResponseEntity<Long> removeEpisodeReply(@PathVariable Long erno){
        log.info("확인용----------------------------------------"+erno);
        episodeReplyService.remove(erno);
        return new ResponseEntity<>(erno, HttpStatus.OK);
    }
    @PutMapping("/{eno}/{erno}")
    public ResponseEntity<Long> modifyEpisodeReply(@RequestBody EpisodeReplyDTO episodeReplyDTO){
        Long erno = episodeReplyDTO.getErno();
        episodeReplyService.modify(episodeReplyDTO);
        log.info("컨트롤확인용----------------------------------------"+episodeReplyDTO);
        
        return new ResponseEntity<>(erno, HttpStatus.OK);
    }


}
