package ldu.guofeng.imdemo.bean;

import java.io.Serializable;


public class ItemModel implements Serializable {

    public static final int CHAT_LEFT = 1001;//左
    public static final int CHAT_RIGHT = 1002;//右
    public int leftOrRight;//左还是右
    public ChatModel chatModel;

    public ItemModel(int leftOrRight, ChatModel chatModel) {
        this.leftOrRight = leftOrRight;
        this.chatModel = chatModel;
    }
}
