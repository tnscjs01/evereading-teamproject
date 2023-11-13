package com.evereading.best.service;

import com.evereading.best.dto.QuestionDTO;
import com.evereading.best.dto.PageRequestDTO;
import com.evereading.best.dto.PageResultDTO;
import com.evereading.best.entity.Member;
import com.evereading.best.entity.Question;

public interface QuestionService {

    Long write(QuestionDTO questionDTO);

    PageResultDTO<QuestionDTO, Question> getList(PageRequestDTO pageRequestDTO);
    
    QuestionDTO read(Long qno);

    void modify(QuestionDTO questionDTO);

    void delete(Long qno);    

    default Question dtoToEntity(QuestionDTO questionDTO) {
        Question question = Question.builder()
                .qno(questionDTO.getQno())
                .title(questionDTO.getTitle())
                .content(questionDTO.getContent())               
                .member(Member.builder()
                        .email(questionDTO.getEmail())
                        .nickname(questionDTO.getNickname())                        
                        .build())
                .answer(questionDTO.getAnswer())
                .regdate(questionDTO.getRegdate())
                .build();
        return question;
    }

    default QuestionDTO entityToDto(Question question) {
        QuestionDTO questionDTO = QuestionDTO.builder()
                .qno(question.getQno())
                .title(question.getTitle())
                .content(question.getContent())
                .email(question.getMember().getEmail())
                .nickname(question.getMember().getNickname())
                .regdate(question.getRegdate())
                .answer(question.getAnswer())
                .ansdate(question.getAnsdate())
                // .userQno(userQno(question.getMember().getEmail()))
                .build();
        return questionDTO;
    }

    PageResultDTO<QuestionDTO, Question> getListByUser(PageRequestDTO pageRequestDTO, String username);
}
