package com.example.laboratory.global.config;

import com.example.laboratory.global.exception.Unauthorized;
import com.example.laboratory.global.provider.JwtTokenProvider;
import com.example.laboratory.token.entity.Token;
import com.example.laboratory.token.repository.TokenRepository;
import com.example.laboratory.user.request.UserSession;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Slf4j
@RequiredArgsConstructor
public class AuthResolver implements HandlerMethodArgumentResolver {

    private final TokenRepository tokenRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(UserSession.class);
    }

    @Override
    @Transactional
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        if (servletRequest == null) {
            log.error(">>> ServletRequest Error");
            throw new Unauthorized();
        }

        Cookie[] cookies = servletRequest.getCookies();
        if(cookies == null || cookies.length == 0){
            log.error(">>> No Have AccessToken");
            return new UserSession(0L, "guest");
        }

        try {
            String accessToken = cookies[0].getValue();
            if(jwtTokenProvider.validateToken(accessToken)){
                String userId = jwtTokenProvider.getClaims(accessToken).getSubject();
                return new UserSession(Long.parseLong(userId), accessToken);
            } else{
                String userId = jwtTokenProvider.getClaims(accessToken).getSubject();
                List<Token> token = tokenRepository.findByUserId(Long.parseLong(userId));

                if(token.isEmpty()) {
                    log.error(">>> No Have RefreshToken");
                    return new UserSession(0L, "guest");
                }

                String refreshToken = token.get(0).getRefreshToken();
                if(jwtTokenProvider.validateToken(refreshToken)) {
                    String newAccessToken = jwtTokenProvider.createAccessToken(Long.parseLong(userId));
                    return new UserSession(Long.parseLong(userId), newAccessToken);
                }else {
                    tokenRepository.deleteByUserId(Long.parseLong(userId)); // TODO: Transcational 버그가 한 차례 발생함. 향후 세부 분석 필요
                    throw new Unauthorized();
                }
            }
        } catch (JwtException e) {
            throw new Unauthorized();
        }
    }
}

/*** 쿠키를 이용한 인증 방법
 *         return new UserSession(session.getUser().getId());
 */