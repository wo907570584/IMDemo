package ldu.guofeng.imdemo.activity;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import ldu.guofeng.imdemo.R;


/**
 * 欢迎页
 *
 * @author 郭峰
 */

public class WelcomeActivity extends Activity {
    private ImageView mImageView;//标志图
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        mContext = this;
        findView();
        init();
    }

    //---------------------------------------------------
    private void findView() {
        mImageView = (ImageView) findViewById(R.id.iv_welcome);
    }

    /**
     * 图片2s等待
     */
    private void init() {
        mImageView.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(mContext, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }
}
