package com.easychat.aspect;

import com.easychat.annotation.GlobalInterceptor;
import com.easychat.entity.constants.Constants;
import com.easychat.entity.dto.TokenUserInfoDto;
import com.easychat.entity.enums.ResponseCodeEnum;
import com.easychat.exception.BusinessException;
import com.easychat.redis.RedisUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;


@Component("globalOperationAspect")
@Aspect
public class GlobalOperationAspect {

    @Resource
    private RedisUtils redisUtils;

    private static final Logger logger =  LoggerFactory.getLogger(GlobalOperationAspect.class);


    @Before("@annotation(com.easychat.annotation.GlobalInterceptor)")
    public void interceptorDo(JoinPoint joinPoint) {
        try {
            Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
            GlobalInterceptor interceptor = method.getAnnotation(GlobalInterceptor.class);
            if (interceptor == null) {
                return;
            }

            // check login or login as admin
            if (interceptor.checklogin() || interceptor.checkAdmin()) {
                checkLogin(interceptor.checkAdmin());
            }
        } catch (BusinessException e) {
            logger.error("Global Interceptor Exception", e);
            throw e;
        } catch (Throwable e) {
            logger.error("Global Interceptor Exception", e);
            throw new BusinessException(ResponseCodeEnum.CODE_500);
        }
    }

    // verify login
    private void checkLogin(Boolean checkAdmin){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader("token");

        TokenUserInfoDto tokenUserInfoDto = (TokenUserInfoDto) redisUtils.get(Constants.REDIS_KEY_WS_TOKEN + token);
        if (tokenUserInfoDto == null) {
            throw new BusinessException(ResponseCodeEnum.CODE_901);
        }
        if (checkAdmin && !tokenUserInfoDto.getAdmin()) {
            throw new BusinessException(ResponseCodeEnum.CODE_404);
        }
    }
}
