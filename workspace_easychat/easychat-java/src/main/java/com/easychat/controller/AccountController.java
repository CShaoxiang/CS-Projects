package com.easychat.controller;


import com.easychat.entity.vo.ResponseVO;
import com.wf.captcha.ArithmeticCaptcha;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController("accountController")
@RequestMapping("/account")
@Validated
public class AccountController extends ABaseController{
    @RequestMapping("/checkCode")
    public ResponseVO checkCode(){
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(100,43);
        String code = captcha.text();

        String checkCodeBase64 = captcha.toBase64();
        Map<String,String> result = new HashMap<>();
        result.put("checkCode",checkCodeBase64);
        return getSuccessResponseVO(result);
    }
}
