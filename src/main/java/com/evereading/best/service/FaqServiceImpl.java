package com.evereading.best.service;

import java.util.Optional;
import java.util.function.Function;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.evereading.best.dto.PageRequestDTO;
import com.evereading.best.dto.PageResultDTO;
import com.evereading.best.dto.FaqDTO;
import com.evereading.best.entity.Faq;
import com.evereading.best.entity.Member;
import com.evereading.best.repository.FaqRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FaqServiceImpl implements FaqService {
    
    private final FaqRepository faqRepository;

    @Override
    public Long write(FaqDTO faqDto, String email) { 

        Faq faq = dtoToEntity(faqDto);
        
        faq.setMember(Member.builder().email(email).build());
        
        faqRepository.save(faq);

        return faq.getFaqno();
    }

    @Override
    public PageResultDTO<FaqDTO, Faq> getList(PageRequestDTO pageRequestDTO) {
        
        Pageable pageable = pageRequestDTO.getPageable(Sort.by("faqno").descending());

        Page<Faq> result = faqRepository.findAll(pageable);

        Function<Faq, FaqDTO> fn = (faq -> entityToDto(faq));

        return new PageResultDTO<>(result, fn);
    }

    @Override
    public FaqDTO read(Long faqno) {

        Optional<Faq> result = faqRepository.findById(faqno);

        return result.isPresent()? entityToDto(result.get()): null;
    }

    @Override
    public void delete(Long faqno) {

        faqRepository.deleteById(faqno);

    }

    @Override
    public void modify(FaqDTO faqDto) {

        Optional<Faq> result = faqRepository.findById(faqDto.getFaqno());

        if(result.isPresent()){

            Faq faq = result.get();

            faq.changeTitle(faqDto.getTitle());
            faq.changeContent(faqDto.getContent());

            faqRepository.save(faq);

        }
    }


}
