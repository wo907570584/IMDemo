package ldu.guofeng.imdemo.im;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Stanza;

import ldu.guofeng.imdemo.base.Constant;
import ldu.guofeng.imdemo.base.IMApplication;
import ldu.guofeng.imdemo.bean.MsgModel;

/**
 * 异步通知packet的接口
 */

public class PacketListener implements StanzaListener {

    @Override
    public void processPacket(final Stanza packet) throws SmackException.NotConnectedException {
        Log.e("【PacketListener】", packet.toString());
        //表示接收到是消息包
        if (packet instanceof Message) {
            switch (((Message) packet).getType()) {
                case chat://表示收到单聊包，
                    String msgBody = ((Message) packet).getBody();
                    if (TextUtils.isEmpty(msgBody))
                        return;
                    // form卍to卍消息类型卍消息内容
                    String[] msgArr = msgBody.split(Constant.SPLIT);
                    MsgModel msg = new MsgModel();
                    msg.setFromUser(msgArr[0]);//发送者
                    msg.setToUser(msgArr[1]);//接收者
                    msg.setType(Integer.parseInt(msgArr[2]));//消息类型
                    msg.setContent(msgArr[3]);//文本内容

                    updateNofitication(msgArr[0], Integer.parseInt(msgArr[2]));
                    //发送一条消息
                    EventBus.getDefault().post(msg);
                    break;
                case groupchat://表示收到群聊包
                    break;
                case error://表示收到错误信息包
                    break;
                default:
                    break;
            }
        }

        //Presence继承自Stanza包，Presence主要有两个用途：
        //1.发出当前状态给服务器、所有客户端。
        //2.发出订阅、同意订阅、拒绝订阅、取消订阅等好友操作请求。
        //每个Presence信息包都有一个类型属性Presence.Type，如下，
        //除了下面类型信息外，Presence还包含其他一些可选的属性：
        //Status: 表示用户状态的自定义文本，例如：外出逛街
        //Priority: 表示发送者资源优先级的非负数
        //Mode: 大体和QQ五种状态相似，chat（Q我吧）、available(默认在线)、away(离开)、xa（忙碌）、dnd（请勿打扰）
        if (packet instanceof Presence) {//表示接收到的是Presence包
            switch (((Presence) packet).getType()) {
                case available://表示收到在线状态
                    break;
                case unavailable://表示收到离线状态
                    break;
                case subscribe://表示收到请求订阅
                    break;
                case subscribed://表示收到同意订阅
                    break;
                case unsubscribed://表示收到拒绝订阅
                    break;
                case unsubscribe://表示收到取消订阅
                    break;
                case error://表示收到错误消息
                    break;
                default:
                    break;
            }

        }

        //IQ也是继承自Stanza，是Info/Query的缩写，IQ是最基本的信息查询包.
        //它被用于从服务器获取信息，或将信息设置到服务器，包括认证、花名册的操作、创建用户等。
        //每个IQ包对象都包含一个类型属性，用于指定改信息包的操作类型，取值为：get、set、result、error。
        if (packet instanceof IQ) {//表示接收到的是IQ包
            switch (((IQ) packet).getType()) {
                case set://表示收到set
                    break;
                case get://表示收到get
                    break;
                case result://表示收到result
                    break;
                case error://表示收到错误
                    break;
                default:
                    break;
            }
        }
    }

    private void updateNofitication(String form, int type) {
        Intent intent_nftc = new Intent();
        intent_nftc.putExtra("form", form);
        intent_nftc.putExtra("type", type);
        intent_nftc.setAction("TYPE_NEW_MSG");
        IMApplication.getMyAppContext().sendBroadcast(intent_nftc);
    }

}
