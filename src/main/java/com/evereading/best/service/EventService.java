package com.evereading.best.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

import com.evereading.best.dto.EventDTO;
import com.evereading.best.dto.PageRequestDTO;
import com.evereading.best.dto.PageResultDTO;
import com.evereading.best.entity.Event;
import com.evereading.best.entity.Member;

public interface EventService {

    Long register(EventDTO eventDTO, String email, MultipartFile carouselImageFile, MultipartFile eventImageFile);
    
    void modify(EventDTO eventDto, MultipartFile carouselImageFile, MultipartFile eventImageFile, UserDetails userDetails);

    PageResultDTO<EventDTO, Event> getList(PageRequestDTO pageRequestDTO);
    
    EventDTO read(Long evno);

    void delete(Long evno);    

    default Event dtoToEntity(EventDTO eventDTO) {
        Event event = Event.builder()
                .evno(eventDTO.getEvno())
                .title(eventDTO.getTitle())
                .content(eventDTO.getContent())
                .carouselImg(eventDTO.getCarouselImg())
                .eventImg(eventDTO.getEventImg())
                .member(Member.builder()
                    .email(eventDTO.getEmail())                        
                    .build())                
                .build();
        return event;
    }

    default EventDTO entityToDto(Event event) {
        EventDTO eventDTO = EventDTO.builder()
                        .evno(event.getEvno())
                        .title(event.getTitle())
                        .content(event.getContent())
                        .regdate(event.getRegdate())
                        .carouselImg(event.getCarouselImg())
                        .eventImg(event.getEventImg())
                        .email(event.getMember().getEmail())
                        .build();
        return eventDTO;
    }
    
}
