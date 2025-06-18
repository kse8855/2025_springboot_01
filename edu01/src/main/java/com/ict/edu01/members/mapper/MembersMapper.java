package com.ict.edu01.members.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ict.edu01.members.vo.MembersVO;
import com.ict.edu01.members.vo.RefreshVO;

@Mapper
public interface MembersMapper {
    
    MembersVO getLogin(MembersVO mvo);
    
    int getRegister(MembersVO mvo);
    
    MembersVO getMyPage(String m_id);

    MembersVO findUserById(@Param("username") String username) ;

    void saveRefreshToken(RefreshVO refreshVO);
    RefreshVO getRefreshToken(String m_id);
    void getRegister2(MembersVO mvo);
}