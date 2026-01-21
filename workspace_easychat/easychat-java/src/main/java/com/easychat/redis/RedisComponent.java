package com.easychat.redis;


import com.easychat.entity.constants.Constants;
import com.easychat.entity.dto.TokenUserInfoDto;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class RedisComponent {

    @Resource
    private RedisUtils redisUtils;


    public void saveTokenUserInfoDto(TokenUserInfoDto tokenUserInfoDto) {
        redisUtils.setex(Constants.REDIS_KEY_WS_TOKEN+ tokenUserInfoDto.getToken(),tokenUserInfoDto,Constants.REDIS_TIME_1DAY * 2);
        redisUtils.setex(Constants.REDIS_KEY_WS_TOKEN_USERID + tokenUserInfoDto.getUserId(),tokenUserInfoDto.getToken(),Constants.REDIS_TIME_1DAY * 2);
    }



    // get User HeartBeat
    public Long getUserHeartBeat(String userId){
        return (Long) redisUtils.get(Constants.REDIS_KEY_WS_USER_HEART_BEAT + userId);
    }

    //Save User Websocket Heartbeat
    public void saveUserHeartBeat(String userId) {
        redisUtils.setex(Constants.REDIS_KEY_WS_USER_HEART_BEAT + userId, System.currentTimeMillis(), Constants.REDIS_KEY_EXPIRES_HEART_BEAT);
    }

    // Delete User Heartbeat
    public void removeUserHeartBeat(String userId) {
        redisUtils.delete(Constants.REDIS_KEY_WS_USER_HEART_BEAT + userId);
    }
}
