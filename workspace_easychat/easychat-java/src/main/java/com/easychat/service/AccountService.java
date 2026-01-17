package com.easychat.service;

import java.util.Map;

public interface AccountService {

    /**
     * Generates a captcha image and stores the expected answer in Redis with TTL.
     * Returns Base64 image data and a unique checkCodeKey.
     */
    Map<String,String> createCheckCode();

    /**
     * Validates a user-submitted captcha against Redis.
     * Typically, deletes the captcha key after validation (one-time use).
     */
    boolean validateCheckCode(String checkCodeKey, String inputCode);
}
