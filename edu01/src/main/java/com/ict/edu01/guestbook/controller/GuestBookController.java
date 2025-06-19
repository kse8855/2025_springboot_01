package com.ict.edu01.guestbook.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ict.edu01.guestbook.service.GuestBookService;
import com.ict.edu01.guestbook.vo.GuestBookVO;
import com.ict.edu01.members.vo.DataVO;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;


@RestController
@RequestMapping("/api/guestbook")
@CrossOrigin
public class GuestBookController {
    @Autowired
    private GuestBookService guestBookService;

    @GetMapping("guestbooklist")
    public DataVO getMethodName() {
        DataVO dataVO = new DataVO();
        try {
            List<GuestBookVO> list = guestBookService.guestbooklist();
            if(list == null){
                dataVO.setSuccess(true);
                dataVO.setMessage("데이터가 존재하지 않습니다");
            }else{
                dataVO.setSuccess(true);
                dataVO.setMessage("asd");
                dataVO.setData(list);
            }
        } catch (Exception e) {
            dataVO.setSuccess(false);
            dataVO.setMessage("서버오류");
        }
        return dataVO;
    }
    
    //이름이 일치하면 생략 가능
    @GetMapping("guestbookdetail")
    public DataVO guestbookdetail(@RequestParam("gb_idx") String gb_idx) {
        DataVO dataVO = new DataVO();
        try {
            GuestBookVO gvo = guestBookService.guestbookdetail(gb_idx);
            if(gvo == null){
                dataVO.setSuccess(false);
                dataVO.setMessage("데이터가 존재하지 않습니다");
            }else{
                dataVO.setSuccess(true);
                dataVO.setMessage("데이터 존재");
                dataVO.setData(gvo);
            }
        } catch (Exception e) {
            dataVO.setSuccess(false);
            dataVO.setMessage("서버오류");
        }
        return dataVO; 
    }

    @PostMapping("guestbookinsert")
    public DataVO getGuestbookinsert(@ModelAttribute GuestBookVO gvo,
    @RequestPart(value = "file", required = false) MultipartFile file){
        DataVO dataVO = new DataVO();
        try {
        // 1. 파일 저장 및 파일명 설정
            if (file != null && !file.isEmpty()) {
                String originalFileName = file.getOriginalFilename();
                // 파일명 중복 방지 (타임스탬프)
                String uniqueFileName = System.currentTimeMillis() + "_" + originalFileName;

                // === uploads 경로는 Spring Boot가 실행되는 "프로젝트 루트" 기준 ===
                String uploadDir = System.getProperty("user.dir") + "/uploads";
                java.io.File dir = new java.io.File(uploadDir);
                if (!dir.exists()) dir.mkdirs();

                // 실제 파일 저장
                String uploadPath = uploadDir + java.io.File.separator + uniqueFileName;
                file.transferTo(new java.io.File(uploadPath));

                // ★★★ VO에 파일명 저장
                gvo.setGb_f_name(uniqueFileName);
            }

            // 2. DB 저장
            int result = guestBookService.getGuestbookinsert(gvo);

            dataVO.setSuccess(true);
            dataVO.setMessage("글쓰기 완료");
        } catch (Exception e) {
            e.printStackTrace();
            dataVO.setSuccess(false);
            dataVO.setMessage("서버 오류 : " + e.getMessage());
        }
    return dataVO;
    }

    @PostMapping("guestbookupdate")
    public DataVO guestbookupdate(
        @ModelAttribute GuestBookVO gvo,
        @RequestPart(value = "file", required = false) MultipartFile file
    ) {
        DataVO dataVO = new DataVO();
        try {
            // 파일처리, 기존 파일명 유지/갱신 등은 기존 insert처럼 처리
            int result = guestBookService.guestbookupdate(gvo, file);
            dataVO.setSuccess(true);
            dataVO.setMessage("수정 완료");
        } catch (Exception e) {
            dataVO.setSuccess(false);
            dataVO.setMessage("수정 실패: " + e.getMessage());
        }
        return dataVO;
    }

    @DeleteMapping("guestbookdelete")
    public DataVO deleteGuestbook(@RequestParam("gb_idx") String gb_idx) {
        DataVO dataVO = new DataVO();
        try {
            int result = guestBookService.deleteGuestbook(gb_idx);
            dataVO.setSuccess(true);
            dataVO.setMessage("삭제 완료");
        } catch (Exception e) {
            dataVO.setSuccess(false);
            dataVO.setMessage("삭제 실패: " + e.getMessage());
        }
        return dataVO;
    }

    
    
    
}
