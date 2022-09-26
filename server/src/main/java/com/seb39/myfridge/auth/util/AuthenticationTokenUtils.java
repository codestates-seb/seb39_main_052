package com.seb39.myfridge.auth.util;

import com.seb39.myfridge.auth.domain.AuthenticationToken;
import com.seb39.myfridge.auth.enums.AppAuthExceptionCode;
import com.seb39.myfridge.auth.exception.AppAuthenticationException;
import org.apache.http.HttpHeaders;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

import static com.seb39.myfridge.auth.util.AppAuthNames.ACCESS_TOKEN;

public class AuthenticationTokenUtils {

    public static AuthenticationToken getTokenFromRequest(HttpServletRequest request){
        String accessToken = getAccessToken(request);
        String refreshToken = getRefreshToken(request);
        return new AuthenticationToken(accessToken,refreshToken);
    }

    public static void addTokenInResponse(HttpServletResponse response, AuthenticationToken token){
        response.addHeader(ACCESS_TOKEN,token.getAccess());
        Cookie cookie = CookieUtil.createHttpOnlyCookie(AppAuthNames.REFRESH_TOKEN, token.getRefresh());
        response.addCookie(cookie);
    }

    public static void removeTokenInResponse(HttpServletResponse response) {
        response.addCookie(CookieUtil.createExpiredCookie(AppAuthNames.REFRESH_TOKEN));
    }

    private static String getAccessToken(HttpServletRequest request) {
        String accessTokenHeader = Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION))
                .orElseThrow(() -> new AppAuthenticationException(AppAuthExceptionCode.ACCESS_TOKEN_NOT_EXIST));
        return authorizationHeaderToAccessToken(accessTokenHeader);
    }

    private static String getRefreshToken(HttpServletRequest request){
        return CookieUtil.getCookieValue(AppAuthNames.REFRESH_TOKEN, request.getCookies())
                .orElse(null);
    }

    private static String authorizationHeaderToAccessToken(String authorizationHeader) {
        return authorizationHeader.replace("Bearer ", "");
    }
}
