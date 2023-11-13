package com.evereading.best.controller;

import java.util.List;

import org.apache.tomcat.jni.User;
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

import com.evereading.best.dto.PageRequestDTO;
import com.evereading.best.dto.PageResultDTO;
import com.evereading.best.dto.ReviewReplyDTO;
import com.evereading.best.entity.Review;
import com.evereading.best.entity.ReviewReply;
import com.evereading.best.repository.ReviewRepository;
import com.evereading.best.service.ReviewReplyService;
import com.evereading.best.service.ReviewService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Controller
@RequestMapping("/review")
@Log4j2
@RequiredArgsConstructor
public class ReviewController {
    
    private final ReviewService reviewService;
    private final ReviewRepository reviewRepository;
    private final ReviewReplyService reviewReplyService;

    @GetMapping("/list")
    public String list(Model model, @AuthenticationPrincipal UserDetails userDetails,
                  @PageableDefault(page = 0, size = 10, sort = "rvno", direction = Sort.Direction.DESC) Pageable pageable,
                  @RequestParam(required = false, defaultValue = "") String searchKeyword){
    
    Page<Review> list = reviewService.getList(pageable);
    
    if (searchKeyword.isEmpty()) {
        list = reviewService.getList(pageable);
    } else {
        list = reviewRepository.findBytitleContaining(searchKeyword, pageable);
    }

    if (userDetails != null){
        model.addAttribute("email", userDetails.getUsername());
    }
    int nowPage = list.getNumber() + 1; 
    int startPage = Math.max(1, list.getNumber() - 9);
    int endPage = Math.min(list.getTotalPages(), list.getNumber() + 9);
    
    model.addAttribute("list", list);
    model.addAttribute("nowPage", nowPage);
    model.addAttribute("startPage", startPage);
    model.addAttribute("endPage", endPage);
    
    return "review/list";
}

    @GetMapping("/register")
    public void register(@AuthenticationPrincipal UserDetails userDetails,
                          Model model){
        if (userDetails != null){
            model.addAttribute("email", userDetails.getUsername());
        }
    }

    @PostMapping("/register")
    public String register(Review vo, @RequestParam("category") String category, @AuthenticationPrincipal UserDetails userDetails,Model model){
        if (userDetails != null){
            model.addAttribute("email", userDetails.getUsername());
        }

        vo.setCategory(category);

        reviewService.register(vo);
        return "redirect:/review/list";
    }

    @Transactional
    @GetMapping("/read")
    public String read(@RequestParam("rvno") long rvno, Model model, PageRequestDTO pageRequestDTO){
        // 조회수
        Review review = reviewRepository.findById(rvno).get();
        review.setCount(review.getCount() + 1);
        // 댓글수
        reviewService.ReplyCount(rvno);
        // 댓글리스트
        PageResultDTO<ReviewReplyDTO, ReviewReply> reviewReplyDTOList = reviewReplyService.getListofReply(rvno, pageRequestDTO);
        model.addAttribute("reply", reviewReplyDTOList);
        // 추천수
        reviewService.getRecommendCnt(rvno);
        // 상세보기
        Review vo = reviewService.read(rvno);
        model.addAttribute("vo", vo);
    
        return "review/read";
    } 

    @GetMapping("/modify/{rvno}")
    public String modify(@PathVariable("rvno") long rvno, Model model){
        model.addAttribute("vo", reviewService.read(rvno));
        return "review/modify";
    }
    
    @PostMapping("/modify/{rvno}")
    public String modify(@PathVariable("rvno") long rvno, Review vo, @RequestParam("category") String category,
                         @AuthenticationPrincipal UserDetails userDetails, Model model){
        Review boardTemp = reviewService.read(rvno);
        boardTemp.setTitle(vo.getTitle());
        boardTemp.setContent(vo.getContent());

        boardTemp.setCategory(category);
        if (userDetails != null){
            model.addAttribute("email", userDetails.getUsername());
        }
        reviewService.register(boardTemp);

        return "redirect:/review/list";
    }

    @GetMapping("/delete")
    public String delete(long rvno){
        reviewService.delete(rvno);
        return "redirect:/review/list";
    }
}
