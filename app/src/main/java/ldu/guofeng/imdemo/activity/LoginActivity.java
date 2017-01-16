package ldu.guofeng.imdemo.activity;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ldu.guofeng.imdemo.im.LoginAsyncTask;
import ldu.guofeng.imdemo.R;
import ldu.guofeng.imdemo.util.PreferencesUtils;
import ldu.guofeng.imdemo.util.ToastUtil;


/**
 * 登录
 *
 * @author 郭峰
 */

public class LoginActivity extends Activity {
    //布局
    private RelativeLayout rl_user;
    private Button mLogin;
    private TextView register;
    private EditText account;
    private EditText password;
    public Context mContext;//上下文
    private LoginAsyncTask loginAsyncTask;


    //---------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext = this;
        findView();
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //初始化SP保存过的账号和密码
        String username = PreferencesUtils.getInstance().getString("username");
        String pwd = PreferencesUtils.getInstance().getString("pwd");
        if (!TextUtils.isEmpty(username)) {
            account.setText(username);
        }
        if (!TextUtils.isEmpty(pwd)) {
            password.setText(pwd);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (loginAsyncTask != null && loginAsyncTask.getStatus() == AsyncTask.Status.RUNNING) {
            loginAsyncTask.cancel(true);
        }
    }

    //---------------------------------------------------
    private void findView() {
        rl_user = (RelativeLayout) findViewById(R.id.rl_user);
        mLogin = (Button) findViewById(R.id.login);
        register = (TextView) findViewById(R.id.register);
        account = (EditText) findViewById(R.id.account);
        password = (EditText) findViewById(R.id.password);
    }

    private void init() {
        //动画
        Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.login_anim);
        anim.setFillAfter(true);
        rl_user.startAnimation(anim);
        //登录按钮事件
        mLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                doLogin();
            }
        });
        //注册按钮事件
        register.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                doRegister();
            }
        });
    }

    //---------------------------------------------------
    private void doLogin() {
        String username = account.getText().toString();
        String pwd = password.getText().toString();
        if (TextUtils.isEmpty(username)) {
            ToastUtil.showShortToast("您的账号忘记填写啦");
            return;
        }
        if (TextUtils.isEmpty(pwd)) {
            ToastUtil.showShortToast("您的密码忘记填写啦");
            return;
        }
        //执行异步登录任务
        loginAsyncTask = new LoginAsyncTask(mContext);
        loginAsyncTask.execute(username, pwd);
    }

    private void doRegister() {
        //Intent intent = new Intent(mContext, RegisterActivity.class);
        //startActivity(intent);
    }
}
