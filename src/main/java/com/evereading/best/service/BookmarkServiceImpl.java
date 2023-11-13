package com.evereading.best.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.function.Function;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.evereading.best.dto.BookmarkDTO;
import com.evereading.best.dto.PageRequestDTO;
import com.evereading.best.dto.PageResultDTO;
import com.evereading.best.entity.Bookmark;
import com.evereading.best.entity.Member;
import com.evereading.best.entity.Novel;
import com.evereading.best.repository.BookmarkRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class BookmarkServiceImpl implements BookmarkService{
    private final BookmarkRepository bookmarkRepository;

    //북마크 목록
    // @Override
    // public List<BookmarkDTO> getList(String email) {
    //     List<Bookmark> result = bookmarkRepository.findByEmail(email,Pageable);
    //     return result.stream().map(entity -> entityToDto(entity)).collect(Collectors.toList());
    //  }
    @Override
    public PageResultDTO<BookmarkDTO, Bookmark> getList(String email, PageRequestDTO requestDTO) {
        Pageable pageable = requestDTO.getPageable(Sort.by("bno").descending());
        Page<Bookmark> result = bookmarkRepository.findByEmail(email, pageable);

        Function<Bookmark, BookmarkDTO> fn = (entity -> entityToDto(entity));
        return new PageResultDTO<>(result, fn);
    }

    //북마크 추가 - 추가하는 방법이 일방통행이므로 중복체크 필요없음
    @Override
    public Long addBookmark(Long nno, String email){
        Bookmark bookmark = Bookmark.builder()
                        .novel(Novel.builder().nno(nno).build())
                        .member(Member.builder().email(email).build())//임시 email로 저장
                        .build();
        bookmarkRepository.save(bookmark);

        return bookmark.getBno();
    }

    //북마크 삭제
    @Override
    public void removeBookmark(Long nno, String email) {
        bookmarkRepository.deleteByNnoAndEmail(nno, email);
    }

    //북마크 확인용
    @Override
    public Boolean isBookmarkOn(Long nno, String email) {
        //email이 null 일때 로그인 하라는 메시지...혹은 로그인 창으로 넘어가게끔 만들기
        Boolean result;
        if(email==null){
            result = false;
        }else{
            Long bno = bookmarkRepository.findByNnoAndEmail(nno,email);
            result = bno!=null; //null이면 false
        }
       
        
        return result;
    }

    //북마크 수
    @Override
    public Long bookmarkCount(Long nno) {
        Long count = bookmarkRepository.totalBookmarkByNno(nno);
        return count;
    }

    
}
