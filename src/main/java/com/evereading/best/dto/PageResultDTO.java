package com.evereading.best.dto;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import lombok.Data;

@Data//목록+페이지 번호가 혼합된 클래스(기능별로 분리하는게 좋음)
public class PageResultDTO<DTO, EN> { //Generic type
    //select한 결과 목록을 저장 - 다른방식으로 해도 됨...
    private List<DTO> dtoList;//목록

    //페이지 번호 - 이 내용이 pageable class에 기본 포함되어 있음
    private int totalPage;
    private int page;
    private int size;
    private int start, end;
    private boolean prev, next;
    private List<Integer> pageList;

    //page<Guestbook> -> List<GuestbookDTO> , generic으로 만들어져서 타입제한X 여러곳에서 쓸 수 있는 유틸리티..
    public PageResultDTO(Page<EN> result, Function<EN,DTO> fn){
        //Page<Guestbook>을 List<GuestbookDTO>로 변환
        dtoList = result.stream().map(fn).collect(Collectors.toList());

        totalPage = result.getTotalPages();
        makePageList(result.getPageable());
    }

    private void makePageList(Pageable pageable){
        this.page = pageable.getPageNumber()+1;
        this.size = pageable.getPageSize();

        int tempEnd = (int)(Math.ceil(page/10.0))*10;
        start = tempEnd - 9;
        prev = start > 1;
        end = totalPage > tempEnd ? tempEnd: totalPage;
        next = totalPage > tempEnd;
        pageList = IntStream.rangeClosed(start,end).boxed().collect(Collectors.toList());
    }
}
