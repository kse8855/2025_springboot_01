package com.ict.edu01.jwt;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;


// JWT 기반 인증을 처리하는 필터 
// HTTP 요청이 올때 마다 딱 한번 실행되며,  JWT 토근을 감시하고, 인증 처리 해줌줌
@Slf4j
@Component
public class JwtRequestFilter extends OncePerRequestFilter{

   @Autowired
   private JwtUtil jwtUtil;
   
   @Autowired
   private UserDetailsService userDetailsService;


    


    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        log.info("JwtRequestFilter 호출");
                
        // 들어오는 요청마다 Authorization 있고 Authorization를 jwt 검증하기 위해서 추출
        final String authorizationHeader = request.getHeader("Authorization");
        String userId = null;
        String jwtToken = null;

        // authorizationHeader 에 "Bearer " 있어야 다음 단계를 할수 있다.
        if(authorizationHeader != null && authorizationHeader .startsWith("Bearer ")){
            jwtToken = authorizationHeader.substring(7);
            try {
                // 토큰 만료 검사 
                if(jwtUtil.isTokenExpired(jwtToken)){
                    log.info("토큰만료");
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "token error");
                    return ;
                }
                userId = jwtUtil.validateAndExtractUserId(jwtToken);
            } catch (Exception e) {
               log.info("토큰 처리 중 오류 발생");
               response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "토근 처리 오류");
            }

        }else{
            log.info("Authorization 비었거나 Bearer 토큰이 없네요 ");
        }

        if(userId != null && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails = userDetailsService.loadUserByUsername(userId);

            if(jwtUtil.validateToken(jwtToken, userDetails)){
                UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authToken);
                log.info("Jwt token ok");
            }else{
                log.info("Jwt token error");
            }
        }

        filterChain.doFilter(request, response);
    }
    
}