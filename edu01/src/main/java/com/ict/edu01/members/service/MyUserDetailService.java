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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println(">>> 사용자 조회 시도: " + username);

        MembersVO member = membersMapper.findUserById(username);

        if (member == null) {
            System.out.println(">>> 사용자 없음: " + username);
            throw new UsernameNotFoundException("해당 사용자가 존재하지 않습니다: " + username);
        }

        System.out.println(">>> 사용자 조회 성공: " + member.getM_id());

        return new User(
            member.getM_id(),
            member.getM_pw(),
            new ArrayList<>() // 권한이 비어 있어도 로그인은 가능
        );
    }
}
