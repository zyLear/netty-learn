package com.zylear.netty.learn.domain;

import java.util.Date;

public class PlayerGameLog {
    private Integer id;

    private String account;

    private Integer gameLogId;

    private Integer gameResult;

    private Integer stepsCount;

    private Integer changeScore;

    private Date createTime;

    private Date lastUpdateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Integer getGameLogId() {
        return gameLogId;
    }

    public void setGameLogId(Integer gameLogId) {
        this.gameLogId = gameLogId;
    }

    public Integer getGameResult() {
        return gameResult;
    }

    public void setGameResult(Integer gameResult) {
        this.gameResult = gameResult;
    }

    public Integer getStepsCount() {
        return stepsCount;
    }

    public void setStepsCount(Integer stepsCount) {
        this.stepsCount = stepsCount;
    }

    public Integer getChangeScore() {
        return changeScore;
    }

    public void setChangeScore(Integer changeScore) {
        this.changeScore = changeScore;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }
}