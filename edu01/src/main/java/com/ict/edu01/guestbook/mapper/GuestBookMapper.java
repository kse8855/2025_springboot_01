package com.ict.edu01.guestbook.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.web.multipart.MultipartFile;

import com.ict.edu01.guestbook.vo.GuestBookVO;



@Mapper
public interface GuestBookMapper {
    List<GuestBookVO> guestbooklist();
    GuestBookVO guestbookdetail(String gb_idx);
    int getGuestbookinsert(GuestBookVO gvo);
    int guestbookupdate(GuestBookVO gvo, MultipartFile file);
    int deleteGuestbook(String gb_idx);
}
