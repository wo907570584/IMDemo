package ldu.guofeng.imdemo.im;

import android.text.TextUtils;

import org.greenrobot.eventbus.EventBus;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;

import ldu.guofeng.imdemo.base.Constant;
import ldu.guofeng.imdemo.bean.MsgModel;


public class ReceiveMsgListener implements ChatMessageListener {

    @Override
    public void processMessage(Chat chat, Message message) {
        String msgBody = message.getBody();
        if (TextUtils.isEmpty(msgBody))
            return;

        // form卍to卍消息类型卍消息内容
        String[] msgArr = msgBody.split(Constant.SPLIT);
        String form = msgArr[0];//发送者
        String to = msgArr[1];//接收者
        String msgtype = msgArr[2];//消息类型
        String msgcontent = msgArr[3];//消息内容

        MsgModel msg = new MsgModel();
        msg.setFromUser(form);
        msg.setToUser(to);
        msg.setType(msgtype);
        msg.setContent(msgcontent);

        switch (msgtype) {
            case Constant.MSG_TYPE_TEXT:
                EventBus.getDefault().post(msg);
                break;
            case Constant.MSG_TYPE_LOC:
                break;
            case Constant.MSG_TYPE_IMG:
                break;
        }
    }


}
