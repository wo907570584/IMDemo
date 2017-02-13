package ldu.guofeng.imdemo.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import ldu.guofeng.imdemo.R;
import ldu.guofeng.imdemo.base.Constant;
import ldu.guofeng.imdemo.im.SmackUtils;
import ldu.guofeng.imdemo.view.CustomReturnToolbar;

/**
 * 添加好友
 */
public class AddFriendActivity extends CustomReturnToolbar {

    private EditText userName;
    private Button btn_add;

    /**
     * 添加好友
     */
    private void doAddFriend() {
        final String username = userName.getText().toString().trim();
        new Thread(new Runnable() {
            @Override
            public void run() {
                SmackUtils.getInstance().addFriend(username + "@" + Constant.IM_HOST);
            }
        }).start();
    }

    //------------------------------------------
    @Override
    protected int provideContentViewId() {
        return R.layout.activity_add_friend;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getToolbar().setTitle("添加好友");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findView();
        initView();
    }

    private void initView() {
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doAddFriend();
            }
        });
    }


    private void findView() {
        userName = (EditText) findViewById(R.id.et_username);
        btn_add = (Button) findViewById(R.id.btn_add);
    }
}
