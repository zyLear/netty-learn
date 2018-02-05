package com.zylear.netty.learn.bean;

import com.zylear.netty.learn.enums.ChooseColor;

/**
 * Created by xiezongyu on 2018/2/5.
 */
public class PlayerRoomInfo {

    private String account;
    private ChooseColor color;
    private Boolean isReady;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public ChooseColor getColor() {
        return color;
    }

    public void setColor(ChooseColor color) {
        this.color = color;
    }

    public Boolean getReady() {
        return isReady;
    }

    public void setReady(Boolean ready) {
        isReady = ready;
    }
}
