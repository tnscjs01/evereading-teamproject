package com.evereading.best.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.evereading.best.dto.NoticeDTO;
import com.evereading.best.dto.PageRequestDTO;
import com.evereading.best.service.NoticeService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/notice")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    @GetMapping("/list")
    public void list(PageRequestDTO pageRequestDTO, Model model){

        model.addAttribute("result", noticeService.getList(pageRequestDTO));

    }

    @GetMapping("/write")
    public void write(){        
    }

    @PostMapping("/write")
    public String writeNotice(NoticeDTO noticeDto, RedirectAttributes redirectAttributes, @AuthenticationPrincipal UserDetails userDetails){

        Long nono = noticeService.write(noticeDto, userDetails.getUsername());

        redirectAttributes.addFlashAttribute("msgType", "write");
        redirectAttributes.addFlashAttribute("msg", nono);

        return "redirect:/notice/list";

    }

    @GetMapping({"/read", "/modify"})
    public void read(long nono, @ModelAttribute("pageRequestDTO") PageRequestDTO pageRequestDTO, Model model){        

        NoticeDTO noticeDto = noticeService.read(nono);

        model.addAttribute("noticeDto", noticeDto);

    }

    @PostMapping("/delete")
    public String delete(long nono, RedirectAttributes redirectAttributes){

        noticeService.delete(nono);

        redirectAttributes.addFlashAttribute("msgType", "delete");
        redirectAttributes.addFlashAttribute("msg", nono);

        return "redirect:/notice/list";
        
    }

    @PostMapping("/modify")
    public String modify(NoticeDTO noticeDto, @ModelAttribute("pageRequestDTO") PageRequestDTO pageRequestDTO, RedirectAttributes redirectAttributes) {

        noticeService.modify(noticeDto);

        redirectAttributes.addAttribute("page", pageRequestDTO.getPage());
        redirectAttributes.addAttribute("nono", noticeDto.getNono());

        return "redirect:/notice/read";
    }

}
