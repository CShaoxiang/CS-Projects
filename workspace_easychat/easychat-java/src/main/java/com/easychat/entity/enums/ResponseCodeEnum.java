package com.easychat.entity.enums;


public enum ResponseCodeEnum {
    CODE_200(200, "Request succeeded"),
    CODE_404(404, "Requested resource not found"),
    CODE_600(600, "Invalid request parameters"),
    CODE_601(601, "Resource already exists"),
    CODE_500(500, "Internal server error. Please contact the administrator");

    private Integer code;

    private String msg;

    ResponseCodeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
