package com.easychat.service.impl;


import com.easychat.entity.constants.Constants;
import com.easychat.redis.RedisUtils;
import com.easychat.service.AccountService;
import com.wf.captcha.ArithmeticCaptcha;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service("accountService")
public class AccountServiceImpl implements AccountService {

    @Resource
    private RedisUtils redisUtils;

    @Override
    public Map<String, String> createCheckCode(){

        // 1) Generate captcha
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(100,43);

        // 2) Extract expected answer
        String code = captcha.text();

        // 3) Generate a unique key for this captcha session
        String checkCodeKey = UUID.randomUUID().toString();

        // 4) Store expected answer in Redis with TTL
        String redisKey = Constants.REDIS_KEY_CHECK_CODE + ":" + checkCodeKey;
        redisUtils.setex(redisKey, code, Constants.REDIS_TIME_1MIN * 6);

        // 5) Return Base64 image + key to a client
        Map<String,String> result =new HashMap<>();
        result.put("checkCode", captcha.toBase64());
        result.put("checkCodeKey", checkCodeKey);
        return result;
    }

    @Override
    public boolean validateCheckCode(String checkCodeKey, String inputCode) {
        if (checkCodeKey == null || inputCode == null){
            return false;
        }

        // 1) Look up expected answer in Redis
        String redisKey = Constants.REDIS_KEY_CHECK_CODE + ":" + checkCodeKey;
        String expected = redisUtils.get(redisKey).toString();

        if (expected == null){
            return false;
        }
        // 2) One-time use: delete after reading (prevents replay)
        redisUtils.delete(redisKey);

        // 3) Compare (case-insensitive is fine for captcha)
        return expected.equalsIgnoreCase(inputCode.trim());

    }
}
