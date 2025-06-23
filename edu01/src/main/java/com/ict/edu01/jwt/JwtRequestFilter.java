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

        String path = request.getRequestURI();
        log.info("JwtRequestFilter call, path={}", path);

        // === [1] permitAll 경로는 무조건 통과! ===
        if (
            "/api/members/refresh".equals(path) ||
            "/api/guestbook/guestbookinsert".equals(path) ||
            "/api/guestbook/guestbooklist".equals(path) ||
            "/api/guestbook/guestbookupdate".equals(path) ||
            "/api/guestbook/guestbookdelete".equals(path) ||
            path.startsWith("/api/guestbook/guestbookdetail") ||
            path.startsWith("/api/guestbook/") ||
            path.startsWith("/uploads/") ||
            "/api/board/boarddetail".equals(path) ||
            path.startsWith("/api/board/boarddetail") || 
            "/api/board/boardinsert".equals(path) ||
            path.startsWith("/api/board/boardinsert") ||
            "/api/board/boardlist".equals(path) ||
            path.startsWith("/api/board/**")
            
        ) {
            filterChain.doFilter(request, response);
            return;
        }

        // === [2] 그 외 경로에서만 JWT 체크 ===
        final String authorizationHeader = request.getHeader("Authorization");
        String userId = null;
        String jwtToken = null;

        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            jwtToken = authorizationHeader.substring(7);
            try {
                if(jwtUtil.isTokenExpired(jwtToken)){
                    log.info("token expire error");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType("application/json; charset=UTF-8");
                    response.getWriter().write("{\"success\":false, \"message\":\"tokenexpired\"}");
                    return ;
                }
                userId =  jwtUtil.validateAndExtractUserId(jwtToken);
                log.info("userId : " + userId);

            } catch (Exception e) {
                log.info("token error");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json; charset=UTF-8");
                response.getWriter().write("{\"success\":false, \"message\":\"tokenexpired\"}");
                return ;
            }

            // 사용자ID가 존재하고 SecurityContext에 인증정보가 없는 경우 등록하기 위해서서
            if(userId != null && SecurityContextHolder.getContext().getAuthentication() == null){
                log.info("jwtToken-2 : " + jwtToken.substring(7));
                UserDetails userDetails = userDetailsService.loadUserByUsername(userId);
                if(jwtUtil.validateToken(jwtToken, userDetails)){
                    UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    log.info("JWT token ok");
                }else{
                    log.info("JWT token error"); 
                }
            }
        }else{
            // 이 else는 그냥 log만 남기고, 401 리턴 금지! 
            log.info("Authorization empty Bearer token empty");
        }

        filterChain.doFilter(request, response);
    }
}