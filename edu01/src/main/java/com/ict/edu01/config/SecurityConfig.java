package com.ict.edu01.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.ict.edu01.jwt.JwtRequestFilter;
import com.ict.edu01.jwt.JwtUtil;
import com.ict.edu01.members.service.MembersService;
import com.ict.edu01.members.service.MyUserDetailService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;


// Configuration : 설정 클래스 (Spring Boot 가 시작될때 실행 된다.)
@Slf4j
@Configuration
public class SecurityConfig {
    
    private final JwtRequestFilter jwtRequestFilter;
    private final JwtUtil jwtUtil;
    private final MyUserDetailService userDetailService;
    private final MembersService membersService;

    // 생성자
    public SecurityConfig(JwtRequestFilter jwtRequestFilter, JwtUtil jwtUtil,
        MyUserDetailService userDetailService, MembersService membersService){
         
        this.jwtRequestFilter = jwtRequestFilter;
        this.userDetailService = userDetailService;
        this.membersService = membersService;
        this.jwtUtil = jwtUtil;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // CORS 설정 ( a -> b 의미는  a 를 받아서 b를 실행하라 (람다식 표현))
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            // CSRF 보호 비활성화 (JWT 사용시 일반적으로 비활성화 )
            // 사용자가 로그인 된 상태를 악용하여, 악의적인 사이트가 사용자의 권한으로 요청을 보내도록 만드는 공격
            // JWT는 세션을 사용하지 않고, Authorization헤더로 인증(CSRF 의 위험이 없음)
            .csrf(csrf -> csrf.disable())

            // 요청별 권한 설정
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/api/members/login").permitAll()     // 허용하는 url만 작성
                .requestMatchers("/api/members/register").permitAll()   // 허용하는 url만 작성
                .requestMatchers("/api/members/refresh").permitAll()   // 허용하는 url만 작성
                .requestMatchers("/oauth2/**").permitAll()   // 허용하는 url만 작성
                .requestMatchers("/oauth2/authorization/**").permitAll()   // 허용하는 url만 작성
                .requestMatchers("/api/guestbook/**").permitAll()
                .requestMatchers("/api/guestbook/guestbookinsert").permitAll()
                .requestMatchers("/api/guestbook/guestbooklist").permitAll()
                .requestMatchers("/uploads/**").permitAll()
                .anyRequest().authenticated())

            // oauth2Login 설정
            // build.gradle 에서 import xxxxxxxxxxxxx-oauth2-client
            // successHandler() : 로그인 성공시 호출
            // userInfoEndpoint => 인증과정에서 인증된 사용자에 대한 정보를 제공하는 API 엔드포인트
            .oauth2Login(oauth2 -> oauth2
                .successHandler(oAuth2AuthenticationSuccessHandler())
                .userInfoEndpoint(userInfo -> userInfo.userService(oAuth2UserService()))
            )

            // 인증 실패(비로그인) 시 401로 JSON 반환 (***이 부분이 핵심!***)
            .exceptionHandling(exception -> exception
                .authenticationEntryPoint((request, response, authException) -> {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().write("{\"error\": \"Unauthorized\"}");
                })
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

            // 사용자 요청이 오면 먼저 jwtRequestFilter가 실행되어, JWT토큰을 검증 할 후
            // 이상이 없으면 SpringSecurity의 인증된 사용자로 처리된다.
            // UsernamePasswordAuthenticationFilter보다 앞에 삽입하라
            http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    
    
    @Bean
    // 동의항목처리
    OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler(){
        return new OAuth2AuthenticationSuccessHandler(jwtUtil, userDetailService, membersService);
    }

    @Bean
    // 사용자 정보 정보
    OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService(){
        return new CustomerOAuth2UserService();
    }

    // CORS 설정
    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedOrigins(Arrays.asList(
            "http://localhost:3000",
            "http://studyjava.shop",
            "http://43.203.201.193"
        ));
        corsConfig.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        corsConfig.setAllowedHeaders(Arrays.asList("*"));
        corsConfig.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);
        return source;
    }

    // 패스워드 인코더
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    // 인증 매니저
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}