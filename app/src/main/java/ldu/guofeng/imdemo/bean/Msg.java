package ldu.guofeng.imdemo.bean;

import java.io.Serializable;

/**
 * Created by GUOFENG on 2017/1/24.
 */

public class Msg implements Serializable {
    private String fromUser;//发送者
    private String toUser;//接收者
    private String type;//信息类型
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
