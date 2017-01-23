package ldu.guofeng.imdemo.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ldu.guofeng.imdemo.R;
import ldu.guofeng.imdemo.activity.AboutActivity;
import ldu.guofeng.imdemo.activity.LoginActivity;
import ldu.guofeng.imdemo.im.SmackUtils;
import ldu.guofeng.imdemo.util.PreferencesUtils;
import ldu.guofeng.imdemo.view.CustomPopWindow;


public class SettingFragment extends Fragment implements View.OnClickListener {

    private Context mContext;//上下文
    private View view;//设置view
    private RelativeLayout about;//作者
    private RelativeLayout exit;//退出
    private TextView name;//昵称

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        view = inflater.inflate(R.layout.fragment_setting, container);
        findView();
        init();
        initName();
        return view;
    }


    private void findView() {
        about = (RelativeLayout) view.findViewById(R.id.rl_about);
        exit = (RelativeLayout) view.findViewById(R.id.rl_app_exit);
        name = (TextView) view.findViewById(R.id.name);
    }

    private void init() {
        about.setOnClickListener(this);
        exit.setOnClickListener(this);
    }

    private void initName() {
        //设置昵称
        name.setText(PreferencesUtils.getInstance().getString("username"));
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.rl_about:
                intent = new Intent(mContext, AboutActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_app_exit:
                showPopView(exit);
                break;
        }
    }

    private void showPopView(RelativeLayout exit) {
        View popview = LayoutInflater.from(mContext).inflate(R.layout.pop_exit, null);
        //创建并显示popWindow
        CustomPopWindow popWindow = new CustomPopWindow.PopupWindowBuilder(mContext)
                .setView(popview)
                .setAnimationStyle(android.R.style.Animation_InputMethod)
                .create()
                .showAtLocation(exit, Gravity.BOTTOM, 0, 0);
        initPop(popWindow, popview);

    }

    public void initPop(final CustomPopWindow popWindow, View popview) {
        TextView exit = (TextView) popview.findViewById(R.id.exit);//退出
        TextView cancel = (TextView) popview.findViewById(R.id.cancel);//取消
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                /**
                 * 断开连接逻辑
                 */
                SmackUtils.getInstance().exitConnect();
                Intent intent = new Intent(mContext, LoginActivity.class);
                mContext.startActivity(intent);
                getActivity().finish();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                popWindow.dissmiss();
            }
        });
    }
}
