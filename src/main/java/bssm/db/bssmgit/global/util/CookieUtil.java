package bssm.db.bssmgit.global.util;

import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Component
public class CookieUtil {

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

}
