package ldu.guofeng.imdemo.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import ldu.guofeng.imdemo.R;
import ldu.guofeng.imdemo.adapter.ChatAdapter;
import ldu.guofeng.imdemo.base.Constant;
import ldu.guofeng.imdemo.bean.ItemModel;
import ldu.guofeng.imdemo.bean.MsgModel;
import ldu.guofeng.imdemo.bean.SessionModel;
import ldu.guofeng.imdemo.im.SmackUtils;
import ldu.guofeng.imdemo.util.PreferencesUtils;
import ldu.guofeng.imdemo.view.CustomReturnToolbar;

/**
 * 聊天
 */
public class ChatActivity extends CustomReturnToolbar implements View.OnClickListener {

    private Context mContext;
    private RecyclerView recyclerView;
    private ChatAdapter adapter;

    private String txtContent;
    private String form, to;

    private EditText et_message;//文本输入框
    private TextView tv_send;//发送标签
    private ImageView chat_more;//用于切换键盘与功能面板
    private LinearLayout chat_more_container;//功能面板布局
    private TextView send_loc;//发送位置标签

    /**
     * 订阅接收消息
     * Subscribe，其含义为订阅者。
     * 在其内传入了threadMode，我们定义为ThreadMode.MAIN，其含义是该方法在UI线程完成。
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void recMsgEventBus(MsgModel msg) {
        if (msg.getFromUser().equals(to)) {
            if (msg.getType() == 1) {
                //在最后插入一条item，包括布局，聊天信息
                adapter.insertLastItem(new ItemModel(ItemModel.LEFT_TEXT, msg));
            } else if (msg.getType() == 2) {
                adapter.insertLastItem(new ItemModel(ItemModel.LEFT_LOCATION, msg));
            }

            //滑动到最后
            recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
        }
    }

    /**
     * 发送文本消息
     */
    private void sendTextMessage() {
        //发送文本消息
        if (txtContent.equals("")) {
            return;
        }

        final String message = form + Constant.SPLIT + to + Constant.SPLIT
                + Constant.MSG_TYPE_TEXT + Constant.SPLIT
                + txtContent;
        new Thread(new Runnable() {
            @Override
            public void run() {
                SmackUtils.getInstance().sendMessage(message, to);
            }
        }).start();


        //在聊天列表插入一条文本消息
        MsgModel msgModel = new MsgModel();
        msgModel.setToUser(to);
        msgModel.setType(Constant.MSG_TYPE_TEXT);
        msgModel.setContent(txtContent);
        adapter.insertLastItem(new ItemModel(ItemModel.RIGHT_TEXT, msgModel));
        insertSession(msgModel);

        //滑动到最后,清空输入框
        recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
        et_message.setText("");
    }

    /**
     * 在消息列表插入一条位置消息
     *
     * @param locInfo
     */
    private void insertLocMessage(String locInfo) {
        //在聊天列表插入一条位置消息
        MsgModel msgModel = new MsgModel();
        msgModel.setToUser(to);
        msgModel.setType(Constant.MSG_TYPE_LOC);
        msgModel.setContent(locInfo);
        adapter.insertLastItem(new ItemModel(ItemModel.RIGHT_LOCTION, msgModel));
        insertSession(msgModel);
    }

    /**
     * 插入会话列表一条会话
     *
     * @param msg
     */
    public void insertSession(MsgModel msg) {
        SessionModel sessionModel = new SessionModel();
        sessionModel.setType(msg.getType());
        sessionModel.setForm(msg.getToUser());
        sessionModel.setContent(msg.getContent());
        EventBus.getDefault().post(sessionModel);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.et_message:
                hidePanelView();//隐藏面板
                break;
            case R.id.tv_send:
                hidePanelView();//隐藏面板
                sendTextMessage();//发送文本消息
                break;
            case R.id.chat_more:
                hideSoftInputView();//隐藏软键盘
                hidePanelHandler.postDelayed(hidePanelTask, 200);//显示面板
                break;
            case R.id.tv_loc:
                sendLocMessage();//发送位置
                break;
            default:
                break;
        }
    }

    /**
     * 发送位置消息
     */
    private void sendLocMessage() {
        Intent intent_loc = new Intent(this, ShareLocActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("to_user", to);
        intent_loc.putExtras(bundle);
        startActivityForResult(intent_loc, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100) {
            if (resultCode == 200) {
                String locInfo = data.getStringExtra("my_location");
                insertLocMessage(locInfo);
            }
        }
    }

    //------------------------------------------------------
    @Override
    protected int provideContentViewId() {
        return R.layout.activity_chat;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!to.equals("") || to != null)
            getToolbar().setTitle(to);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        findView();
        init();
        initEditText();
    }

    private void findView() {
        recyclerView = (RecyclerView) findViewById(R.id.recylerView);
        et_message = (EditText) findViewById(R.id.et_message);
        tv_send = (TextView) findViewById(R.id.tv_send);
        chat_more = (ImageView) findViewById(R.id.chat_more);
        chat_more_container = (LinearLayout) findViewById(R.id.chat_more_container);
        send_loc = (TextView) findViewById(R.id.tv_loc);
    }

    private void init() {
        //注册EventBus
        EventBus.getDefault().register(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter = new ChatAdapter(mContext));
        et_message.setOnClickListener(this);
        tv_send.setOnClickListener(this);
        chat_more.setOnClickListener(this);
        send_loc.setOnClickListener(this);
        form = PreferencesUtils.getInstance().getString("username");
        //接收数据
        Bundle bundle = this.getIntent().getExtras();
        to = bundle.getString("to_user");
    }

    private void initEditText() {
        et_message.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //去掉空格的消息
                txtContent = s.toString().trim();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //解除注册
        EventBus.getDefault().unregister(this);
    }

    //------------------------------------------------

    Handler hidePanelHandler = new Handler();
    Runnable hidePanelTask = new Runnable() {
        @Override
        public void run() {
            showPanelView();//隐藏面板
        }
    };

    /**
     * 隐藏面板
     */
    private void hidePanelView() {
        if (chat_more_container.getVisibility() == View.VISIBLE) {
            chat_more_container.setVisibility(View.GONE);
        }
    }

    /**
     * 显示面板
     */
    private void showPanelView() {
        if (chat_more_container.getVisibility() == View.GONE) {
            chat_more_container.setVisibility(View.VISIBLE);
        } else {
            chat_more_container.setVisibility(View.GONE);
        }
    }

    /**
     * 隐藏软键盘
     */
    public void hideSoftInputView() {
        InputMethodManager manager = ((InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE));
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
