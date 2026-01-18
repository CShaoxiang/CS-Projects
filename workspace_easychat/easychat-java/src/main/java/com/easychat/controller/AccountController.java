package com.easychat.controller;



import com.easychat.entity.dto.LoginRequestDTO;
import com.easychat.entity.dto.RegisterRequestDTO;
import com.easychat.entity.vo.ResponseVO;
import com.easychat.service.impl.AccountServiceImpl;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;


@RestController("accountController")
@RequestMapping("/account")
@Validated
public class AccountController extends ABaseController{

    @Resource
    private final AccountServiceImpl accountService;


    public AccountController(AccountServiceImpl accountService) {
        this.accountService = accountService;
    }


    @GetMapping("/checkCode")
    public ResponseVO checkCode(){
        return getSuccessResponseVO(accountService.createCheckCode());
    }

    @PostMapping("/login")
    public ResponseVO login(@Validated LoginRequestDTO request) {
        return getSuccessResponseVO(accountService.login(request));
    }

    @PostMapping("/register")
    public ResponseVO register(@Validated RegisterRequestDTO request) {
        accountService.register(request);
        return getSuccessResponseVO(null);
    }

}
