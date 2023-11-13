package com.evereading.best.service;

import java.util.Optional;
import java.util.function.Function;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.evereading.best.dto.PageRequestDTO;
import com.evereading.best.dto.PageResultDTO;
import com.evereading.best.dto.NoticeDTO;
import com.evereading.best.entity.Member;
import com.evereading.best.entity.Notice;
import com.evereading.best.repository.NoticeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {
    
    private final NoticeRepository noticeRepository;

    @Override
    public Long write(NoticeDTO noticeDto, String email) { 

        Notice notice = dtoToEntity(noticeDto);
        
        notice.setMember(Member.builder().email(email).build());

        noticeRepository.save(notice);

        return notice.getNono();
    }

    @Override
    public PageResultDTO<NoticeDTO, Notice> getList(PageRequestDTO pageRequestDTO) {
        
        Pageable pageable = pageRequestDTO.getPageable(Sort.by("nono").descending());

        Page<Notice> result = noticeRepository.findAll(pageable);

        Function<Notice, NoticeDTO> fn = (notice -> entityToDto(notice));

        return new PageResultDTO<>(result, fn);
    }


    @Override
    public NoticeDTO read(Long nono) {

        Optional<Notice> result = noticeRepository.findById(nono);

        return result.isPresent()? entityToDto(result.get()): null;
    }

    @Override
    public void delete(Long nono) {

        noticeRepository.deleteById(nono);

    }

    @Override
    public void modify(NoticeDTO noticeDto) {

        Optional<Notice> result = noticeRepository.findById(noticeDto.getNono());

        if(result.isPresent()){

            Notice notice = result.get();

            notice.changeTitle(noticeDto.getTitle());
            notice.changeContent(noticeDto.getContent());

            noticeRepository.save(notice);

        }
        
    }

}