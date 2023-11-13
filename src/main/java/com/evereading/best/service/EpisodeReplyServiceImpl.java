package com.evereading.best.service;

import java.util.Optional;
import java.util.function.Function;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.evereading.best.dto.EpisodeReplyDTO;
import com.evereading.best.dto.PageRequestDTO;
import com.evereading.best.dto.PageResultDTO;
import com.evereading.best.entity.EpisodeReply;
import com.evereading.best.entity.Member;
import com.evereading.best.repository.EpisodeReplyRepository;
import com.evereading.best.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class EpisodeReplyServiceImpl implements EpisodeReplyService{
    
    private final EpisodeReplyRepository episodeReplyRepository;
    private final MemberRepository memberRepository;

    // 페이징 된 댓글 목록
    @Override
    public PageResultDTO<EpisodeReplyDTO, EpisodeReply> getListEpisodeReply(Long eno, PageRequestDTO requestDTO) {
        Pageable pageable = requestDTO.getPageable(Sort.by("erno"));
        Page<EpisodeReply> result = episodeReplyRepository.findByEpisode(eno, pageable);
    
        Function<EpisodeReply,EpisodeReplyDTO> fn = (entity -> entityToDto(entity));
        
        return new PageResultDTO<>(result, fn);
    }

    //소설 댓글 쓰기
    @Override
    public Long register(EpisodeReplyDTO episodeReplyDTO) {
        episodeReplyDTO.getContent().replace("\r\n", "<br>");
        EpisodeReply episodeReply = dtoToEntity(episodeReplyDTO);
        episodeReplyRepository.save(episodeReply);
        return episodeReply.getErno();
    }

    //댓글 수정
    @Override
    public void modify(EpisodeReplyDTO episodeReplyDTO) {
        Optional<EpisodeReply> result = episodeReplyRepository.modifyEpisodeReply(episodeReplyDTO.getEno(), episodeReplyDTO.getErno());
        log.info("서비스확인용----------------------------------------"+episodeReplyDTO);
        log.info("서비스확인용----------------------------------------"+result.get());
        EpisodeReply episodeReply = result.get();
        episodeReply.changeContent(episodeReplyDTO.getContent());
        episodeReplyRepository.save(episodeReply);
    }

    //댓글 삭제
    @Override
    public void remove(Long erno) {
        episodeReplyRepository.deleteById(erno);
    }
    
    @Override
    public String findNickName(EpisodeReply episodeReply) {
        Optional<Member> result = memberRepository.findById(episodeReply.getEmail());
        String nickName = result.get().getNickname();
        return nickName;
    }

   
    
}
