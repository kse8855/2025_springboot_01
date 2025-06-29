package com.ict.edu01.members.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ict.edu01.members.mapper.MembersMapper;
import com.ict.edu01.members.vo.MembersVO;
import com.ict.edu01.members.vo.RefreshVO;

@Service
public class MembersServiceImpl implements MembersService{

    @Autowired
    private MembersMapper membersMapper;

    @Override
    public MembersVO getLogin(MembersVO mvo) {
        System.out.println(">>>> DB 접속 성공 여부 확인 로그");
        return membersMapper.getLogin(mvo);
    }

    @Override
    public int getRegister(MembersVO mvo) {
        return membersMapper.getRegister(mvo);
    }

    @Override
    public MembersVO getMyPage(String m_idx) {
        return membersMapper.getMyPage(m_idx);
    }

    @Override
    public void saveRefreshToken(String m_id, String refreshToken, Date expiry_date) {
        membersMapper.saveRefreshToken(new RefreshVO (m_id, refreshToken, expiry_date));
    }

    @Override
    public RefreshVO getRefreshToken(String m_id) {
        return membersMapper.getRefreshToken(m_id);
    }

    @Override
    public void getRegister2(MembersVO mvo) {
        membersMapper.getRegister2(mvo);
    }
    
}
