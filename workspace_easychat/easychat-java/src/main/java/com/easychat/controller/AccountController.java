package com.easychat.controller;



import com.easychat.entity.vo.ResponseVO;
import com.easychat.service.impl.AccountServiceImpl;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;


@RestController("accountController")
@RequestMapping("/account")
@Validated
public class AccountController extends ABaseController{

    @Resource
    private AccountServiceImpl accountService;

    @RequestMapping("/checkCode")
    public ResponseVO checkCode(){
        return getSuccessResponseVO(accountService.createCheckCode());
    }
}
