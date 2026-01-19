package com.easychat.entity.dto;

public class CheckCodeResponseDTO {
    private String checkCodeKey;
    private String checkCodeBase64;

    public CheckCodeResponseDTO() {}

    public CheckCodeResponseDTO(String checkCodeKey, String checkCodeBase64) {
        this.checkCodeKey = checkCodeKey;
        this.checkCodeBase64 = checkCodeBase64;
    }

    public String getCheckCodeKey() {
        return checkCodeKey;
    }

    public void setCheckCodeKey(String checkCodeKey) {
        this.checkCodeKey = checkCodeKey;
    }

    public String getCheckCodeBase64() {
        return checkCodeBase64;
    }

    public void setCheckCodeBase64(String checkCodeBase64) {
        this.checkCodeBase64 = checkCodeBase64;
    }
}
