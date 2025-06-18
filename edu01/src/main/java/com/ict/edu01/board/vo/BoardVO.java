package com.ict.edu01.board.vo;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class BoardVO {
    private int b_idx, hit, b_groups, b_step, b_lev, active;
    private String writer, title, content, pwd, regdate, f_name;
    private MultipartFile file_name;
}
