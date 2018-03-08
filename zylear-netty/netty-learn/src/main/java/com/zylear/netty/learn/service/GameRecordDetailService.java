package com.zylear.netty.learn.service;

import com.zylear.netty.learn.domain.GameRecordDetail;
import com.zylear.netty.learn.enums.RoomType;

/**
 * Created by xiezongyu on 2018/3/8.
 */
public interface GameRecordDetailService {

    void insert(GameRecordDetail gameRecordDetail);
}
