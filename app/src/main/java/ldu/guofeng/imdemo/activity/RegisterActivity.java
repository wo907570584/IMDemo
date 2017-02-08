package ldu.guofeng.imdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import ldu.guofeng.imdemo.R;
import ldu.guofeng.imdemo.im.SmackUtils;
import ldu.guofeng.imdemo.util.PreferencesUtils;
import ldu.guofeng.imdemo.util.ToastUtils;
import ldu.guofeng.imdemo.view.CustomReturnToolbar;

/**
 * 注册页
 */
public class RegisterActivity extends CustomReturnToolbar {

    private Button btn_register;
    private EditText et_username;
    private EditText et_password;
    private Context mContext;

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_register;
    }


    @Override
    protected void onResume() {
        super.onResume();
        getToolbar().setTitle("注册");

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        findView();
        init();
    }


    private void findView() {
        btn_register = (Button) findViewById(R.id.register);
        et_username = (EditText) findViewById(R.id.username);
        et_password = (EditText) findViewById(R.id.password);
    }

    private void init() {
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doRegister();
            }
        });
    }

    private void doRegister() {
        final String username = et_username.getText().toString();
        final String password = et_password.getText().toString();
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            ToastUtils.showShortToast("昵称/密码不能为空");
            return;
        }
        /**
         * 注册逻辑......
         */
        new Thread(new Runnable() {
            @Override
            public void run() {
                //建立连接
                SmackUtils.getInstance().getXMPPConnection();
                if (SmackUtils.getInstance().register(username, password)) {
                    PreferencesUtils.getInstance().putString("username", username);
                    PreferencesUtils.getInstance().putString("pwd", password);
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }).start();
    }
}
