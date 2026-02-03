package com.easychat.controller;



import cn.hutool.core.bean.BeanUtil;
import com.easychat.annotation.GlobalInterceptor;
import com.easychat.entity.dto.LoginRequestDTO;
import com.easychat.entity.dto.RegisterRequestDTO;
import com.easychat.entity.dto.SysSettingDto;
import com.easychat.entity.vo.ResponseVO;
import com.easychat.entity.vo.SysSettingVO;
import com.easychat.redis.RedisComponent;
import com.easychat.service.AccountService;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.annotation.Resources;


@RestController("accountController")
@RequestMapping("/account")
@Validated
public class AccountController extends ABaseController{


    private final AccountService accountService;

    @Resource
    private  RedisComponent redisComponent;


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

    @RequestMapping(value = "/getSysSetting")
    @GlobalInterceptor
    public ResponseVO getSysSetting() {
        SysSettingDto sysSettingDto = redisComponent.getSysSetting();
        return getSuccessResponseVO(BeanUtil.copyProperties(sysSettingDto, SysSettingVO.class));
    }

}
