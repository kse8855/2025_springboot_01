package com.ict.edu01.guestbook.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.ict.edu01.guestbook.vo.GuestBookVO;

public interface GuestBookService {
    List<GuestBookVO> guestbooklist();
    GuestBookVO guestbookdetail(String gb_idx);
    int getGuestbookinsert(GuestBookVO gvo);
    int guestbookupdate(GuestBookVO gvo, MultipartFile file);
    int deleteGuestbook(String gb_idx);
}
