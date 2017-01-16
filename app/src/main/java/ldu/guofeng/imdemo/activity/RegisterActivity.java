package ldu.guofeng.imdemo.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import ldu.guofeng.imdemo.R;
import ldu.guofeng.imdemo.util.ToastUtils;
import ldu.guofeng.imdemo.view.CustomReturnToolbar;

public class RegisterActivity extends CustomReturnToolbar {

    private Button btn_register;
    private EditText et_username;
    private EditText et_password;

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_register;
    }

    @Override
    public boolean canBack() {
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getToolbar().setTitle("注册");

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        String username = et_username.getText().toString();
        String password = et_password.getText().toString();
        if (TextUtils.isEmpty(username)) {
            ToastUtils.showShortToast("请填写昵称");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            ToastUtils.showLongToast("请填写密码");
            return;
        }
        /**
         * 注册逻辑......
         */
    }

}
