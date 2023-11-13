package com.evereading.best.service;

import java.util.Optional;
import java.util.function.Function;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.evereading.best.dto.PageRequestDTO;
import com.evereading.best.dto.PageResultDTO;
import com.evereading.best.dto.ReviewReplyDTO;
import com.evereading.best.entity.ReviewReply;
import com.evereading.best.repository.ReviewReplyRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewReplyServiceImpl implements ReviewReplyService {

    private final ReviewReplyRepository reviewReplyRepository;

    @Override
    public PageResultDTO<ReviewReplyDTO, ReviewReply> getListofReply(Long rvno, PageRequestDTO pageRequestDTO) {
       Pageable pageable = pageRequestDTO.getPageable(Sort.by("rrno").descending());

       Page<ReviewReply> result = reviewReplyRepository.findByReview(rvno,pageable);

       Function<ReviewReply,ReviewReplyDTO> fn = (reviewReply -> entityToDto(reviewReply));
       return new PageResultDTO<>(result, fn);
    }

    @Override
    public Long register(ReviewReplyDTO reviewReplyDTO) {
       ReviewReply reviewReply = dtoToEntity(reviewReplyDTO);

       reviewReplyRepository.save(reviewReply);

       return reviewReply.getRrno();
    }

    @Override
    public void modify(ReviewReplyDTO reviewReplyDTO) {
        Optional<ReviewReply> result = reviewReplyRepository.findById(reviewReplyDTO.getRrno());    
        
        if(result.isPresent()){
            ReviewReply reviewReply = result.get();
            reviewReply.changeContent(reviewReplyDTO.getContent());

            reviewReplyRepository.save(reviewReply);
        }
    }

    @Override
    public void remove(Long rvno) {

        reviewReplyRepository.deleteById(rvno);
    
    }
    
}
