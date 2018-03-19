package com.zylear.netty.learn.constant;

/**
 * @author 28444
 * @date 2018/2/4.
 */
public class OperationCode {

    public static final short CHECK_VERSION = 1;

    public static final short LOGIN = 2;

    public static final short CREATE_ROOM = 3;

    public static final short JOIN_ROOM = 4;

    public static final short LEAVE_ROOM = 5;

    public static final short READY = 6;

    public static final short CHESS_DONE = 7;

    public static final short UPDATE_ROOM_PLAYERS_INFO = 8;

    public static final short CHOOSE_COLOR = 9;

    public static final short START_BLOKUS = 10;

    public static final short START_BLOKUS_TWO_PEOPLE = 11;

    public static final short WIN = 12;

    public static final short LOSE = 13;

    public static final short GIVE_UP = 14;

    public static final short CHAT_IN_GAME = 15;

    public static final short ROOM_LIST = 16;

    public static final short REGISTER = 17;

    public static final short RANK_INFO = 18;

    public static final short PROFILE = 19;

    public static final short CHAT_IN_ROOM = 20;

    public static final short LEAVE_ROOM_FROM_GAME = 21;

    public static final short INIT_PLAYER_INFO_IN_GAME = 22;

    public static final short LOGOUT = 23;



    public static final short HEARTBEAT = 10000;

    public static final short QUIT = 10001;


}
