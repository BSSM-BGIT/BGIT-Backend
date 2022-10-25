package bssm.db.bssmgit.global.util;

import bssm.db.bssmgit.domain.user.web.dto.response.TokenResponseDto;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import static bssm.db.bssmgit.global.jwt.JwtProperties.ACCESS_TOKEN_VALID_TIME;
import static bssm.db.bssmgit.global.jwt.JwtProperties.REFRESH_TOKEN_VALID_TIME;

@Component
public class CookieProvider {

    public Cookie createCookie(String name, String value, long time) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
//        cookie.setSecure(true);
        cookie.setMaxAge((int) time);
        cookie.setPath("/");
//        cookie.setDomain(COOKIE_DOMAIN);
        return cookie;
    }

    public Cookie getCookie(HttpServletRequest req, String name) {
        final Cookie[] cookies = req.getCookies();
        if (cookies == null) return null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(name)) {
                return cookie;
            }
        }
        return null;
    }

    public TokenResponseDto jwtToCookies(String accessToken, String refreshToken) {
        Cookie accessTokenCookie = createCookie("ACCESS-TOKEN", accessToken, ACCESS_TOKEN_VALID_TIME);
        Cookie refreshTokenCookie = createCookie("REFRESH-TOKEN", refreshToken, REFRESH_TOKEN_VALID_TIME);

        return TokenResponseDto.builder()
                .accessToken(accessTokenCookie)
                .refreshToken(refreshTokenCookie)
                .build();
    }
}
