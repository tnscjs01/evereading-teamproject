package com.evereading.best.service;

import java.util.Optional;
import java.util.function.Function;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.evereading.best.dto.FreeReplyDTO;
import com.evereading.best.dto.PageRequestDTO;
import com.evereading.best.dto.PageResultDTO;
import com.evereading.best.entity.FreeReply;
import com.evereading.best.repository.FreeReplyRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
public class FreeReplyServiceImpl implements FreeReplyService {
    
    private final FreeReplyRepository freeReplyRepository;

    @Override
    public PageResultDTO<FreeReplyDTO, FreeReply> getListofReply(Long fno, PageRequestDTO pageRequestDTO) {
        Pageable pageable = pageRequestDTO.getPageable(Sort.by("frno").descending());

        Page<FreeReply> result = freeReplyRepository.findByFreeBoard(fno, pageable);

        Function<FreeReply,FreeReplyDTO> fn = (freeReply -> entityToDto(freeReply));
        return new PageResultDTO<>(result, fn);
    }

    @Override
    public Long register(FreeReplyDTO freeReplyDTO) {
        FreeReply freeReply = dtoToEntity(freeReplyDTO);

        freeReplyRepository.save(freeReply);

        return freeReply.getFrno();
    }

    @Override
    public void modify(FreeReplyDTO freeReplyDTO) {
        Optional<FreeReply> result =
                    freeReplyRepository.findById(freeReplyDTO.getFrno());

        if(result.isPresent()){

            FreeReply freeReply = result.get();
            freeReply.changeContent(freeReplyDTO.getContent());

            freeReplyRepository.save(freeReply);
        }
    }

    @Override
    public void remove(Long frno) {
        
        freeReplyRepository.deleteById(frno);

    }
}
