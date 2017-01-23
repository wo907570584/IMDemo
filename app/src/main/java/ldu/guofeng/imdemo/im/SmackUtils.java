package ldu.guofeng.imdemo.im;

import android.util.Log;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.iqregister.AccountManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ldu.guofeng.imdemo.base.Constant;
import ldu.guofeng.imdemo.base.IMApplication;
import ldu.guofeng.imdemo.bean.Friend;

public class SmackUtils {

    private static SmackUtils smackUtils;

    public static SmackUtils getInstance() {
        if (smackUtils == null) {
            smackUtils = new SmackUtils();
        }
        return smackUtils;
    }

    /**
     * 建立连接
     */
    public void getXMPPConnection() {
        if (IMApplication.connection == null || !IMApplication.connection.isConnected()) {
            XMPPTCPConnectionConfiguration.Builder builder = XMPPTCPConnectionConfiguration.builder();
            builder.setHost(Constant.IM_HOST);//ip
            builder.setPort(Constant.IM_PORT);//端口
            builder.setServiceName(Constant.IM_SERVER);//此处填写openfire服务器名称
            builder.setCompressionEnabled(false);//是否允许使用压缩
            builder.setDebuggerEnabled(true);//是否开启调试
            builder.setSendPresence(true);//是否发送Presece信息
            // 是否使用SASL
            SASLAuthentication.blacklistSASLMechanism("DIGEST-MD5");
            // 设置TLS安全模式时使用的连接
            builder.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
            IMApplication.connection = new XMPPTCPConnection(builder.build());
        }
    }

    /**
     * 登录
     *
     * @param username
     * @param password
     * @return
     */

    public boolean login(String username, String password) {
        try {
            if (IMApplication.connection.isConnected()) {
                IMApplication.connection.disconnect();
            }
            IMApplication.connection.connect();
            IMApplication.connection.login(username, password);
            if (IMApplication.connection.isAuthenticated()) {
                return true;
            }
        } catch (IOException | SmackException | XMPPException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 注册
     *
     * @param username
     * @param password
     */
    public boolean register(String username, String password) {
        try {
            if (IMApplication.connection.isConnected()) {
                IMApplication.connection.disconnect();
            }
            IMApplication.connection.connect();
            Map<String, String> map = new HashMap<String, String>();
            map.put("username", username);
            map.put("password", password);
            map.put("phone", "Android");
            AccountManager accountManager = AccountManager.getInstance(IMApplication.connection);
            accountManager.sensitiveOperationOverInsecureConnection(true);
            accountManager.createAccount(username, password, map);
        } catch (SmackException | IOException | XMPPException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 获取好友列表
     *
     * @return
     */
    public List<Friend> getFriendsList() {
        List<Friend> list = new ArrayList<Friend>();
        try {
            if (IMApplication.connection.isConnected()) {
                IMApplication.connection.disconnect();
            }
            IMApplication.connection.connect();
            //Roster对象翻译成中文为"花名册",表示用户的所有好友清单以及申请加好友的用户清单
            Roster roster = Roster.getInstanceFor(IMApplication.connection);
            Collection<RosterEntry> rosterEntries = roster.getEntries();
            for (RosterEntry rosterentry : rosterEntries) {
                Friend friend = new Friend();
                friend.setName(rosterentry.getUser().split("@")[0]);
                Log.e("IMDemo", rosterentry.getUser());
                list.add(friend);
            }
        } catch (SmackException | IOException | XMPPException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 删除好友
     *
     * @param userJID
     * @return
     */
    public int deleteFriend(String userJID) {
        try {
            if (IMApplication.connection.isConnected()) {
                IMApplication.connection.disconnect();
            }
            IMApplication.connection.connect();
            Roster roster = Roster.getInstanceFor(IMApplication.connection);
            roster.removeEntry(roster.getEntry(userJID));
        } catch (SmackException | XMPPException | IOException e) {
            e.printStackTrace();
        }
        return 1;
    }

    /**
     * 断开连接
     */
    public void exitConnect() {
        if (IMApplication.connection != null) {
            IMApplication.connection.disconnect();
            IMApplication.connection = null;
        }
    }


    public void sendMessage(String message, String to) {
        try {

            if (IMApplication.connection.isConnected()) {
                IMApplication.connection.disconnect();
            }
            IMApplication.connection.connect();
            ChatManager mChatManager = ChatManager.getInstanceFor(IMApplication.connection);
            Chat mChat = mChatManager.createChat(to + "@" + Constant.IM_HOST);
            mChat.sendMessage(message);
        } catch (SmackException | IOException | XMPPException e) {
            e.printStackTrace();
        }
    }
}
