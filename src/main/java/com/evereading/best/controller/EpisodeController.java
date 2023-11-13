package com.evereading.best.controller;


import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.evereading.best.dto.EpisodeDTO;
import com.evereading.best.dto.EpisodeReplyDTO;
import com.evereading.best.dto.NovelDTO;
import com.evereading.best.dto.PageRequestDTO;
import com.evereading.best.dto.PageResultDTO;
import com.evereading.best.dto.RecentNovelDTO;
import com.evereading.best.entity.Episode;
import com.evereading.best.entity.EpisodeReply;
import com.evereading.best.service.BookmarkService;
import com.evereading.best.service.EpisodeReplyService;
import com.evereading.best.service.EpisodeService;
import com.evereading.best.service.NovelService;
import com.evereading.best.service.RecentNovelService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;


@Controller
@RequiredArgsConstructor
@RequestMapping("/episode")
@Log4j2
public class EpisodeController {
    
    private final EpisodeService episodeService;
    private final EpisodeReplyService episodeReplyService;
    private final NovelService novelService;
    private final BookmarkService bookmarkService; //좋아요 버튼..
    private final RecentNovelService recentNovelService; 

    //목록
    @GetMapping("/list")
    public void list(Model model, PageRequestDTO pageRequestDTO, Long nno,@AuthenticationPrincipal UserDetails userDetails){
        PageResultDTO<EpisodeDTO, Episode> result = episodeService.getList(nno,pageRequestDTO);
        NovelDTO novelDTO = novelService.read(nno);
        log.info(novelDTO);

        Long firstEpisode = episodeService.getFirstEpisode(nno);
        Long totalCount = episodeService.getTotalCount(nno);
        Long totalEpisode = episodeService.getTotalEpisode(nno);

        Long bookmarkCount = bookmarkService.bookmarkCount(nno);

        //로그인 했을때만 사용되는 항목
        boolean bookmarkCheck; //nullpoint error 방지...
        if(userDetails!=null){
            bookmarkCheck = bookmarkService.isBookmarkOn(nno,userDetails.getUsername());
            RecentNovelDTO recentDTO = recentNovelService.findRecent(userDetails.getUsername(), nno);

            model.addAttribute("recent", recentDTO);
            model.addAttribute("username", userDetails.getUsername());
        }else{
            bookmarkCheck = false;
        }

        model.addAttribute("novel",novelDTO);
        model.addAttribute("result",result);

        model.addAttribute("first",firstEpisode); 
        model.addAttribute("totalCount",totalCount);
        model.addAttribute("totalEpisode",totalEpisode);
        model.addAttribute("bookmarkCheck",bookmarkCheck);
        model.addAttribute("bookmarkCount",bookmarkCount);
    }

    //작성
    @GetMapping("/register")
    public void register(@ModelAttribute("nno") Long nno){
    };

    @PostMapping("/register")
    public String register(EpisodeDTO episodeDTO){ //return에 redirct 사용시 타입 String으로 하기
        episodeService.register(episodeDTO);
        Long nno = episodeDTO.getNno();

        return "redirect:/episode/list?nno="+nno;//글 작성 후 목록으로 이동
    }

    //읽기
    @GetMapping({"/read","/modify"})
    public void read(Long eno, Long nno, Model model,PageRequestDTO pageRequestDTO){
        //에피소드
        EpisodeDTO result = episodeService.readNoCount(eno);
        Long previous = episodeService.findPreviousEpisode(eno, nno);
        Long next = episodeService.findNextEpisode(eno, nno);
        NovelDTO novel = novelService.read(nno);
        //댓글
        PageResultDTO<EpisodeReplyDTO, EpisodeReply> reply = episodeReplyService.getListEpisodeReply(eno, pageRequestDTO);

        model.addAttribute("dto",result);
        model.addAttribute("reply",reply);
        model.addAttribute("pre",previous);
        model.addAttribute("next",next);
        model.addAttribute("novel",novel);
    }

    //수정
    @PostMapping("modify")
    public String modify(EpisodeDTO episodeDTO){
        episodeService.modify(episodeDTO);

        return "redirect:/episode/list?nno="+episodeDTO.getNno();
    }

    //삭제
    @GetMapping("/delete")
    public String delete(Long eno,Long nno){
        episodeService.delete(eno);

        return "redirect:/episode/list?nno="+nno;
    }

    //뷰페이지
    @GetMapping("/viewer")
    public void viewerPage(Long nno, Long eno, Model model){
        EpisodeDTO result = episodeService.read(eno);

        Long previous = episodeService.findPreviousEpisode(eno, nno);
        Long next = episodeService.findNextEpisode(eno, nno);

        model.addAttribute("dto",result);
        model.addAttribute("pre",previous);
        model.addAttribute("next",next);
    }
    
}
