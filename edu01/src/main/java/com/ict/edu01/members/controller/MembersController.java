package com.ict.edu01.members.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ict.edu01.jwt.JwtService;
import com.ict.edu01.jwt.JwtUtil;
import com.ict.edu01.members.service.MembersService;
import com.ict.edu01.members.service.MyUserDetailService;
import com.ict.edu01.members.vo.DataVO;
import com.ict.edu01.members.vo.MembersVO;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/members")
public class MembersController {


    @Autowired
    private MembersService membersService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    MyUserDetailService userDetailService;
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private JwtService jwtService;

    MembersController(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/hello")
    public String getHello() {
        return "Hello, SpringBoot";
    }

    @PostMapping("/login")
    public DataVO getLogin(@RequestBody MembersVO mvo) {

        DataVO dataVO = new DataVO();

        try{

        // DataVO dataVO = new DataVO();
        // 만약에 맞면 
        // dataVO.setSuccess(true);
        // dataVO.setMessage("로그인 성공");
        
        // 만약 정보 전달할 data가 하나이면  
        // dataVO.setData(정보);
        
        // 만약 정보 전달할 data가 여러개 이면 
        // Map<String,Object> result = new HashMap<>();
        // result.put("list", list);
        // result.put("membersVO", mvo);
        // result.put("totalCount", totalCount);

        // 맞지 않으면 
        // dataVO.setSuccess(false);
        // dataVO.setMessage("로그인 실패");
        
        // DB 에 가서 m_id 와 m_pw가 맞는지 확인한다.
        /* jwt 사용하지 않은 로그인
        MembersVO membersVO = membersService.getLogin(mvo);
        if(membersVO == null){
            dataVO.setSuccess(false);
            dataVO.setMessage("로그인 실패");
        }else{
            // 로그인 성공했을 때 token 처리
            
            dataVO.setSuccess(true);
            dataVO.setMessage("로그인 성공");
            dataVO.setData(membersVO);
        }
        */

            UserDetails userDetails = userDetailService.loadUserByUsername(mvo.getM_id());
            if(! passwordEncoder.matches(mvo.getM_pw(), userDetails.getPassword())){
                return new DataVO(false, "비밀번호틀렸음", null);
            }


            String accessToken = jwtUtil.generateAccessToken(mvo.getM_id());
            String refreshToken = jwtUtil.generateRefreshToken(mvo.getM_id());
         
            // jwt를 활용한 로그인 처리 
            //Map<String, String> tokens = jwtService.login(mvo);
            membersService.saveRefreshToken(mvo.getM_id(), refreshToken, jwtUtil.extractExpiration(refreshToken));

            Map<String, String> tokens = new HashMap<>();
            tokens.put("accessToken", accessToken);
            tokens.put("refreshToken", refreshToken);
            
            dataVO.setSuccess(true);
            dataVO.setData(tokens);
            dataVO.setMessage("로그인 성공");
        }catch(Exception e){
            dataVO.setSuccess(false);
            dataVO.setMessage("서버 오류 : " + e.getMessage());
        }

        return dataVO;
    }
    
    @PostMapping("/register")
    public DataVO getRegister(@RequestBody MembersVO mvo) {
        DataVO dataVO = new DataVO();
        try {
            //비밀번호 암호화 
            mvo.setM_pw(passwordEncoder.encode(mvo.getM_pw()));
            int result = membersService.getRegister(mvo);
            if(result >0){
                dataVO.setSuccess(true);
                dataVO.setMessage("회원가입 성공");
            }else{
                dataVO.setSuccess(false);
                dataVO.setMessage("회원가입 실패");
            }

        } catch (Exception e) {
            dataVO.setSuccess(false);
            dataVO.setMessage("서버 오류 : " + e.getMessage());
        }
        return dataVO;
    }
    
    @PostMapping("/mypage")
    public DataVO getMyPage(@RequestBody MembersVO mvo2) {
        System.out.println("m_idx : " + mvo2);
        DataVO dataVO = new DataVO();
        try {
            MembersVO mvo = membersService.getMyPage(mvo2.getM_idx());
            if(mvo == null){
                dataVO.setSuccess(false);
                dataVO.setMessage("잘못된 정보 입니다.");
            }else{
                dataVO.setSuccess(true);
                dataVO.setMessage("가져오기 성공");
                dataVO.setData(mvo);
            }
        } catch (Exception e) {
            dataVO.setSuccess(false);
            dataVO.setMessage("서버 오류 : " + e.getMessage());
        }
        return dataVO;
    }
    
}
