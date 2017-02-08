package ldu.guofeng.imdemo.bean;

import java.io.Serializable;

/**
 * 消息体
 */

public class MsgModel implements Serializable {

    private String fromUser;//发送者
    private String toUser;//接收者
    private int type;//信息类型
    private String content;//信息内容

    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
