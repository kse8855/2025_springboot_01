package com.ict.edu01.board.service;

import java.util.List;

import com.ict.edu01.board.vo.BoardVO;

public interface BoardService {

    List<BoardVO> boardlist();
    BoardVO boarddetail(String b_idx);
    int BoardInsert (BoardVO bvo);
}
