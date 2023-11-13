package com.evereading.best.controller;

import java.time.LocalDateTime;

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

import com.evereading.best.dto.NovelDTO;
import com.evereading.best.dto.PageRequestDTO;
import com.evereading.best.dto.PageResultDTO;
import com.evereading.best.entity.Novel;
import com.evereading.best.service.NovelService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/novel")
@RequiredArgsConstructor
public class NovelController {
    
    private final NovelService novelService;

    @GetMapping("/list")
    public void list(PageRequestDTO pageRequestDTO, Model model){
        model.addAttribute("result", novelService.getList(pageRequestDTO));

        // "NEW" 및 "UP"에 대한 임계값 날짜 계산
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime isNew = now.minusDays(3);
        LocalDateTime isUp = now.minusDays(1);

        model.addAttribute("isNew", isNew);
        model.addAttribute("isUp", isUp);
    }

    @GetMapping("/register")
    public void register(){        
    }

    @PostMapping("/register")
    public String register(NovelDTO novelDTO, RedirectAttributes redirectAttributes, 
                        @AuthenticationPrincipal UserDetails userDetails, MultipartFile imageFile){

        Long nno = novelService.register(novelDTO, userDetails.getUsername(), imageFile);

        redirectAttributes.addFlashAttribute("msgType", "register");
        redirectAttributes.addFlashAttribute("msg", nno);

        return "redirect:/library/myNovel?email="+userDetails.getUsername();

    }

    @GetMapping("/modify")
    public void readForModify(long nno, @ModelAttribute("pageRequestDTO") PageRequestDTO pageRequestDTO, Model model, 
                            @AuthenticationPrincipal UserDetails userDetails, RedirectAttributes redirectAttributes){        

        NovelDTO novelDto = novelService.readForModify(nno, userDetails);

        String username = userDetails.getUsername();

        model.addAttribute("username", username);        
        model.addAttribute("novelDto", novelDto);

    }    

    @PostMapping("/modify")
    public String modify(NovelDTO novelDto, @ModelAttribute("pageRequestDTO") PageRequestDTO pageRequestDTO, 
                        RedirectAttributes redirectAttributes, MultipartFile imageFile, @AuthenticationPrincipal UserDetails userDetails) {

        novelService.modify(novelDto, imageFile, userDetails);
        
        redirectAttributes.addAttribute("page", pageRequestDTO.getPage());
        redirectAttributes.addAttribute("nno", novelDto.getNno());

        return "redirect:/episode/list?nno="+novelDto.getNno();

    }

    @PostMapping("/delete")
    public String delete(long nno, RedirectAttributes redirectAttributes, @AuthenticationPrincipal UserDetails userDetails){

        novelService.delete(nno, userDetails);

        redirectAttributes.addFlashAttribute("msgType", "delete");
        redirectAttributes.addFlashAttribute("msg", nno);

        return "redirect:/novel/list";

    }

    @GetMapping("/search")
    public String search(@RequestParam("keyword") String keyword, PageRequestDTO pageRequestDTO, Model model) {

        PageResultDTO<NovelDTO, Novel> result = novelService.search(pageRequestDTO, keyword);

        model.addAttribute("result", result);
        model.addAttribute("keyword", keyword);
        
        // "NEW" 및 "UP"에 대한 임계값 날짜 계산
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime isNew = now.minusDays(3);
        LocalDateTime isUp = now.minusDays(1);

        model.addAttribute("isNew", isNew);
        model.addAttribute("isUp", isUp);

        return "/novel/list";

    }
    
    
    @GetMapping("/fantasy")
    public void listFantasy(PageRequestDTO pageRequestDTO, Model model) {

        PageResultDTO<NovelDTO, Novel> result = novelService.getListByCategory(pageRequestDTO, "판타지");

        model.addAttribute("result", result);
        
        // "NEW" 및 "UP"에 대한 임계값 날짜 계산
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime isNew = now.minusDays(3);
        LocalDateTime isUp = now.minusDays(1);

        model.addAttribute("isNew", isNew);
        model.addAttribute("isUp", isUp);

    }

    @GetMapping("/martialArts")
    public void listMartialArts(PageRequestDTO pageRequestDTO, Model model) {

        PageResultDTO<NovelDTO, Novel> result = novelService.getListByCategory(pageRequestDTO, "무협");

        model.addAttribute("result", result);
        
        // "NEW" 및 "UP"에 대한 임계값 날짜 계산
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime isNew = now.minusDays(3);
        LocalDateTime isUp = now.minusDays(1);

        model.addAttribute("isNew", isNew);
        model.addAttribute("isUp", isUp);

    }

    @GetMapping("/modern")
    public void listModern(PageRequestDTO pageRequestDTO, Model model) {

        PageResultDTO<NovelDTO, Novel> result = novelService.getListByCategory(pageRequestDTO, "현대");

        model.addAttribute("result", result);
        
        // "NEW" 및 "UP"에 대한 임계값 날짜 계산
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime isNew = now.minusDays(3);
        LocalDateTime isUp = now.minusDays(1);

        model.addAttribute("isNew", isNew);
        model.addAttribute("isUp", isUp);

    }

    @GetMapping("/romance")
    public void listRomance(PageRequestDTO pageRequestDTO, Model model) {

        PageResultDTO<NovelDTO, Novel> result = novelService.getListByCategory(pageRequestDTO, "로맨스");

        model.addAttribute("result", result);
        
        // "NEW" 및 "UP"에 대한 임계값 날짜 계산
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime isNew = now.minusDays(3);
        LocalDateTime isUp = now.minusDays(1);

        model.addAttribute("isNew", isNew);
        model.addAttribute("isUp", isUp);

    }

    @GetMapping("/other")
    public void listOther(PageRequestDTO pageRequestDTO, Model model) {

        PageResultDTO<NovelDTO, Novel> result = novelService.getListByCategory(pageRequestDTO, "기타");

        model.addAttribute("result", result);
        
        // "NEW" 및 "UP"에 대한 임계값 날짜 계산
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime isNew = now.minusDays(3);
        LocalDateTime isUp = now.minusDays(1);

        model.addAttribute("isNew", isNew);
        model.addAttribute("isUp", isUp);

    }

    private String getCategoryText(String category) {

        String categoryText = "";
    
        switch (category) {
            case "판타지":
                categoryText = "fantasy";
                break;
            case "무협":
                categoryText = "martialArts";
                break;
            case "현대":
                categoryText = "modern";
                break;
            case "로맨스":
                categoryText = "romance";
                break;
            case "기타":
                categoryText = "other";
                break;
        }    
        return categoryText;

    }

    @GetMapping("/searchByCategory")
    public String searchByCategory(@RequestParam("category") String category, @RequestParam("keyword") String keyword, 
                                    PageRequestDTO pageRequestDTO, Model model) {

        String categoryText = getCategoryText(category);
        
        if ("장르선택".equals(category)) {
            category = ""; // "장르선택"이 선택된 경우, category를 빈 문자열로 설정하여 모든 장르에서 검색하도록 합니다.
        }
        
        PageResultDTO<NovelDTO, Novel> result = novelService.searchByCategory(category, keyword, pageRequestDTO);

        model.addAttribute("result", result);
        model.addAttribute("category", category);
        model.addAttribute("keyword", keyword);
        model.addAttribute("categoryText", categoryText);

        // "NEW" 및 "UP"에 대한 임계값 날짜 계산
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime isNew = now.minusDays(3);
        LocalDateTime isUp = now.minusDays(1);

        model.addAttribute("isNew", isNew);
        model.addAttribute("isUp", isUp);
        
        return "/novel/" + categoryText;
    }
    
    @GetMapping("/total")
    public void total(PageRequestDTO pageRequestDTO, Model model) {

        model.addAttribute("result", novelService.getListByTotalCount(pageRequestDTO));

        // "NEW" 및 "UP"에 대한 임계값 날짜 계산
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime isNew = now.minusDays(3);
        LocalDateTime isUp = now.minusDays(1);

        model.addAttribute("isNew", isNew);
        model.addAttribute("isUp", isUp);

    }

    @GetMapping("/completed")
    public void listCompleted(PageRequestDTO pageRequestDTO, Model model) {

        PageResultDTO<NovelDTO, Novel> result = novelService.getCompletedNovelList(pageRequestDTO);

        model.addAttribute("result", result);

        // "NEW" 및 "UP"에 대한 임계값 날짜 계산
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime isNew = now.minusDays(3);
        LocalDateTime isUp = now.minusDays(1);

        model.addAttribute("isNew", isNew);
        model.addAttribute("isUp", isUp);
    }
    
    // 임시 소설 페이지 개발 완료 후 삭제
    @GetMapping("/temporary")
    public void temporary(PageRequestDTO pageRequestDTO, Model model){

        model.addAttribute("result", novelService.getList(pageRequestDTO));

        // "NEW" 및 "UP"에 대한 임계값 날짜 계산
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime isNew = now.minusDays(3);
        LocalDateTime isUp = now.minusDays(1);

        model.addAttribute("isNew", isNew);
        model.addAttribute("isUp", isUp);
    }

    @GetMapping("/monthly")
    public void monthly(PageRequestDTO pageRequestDTO, Model model) {

        model.addAttribute("result", novelService.getListByMonthlyCount(pageRequestDTO));

        // "NEW" 및 "UP"에 대한 임계값 날짜 계산
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime isNew = now.minusDays(3);
        LocalDateTime isUp = now.minusDays(1);

        model.addAttribute("isNew", isNew);
        model.addAttribute("isUp", isUp);

    }

    @GetMapping("/weekly")
    public void weekly(PageRequestDTO pageRequestDTO, Model model) {

        model.addAttribute("result", novelService.getListByWeeklyCount(pageRequestDTO));

        // "NEW" 및 "UP"에 대한 임계값 날짜 계산
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime isNew = now.minusDays(3);
        LocalDateTime isUp = now.minusDays(1);

        model.addAttribute("isNew", isNew);
        model.addAttribute("isUp", isUp);

    }

    
    @GetMapping("/daily")
    public void daily(PageRequestDTO pageRequestDTO, Model model) {

        model.addAttribute("result", novelService.getListByDailyCount(pageRequestDTO));

        // "NEW" 및 "UP"에 대한 임계값 날짜 계산
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime isNew = now.minusDays(3);
        LocalDateTime isUp = now.minusDays(1);

        model.addAttribute("isNew", isNew);
        model.addAttribute("isUp", isUp);

    }
    
}
