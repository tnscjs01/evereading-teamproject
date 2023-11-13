package com.evereading.best.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.evereading.best.dto.EventDTO;
import com.evereading.best.dto.PageRequestDTO;
import com.evereading.best.dto.PageResultDTO;
import com.evereading.best.entity.Event;
import com.evereading.best.entity.Member;
import com.evereading.best.repository.EventRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService{

    private final EventRepository eventRepository;

    @Override
    public Long register(EventDTO eventDTO, String email, MultipartFile carouselImageFile, MultipartFile eventImageFile) {

        Event event = dtoToEntity(eventDTO);

        String carouselUploadDirectory = "C:\\carouselImg"; 
        String eventUploadDirectory = "C:\\eventImg"; 
        
        String carouselImageFileName;
        String eventImageFileName;

        try {

            Path uploadPath1 = Paths.get(carouselUploadDirectory);
            Files.createDirectories(uploadPath1);

            if (carouselImageFile != null && !carouselImageFile.isEmpty()) {
                // 사용자가 이미지를 업로드한 경우
                carouselImageFileName = UUID.randomUUID().toString() + "_" + carouselImageFile.getOriginalFilename();
                Files.copy(carouselImageFile.getInputStream(), uploadPath1.resolve(carouselImageFileName));

            } else {
                // 사용자가 이미지를 제공하지 않은 경우
                carouselImageFileName = "default_carouselImg.jpg";

                // 기본 이미지를 클래스패스에서 읽어와 업로드 디렉토리에 복사
                try (InputStream defaultImageStream = getClass().getResourceAsStream("/static/img/default_carouselImg.jpg")) {
                    Files.copy(defaultImageStream, uploadPath1.resolve(carouselImageFileName), StandardCopyOption.REPLACE_EXISTING);
                }

            }

            event.setCarouselImg(carouselImageFileName);
            event.setMember(Member.builder().email(email).build());

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        try {

            Path uploadPath2 = Paths.get(eventUploadDirectory);
            Files.createDirectories(uploadPath2);

            if (eventImageFile != null && !eventImageFile.isEmpty()) {
                // 사용자가 이미지를 업로드한 경우
                eventImageFileName = UUID.randomUUID().toString() + "_" + eventImageFile.getOriginalFilename();
                Files.copy(eventImageFile.getInputStream(), uploadPath2.resolve(eventImageFileName));

            } else {
                // 사용자가 이미지를 제공하지 않은 경우
                eventImageFileName = "default_eventImg.jpg"; // 기본 이미지 파일명

                // 기본 이미지를 클래스패스에서 읽어와 업로드 디렉토리에 복사
                try (InputStream defaultImageStream = getClass().getResourceAsStream("/static/img/default_eventImg.jpg")) {
                    Files.copy(defaultImageStream, uploadPath2.resolve(eventImageFileName), StandardCopyOption.REPLACE_EXISTING);
                }

            }

            event.setEventImg(eventImageFileName);
            event.setMember(Member.builder().email(email).build());

            eventRepository.save(event);

            return event.getEvno();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }
    
    @Override
    public void modify(EventDTO eventDto, MultipartFile carouselImageFile, MultipartFile eventImageFile, UserDetails userDetails) {

        Optional<Event> result = eventRepository.findById(eventDto.getEvno());

        if (result.isPresent()) {
            Event event = result.get();

            // 권한 검사: 현재 사용자의 이메일과 게시글 작성자의 이메일을 비교하여 수정 권한 확인
            if (!userDetails.getUsername().equals(event.getMember().getEmail()) && !userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
                throw new AccessDeniedException("수정 권한이 없습니다.");
            }

            // 새로운 이미지 파일이 업로드된 경우에만 이미지 파일을 업데이트합니다.
            if (carouselImageFile != null && !carouselImageFile.isEmpty()) {

                String carouselUploadDirectory = "C:\\carouselImg";
                String carouselImageFileName = UUID.randomUUID().toString() + "_" + carouselImageFile.getOriginalFilename();

                try {

                    Path uploadPath1 = Paths.get(carouselUploadDirectory);
                    Files.createDirectories(uploadPath1);

                    // 이전 이미지 파일을 삭제
                    if (event.getCarouselImg() != null) {
                        Path previousImage = uploadPath1.resolve(event.getCarouselImg());
                        Files.deleteIfExists(previousImage);
                    }

                    Files.copy(carouselImageFile.getInputStream(), uploadPath1.resolve(carouselImageFileName));

                } catch (IOException e) {
                    e.printStackTrace();
                }

                event.changeCarouselImg(carouselImageFileName);

            }

            // 새로운 이미지 파일이 업로드된 경우에만 이미지 파일을 업데이트합니다.
            if (eventImageFile != null && !eventImageFile.isEmpty()) {

                String eventUploadDirectory = "C:\\eventImg";
                String eventImageFileName = UUID.randomUUID().toString() + "_" + eventImageFile.getOriginalFilename();

                try {

                    Path uploadPath2 = Paths.get(eventUploadDirectory);
                    Files.createDirectories(uploadPath2);

                    // 이전 이미지 파일을 삭제
                    if (event.getEventImg() != null) {
                        Path previousImage = uploadPath2.resolve(event.getEventImg());
                        Files.deleteIfExists(previousImage);
                    }

                    Files.copy(eventImageFile.getInputStream(), uploadPath2.resolve(eventImageFileName));

                } catch (IOException e) {
                    e.printStackTrace();
                }

                event.changeEventImg(eventImageFileName);
                
            }
            
            event.changeTitle(eventDto.getTitle());
            event.changeContent(eventDto.getContent());

            eventRepository.save(event);
        }
    }

    @Override
    public PageResultDTO<EventDTO, Event> getList(PageRequestDTO pageRequestDTO) {
        
        Pageable pageable = pageRequestDTO.getPageable(Sort.by("evno").descending());

        Page<Event> result = eventRepository.findAll(pageable);

        Function<Event, EventDTO> fn = (event -> entityToDto(event));

        return new PageResultDTO<>(result, fn);
    }


    @Override
    public EventDTO read(Long evno) {

        Optional<Event> result = eventRepository.findById(evno);

        return result.isPresent()? entityToDto(result.get()): null;
    }

    @Override
    public void delete(Long evno) {

        eventRepository.deleteById(evno);

    }
    
}
