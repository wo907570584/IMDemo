package ldu.guofeng.imdemo.im;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.iqregister.AccountManager;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import ldu.guofeng.imdemo.base.Constant;
import ldu.guofeng.imdemo.base.IMApplication;
import ldu.guofeng.imdemo.util.PreferencesUtils;

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
            builder.setHost(Constant.IM_HOST);
            builder.setPort(Constant.IM_PORT);
            builder.setServiceName(Constant.IM_SERVER);//此处指代PC机器名称
            builder.setCompressionEnabled(false);
            builder.setDebuggerEnabled(true);
            builder.setSendPresence(true);
            //SASL
            SASLAuthentication.blacklistSASLMechanism("DIGEST-MD5");
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
                PreferencesUtils.getInstance().putString("username", username);
                PreferencesUtils.getInstance().putString("pwd", password);
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
            map.put("mail", "122878586@qq.com");
            AccountManager accountManager = AccountManager.getInstance(IMApplication.connection);
            accountManager.sensitiveOperationOverInsecureConnection(true);
            accountManager.createAccount(username, password, map);
        } catch (SmackException | IOException | XMPPException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean reg(String username, String password) throws SmackException.NotConnectedException {

        return false;
    }

    public void sendMessage(String from, String to, String content) {

    }

    public void addRosterItem(String user, String name, String[] groups) {

    }

    public void removeRoster(String user) {

    }

    public void exitConnect() {
        if (IMApplication.connection != null) {
            IMApplication.connection.disconnect();
            IMApplication.connection = null;
        }
    }

}
