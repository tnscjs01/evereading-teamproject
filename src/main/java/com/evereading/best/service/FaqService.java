package com.evereading.best.service;

import com.evereading.best.dto.FaqDTO;
import com.evereading.best.dto.PageRequestDTO;
import com.evereading.best.dto.PageResultDTO;
import com.evereading.best.entity.Faq;
import com.evereading.best.entity.Member;

public interface FaqService {

    Long write(FaqDTO faqDTO, String email);

    PageResultDTO<FaqDTO, Faq> getList(PageRequestDTO pageRequestDTO);
    
    FaqDTO read(Long faqno);

    void modify(FaqDTO faqDTO);

    void delete(Long faqno);    

    default Faq dtoToEntity(FaqDTO faqDTO) {
        Faq faq = Faq.builder()
                .faqno(faqDTO.getFaqno())
                .title(faqDTO.getTitle())
                .content(faqDTO.getContent())
                                .member(Member.builder()
                                .email(faqDTO.getEmail())                        
                                .build())                 
                .build();
        return faq;
    }

    default FaqDTO entityToDto(Faq faq) {
    FaqDTO faqDTO = FaqDTO.builder()
            .faqno(faq.getFaqno())
            .title(faq.getTitle())
            .content(faq.getContent())
            .regdate(faq.getRegdate())
            .email(faq.getMember().getEmail())
            .build();
    return faqDTO;
    }
    
}
