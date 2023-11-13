package com.evereading.best.controller;

import java.util.List;

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

import com.evereading.best.dto.FreeReplyDTO;
import com.evereading.best.service.FreeReplyService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/Freply")
public class FreeReplyController {
    
    private final FreeReplyService freeReplyService;

    @PostMapping("/save")
    public ResponseEntity<Long> register(@RequestBody FreeReplyDTO freeReplyDTO){

        Long frno = freeReplyService.register(freeReplyDTO);

        return new ResponseEntity<>(frno, HttpStatus.OK);
    }

    @PutMapping("/{fno}/{frno}")
    public ResponseEntity<Long> modify(@PathVariable Long frno,
                                       @RequestBody FreeReplyDTO freeReplyDTO){
        freeReplyService.modify(freeReplyDTO);
        
        return new ResponseEntity<>(frno,HttpStatus.OK);
    }
    @DeleteMapping("/{fno}/{frno}")
    public ResponseEntity<Long> remove(@PathVariable Long frno){

        freeReplyService.remove(frno);

        return new ResponseEntity<>(frno,HttpStatus.OK);
    }
}