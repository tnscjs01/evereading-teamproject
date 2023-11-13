package com.evereading.best.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.evereading.best.dto.FreeReplyDTO;
import com.evereading.best.dto.PageRequestDTO;
import com.evereading.best.dto.PageResultDTO;
import com.evereading.best.entity.FreeBoard;
import com.evereading.best.entity.FreeReply;
import com.evereading.best.repository.FreeBoardRepository;
import com.evereading.best.service.FreeBoardService;
import com.evereading.best.service.FreeReplyService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Controller
@RequestMapping("/free")
@RequiredArgsConstructor
public class FreeBoardController {
    
    private final FreeBoardService freeBoardService;
    private final FreeBoardRepository freeBoardRepository;
    private final FreeReplyService freeReplyService;

    @GetMapping("/list")
    public String list(Model model, @AuthenticationPrincipal UserDetails userDetails,
                  @PageableDefault(page = 0, size = 10, sort = "fno", direction = Sort.Direction.DESC) Pageable pageable,
                  @RequestParam(required = false, defaultValue = "") String searchKeyword){
    
    Page<FreeBoard> result = freeBoardService.getList(pageable);
    
    if (searchKeyword.isEmpty()) {
        result = freeBoardService.getList(pageable);
    } else {
        result = freeBoardRepository.findBytitleContaining(searchKeyword, pageable);
    }

    if (userDetails != null){
        model.addAttribute("email", userDetails.getUsername());
    }

    int nowPage = result.getNumber() + 1; 
    int startPage = Math.max(1, result.getNumber() - 9);
    int endPage = Math.min(result.getTotalPages(), result.getNumber() + 9);
    
    model.addAttribute("list", result);
    model.addAttribute("nowPage", nowPage);
    model.addAttribute("startPage", startPage);
    model.addAttribute("endPage", endPage);
    
    return "free/list";
}

    @GetMapping("/register")
    public void register(@AuthenticationPrincipal UserDetails userDetails,
                           Model model){
        if (userDetails != null){
            model.addAttribute("email", userDetails.getUsername());
        }
    }

    @PostMapping("/register")
    public String register(FreeBoard vo,@AuthenticationPrincipal UserDetails userDetails,
                           Model model){
        freeBoardService.register(vo);
        System.out.println(userDetails);
        if (userDetails != null){
            model.addAttribute("email", userDetails.getUsername());
        }
        return "redirect:/free/list";
    }

    @Transactional
    @GetMapping("/read")
    public String read(@RequestParam("fno") long fno, Model model, PageRequestDTO pageRequestDTO){
        // 조회수
        FreeBoard board = freeBoardRepository.findById(fno).get();
        board.setCount(board.getCount() + 1);
        // 댓글수
        freeBoardService.ReplyCount(fno);
        // 댓글리스트 
        PageResultDTO<FreeReplyDTO, FreeReply> freeReplyDTOList = freeReplyService.getListofReply(fno, pageRequestDTO);
        model.addAttribute("reply", freeReplyDTOList);
        // 추천수
        freeBoardService.getRecommendCnt(fno);
        // 상세보기
        FreeBoard vo=freeBoardService.read(fno);
        model.addAttribute("vo", vo);
        
        return "free/read";
    } 

    @GetMapping("/modify/{fno}")
    public String modify(@PathVariable("fno") long fno, Model model){
        model.addAttribute("vo", freeBoardService.read(fno));
        return "free/modify";
    }
    
    @PostMapping("/modify/{fno}")
    public String modify(@PathVariable("fno") long fno, FreeBoard vo,
                         @AuthenticationPrincipal UserDetails userDetails,Model model){
        FreeBoard boardTemp = freeBoardService.read(fno);
        boardTemp.setTitle(vo.getTitle());
        boardTemp.setContent(vo.getContent());
        if (userDetails != null){
            model.addAttribute("email", userDetails.getUsername());
        }
        freeBoardService.register(boardTemp);

        return "redirect:/free/list";
    }

    @GetMapping("/delete")
    public String delete(long fno){
        freeBoardService.delete(fno);
        return "redirect:/free/list";
    }
}
