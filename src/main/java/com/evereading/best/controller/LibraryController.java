package com.evereading.best.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.evereading.best.dto.BookmarkDTO;
import com.evereading.best.dto.NovelDTO;
import com.evereading.best.dto.PageRequestDTO;
import com.evereading.best.dto.PageResultDTO;
import com.evereading.best.dto.RecentNovelDTO;
import com.evereading.best.entity.Bookmark;
import com.evereading.best.entity.Novel;
import com.evereading.best.entity.RecentNovel;
import com.evereading.best.service.BookmarkService;
import com.evereading.best.service.NovelService;
import com.evereading.best.service.RecentNovelService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/library")
@RequiredArgsConstructor
public class LibraryController {
    private final BookmarkService bookmarkService;
    private final RecentNovelService recentNovelService;
    private final NovelService novelService;

    @GetMapping("")
    public String loginLibrary(@AuthenticationPrincipal UserDetails userDetails){
        String username = userDetails.getUsername();

        return "redirect:/library/recent?email="+username;
    }

    // 페이지 이동시 ?email= 값 줘야됨
    // 북마크
    @GetMapping("/bookmark")
    public void bookmarkPage(Model model,String email, PageRequestDTO pageRequestDTO){
        // List<BookmarkDTO> result = bookmarkService.getList(email);
        PageResultDTO<BookmarkDTO, Bookmark> result = bookmarkService.getList(email, pageRequestDTO);
        model.addAttribute("list", result);
    }

    //최근 본 목록
    @GetMapping("/recent")
    public void recentPage(Model model, String email, PageRequestDTO pageRequestDTO){
        // List<RecentNovelDTO> list = recentNovelService.getRecentList(email);
        PageResultDTO<RecentNovelDTO, RecentNovel> result = recentNovelService.getRecentList(email, pageRequestDTO);
        model.addAttribute("list", result);
    }
    
    @GetMapping("/myNovel")
    public void myNovelPage(Model model, String email,PageRequestDTO pageRequestDTO){
       PageResultDTO<NovelDTO,Novel> list = novelService.myNovelList(email,pageRequestDTO);
        model.addAttribute("list", list);
    }
    @GetMapping("/my")
    public String myNovelLoginCheck(@AuthenticationPrincipal UserDetails userDetails){
        String username = userDetails.getUsername();

        return "redirect:/library/myNovel?email="+username;
    }
}
