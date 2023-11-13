package com.evereading.best.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.evereading.best.dto.QuestionDTO;
import com.evereading.best.dto.PageRequestDTO;
import com.evereading.best.service.QuestionService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/question")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    @GetMapping("/list")
    public void list(PageRequestDTO pageRequestDTO, Model model, Authentication authentication) {

        boolean isAdmin = authentication.getAuthorities().stream()
                                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin) {

            model.addAttribute("result", questionService.getList(pageRequestDTO));

        } else {

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();

            model.addAttribute("result", questionService.getListByUser(pageRequestDTO, username));

        }

    }

    @GetMapping("/write")
    public void write(){        
    }

    @PostMapping("/write")
    public String writeQuestion(QuestionDTO questionDto, RedirectAttributes redirectAttributes, Authentication authentication) {
        
        String username = authentication.getName();
        questionDto.setEmail(username);
        Long qno = questionService.write(questionDto);

        redirectAttributes.addFlashAttribute("msgType", "write");
        redirectAttributes.addFlashAttribute("msg", qno);

        return "redirect:/question/list";

    }

    @GetMapping({"/read", "/modify"})
    public void read(long qno, @ModelAttribute("pageRequestDTO") PageRequestDTO pageRequestDTO, Model model){        

        QuestionDTO questionDto = questionService.read(qno);

        model.addAttribute("questionDto", questionDto);

    }

    @PostMapping("/delete")
    public String delete(long qno, RedirectAttributes redirectAttributes){

        QuestionDTO questionDto = questionService.read(qno);

        questionService.delete(qno);

        redirectAttributes.addFlashAttribute("msgType", "delete");
        redirectAttributes.addFlashAttribute("msg", qno);
        redirectAttributes.addFlashAttribute("writer", questionDto.getEmail());

        return "redirect:/question/list";

    }

    @PostMapping("/modify")
    public String modify(QuestionDTO questionDto, @ModelAttribute("pageRequestDTO") PageRequestDTO pageRequestDTO, RedirectAttributes redirectAttributes) {

        questionService.modify(questionDto);

        redirectAttributes.addAttribute("page", pageRequestDTO.getPage());
        redirectAttributes.addAttribute("qno", questionDto.getQno());

        return "redirect:/question/read";
        
    }

}
