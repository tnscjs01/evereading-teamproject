package com.evereading.best.service;

import java.util.List;

import com.evereading.best.dto.BookmarkDTO;
import com.evereading.best.dto.PageRequestDTO;
import com.evereading.best.dto.PageResultDTO;
import com.evereading.best.entity.Bookmark;
import com.evereading.best.entity.Member;
import com.evereading.best.entity.Novel;

public interface BookmarkService {
    //목록
    // List<BookmarkDTO> getList(String email);
    PageResultDTO<BookmarkDTO, Bookmark> getList(String email, PageRequestDTO requestDTO);

    //북마크 추가
    Long addBookmark(Long nno,String email);

    //북마크 삭제
    void removeBookmark(Long nno,String email);

    //북마크 확인
    Boolean isBookmarkOn(Long nno,String email);

    //소설별 북마크수
    Long bookmarkCount(Long nno);

    default BookmarkDTO entityToDto(Bookmark entity){
        BookmarkDTO dto = BookmarkDTO.builder()
                    .bno(entity.getBno())
                    .nno(entity.getNovel().getNno())
                    .title(entity.getNovel().getTitle())
                    .nickname(entity.getNovel().getMember().getNickname())
                    .thumbnail(entity.getNovel().getThumbnail())
                    .email(entity.getMember().getEmail())
                    .build();
        return dto;
    }

    default Bookmark dtoToEntity(BookmarkDTO dto){
        Bookmark entity = Bookmark.builder()
                    .bno(dto.getBno())
                    .novel(Novel.builder().nno(dto.getNno()).build())
                    .member(Member.builder().email(dto.getEmail()).build())
                    .build();
        return entity;
    }
}
