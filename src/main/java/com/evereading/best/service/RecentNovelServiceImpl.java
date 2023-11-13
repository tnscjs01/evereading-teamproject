package com.evereading.best.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.function.Function;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.evereading.best.dto.BookmarkDTO;
import com.evereading.best.dto.PageRequestDTO;
import com.evereading.best.dto.PageResultDTO;
import com.evereading.best.dto.RecentNovelDTO;
import com.evereading.best.entity.Bookmark;
import com.evereading.best.entity.RecentNovel;
import com.evereading.best.repository.RecentNovelRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;


@Service
@RequiredArgsConstructor
@Log4j2
public class RecentNovelServiceImpl implements RecentNovelService{
    private final RecentNovelRepository recentNovelRepository;
    
    // @Override
    // public List<RecentNovelDTO> getRecentList(String email) {
    //     List<RecentNovel> result = recentNovelRepository.findRecentByEmail(email);

    //     return result.stream().map(entity -> entityToDto(entity)).collect(Collectors.toList());
    // }
    @Override
    public PageResultDTO<RecentNovelDTO, RecentNovel> getRecentList(String email, PageRequestDTO requestDTO) {
        Pageable pageable = requestDTO.getPageable(Sort.by("date").descending());
        Page<RecentNovel> result = recentNovelRepository.findRecentByEmail(email, pageable);

        Function<RecentNovel, RecentNovelDTO> fn = (entity -> entityToDto(entity));
        return new PageResultDTO<>(result, fn);
    }


    @Override
    public void removeRecent(Long nno) {
        recentNovelRepository.deleteByNno(nno);
    }

    //최근 본 목록 추가
    @Override
    public Long addRecent(RecentNovelDTO recentNovelDTO) {
        String novelWriter = recentNovelRepository.novelWriter(recentNovelDTO.getNno());
        //본인이 쓴 소설일 경우 최근 본 목록에 추가X
        if(recentNovelDTO.getEmail().equals(novelWriter)){
            return null;
        }
        
        // 중복 체크 먼저
        Optional<RecentNovel> check = recentNovelRepository.findByEmailAndNno(recentNovelDTO.getEmail(), recentNovelDTO.getNno());

        if(check.isPresent()){//기존에 저장 된 것이 있으면 수정
            RecentNovel entity = check.get();
            entity.changeLastEpisode(recentNovelDTO.getLastEpisode());
            entity.changeLastEno(recentNovelDTO.getLastEno());
            entity.changeDate();
            recentNovelRepository.save(entity);
        }else{//기존에 저장 된 것이 없으면 새로 추가
            RecentNovel result = dtoToEntity(recentNovelDTO);
            result.changeDate();
            recentNovelRepository.save(result);
        }

        return recentNovelDTO.getRnno();
    }

    @Override
    public RecentNovelDTO findRecent(String email, Long nno) {
        Optional<RecentNovel> result = recentNovelRepository.findByEmailAndNno(email, nno);
        try{ RecentNovel recentNovel = result.get();
              return entityToDto(recentNovel);
        }
        catch(Exception e){
            log.info(e);
            return null;
        }

       
    }


    @Override
    public String findLastEpisodeTitle(Long eno) {
        String title = recentNovelRepository.findLastEpisodeTitle(eno);
        return title;
    }

   

    
}
