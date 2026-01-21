package com.easychat.service;

import com.easychat.entity.dto.*;


public interface AccountService {

    CheckCodeResponseDTO createCheckCode();

    /**
     * Validates a user-submitted captcha against Redis.
     * Typically, deletes the captcha key after validation (one-time use).
     */
    boolean validateCheckCode(String checkCodeKey, String inputCode);

    TokenUserInfoDto login(LoginRequestDTO request);
    void register(RegisterRequestDTO request);
}
