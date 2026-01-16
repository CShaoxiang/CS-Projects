package com.easychat.controller;


import com.easychat.entity.vo.ResponseVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("accountController")
@RequestMapping("/account")
@Validated
public class AccountController extends ABaseController{
    @RequestMapping("/checkCode")
    public ResponseVO checkCode(){
        return getSuccessResponseVO(null);
    }
}
