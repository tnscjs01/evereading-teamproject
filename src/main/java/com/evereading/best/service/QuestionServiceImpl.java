package com.evereading.best.service;

import java.time.LocalDateTime;
import java.util.Optional;

import java.util.function.Function;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.evereading.best.dto.PageRequestDTO;
import com.evereading.best.dto.PageResultDTO;
import com.evereading.best.dto.QuestionDTO;
import com.evereading.best.entity.Question;
import com.evereading.best.repository.QuestionRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {
    
    private final QuestionRepository questionRepository;

    @Override
    public Long write(QuestionDTO questionDto) { 

        Question question = dtoToEntity(questionDto);  

        question.setRegdate(LocalDateTime.now());

        questionRepository.save(question);

        return question.getQno();
    }

    @Override
    public PageResultDTO<QuestionDTO, Question> getList(PageRequestDTO pageRequestDTO) {
        
        Pageable pageable = pageRequestDTO.getPageable(Sort.by("qno").descending());

        Page<Question> result = questionRepository.findAll(pageable);

        Function<Question, QuestionDTO> fn = (question -> entityToDto(question));

        return new PageResultDTO<>(result, fn);
    }

    @Override
    public QuestionDTO read(Long qno) {

        Optional<Question> result = questionRepository.findById(qno);

        return result.isPresent()? entityToDto(result.get()): null;
    }

    @Override
    public void delete(Long qno) {

        questionRepository.deleteById(qno);

    }

    @Override
    public void modify(QuestionDTO questionDto) {

        Optional<Question> result = questionRepository.findById(questionDto.getQno());

        if(result.isPresent()){

            Question question = result.get();

            question.changeTitle(questionDto.getTitle());
            question.changeContent(questionDto.getContent());
            question.changeAnswer(questionDto.getAnswer());

            // 수정일(ansdate)을 현재 시간으로 업데이트
            question.setAnsdate(LocalDateTime.now());

            questionRepository.save(question);

        }

    }

    @Override
    public PageResultDTO<QuestionDTO, Question> getListByUser(PageRequestDTO pageRequestDTO, String username) {

        Pageable pageable = pageRequestDTO.getPageable(Sort.by("qno").descending());

        Page<Question> result = questionRepository.findByMember_Email(username, pageable);
        
        Function<Question, QuestionDTO> fn = (question -> entityToDto(question));   
        
        return new PageResultDTO<>(result, fn);

    }  

}