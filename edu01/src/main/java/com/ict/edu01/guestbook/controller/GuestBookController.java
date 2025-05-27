package com.ict.edu01.guestbook.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ict.edu01.guestbook.service.GuestBookService;
import com.ict.edu01.guestbook.vo.GuestBookVO;
import com.ict.edu01.members.vo.DataVO;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/guestbook")
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
    
    //이름이 일치하면 생략 가능능
    @GetMapping("guestbookdetail")
    public String guestbookdetail(@RequestParam("gb_idx") String gb_idx) {
        DataVO dataVO = new DataVO();
        try {
            GuestBookVO gvo = guestBookService.guestbookdetail(gb_idx);
            if(gvo == null){
                dataVO.setSuccess(true);
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
    
    
    
}
