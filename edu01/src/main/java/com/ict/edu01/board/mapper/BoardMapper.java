package com.ict.edu01.board.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ict.edu01.board.vo.BoardVO;

@Mapper
public interface BoardMapper {

    List<BoardVO> boardlist();
    BoardVO boarddetail(String b_idx);
    int BoardInsert (BoardVO bvo);
}
