package com.easychat.entity.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * Registration request data transfer object.
 * Carries user registration input from client to service layer.
 */
public class RegisterRequestDTO implements Serializable {

    @NotBlank(message = "Captcha key must not be blank")
    private String checkCodeKey;

    @NotBlank(message = "Captcha code must not be blank")
    private String checkCode;

    @NotBlank(message = "Email must not be blank")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Nickname must not be blank")
    private String nickName;

    @NotBlank(message = "Password must not be blank")
    private String password;

    // getters & setters
    public String getCheckCodeKey() { return checkCodeKey; }
    public void setCheckCodeKey(String checkCodeKey) { this.checkCodeKey = checkCodeKey; }

    public String getCheckCode() { return checkCode; }
    public void setCheckCode(String checkCode) { this.checkCode = checkCode; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getNickName() { return nickName; }
    public void setNickName(String nickName) { this.nickName = nickName; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}

