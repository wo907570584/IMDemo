package ldu.guofeng.imdemo.activity;

import android.os.Bundle;

import ldu.guofeng.imdemo.R;
import ldu.guofeng.imdemo.view.CustomReturnToolbar;

public class AddFriendActivity extends CustomReturnToolbar {


    //------------------------------------------
    @Override
    protected int provideContentViewId() {
        return R.layout.activity_add_friend;
    }

    @Override
    public boolean canBack() {
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getToolbar().setTitle("添加好友");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
