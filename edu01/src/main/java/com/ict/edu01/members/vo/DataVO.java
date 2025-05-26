package com.ict.edu01.members.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataVO {
    //클라이언트에 결과 보낼 때 사용하는 VO

    private boolean success;
    private String message;
    private Object data;

    
}
