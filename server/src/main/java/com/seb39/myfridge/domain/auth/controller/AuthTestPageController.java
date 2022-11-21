package com.seb39.myfridge.domain.auth.controller;

import com.seb39.myfridge.domain.auth.annotation.AuthMemberId;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AuthTestPageController {

    /**
     * 인증 테스트를 위한 Method
     */
    @GetMapping("/api/authtest")
    @ResponseBody
    @Secured("ROLE_USER")
    public String authTest(@AuthMemberId Long memberId){
        return ""+memberId;
    }
}
