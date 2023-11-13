package com.evereading.best.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.evereading.best.dto.EventDTO;
import com.evereading.best.dto.PageRequestDTO;
import com.evereading.best.service.EventService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/event")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @GetMapping("/list")
    public void list(PageRequestDTO pageRequestDTO, Model model){

        model.addAttribute("result", eventService.getList(pageRequestDTO));

    }

    @GetMapping("/register")
    public void register() {
    }

    @PostMapping("/register")
    public String register(@ModelAttribute EventDTO eventDTO, RedirectAttributes redirectAttributes, @AuthenticationPrincipal UserDetails userDetails, 
                        @RequestParam("carouselImageFile") MultipartFile carouselImageFile, @RequestParam("eventImageFile") MultipartFile eventImageFile) {

        Long evno = eventService.register(eventDTO, userDetails.getUsername(), carouselImageFile, eventImageFile);

        redirectAttributes.addFlashAttribute("msgType", "register");
        redirectAttributes.addFlashAttribute("msg", evno);

        return "redirect:/event/list";

    }

    @PostMapping("/modify")
    public String modify(EventDTO eventDto, @ModelAttribute("pageRequestDTO") PageRequestDTO pageRequestDTO, RedirectAttributes redirectAttributes, 
                        @RequestParam("carouselImageFile") MultipartFile carouselImageFile, @RequestParam("eventImageFile") MultipartFile eventImageFile, 
                        @AuthenticationPrincipal UserDetails userDetails) {

        eventService.modify(eventDto, carouselImageFile, eventImageFile, userDetails);
        
        redirectAttributes.addAttribute("page", pageRequestDTO.getPage());
        redirectAttributes.addAttribute("evno", eventDto.getEvno());

        return "redirect:/event/list?nno="+eventDto.getEvno();

    }

    @GetMapping({"/read", "/modify"})
    public void read(long evno, @ModelAttribute("pageRequestDTO") PageRequestDTO pageRequestDTO, Model model){        

        EventDTO eventDto = eventService.read(evno);

        model.addAttribute("eventDto", eventDto);

    }

    @PostMapping("/delete")
    public String delete(long evno, RedirectAttributes redirectAttributes){
        
        eventService.delete(evno);

        redirectAttributes.addFlashAttribute("msgType", "delete");
        redirectAttributes.addFlashAttribute("msg", evno);

        return "redirect:/event/list";

    }
    
}