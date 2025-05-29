package com.ict.edu01.members.service;

import org.springframework.stereotype.Service;

import com.ict.edu01.members.mapper.MembersMapper;
import com.ict.edu01.members.vo.MembersVO;

import java.util.ArrayList;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MyUserDetailService implements UserDetailsService {

    private final MembersMapper membersMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        MembersVO member = membersMapper.findUserById(username);
        return new User(member.getM_id(), member.getM_pw(), new ArrayList<>());
    }
}
