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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.evereading.best.dto.NovelDTO;
import com.evereading.best.dto.PageRequestDTO;
import com.evereading.best.dto.PageResultDTO;
import com.evereading.best.entity.Member;
import com.evereading.best.entity.Novel;
import com.evereading.best.repository.BookmarkRepository;
import com.evereading.best.repository.EpisodeReplyRepository;
import com.evereading.best.repository.EpisodeRepository;
import com.evereading.best.repository.NovelRepository;
import com.evereading.best.repository.RecentNovelRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NovelServiceImpl implements NovelService {

    private final NovelRepository novelRepository;
    private final BookmarkRepository bookmarkRepository;
    private final EpisodeReplyRepository episodeReplyRepository;
    private final EpisodeRepository episodeRepository;
    private final RecentNovelRepository recentNovelRepository;

    @Override
    public NovelDTO read(Long nno) {

        Optional<Novel> novel = novelRepository.findById(nno);

        return entityToDto(novel.get());//novel.get() : entity
    }

    @Override
    public PageResultDTO<NovelDTO, Novel> getList(PageRequestDTO pageRequestDTO) {
        
        Pageable pageable = pageRequestDTO.getPageable(Sort.by("nno").descending());

        Page<Novel> result = novelRepository.findAll(pageable);

        Function<Novel, NovelDTO> fn = (entity -> entityToDto(entity));

        return new PageResultDTO<>(result, fn);
    }

    @Override
    public PageResultDTO<NovelDTO, Novel> search(PageRequestDTO pageRequestDTO, String keyword) {

        Pageable pageable = pageRequestDTO.getPageable(Sort.by("nno").descending());

        Page<Novel> result = novelRepository.search(keyword, pageable);

        Function<Novel, NovelDTO> fn = (entity -> entityToDto(entity));

        return new PageResultDTO<>(result, fn);
    }

    @Override
    public Long register(NovelDTO novelDTO, String email, MultipartFile imageFile) {

        Novel novel = dtoToEntity(novelDTO);

        String uploadDirectory = "C:\\novelThumbnail";
        String fileName;

        try {

            Path uploadPath = Paths.get(uploadDirectory);
            Files.createDirectories(uploadPath);

            if (imageFile != null && !imageFile.isEmpty()) {
                // 사용자가 이미지를 업로드한 경우
                fileName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();
                Files.copy(imageFile.getInputStream(), uploadPath.resolve(fileName));

            } else {
                // 사용자가 이미지를 제공하지 않은 경우
                fileName = "default_thumbnail.jpg";

                // 기본 이미지를 클래스패스에서 읽어와 업로드 디렉토리에 복사
                try (InputStream defaultImageStream = getClass().getResourceAsStream("/static/img/default_thumbnail.jpg")) {
                    Files.copy(defaultImageStream, uploadPath.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
                }

            }

            novel.setThumbnail(fileName);
            novel.setMember(Member.builder().email(email).build());

            novelRepository.save(novel);

            return novel.getNno();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public PageResultDTO<NovelDTO,Novel> myNovelList(String email, PageRequestDTO pageRequestDTO) {

        Pageable pageable = pageRequestDTO.getPageable(Sort.by("regdate").descending());
        
        Page<Novel> result = novelRepository.myNovelList(email,pageable);

        Function<Novel,NovelDTO> fn = (entity -> entityToDto(entity));

        return new PageResultDTO<>(result, fn);

    }

    @Override
    public NovelDTO readForModify(Long nno, UserDetails userDetails) {

        Optional<Novel> novel = novelRepository.findById(nno);

        // 권한 검사: 현재 사용자의 이메일과 게시글 작성자의 이메일을 비교하여 수정 권한 확인
        if (!userDetails.getUsername().equals(entityToDto(novel.get()).getEmail()) && !userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            throw new AccessDeniedException("수정 권한이 없습니다.");
        }      

        return entityToDto(novel.get());
    }

    @Override
    public void modify(NovelDTO novelDto, MultipartFile imageFile, UserDetails userDetails) {

        Optional<Novel> result = novelRepository.findById(novelDto.getNno());

        if (result.isPresent()) {

            Novel novel = result.get();

            // 권한 검사: 현재 사용자의 이메일과 게시글 작성자의 이메일을 비교하여 수정 권한 확인
            if (!userDetails.getUsername().equals(novel.getMember().getEmail()) && !userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
                throw new AccessDeniedException("수정 권한이 없습니다.");
            }

            // 새로운 이미지 파일이 업로드된 경우에만 이미지 파일을 업데이트합니다.
            if (imageFile != null && !imageFile.isEmpty()) {
                
                String uploadDirectory = "C:\\novelThumbnail";
                String fileName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();

                try {
                    
                    Path uploadPath = Paths.get(uploadDirectory);
                    Files.createDirectories(uploadPath);

                    // 이전 이미지 파일을 삭제
                    if (novel.getThumbnail() != null) {
                        Path previousImage = uploadPath.resolve(novel.getThumbnail());
                        Files.deleteIfExists(previousImage);
                    }

                    Files.copy(imageFile.getInputStream(), uploadPath.resolve(fileName));
                    
                } catch (IOException e) {
                    e.printStackTrace();
                }

                novel.changeThumbnail(fileName);

            }

            novel.changeTitle(novelDto.getTitle());
            novel.changeContent(novelDto.getContent());
            novel.changeCategory(novelDto.getCategory());
            novel.changeCompleted(novelDto.isCompleted());

            novelRepository.save(novel);
        }
    }

    @Override
    @Transactional
    public void delete(Long nno, UserDetails userDetails) {

        Optional<Novel> novel = novelRepository.findById(nno);

        // 권한 검사: 현재 사용자의 이메일과 게시글 작성자의 이메일을 비교하여 수정 권한 확인
        if (!userDetails.getUsername().equals(entityToDto(novel.get()).getEmail()) && !userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            throw new AccessDeniedException("삭제 권한이 없습니다.");
        }   

        recentNovelRepository.deleteByNno(nno);
        bookmarkRepository.deleteByNovel(nno);
        episodeReplyRepository.deleteByNovel(nno);
        episodeRepository.deleteByNovel(nno);
        novelRepository.deleteById(nno);

    }

    @Override
    public PageResultDTO<NovelDTO, Novel> getListByTotalCount(PageRequestDTO pageRequestDTO) {
        
        Pageable pageable = pageRequestDTO.getPageable(Sort.by(

            Sort.Order.desc("totalCount"),
            Sort.Order.desc("nno")

        ));

        Page<Novel> result = novelRepository.findAll(pageable);

        Function<Novel, NovelDTO> fn = (entity -> entityToDto(entity));

        return new PageResultDTO<>(result, fn);
    }

    @Override
    public PageResultDTO<NovelDTO, Novel> getListByCategory(PageRequestDTO pageRequestDTO, String category) {

        Pageable pageable = pageRequestDTO.getPageable(Sort.by("nno").descending());

        Page<Novel> result = novelRepository.findAllByCategory(category, pageable);

        Function<Novel, NovelDTO> fn = (entity -> entityToDto(entity));

        return new PageResultDTO<>(result, fn);
    }

    @Override
    public PageResultDTO<NovelDTO, Novel> searchByCategory(String category, String keyword, PageRequestDTO pageRequestDTO) {

        Pageable pageable = pageRequestDTO.getPageable(Sort.by("nno").descending());

        Page<Novel> result = novelRepository.searchByCategory(category, keyword, pageable);
        
        Function<Novel, NovelDTO> fn = (entity -> entityToDto(entity));

        return new PageResultDTO<>(result, fn);

    }

    @Override
    public PageResultDTO<NovelDTO, Novel> getCompletedNovelList(PageRequestDTO pageRequestDTO) {

        Pageable pageable = pageRequestDTO.getPageable(Sort.by("nno").descending());

        Page<Novel> result = novelRepository.findAllByCompleted(true, pageable);

        Function<Novel, NovelDTO> fn = (entity -> entityToDto(entity));

        return new PageResultDTO<>(result, fn);
    }

    @Override
    public PageResultDTO<NovelDTO, Novel> getListByMonthlyCount(PageRequestDTO pageRequestDTO) {
        
        Pageable pageable = pageRequestDTO.getPageable(Sort.by(

            Sort.Order.desc("monthlyCount"),
            Sort.Order.desc("nno")

        ));

        Page<Novel> result = novelRepository.findAll(pageable);

        Function<Novel, NovelDTO> fn = (entity -> entityToDto(entity));

        return new PageResultDTO<>(result, fn);
    }

    @Override
    public PageResultDTO<NovelDTO, Novel> getListByWeeklyCount(PageRequestDTO pageRequestDTO) {
        
        Pageable pageable = pageRequestDTO.getPageable(Sort.by(

            Sort.Order.desc("weeklyCount"),
            Sort.Order.desc("nno")

        ));

        Page<Novel> result = novelRepository.findAll(pageable);

        Function<Novel, NovelDTO> fn = (entity -> entityToDto(entity));

        return new PageResultDTO<>(result, fn);
    }

    @Override
    public PageResultDTO<NovelDTO, Novel> getListByDailyCount(PageRequestDTO pageRequestDTO) {
        
        Pageable pageable = pageRequestDTO.getPageable(Sort.by(

            Sort.Order.desc("dailyCount"),
            Sort.Order.desc("nno")

        ));

        Page<Novel> result = novelRepository.findAll(pageable);

        Function<Novel, NovelDTO> fn = (entity -> entityToDto(entity));

        return new PageResultDTO<>(result, fn);
    }
    
}