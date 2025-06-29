package com.ict.edu01.guestbook.vo;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GuestBookVO {
    private int gb_idx;
    private String gb_name, gb_subject, gb_content, gb_email, gb_f_name, 
    gb_regdate, gb_pw, gb_old_f_name;
    
}