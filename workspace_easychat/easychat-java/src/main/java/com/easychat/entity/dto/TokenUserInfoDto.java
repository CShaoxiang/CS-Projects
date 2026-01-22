package com.easychat.entity.dto;

import java.io.Serializable;

public class TokenUserInfoDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String token;
    private String userId;
    private  String  userName;
    private  Boolean  admin;

    public String getUserName() {return userName;}

    public void setUserName(String userName) {this.userName = userName;}

    public String getToken() {return token;}

    public void setToken(String token) {this.token = token;}

    public String getUserId() {return userId;}

    public void setUserId(String userId) {this.userId = userId;}

    public Boolean getAdmin() {return admin;}

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }
}
