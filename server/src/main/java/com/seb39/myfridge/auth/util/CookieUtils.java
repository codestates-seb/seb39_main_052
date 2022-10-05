package com.seb39.myfridge.auth.util;

import javax.servlet.http.Cookie;
import java.util.Arrays;
import java.util.Optional;

public class CookieUtils {

    public static Optional<String> getCookieValue(String name, Cookie[] cookies){
        if(cookies == null){
            return Optional.empty();
        }

        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(name))
                .map(Cookie::getValue)
                .findAny();
    }

    public static Cookie createHttpOnlyCookie(String name, String token){
        Cookie cookie = new Cookie(name, token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setDomain("seb39myfridge.ml");
        return cookie;
    }

    public static Cookie createExpiredCookie(String name){
        Cookie cookie = new Cookie(name,null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setDomain("seb39myfridge.ml");
        cookie.setMaxAge(0);
        return cookie;
    }
}
