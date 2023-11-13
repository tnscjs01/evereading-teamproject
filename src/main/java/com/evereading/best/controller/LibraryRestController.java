package com.evereading.best.controller;

import java.io.File;
import java.net.URLDecoder;
import java.nio.file.Files;

import java.util.Map;
import java.util.HashMap;

import org.apache.tomcat.jni.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.evereading.best.dto.NovelDTO;
import com.evereading.best.dto.RecentNovelDTO;
import com.evereading.best.service.BookmarkService;
import com.evereading.best.service.EpisodeService;
import com.evereading.best.service.NovelService;
import com.evereading.best.service.RecentNovelService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/rest")
@RequiredArgsConstructor
@Log4j2
public class LibraryRestController {
    private final BookmarkService bookmarkService;
    private final RecentNovelService recentNovelService;
    private final EpisodeService episodeService;
    private final NovelService novelService;

    //북마크 추가
    @PostMapping("/bookmark/{nno}")
    public ResponseEntity<Long> addBookmark(@PathVariable Long nno, @AuthenticationPrincipal UserDetails userDetails){
        Long bno = bookmarkService.addBookmark(nno,userDetails.getUsername());
        return new ResponseEntity<>(bno,HttpStatus.OK);
    }
    //북마크 삭제
    @DeleteMapping("/bookmark/{nno}")
    public ResponseEntity<Long> removeBookmark(@PathVariable Long nno, @AuthenticationPrincipal UserDetails userDetails){
        bookmarkService.removeBookmark(nno,userDetails.getUsername());
        return new ResponseEntity<>(nno, HttpStatus.OK);
    }

    //최근목록 추가&수정
    @PostMapping("/recent/{nno}")
    public ResponseEntity<Long> addRecentNovel(@RequestBody RecentNovelDTO recentNovelDTO){
        log.info(recentNovelDTO);
        Long rnno = recentNovelService.addRecent(recentNovelDTO);
        return new ResponseEntity<>(rnno, HttpStatus.OK);
    }
    //최근 목록 삭제
    @DeleteMapping("/recent/{nno}")
    public ResponseEntity<Long> removeRecentNovel(@PathVariable Long nno){
        recentNovelService.removeRecent(nno);
        return new ResponseEntity<>(nno, HttpStatus.OK);
    }

    //소설 삭제
    @DeleteMapping("/myNovel/{nno}")
    public ResponseEntity<Long> removeMyNovel(@PathVariable Long nno){
        //요청한 사용자 정보를 가져옴..
        UserDetails username = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        novelService.delete(nno, username);
        return new ResponseEntity<>(nno, HttpStatus.OK);
    }

    @Value("${novel.upload.dir}")
    private String uploadPath;

    //이미지 출력용
    @GetMapping("/display")
    public ResponseEntity<byte[]> getFile(String fileName){
        ResponseEntity<byte[]> result = null;

        try{
            String srcFileName = URLDecoder.decode(fileName, "UTF-8");
            File file = new File(uploadPath + File.separator + srcFileName);

            HttpHeaders header = new HttpHeaders();

            header.add("Content-Type", Files.probeContentType(file.toPath()));
            result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), header, HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<byte[]>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return result;
    }

    //count 출력용
    @GetMapping("/count/{nno}")
    public ResponseEntity<Map<String, Long>> count(@PathVariable Long nno){
        Map<String, Long> map = new HashMap<>();
        Long bookmarkCount = bookmarkService.bookmarkCount(nno);
        Long viewCount = episodeService.getTotalCount(nno);

        map.put("bookmarkCount", bookmarkCount);
        map.put("viewCount", viewCount);

        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    //최근 본 회차 타이틀용 - episode list 페이지에서 사용
    @GetMapping("/episode/{eno}")
    public ResponseEntity<String> findLastEpisodeTitle(@PathVariable Long eno){
        String title = recentNovelService.findLastEpisodeTitle(eno);

        return new ResponseEntity<>(title,HttpStatus.OK);
    }

    //내 서재에 new&up 만드는 용으로 가져오는 소설 데이터
    @GetMapping("/novel/{nno}")
    public ResponseEntity<NovelDTO> findNovelByNno(@PathVariable Long nno){
        NovelDTO novel = novelService.read(nno);

        return new ResponseEntity<>(novel,HttpStatus.OK);
    }   
}
