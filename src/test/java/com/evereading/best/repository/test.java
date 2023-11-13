package com.evereading.best.repository;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashSet;
import java.util.UUID;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.evereading.best.dto.NovelDTO;
import com.evereading.best.entity.Member;
import com.evereading.best.entity.MemberRole;
import com.evereading.best.entity.Novel;

@SpringBootTest
public class test {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private NovelRepository novelRepository;

    @Test
    public void createUser() {

        IntStream.rangeClosed(1,100).forEach(i -> {
            Member member = Member.builder()
                    .email("user"+i+"@daum.net")
                    .nickname("사용자"+i)
                    .social(false)
                    .roleSet(new HashSet<MemberRole>())
                    .pw( passwordEncoder.encode("1") )
                    .build();

            //default role
            member.addMemberRole(MemberRole.USER);

            if(i > 90){
                member.addMemberRole(MemberRole.ADMIN);
            }

            memberRepository.save(member);

        });

    }


    @Test
    public void createNovel() {
        // 각 사용자가 5개의 소설 생성
        IntStream.rangeClosed(1, 10).forEach(i -> {

            IntStream.rangeClosed(1, 5).forEach(j -> {
                NovelDTO novelDTO = NovelDTO.builder()
                        .title("사용자" + i + "의 소설 " + j + "번의 소설 제목")
                        .content("사용자" + i + "의 소설 " + j + "번의 소설 내용")
                        .category(getCategoryForIndex(j))
                        .completed(j % 2 == 0) // 번갈아가며 완료 여부 설정
                        .build();

                // 이미지 파일을 제공하지 않는 상황을 시뮬레이션합니다.
                MockMultipartFile imageFile = null;

                Long nno = registerNovel(novelDTO, "user" + i + "@daum.net", imageFile);

            });
        });

    }

    private String getCategoryForIndex(int index) {
        switch (index) {
            case 1:
                return "판타지";
            case 2:
                return "무협";
            case 3:
                return "현대";
            case 4:
                return "로맨스";
            case 5:
                return "기타";
            default:
                return "기타";
        }
    }

    private Long registerNovel(NovelDTO novelDTO, String email, MockMultipartFile imageFile) {
        Novel novel = dtoToEntity(novelDTO);

        String uploadDirectory = "C:\\novelThumbnail";
        String fileName;

        try {
            Path uploadPath = Paths.get(uploadDirectory);
            Files.createDirectories(uploadPath);

            if (imageFile != null && !imageFile.isEmpty()) {
                // 사용자가 이미지를 제공한 경우
                fileName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();
                Files.copy(imageFile.getInputStream(), uploadPath.resolve(fileName));
            } else {
                // 사용자가 이미지를 제공하지 않은 경우, 기본 썸네일 이미지를 사용합니다.
                fileName = "default_thumbnail.jpg";

                // 클래스패스에서 기본 이미지를 업로드 디렉터리로 복사합니다.
                try (InputStream defaultImageStream = getClass().getResourceAsStream("/static/img/default_thumbnail.jpg")) {
                    Files.copy(defaultImageStream, uploadPath.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
                }
            }

            novel.setThumbnail(fileName);
            novel.setMember(Member.builder().email(email).build());

            novelRepository.save(novel);

            return novel.getNno();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Novel dtoToEntity(NovelDTO novelDTO) {
        Novel novel = new Novel();
        novel.setTitle(novelDTO.getTitle());
        novel.setContent(novelDTO.getContent());
        novel.setCategory(novelDTO.getCategory());
        novel.setCompleted(novelDTO.isCompleted());
        // 나머지 Novel 엔티티 필드 설정
        return novel;
    }

    
}
