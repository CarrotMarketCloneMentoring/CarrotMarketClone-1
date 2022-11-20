package com.daangn.clone.common.interceptor;

import com.daangn.clone.common.enums.Status;
import com.daangn.clone.common.jwt.JwtUtil;
import com.daangn.clone.common.response.ApiResponse;
import com.daangn.clone.common.response.ApiResponseStatus;
import com.daangn.clone.member.repository.MemberRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;
    private ObjectMapper objectMapper = new ObjectMapper();

    /** 요청이 Controller에게 도달하기 전에 먼저 , 이 인터셉터의 preHandle() 이 호출된다
     * preHandle()의 리턴값이 false 면 요청이 Controller에게 도달하지 x  <- 이점을 이용!!
     */

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        /**  1. jwtUtil을 사용하여 토큰에 저장된 username 정보를 꺼냄 */
        Optional<Claims> memberId = Optional.ofNullable(request.getHeader("token"))
                .map(t -> jwtUtil.parseJwtToken(t));


        // 만약 해석된 username 값이 존재하지 않는다면 -> 이는 필수 헤더인 token 값이 존재하지 않았기 떄문 -> 그에 따른 응답을 보낸다
        if(memberId.isEmpty()){
            return sendErrorResponse(true, false, response, ApiResponseStatus.NO_JWT_TOKEN);
        }

        // jwt token에 저장된 username 꺼내서 , 그 값이 유효한지를 판단
        if(!memberRepository.existsByIdAndStatus(Long.valueOf(memberId.get().getSubject()), Status.ACTIVE)){
            return sendErrorResponse(false, false, response, ApiResponseStatus.INVALID_JWT_TOKEN);
        }

        /** 2. jwt token이 존재하고 , 그 token이 유효하다면 (그 token안에 든 kakaoId가 유효) -> preHandle() 에서 true를 반환하여 ,
         * Controller에게 요청이 전달하게 해줌 -> 즉 인가를 실행 */
        request.setAttribute("memberId",Long.valueOf( memberId.get().getSubject()));
        return true;
    }



    /** 토큰이 존재하지 않거나 , 유효하지 않은 문제상황인 경우 -> 그 문제상황에 예외를 터뜨리지 않고 , direct로 JSON 응답을 보내는 경우 */
    private boolean sendErrorResponse(boolean isEmptyToken , boolean isValidToken, HttpServletResponse response, ApiResponseStatus status){



        //응답의 meta 정보를 setting한 후
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            //실질적인 응답값을 , JSON 형식의 String으로 변환화여 보낸다. (단 BaseResponse라는 공통 응답 형식을 지키면서)
            String result = objectMapper.writeValueAsString(ApiResponse.fail(status));
            response.getWriter().print(result);
        }
        catch (IOException e){
            log.error("필수 헤더인 token값이 들어오지 않았거나 , 유효하지 않은 toekn 값이 들어와, 그에따른 응답을 처리하는 과정에서 IOException이 발생하였습니다.");
        }

        log.error("EXCEPTION = {}, message = {}", status, status.getMessage());
        return false;
    }

}

