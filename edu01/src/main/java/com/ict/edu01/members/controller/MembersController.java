package com.ict.edu01.members.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ict.edu01.members.service.MembersService;
import com.ict.edu01.members.vo.DataVO;
import com.ict.edu01.members.vo.MembersVO;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/api/members")
public class MembersController {

    @Autowired
    private MembersService membersService;
    
    @GetMapping("/hello")
    public String getHello() {
        return "Hello, SpringBoot";
    }

    @PostMapping("/login")
    public DataVO getLogin(@RequestBody MembersVO mvo) {
        
        DataVO dataVO = new DataVO();

        try{
        //DB에 가서 m_id, m_pw가 맞는지 확인
        MembersVO membersVO = membersService.getLogin(mvo);
        
        //DataVO dataVO = new DataVO();

        // //만약에 맞다면
        // dataVO.setSuccess(true);
        // dataVO.setMessage("로그인성공");

        // //전달할 data가 하나라면 
        // dataVO.setData(정보);
        // //여러개라면
        // Map<String, Object> result = new HashMap<>();
        // result.put(key:"list" list);
        // result.put(key:"membersVO" mvo);
        // result.put(key:"totalCount" totalCount);

        // //맞지 않다면
        // dataVO.setSuccess(false);
        // dataVO.setMessage("로그인실패");

        if(membersVO == null){
            dataVO.setSuccess(false);
            dataVO.setMessage("로그인실패");
        }else{
            dataVO.setSuccess(true);
            dataVO.setMessage("로그인 성공");
            dataVO.setData(membersVO);
        }
        }catch(Exception e){
            dataVO.setSuccess(false);
            dataVO.setMessage("로그인 실패");
        }
        return dataVO;
    }

   @PostMapping("/register")
   public DataVO getRegister(@RequestBody MembersVO mvo) {
       DataVO dataVO = new DataVO();

       try {
        
            int result = membersService.getRegister(mvo);
            if(result > 0){
                dataVO.setSuccess(true);
                dataVO.setMessage("회원가입 성공");
            }else{
                dataVO.setSuccess(false);
                dataVO.setMessage("회원가입 실패");
            }
       } catch (Exception e) {
            dataVO.setSuccess(false);
            dataVO.setMessage("회원가입 실패");
       }
       
       return dataVO;
   }

   @PostMapping("mypage")
   public DataVO getMyPage(@RequestBody MembersVO mvo2) {
       System.out.println("m_idx:" + mvo2);
       DataVO dataVO = new DataVO();
       try {
        MembersVO mvo = membersService.getMyPage(mvo2.getM_idx());
        if(mvo == null){
            dataVO.setSuccess(false);
            dataVO.setMessage("잘못된 정보");
        }else{
            dataVO.setSuccess(true);
            dataVO.setMessage("성공");
            dataVO.setData(mvo);
        }
       } catch (Exception e) {
        dataVO.setSuccess(false);
            dataVO.setMessage("오류: " + e.getMessage());
       }
       
       return dataVO;
   }
   
   
    
    
    
}
