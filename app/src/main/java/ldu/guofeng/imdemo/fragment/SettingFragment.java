package ldu.guofeng.imdemo.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import ldu.guofeng.imdemo.R;
import ldu.guofeng.imdemo.activity.AboutActivity;


public class SettingFragment extends Fragment implements View.OnClickListener {
    private RelativeLayout about;//作者
    private View view;
    private Context mContext;//上下文
    //private LayoutInflater layoutInflater;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        view = inflater.inflate(R.layout.fragment_setting, container);
        ///layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        findView();
        init();
        return view;
    }


    private void findView() {
        about = (RelativeLayout) view.findViewById(R.id.rl_about);

    }

    private void init() {
        about.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.rl_about:
                intent = new Intent(mContext, AboutActivity.class);
                startActivity(intent);
                break;
        }
    }
}
