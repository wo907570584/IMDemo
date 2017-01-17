package ldu.guofeng.imdemo.base;

/**
 * 常量类
 *
 * @author 郭峰
 */
public class Constant {

    //服务器地址
    public static final String IM_HOST = "192.168.1.115";
    public static final int IM_PORT = 5222;
    public static final String IM_SERVER = "192.168.1.115";

    //消息分隔符
    public static final String SPLIT = "卍";
    //通知ID
    public static final int NOTIFY_ID = 0x90;

    //新消息广播
    public static final String ACTION_NEW_MSG = "happytalk.ldu.guofeng.new_message";
    //添加好友请求广播
    public static final String ACTION_ADD_FRIEND = "happytalk.ldu.guofeng.add_friend";
    //同意好友请求广播
    public static final String ACTION_AGREE_ADD = "happytalk.ldu.guofeng.agree_add";
    //定位成功广播
    public static final String ACTION_GET_LOCATION = "happytalk.ldu.guofeng.get_location";

    //消息类型
    public static final String MSG_TYPE_TEXT = "msg_type_text";//文本消息
    public static final String MSG_TYPE_LOCATION = "msg_type_location";//位置
    public static final String MSG_TYPE_ADD_FRIEND = "msg_type_add_friend";//添加好友
    public static final String MSG_TYPE_ADD_FRIEND_SUCCESS = "msg_type_add_friend_success";//同意添加好友

}
