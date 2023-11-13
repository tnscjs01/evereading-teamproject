package com.evereading.best.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.function.Function;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.evereading.best.dto.EpisodeDTO;
import com.evereading.best.dto.PageRequestDTO;
import com.evereading.best.dto.PageResultDTO;
import com.evereading.best.entity.Episode;
import com.evereading.best.entity.Novel;
import com.evereading.best.repository.EpisodeRepository;
import com.evereading.best.repository.NovelRepository;
import com.evereading.best.repository.EpisodeReplyRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EpisodeServiceImpl implements EpisodeService {
    private final EpisodeRepository episodeRepository;
    private final EpisodeReplyRepository episodeReplyRepository;
    private final NovelRepository novelRepository;

    // 소설별 회차 목록------------------------------------------------------------------------------------
    @Override
    public PageResultDTO<EpisodeDTO, Episode> getList(Long nno,PageRequestDTO requestDTO) {

        Pageable pageable = requestDTO.getPageable(Sort.by("eno").descending());
        // Page<Episode> result = episodeRepository.findAll(pageable);
        Page<Episode> result = episodeRepository.findByNno(nno,pageable);

        Function<Episode,EpisodeDTO> fn = (entity -> entityToDto(entity));

        return new PageResultDTO<>(result,fn);
    }

    //첫화 찾기
    @Override
    public Long getFirstEpisode(Long nno) {
        Long firstEpisode = episodeRepository.findFirstEpisode(nno);

        return firstEpisode;
    }

    //조회수 합산
    @Override
    public Long getTotalCount(Long nno) {
        Long totalCount = episodeRepository.getTotalCount(nno);
        return totalCount;
    }

    //총 회차 수
    @Override
    public Long getTotalEpisode(Long nno) {
        Long totalEpisode = episodeRepository.getTotalEpisode(nno);
        return totalEpisode;
    }
    //회차 번호정리
    @Override
    public Long epiNum(Long nno, Long eno) {
       Long epinum = episodeRepository.episodeNum(nno, eno);
       return epinum;
    }

    

    //작성-----------------------------------------------------------------------------------
    @Override
    public Long register(EpisodeDTO episodeDTO) {
        if(episodeDTO.getCount()==null){ //이거 없으면 null오류...
            episodeDTO.setCount(0L);
        }
        
        Episode episode = dtoToEntity(episodeDTO);
        episodeRepository.save(episode);

        return episode.getEno();// view에 전달해서 글 번호 출력 안할거면 void나 String+redirect처리
    }

    //읽기-----------------------------------------------------------------------------------
    @Transactional// 이거 없으면 조회수 안오름
    @Override
    public EpisodeDTO read(Long eno) {
        Optional<Episode> result = episodeRepository.findById(eno);
        Episode episode = result.get();

        episode.changeCount(episode.getCount()+1);//조회수 +1

        updateTotalCount(episode.getNovel().getNno()); // totalCount 업데이트

        LocalDateTime regdate = episode.getRegdate();
        LocalDateTime now = LocalDateTime.now();
    
        // 업데이트할 기간을 계산합니다.
        if (ChronoUnit.DAYS.between(regdate, now) <= 1) {
            updateDailyCount(episode.getNovel().getNno());
        }
        if (ChronoUnit.WEEKS.between(regdate, now) <= 1) {
            updateWeeklyCount(episode.getNovel().getNno());
        }
        if (ChronoUnit.MONTHS.between(regdate, now) <= 1) {
            updateMonthlyCount(episode.getNovel().getNno());
        }

        return entityToDto(episode);
    }

    //수정-----------------------------------------------------------------------------------
    @Transactional
    @Override
    public void modify(EpisodeDTO episodeDTO) {
       Optional<Episode> result = episodeRepository.findById(episodeDTO.getEno());
        if(result!=null){
            Episode entity = result.get();
            entity.changeTitle(episodeDTO.getTitle());
            entity.changeContent(episodeDTO.getContent());
            entity.changeNote(episodeDTO.getNote());
            episodeRepository.save(entity);
        }
    }


    //삭제-----------------------------------------------------------------------------------
    @Override
    public void delete(Long eno) {
        episodeReplyRepository.deleteByEno(eno);
        episodeRepository.deleteById(eno);
    }


    //댓글 갯수 확인
    @Override
    public Long replyCount(Long eno) {
        Long replyCount = episodeReplyRepository.replyCount(eno);

        return replyCount;
    }

    // 총조회수 업데이트
    @Override
    @Transactional
    public void updateTotalCount(Long nno) {
        Long totalCount = episodeRepository.getTotalCount(nno);
        Optional<Novel> novelOptional = novelRepository.findById(nno);
        
        if (novelOptional.isPresent()) {
            Novel novel = novelOptional.get();
            novel.setTotalCount(totalCount);
            novelRepository.save(novel);
        }
    }

    //이전 에피소드 찾기
    @Override
    public Long findPreviousEpisode(Long eno, Long nno) {
        Long priviousEno = episodeRepository.findPreviousEpisode(eno, nno);
        //null일때 view페이지에서 링크 없애기
        return priviousEno;
    }

    //다음 에피소드 찾기
    @Override
    public Long findNextEpisode(Long eno, Long nno) {
        //null일때 view페이지에서 링크 없애기
        Long nextEno = episodeRepository.findNextEpisode(eno, nno);
        return nextEno;
    }

    // 월간 조회수 업데이트
    @Override
    @Transactional
    public void updateMonthlyCount(Long nno) {
        Long monthlyCount = episodeRepository.getMonthlyCount(nno);
        Optional<Novel> novelOptional = novelRepository.findById(nno);

        if (novelOptional.isPresent()) {
            Novel novel = novelOptional.get();
            novel.setMonthlyCount(monthlyCount != null ? monthlyCount : 0); // null일 때 0으로 처리
            novelRepository.save(novel);
        }
    }

    // 주간 조회수 업데이트
    @Override
    @Transactional
    public void updateWeeklyCount(Long nno) {
        Long weeklyCount = episodeRepository.getWeeklyCount(nno);
        Optional<Novel> novelOptional = novelRepository.findById(nno);

        if (novelOptional.isPresent()) {
            Novel novel = novelOptional.get();
            novel.setWeeklyCount(weeklyCount != null ? weeklyCount : 0); // null일 때 0으로 처리
            novelRepository.save(novel);
        }
    }

    // 일일 조회수 업데이트
    @Override
    @Transactional
    public void updateDailyCount(Long nno) {
        Long dailyCount = episodeRepository.getDailyCount(nno);
        Optional<Novel> novelOptional = novelRepository.findById(nno);

        if (novelOptional.isPresent()) {
            Novel novel = novelOptional.get();
            novel.setDailyCount(dailyCount != null ? dailyCount : 0); // null일 때 0으로 처리
            novelRepository.save(novel);
        }
    }

    //조회수와 상관없는 read
    @Override
    public EpisodeDTO readNoCount(Long eno) {
        Optional<Episode> result = episodeRepository.findById(eno);
        Episode episode = result.get();
        
        return entityToDto(episode);
    }   
    
}
