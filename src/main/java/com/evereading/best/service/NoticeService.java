package com.evereading.best.service;

import com.evereading.best.dto.NoticeDTO;
import com.evereading.best.dto.PageRequestDTO;
import com.evereading.best.dto.PageResultDTO;
import com.evereading.best.entity.Member;
import com.evereading.best.entity.Notice;

public interface NoticeService {

    Long write(NoticeDTO noticeDTO, String email);

    PageResultDTO<NoticeDTO, Notice> getList(PageRequestDTO pageRequestDTO);
    
    NoticeDTO read(Long nono);

    void modify(NoticeDTO noticeDTO);

    void delete(Long nono);    

    default Notice dtoToEntity(NoticeDTO noticeDTO) {
        Notice notice = Notice.builder()
                .nono(noticeDTO.getNono())
                .title(noticeDTO.getTitle())
                .content(noticeDTO.getContent())
                .member(Member.builder()
                            .email(noticeDTO.getEmail())                        
                            .build())                
                .build();
        return notice;
    }

    default NoticeDTO entityToDto(Notice notice) {
    NoticeDTO noticeDTO = NoticeDTO.builder()
            .nono(notice.getNono())
            .title(notice.getTitle())
            .content(notice.getContent())
            .regdate(notice.getRegdate())
            .email(notice.getMember().getEmail())
            .build();
    return noticeDTO;
    }
    
}
