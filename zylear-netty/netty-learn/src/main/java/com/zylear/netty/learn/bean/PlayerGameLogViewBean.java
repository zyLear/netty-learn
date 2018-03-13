package com.zylear.netty.learn.bean;

import com.zylear.netty.learn.enums.GameResult;
import com.zylear.netty.learn.enums.GameType;

import java.util.Date;

/**
 * Created by xiezongyu on 2018/3/10.
 */
public class PlayerGameLogViewBean {

    private GameResult gameResult;
    private GameType gameType;
    private Integer stepsCount;
    private String detail;
    private Date time;
    private Integer changeScore;

    public GameResult getGameResult() {
        return gameResult;
    }

    public void setGameResult(GameResult gameResult) {
        this.gameResult = gameResult;
    }

    public GameType getGameType() {
        return gameType;
    }

    public void setGameType(GameType gameType) {
        this.gameType = gameType;
    }

    public Integer getStepsCount() {
        return stepsCount;
    }

    public void setStepsCount(Integer stepsCount) {
        this.stepsCount = stepsCount;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Integer getChangeScore() {
        return changeScore;
    }

    public void setChangeScore(Integer changeScore) {
        this.changeScore = changeScore;
    }
}
