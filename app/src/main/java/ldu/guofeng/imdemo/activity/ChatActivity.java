package ldu.guofeng.imdemo.activity;

import android.app.Activity;
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
import ldu.guofeng.imdemo.bean.ChatModel;
import ldu.guofeng.imdemo.bean.ItemModel;
import ldu.guofeng.imdemo.bean.Msg;
import ldu.guofeng.imdemo.im.SmackUtils;
import ldu.guofeng.imdemo.util.PreferencesUtils;
import ldu.guofeng.imdemo.view.CustomReturnToolbar;

public class ChatActivity extends CustomReturnToolbar implements View.OnClickListener {

    private RecyclerView recyclerView;
    private ChatAdapter adapter;
    private EditText et_message;// 键盘焦点View，用于输入内容
    private TextView tv_send;
    private String txtContent;
    private String form, to;
    private LinearLayout chat_more_container;//+布局
    private ImageView chat_more;// 用于切换键盘与面板的按钮View


    @Override
    protected int provideContentViewId() {
        return R.layout.activity_chat;
    }

    @Override
    public boolean canBack() {
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!to.equals("") || to != null)
            getToolbar().setTitle(to);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.et_message:
                hidePanelView();//隐藏面板
                break;
            case R.id.tv_send:
                hidePanelView();//隐藏面板
                sendTextMessage();//发送消息
                break;
            case R.id.chat_more:
                hideSoftInputView();//隐藏软键盘
                hidePanelHandler.postDelayed(hidePanelTask, 200);//显示面板
                break;
            default:
                break;
        }
    }

    /**
     * 订阅接收消息
     * Subscribe，其含义为订阅者。
     * 在其内传入了threadMode，我们定义为ThreadMode.MAIN，其含义是该方法在UI线程完成。
     *
     * @param msg
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void recMsgEventBus(Msg msg) {
        //填充一条聊天信息，包括头像，消息内容
        ChatModel model = new ChatModel();
        model.setIcon("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=1503652422,1776761182&fm=21&gp=0.jpg");
        model.setContent(msg.getContent());
        //在最后插入一条item，包括布局，聊天信息
        adapter.insertLastItem(new ItemModel(ItemModel.CHAT_LEFT, model));
        //滑动到最后
        recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
    }

    /**
     * 发送文本消息
     */
    private void sendTextMessage() {

        if (txtContent.equals("")) {
            return;
        }
        //填充一条聊天信息，包括头像，消息内容
        ChatModel model = new ChatModel();
        model.setIcon("http://img4.imgtn.bdimg.com/it/u=48797599,160266951&fm=21&gp=0.jpg");
        model.setContent(txtContent);
        //在最后插入一条item，包括布局，聊天信息
        adapter.insertLastItem(new ItemModel(ItemModel.CHAT_RIGHT, model));

        //--------------
        final String message = form + Constant.SPLIT + to + Constant.SPLIT
                + Constant.MSG_TYPE_TEXT + Constant.SPLIT
                + txtContent;

        new Thread(new Runnable() {
            @Override
            public void run() {
                SmackUtils.getInstance().sendMessage(message, to);
            }
        }).start();

        //滑动到最后
        recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
        //清空输入框
        et_message.setText("");
    }

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

    //--------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    }

    private void init() {
        //注册EventBus
        EventBus.getDefault().register(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter = new ChatAdapter());
        et_message.setOnClickListener(this);
        tv_send.setOnClickListener(this);
        chat_more.setOnClickListener(this);

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
}
