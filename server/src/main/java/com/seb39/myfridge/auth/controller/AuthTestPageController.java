package com.seb39.myfridge.auth.controller;

import com.seb39.myfridge.auth.PrincipalDetails;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class AuthTestPageController {

    /**
     * 인증 테스트 페이지를 위한 API
     */
    @GetMapping("/")
    public String index(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model){
        if(principalDetails != null){
            model.addAttribute("username",principalDetails.getUsername());
        }
        return "index";
    }

    /**
     * 인증 테스트를 위한 Method
     */
    @GetMapping("/api/authtest")
    @ResponseBody
    @Secured("ROLE_USER")
    public String authTest(@AuthenticationPrincipal PrincipalDetails principal){
        return principal.getMemberId() + " / " + principal.getUsername();
    }

}
