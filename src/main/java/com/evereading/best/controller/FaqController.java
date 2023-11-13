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

import com.evereading.best.dto.FaqDTO;
import com.evereading.best.dto.PageRequestDTO;
import com.evereading.best.service.FaqService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/faq")
@RequiredArgsConstructor
public class FaqController {

    private final FaqService faqService;

    @GetMapping("/list")
    public void list(PageRequestDTO pageRequestDTO, Model model){
        
        model.addAttribute("result", faqService.getList(pageRequestDTO));

        String[] englishNumbers = {"One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten"};

        model.addAttribute("englishNumbers", englishNumbers);

    }

    @GetMapping("/write")
    public void write(){        
    }

    @PostMapping("/write")
    public String writeFaq(FaqDTO faqDto, RedirectAttributes redirectAttributes, @AuthenticationPrincipal UserDetails userDetails){

        Long faqno = faqService.write(faqDto, userDetails.getUsername());

        redirectAttributes.addFlashAttribute("msgType", "write");
        redirectAttributes.addFlashAttribute("msg", faqno);

        return "redirect:/faq/list";

    }

    @GetMapping({"/read", "/modify"})
    public void read(long faqno, @ModelAttribute("pageRequestDTO") PageRequestDTO pageRequestDTO, Model model){        

        FaqDTO faqDto = faqService.read(faqno);

        model.addAttribute("faqDto", faqDto);

    }

    @PostMapping("/delete")
    public String delete(long faqno, RedirectAttributes redirectAttributes){

        faqService.delete(faqno);

        redirectAttributes.addFlashAttribute("msgType", "delete");
        redirectAttributes.addFlashAttribute("msg", faqno);

        return "redirect:/faq/list";

    }

    @PostMapping("/modify")
    public String modify(FaqDTO faqDto, @ModelAttribute("pageRequestDTO") PageRequestDTO pageRequestDTO, RedirectAttributes redirectAttributes) {

        faqService.modify(faqDto);

        redirectAttributes.addAttribute("page", pageRequestDTO.getPage());
        redirectAttributes.addAttribute("faqno", faqDto.getFaqno());

        return "redirect:/faq/read";

    }

}