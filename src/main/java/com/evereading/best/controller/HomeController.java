package com.evereading.best.controller;

import java.time.LocalDateTime;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.evereading.best.dto.PageRequestDTO;
import com.evereading.best.service.EventService;
import com.evereading.best.service.NovelService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final NovelService novelService;
    private final EventService eventService;

    @GetMapping("/home")
    public void home(PageRequestDTO pageRequestDTO, Model model) {

        model.addAttribute("resultNovel", novelService.getListByTotalCount(pageRequestDTO));
        model.addAttribute("resultEvent", eventService.getList(pageRequestDTO));

        // "NEW" 및 "UP"에 대한 임계값 날짜 계산
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime isNew = now.minusDays(3);
        LocalDateTime isUp = now.minusDays(1);

        model.addAttribute("isNew", isNew);
        model.addAttribute("isUp", isUp);

    }
    
}
