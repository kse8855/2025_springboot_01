package com.ict.edu01.board.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ict.edu01.board.mapper.BoardMapper;
import com.ict.edu01.board.vo.BoardVO;

@Service
public class BoardServiceImpl implements BoardService{

    @Autowired
    public BoardMapper boardMapper;

    @Override
    public List<BoardVO> boardlist() {
        return boardMapper.boardlist();
    }

    @Override
    public BoardVO boarddetail(String b_idx) {
        return boardMapper.boarddetail(b_idx);
    }

    @Override
    public int BoardInsert(BoardVO bvo) {
        return boardMapper.BoardInsert(bvo);
    }
    
}
