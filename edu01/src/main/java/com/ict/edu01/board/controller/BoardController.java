package com.ict.edu01.board.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ict.edu01.board.service.BoardService;
import com.ict.edu01.board.vo.BoardVO;
import com.ict.edu01.guestbook.vo.GuestBookVO;
import com.ict.edu01.members.vo.DataVO;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;


@RestController
@RequestMapping("/api/board")

public class BoardController {
    @Autowired
    private BoardService boardService;

    @GetMapping("boardlist")
    public DataVO boardList() {
        DataVO dataVO = new DataVO();
        try {
            List<BoardVO> list = boardService.boardlist();
            dataVO.setSuccess(true);
            dataVO.setMessage("성공");
            dataVO.setData(list);
        } catch (Exception e) {
            dataVO.setSuccess(false);
            dataVO.setMessage("서버 오류");
        }
        return dataVO;
    }

    @GetMapping("/boarddetail")
    public DataVO boarddetail(@RequestParam("b_idx") String b_idx) {
        DataVO dataVO = new DataVO();
        try {
            BoardVO bvo = boardService.boarddetail(b_idx);
            if(bvo == null){
                dataVO.setSuccess(false);
                dataVO.setMessage("데이터 없음");
            }else{
                dataVO.setSuccess(true);
                dataVO.setMessage("데이터 있음");
                dataVO.setData(bvo);
            }
        } catch (Exception e) {
            dataVO.setSuccess(false);
            dataVO.setMessage("서버오류");
        }
        return dataVO;
    }

    @PostMapping("boardinsert")
    public DataVO getBoardInsert(@ModelAttribute BoardVO gvo,
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
                gvo.setF_name(uniqueFileName);
            }

            // 2. DB 저장
            int result = boardService.getBoardInsert(gvo);

            dataVO.setSuccess(true);
            dataVO.setMessage("글쓰기 완료");
        } catch (Exception e) {
            e.printStackTrace();
            dataVO.setSuccess(false);
            dataVO.setMessage("서버 오류 : " + e.getMessage());
        }
    return dataVO;
    }
    
}