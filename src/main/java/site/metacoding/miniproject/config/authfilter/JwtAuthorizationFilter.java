package site.metacoding.miniproject.config.authfilter;

import java.io.IOException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import lombok.extern.slf4j.Slf4j;
import site.metacoding.miniproject.dto.SessionUserDto;
import site.metacoding.miniproject.utill.SecretKey;
import site.metacoding.miniproject.utill.JWTToken.CookieForToken;
import site.metacoding.miniproject.utill.JWTToken.TokenToSinedDto;

@Slf4j
public class JwtAuthorizationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String tokenForCookie = CookieForToken.cookieToToken(req.getCookies());
        if (tokenForCookie == null) {
            return;
        }

        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(SecretKey.SECRETKEY.key())).build().verify(tokenForCookie);
        
        //map 형식으로 저장되어있는 토큰값을 map형식으로 가져온다.
        Map<String, Object> getSigned = decodedJWT.getClaim("sessionUserDto").asMap();

        TokenToSinedDto tokenToSinedDto = new TokenToSinedDto();
        SessionUserDto sessionUserDto = tokenToSinedDto.tokenToSignedDto(getSigned);

        HttpSession session = req.getSession();

        session.setAttribute("principal", sessionUserDto);

        chain.doFilter(req, resp);
    }

}
