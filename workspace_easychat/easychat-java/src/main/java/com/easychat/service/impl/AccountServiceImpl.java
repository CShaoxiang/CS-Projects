package com.easychat.service.impl;

import com.easychat.entity.config.AppConfig;
import com.easychat.entity.constants.Constants;
import com.easychat.entity.dto.*;
import com.easychat.entity.enums.UserStatusEnum;
import com.easychat.entity.po.UserInfo;
import com.easychat.entity.query.UserInfoQuery;
import com.easychat.entity.vo.UserInfoVO;
import com.easychat.exception.BusinessException;
import com.easychat.mappers.UserInfoMapper;
import com.easychat.redis.RedisComponent;
import com.easychat.redis.RedisUtils;
import com.easychat.service.AccountService;
import com.easychat.utils.StringTools;
import com.wf.captcha.ArithmeticCaptcha;
import org.apache.commons.lang3.ArrayUtils;
import cn.hutool.core.bean.BeanUtil;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.annotation.Resource;
import java.util.Date;
import java.util.UUID;

@Service("accountService")
public class AccountServiceImpl implements AccountService {

    private static final int CAPTCHA_WIDTH = 100;
    private static final int CAPTCHA_HEIGHT = 42;
    private static final long CAPTCHA_TTL_SECONDS = Constants.REDIS_TIME_1MIN * 6;
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(AccountServiceImpl.class);

    @Resource
    private  RedisUtils<String> redisUtils;

    @Resource
    private  RedisComponent redisComponent;

    @Resource
    private  UserInfoServiceImpl userInfoService;

    @Resource
    private UserInfoMapper<UserInfo, UserInfoQuery> userInfoMapper;

    @Resource
    private AppConfig appConfig;

    @Override
    public CheckCodeResponseDTO createCheckCode() {
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
        if (StringTools.isEmpty(checkCodeKey) || StringTools.isEmpty(inputCode)) {
            return false;
        }

        // 1) Look up expected answer in Redis
        String redisKey = Constants.REDIS_KEY_CHECK_CODE + ":" + checkCodeKey;
        String expected = redisUtils.get(redisKey);

        // 2) One-time use: delete after reading (prevents replay)
        redisUtils.delete(redisKey);

        if (expected == null) {
            return false;
        }

        // 3) Compare (case-insensitive is fine for captcha)
        return expected.equalsIgnoreCase(inputCode.trim());
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void register(RegisterRequestDTO request) {

        boolean captchaOk = validateCheckCode(request.getCheckCodeKey(), request.getCheckCode());
        if (!captchaOk) {
            throw new BusinessException("Invalid or expired Captcha");
        }

        UserInfo userInfo = this.userInfoMapper.selectByEmail(request.getEmail());

        if (null != userInfo) {
            throw new BusinessException("User already exist");

        }
        String userId = StringTools.getUserId();

        Date currentDate = new Date();
        userInfo = new UserInfo();

        userInfo.setUserId(userId);


        userInfo.setEmail(request.getEmail());
        userInfo.setPassword(StringTools.encodeByMD5(request.getPassword()));
        userInfo.setUserName(request.getUserName());
        userInfo.setCreateTime(currentDate);
        userInfo.setLastOffTime(currentDate.getTime());
        userInfo.setStatus(UserStatusEnum.ENABLE.getStatus());
        this.userInfoMapper.insert(userInfo);

           //TODO Add chatbot

    }

    @Override
    public UserInfoVO login(LoginRequestDTO request) {

        UserInfo userInfo = userInfoService.getUserInfoByEmail(request.getEmail());
        // NOTE: replace with hashed password verification
        String inputPw = StringTools.encodeByMD5(request.getPassword());

        if (!validateCheckCode(request.getCheckCodeKey(), request.getCheckCode())) {
            throw new BusinessException("Invalid or expired Captcha");
        }
        if (null == userInfo || !StringTools.safeEquals(inputPw,userInfo.getPassword())) {
            throw new BusinessException("Email or password incorrect");
        }

        if (UserStatusEnum.DISABLE.getStatus().equals(userInfo.getStatus())) {
            throw new BusinessException("Account disabled");
        }

        //TODO SEARCH GROUP
        //TODO SEARCH CONTACT

        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfo(userInfo);

        Long lastHeartBeat = redisComponent.getUserHeartBeat(userInfo.getUserId());
        if (lastHeartBeat != null) {
            throw new BusinessException("The account has login");
        }

        // session token generation to redis
        String token = StringTools.encodeByMD5(tokenUserInfoDto.getUserId() + StringTools.getRandomString(Constants.LENGTH_20));
        tokenUserInfoDto.setToken(token);
        redisComponent.saveTokenUserInfoDto(tokenUserInfoDto);

        UserInfoVO userInfoVo = BeanUtil.copyProperties(userInfo, UserInfoVO.class);
        userInfoVo.setToken(tokenUserInfoDto.getToken());
        userInfoVo.setAdmin(tokenUserInfoDto.getAdmin());

        return userInfoVo;
    }

    private TokenUserInfoDto getTokenUserInfo( UserInfo userInfo) {
        TokenUserInfoDto tokenUserInfoDto = new TokenUserInfoDto();
        tokenUserInfoDto.setUserId(userInfo.getUserId());
        tokenUserInfoDto.setUserName(userInfo.getUserName());

        String adminEmails = appConfig.getAdminEmails();
        if (!StringTools.isEmpty(adminEmails) && ArrayUtils.contains(adminEmails.split(","), userInfo.getEmail())) {
            tokenUserInfoDto.setAdmin(true);
        } else {
            tokenUserInfoDto.setAdmin(false);
        }
        return tokenUserInfoDto;
    }

}
