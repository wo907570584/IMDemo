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
import org.jivesoftware.smack.sasl.SASLMechanism;
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
import ldu.guofeng.imdemo.util.PreferencesUtils;

/**
 * 封装 Smack 常用方法
 */
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
            XMPPTCPConnectionConfiguration builder = XMPPTCPConnectionConfiguration.builder()
                    .setHost(Constant.IM_HOST)//ip
                    .setPort(Constant.IM_PORT)//端口
                    .setServiceName(Constant.IM_SERVER)//此处填写openfire服务器名称
                    .setCompressionEnabled(false)//是否允许使用压缩
                    .setSendPresence(true)//是否发送Presece信息
                    .setDebuggerEnabled(true)//是否开启调试
                    .setResource("Android")//设置登陆设备标识
                    .setConnectTimeout(15 * 1000)//连接超时时间
                    .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)//设置TLS安全模式时使用的连接
                    .build();
            // 是否使用SASL
            SASLAuthentication.blacklistSASLMechanism(SASLMechanism.DIGESTMD5);
            IMApplication.connection = new XMPPTCPConnection(builder);
            try {
                IMApplication.connection.connect();
            } catch (SmackException | IOException | XMPPException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 检查连接
     */
    private void checkConnect() {
        if (IMApplication.connection == null) {//null
            getXMPPConnection();
        }
        if (!IMApplication.connection.isConnected()) {//没有连接到服务器
            try {
                IMApplication.connection.connect();
            } catch (SmackException | IOException | XMPPException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 断开连接
     */
    public void exitConnect() {
        if (IMApplication.connection != null && IMApplication.connection.isConnected()) {
            IMApplication.connection.disconnect();
            IMApplication.connection = null;
        }
    }


    /**
     * 检查登录
     */
    private void checkLogin() {
        if (!IMApplication.connection.isAuthenticated()) {//没有连接到服务器
            try {
                IMApplication.connection.login(
                        PreferencesUtils.getInstance().getString("username"),
                        PreferencesUtils.getInstance().getString("pwd")
                );
            } catch (SmackException | IOException | XMPPException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 注册
     *
     * @param username
     * @param password
     */
    public boolean register(String username, String password) {
        try {
            checkConnect();
            Map<String, String> map = new HashMap<String, String>();
            map.put("phone", "Android");
            AccountManager accountManager = AccountManager.getInstance(IMApplication.connection);
            //敏感操作跳过不安全的连接
            accountManager.sensitiveOperationOverInsecureConnection(true);
            accountManager.createAccount(username, password, map);
        } catch (SmackException | XMPPException e) {
            e.printStackTrace();
            return false;
        }
        return true;
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
            checkConnect();
            if (IMApplication.connection.isAuthenticated()) {//已经登录
                return true;
            } else {
                IMApplication.connection.login(username, password);//登录
                return IMApplication.connection.isAuthenticated();
            }
        } catch (IOException | SmackException | XMPPException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取好友列表
     *
     * @return
     */
    public List<Friend> getFriendsList() {
        checkConnect();
        checkLogin();
        List<Friend> list = new ArrayList<Friend>();
        //Roster对象翻译成中文为"花名册",表示用户的所有好友清单以及申请加好友的用户清单
        Roster roster = Roster.getInstanceFor(IMApplication.connection);
        Collection<RosterEntry> rosterEntries = roster.getEntries();
        for (RosterEntry rosterentry : rosterEntries) {
            Friend friend = new Friend();
            if (!rosterentry.getType().toString().equals("none")) {
                friend.setName(rosterentry.getUser().split("@")[0]);
                list.add(friend);
                Log.e("IMDemo", rosterentry.getUser());
            }

        }
        return list;
    }

    /**
     * 发送消息
     *
     * @param message
     * @param to
     */
    public void sendMessage(String message, String to) {
        try {
            checkConnect();
            checkLogin();
            ChatManager mChatManager = ChatManager.getInstanceFor(IMApplication.connection);
            Chat mChat = mChatManager.createChat(to + "@" + Constant.IM_HOST);
            mChat.sendMessage(message);
            mChat.close();
        } catch (SmackException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加好友
     *
     * @param userName
     */
    public void addFriend(String userName) {
        try {
            checkConnect();
            checkLogin();
            Roster roster = Roster.getInstanceFor(IMApplication.connection);
            roster.createEntry(userName, userName, null);
        } catch (SmackException | XMPPException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除好友
     *
     * @param userJID
     * @return
     */
    public void deleteFriend(String userJID) {
        try {
            checkConnect();
            checkLogin();
            Roster roster = Roster.getInstanceFor(IMApplication.connection);
            roster.removeEntry(roster.getEntry(userJID));
        } catch (SmackException | XMPPException e) {
            e.printStackTrace();
        }
    }
}
