package com.ict.edu01.members.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MembersVO {
    
    private String m_id, m_pw, m_name, m_addr, m_addr2, m_email, m_phone, m_reg, 
                    m_active_reg, sns_email_naver, sns_email_kakao, sns_provider;
    private int m_idx, m_active;
}
