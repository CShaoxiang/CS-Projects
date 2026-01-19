package com.easychat.service;

import com.easychat.entity.dto.CheckCodeResponseDTO;
import com.easychat.entity.dto.LoginRequestDTO;
import com.easychat.entity.dto.LoginResponseDTO;
import com.easychat.entity.dto.RegisterRequestDTO;


public interface AccountService {

    CheckCodeResponseDTO createCheckCode();

    /**
     * Validates a user-submitted captcha against Redis.
     * Typically, deletes the captcha key after validation (one-time use).
     */
    boolean validateCheckCode(String checkCodeKey, String inputCode);

    LoginResponseDTO login(LoginRequestDTO request);
    void register(RegisterRequestDTO request);
}
