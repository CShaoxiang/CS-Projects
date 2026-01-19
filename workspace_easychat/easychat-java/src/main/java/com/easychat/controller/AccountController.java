package com.easychat.controller;



import com.easychat.entity.dto.LoginRequestDTO;
import com.easychat.entity.dto.RegisterRequestDTO;
import com.easychat.entity.vo.ResponseVO;
import com.easychat.service.AccountService;
import com.easychat.service.impl.AccountServiceImpl;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


@RestController("accountController")
@RequestMapping("/account")
@Validated
public class AccountController extends ABaseController{


    private final AccountService accountService;


    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }


    @GetMapping("/checkCode")
    public ResponseVO checkCode(){
        return getSuccessResponseVO(accountService.createCheckCode());
    }

    @PostMapping("/login")
    public ResponseVO login(@Validated @RequestBody LoginRequestDTO request) {
        return getSuccessResponseVO(accountService.login(request));
    }

    @PostMapping("/register")
    public ResponseVO register(@Validated  @RequestBody RegisterRequestDTO request) {
        accountService.register(request);
        return getSuccessResponseVO(null);
    }

}
