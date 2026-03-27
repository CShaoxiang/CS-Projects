package com.easychat;


import com.easychat.entity.constants.Constants;
import com.easychat.entity.dto.LoginRequestDTO;
import com.easychat.entity.po.UserInfo;
import com.easychat.entity.vo.UserInfoVO;
import com.easychat.redis.RedisComponent;
import com.easychat.redis.RedisUtils;
import com.easychat.service.impl.AccountServiceImpl;
import com.easychat.service.impl.UserInfoServiceImpl;
import com.easychat.utils.StringTools;
import org.junit.jupiter.api.Test;
import com.easychat.entity.config.AppConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class AccountServiceImplTest {

    @Autowired
    private AccountServiceImpl service;

    @MockBean private RedisComponent redisComponent;
    @MockBean private RedisUtils<String> redisUtils;
    @MockBean private UserInfoServiceImpl userInfoService;
    @MockBean private AppConfig appConfig;

    @Test
    void login_success_returnsVO_and_savesTokenToRedis() {
        // Arrange
        when(appConfig.getAdminEmails()).thenReturn("admin@test.com");

        // captcha: validateCheckCode() will do redisUtils.get(key) then delete(key)
        when(redisUtils.get(Constants.REDIS_KEY_CHECK_CODE + ":key")).thenReturn("123");

        UserInfo userInfo = new UserInfo();
        userInfo.setUserId("U1001");
        userInfo.setEmail("a@test.com");
        userInfo.setUserName("Simon");
        userInfo.setPassword(StringTools.encodeByMD5("pw"));
        userInfo.setStatus(1);

        when(userInfoService.getUserInfoByEmail("a@test.com")).thenReturn(userInfo);
        when(redisComponent.getUserHeartBeat("U1001")).thenReturn(null);

        LoginRequestDTO req = new LoginRequestDTO();
        req.setEmail("a@test.com");
        req.setPassword("pw");
        req.setCheckCodeKey("key");
        req.setCheckCode("123");

        // Act
        UserInfoVO vo = service.login(req);

        // Assert
        assertNotNull(vo);
        assertEquals("U1001", vo.getUserId());
        assertEquals("Simon", vo.getUserName());
        assertNotNull(vo.getToken());

        verify(redisComponent).saveTokenUserInfoDto(any());
        verify(redisUtils).delete(Constants.REDIS_KEY_CHECK_CODE + ":key"); // one-time captcha
    }
}

