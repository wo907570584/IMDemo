package ldu.guofeng.imdemo.bean;

import java.io.Serializable;

/**
 * 会话体
 */

public class SessionModel implements Serializable {
    private String form;//发送者
    private int type;//信息类型
    private String content;//信息内容

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
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
