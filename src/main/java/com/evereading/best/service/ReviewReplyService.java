package com.evereading.best.service;

import com.evereading.best.dto.PageRequestDTO;
import com.evereading.best.dto.PageResultDTO;
import com.evereading.best.dto.ReviewReplyDTO;
import com.evereading.best.entity.Member;
import com.evereading.best.entity.Review;
import com.evereading.best.entity.ReviewReply;

public interface ReviewReplyService {
    PageResultDTO<ReviewReplyDTO,ReviewReply> getListofReply(Long rvno, PageRequestDTO pageRequestDTO);

    Long register(ReviewReplyDTO reviewReplyDTO);

    void modify(ReviewReplyDTO reviewReplyDTO);

    void remove(Long rvno);

    default ReviewReply dtoToEntity(ReviewReplyDTO reviewReplyDTO){

        ReviewReply reviewReply = ReviewReply.builder()
                    .rrno(reviewReplyDTO.getRrno())
                    .content(reviewReplyDTO.getContent())
                    .review(Review.builder().rvno(reviewReplyDTO.getBoardId()).build())
                    .member(Member.builder().email(reviewReplyDTO.getEmail()).build())
                    .build();

        return reviewReply;
    }
    default ReviewReplyDTO entityToDto(ReviewReply reviewReply){

        ReviewReplyDTO reviewReplyDTO = ReviewReplyDTO.builder()
                    .rrno(reviewReply.getRrno())
                    .boardId(reviewReply.getReview().getRvno())
                    .email(reviewReply.getMember().getEmail())
                    .nickname(reviewReply.getMember().getNickname())
                    .content(reviewReply.getContent())
                    .regdate(reviewReply.getRegdate())
                    .build();
        return reviewReplyDTO;
    }
}
