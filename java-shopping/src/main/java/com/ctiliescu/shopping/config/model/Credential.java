package com.ctiliescu.shopping.config.model;

public class Credential {
    private String jwdId;
    private int userId;

    public Credential(String jwdId, int userId) {
        this.jwdId = jwdId;
        this.userId = userId;
    }

    public String getJwdId() {
        return jwdId;
    }

    public void setJwdId(String jwdId) {
        this.jwdId = jwdId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
