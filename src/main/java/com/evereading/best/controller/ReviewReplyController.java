package com.evereading.best.controller;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.evereading.best.dto.ReviewReplyDTO;
import com.evereading.best.service.ReviewReplyService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/Rreply")
public class ReviewReplyController {

    private final ReviewReplyService reviewReplyService;

    @PostMapping("/save")
    public ResponseEntity<Long> register(@RequestBody ReviewReplyDTO reviewReplyDTO){
        Long rrno = reviewReplyService.register(reviewReplyDTO);

        return new ResponseEntity<>(rrno, HttpStatus.OK);
    }

    @PutMapping("/{rvno}/{rrno}")
    public ResponseEntity<Long> modify(@PathVariable Long rrno,
                                       @RequestBody ReviewReplyDTO reviewReplyDTO){
    reviewReplyService.modify(reviewReplyDTO);
    return new ResponseEntity<>(rrno,HttpStatus.OK);
    }
    @DeleteMapping("/{rvno}/{rrno}")
    public ResponseEntity<Long> remove(@PathVariable Long rrno){
        reviewReplyService.remove(rrno);
        return new ResponseEntity<>(rrno,HttpStatus.OK);
    }
}
