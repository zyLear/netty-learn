package com.zylear.netty.learn.util;

import com.zylear.netty.learn.bean.MessageBean;
import com.zylear.netty.learn.bean.PlayerRoomInfo;
import com.zylear.netty.learn.constant.OperationCode;
import com.zylear.netty.learn.constant.StatusCode;
import com.zylear.netty.learn.enums.ChooseColor;
import com.zylear.proto.BlokusOuterClass.*;

import java.util.Map;
import java.util.Map.Entry;

/**
 * Created by xiezongyu on 2018/2/6.
 */
public class MessageFormater {

    public static MessageBean formatPlayerRoomInfoMessage(Map<String, PlayerRoomInfo> playerRoomInfoMap) {
        MessageBean message = new MessageBean();
        message.setOperationCode(OperationCode.UPDATE_ROOM_PLAYERS_INFO);
        message.setStatusCode(StatusCode.SUCCESS);
        BLOKUSRoomPlayerList.Builder builder = BLOKUSRoomPlayerList.newBuilder();
        for (Entry<String, PlayerRoomInfo> entry : playerRoomInfoMap.entrySet()) {
            PlayerRoomInfo playerRoomInfo = entry.getValue();
            BLOKUSRoomPlayerInfo.Builder itemBuilder = BLOKUSRoomPlayerInfo.newBuilder();
            itemBuilder.setAccount(playerRoomInfo.getAccount());
            itemBuilder.setColor(playerRoomInfo.getColor().getValue());
            itemBuilder.setIsReady(playerRoomInfo.getReady());
            builder.addItmes(itemBuilder);
        }
        message.setData(builder.build().toByteArray());
        return message;
    }

    public static MessageBean formatGiveUpMessage(ChooseColor color) {
        MessageBean message = new MessageBean();
        message.setOperationCode(OperationCode.GIVE_UP);
        message.setStatusCode(StatusCode.SUCCESS);

        BLOKUSChooseColor.Builder builder = BLOKUSChooseColor.newBuilder();
        builder.setColor(color.getValue());

        message.setData(builder.build().toByteArray());
        return message;
    }
}
