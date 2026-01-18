package com.easychat.service.impl;


import com.easychat.entity.constants.Constants;
import com.easychat.entity.dto.*;
import com.easychat.entity.po.UserInfo;
import com.easychat.exception.BusinessException;
import com.easychat.redis.RedisUtils;
import com.easychat.service.AccountService;
import com.wf.captcha.ArithmeticCaptcha;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service("accountService")
public class AccountServiceImpl implements AccountService {

    private static final int CAPTCHA_WIDTH = 100;
    private static final int CAPTCHA_HEIGHT = 42;
    private static final long CAPTCHA_TTL_SECONDS = Constants.REDIS_TIME_1MIN * 6 ;


    private final RedisUtils<String> redisUtils;
    private final UserInfoServiceImpl userInfoService;

    public AccountServiceImpl(RedisUtils<String> redisUtils, UserInfoServiceImpl userInfoService) {
        this.redisUtils = redisUtils;
        this.userInfoService = userInfoService;
    }


    @Override
    public CheckCodeResponseDTO createCheckCode(){
        // 1) Generate captcha
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(CAPTCHA_WIDTH, CAPTCHA_HEIGHT);

        // 2) Extract expected answer
        String code = captcha.text();
        // 3) Generate a unique key for this captcha session
        String checkCodeKey = UUID.randomUUID().toString();

        // 4) Store expected answer in Redis with TTL
        String redisKey = Constants.REDIS_KEY_CHECK_CODE + ":" + checkCodeKey;
        redisUtils.setex(redisKey, code, CAPTCHA_TTL_SECONDS);

        return new CheckCodeResponseDTO(checkCodeKey, captcha.toBase64());
    }

    @Override
    public boolean validateCheckCode(String checkCodeKey, String inputCode) {
        if (isBlank(checkCodeKey)||  isBlank(inputCode)) {
            return false;
        }

        // 1) Look up expected answer in Redis
        String redisKey = Constants.REDIS_KEY_CHECK_CODE + ":" + checkCodeKey;
        String expected = redisUtils.get(redisKey);

        // 2) One-time use: delete after reading (prevents replay)
        redisUtils.delete(redisKey);

        if (expected == null){
            return false;
        }

        // 3) Compare (case-insensitive is fine for captcha)
        return expected.equalsIgnoreCase(inputCode.trim());
    }

    @Override
    public LoginResponseDTO login(LoginRequestDTO request) {
        boolean captchaOk = validateCheckCode(request.getCheckCodeKey(), request.getCheckCode());
        if (!captchaOk) {
            throw new BusinessException("Invalid or expired Captcha");
        }

        UserInfo user = userInfoService.getUserInfoByEmail(request.getEmail());
        if (user == null) {
            throw new BusinessException("Email or password incorrect");
        }

        // NOTE: replace with hashed password verification
        if (!safeEquals(user.getPassword(), request.getPassword())) {
            throw new BusinessException("Email or password incorrect");
        }

        // NOTE: replace with JWT/session token generation
        String token = UUID.randomUUID().toString();
        return new LoginResponseDTO(token);
    }

    @Override
    public void register(RegisterRequestDTO request) {

        boolean captchaOk = validateCheckCode(request.getCheckCodeKey(), request.getCheckCode());
        if (!captchaOk) {
            throw new BusinessException("Invalid or expired Captcha");
        }

        UserInfo userExist = userInfoService.getUserInfoByEmail(request.getEmail());

        if (userExist != null) {
            throw new BusinessException("Email already in use");
        }

        UserInfo user = new UserInfo();
        user.setEmail(request.getEmail());
        user.setNickName(request.getNickName());


        // NOTE: store hashed password
        user.setPassword(request.getPassword());
        userInfoService.add(user);

    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private boolean safeEquals(String a, String b) {
        return a != null && a.equals(b);
    }
}
