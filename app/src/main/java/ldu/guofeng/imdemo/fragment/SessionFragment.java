package ldu.guofeng.imdemo.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import ldu.guofeng.imdemo.R;
import ldu.guofeng.imdemo.adapter.SessionAdapter;
import ldu.guofeng.imdemo.bean.MsgModel;
import ldu.guofeng.imdemo.bean.SessionModel;
import ldu.guofeng.imdemo.util.DividerItemDecoration;

/**
 * 会话页
 */
public class SessionFragment extends Fragment {
    private View mView;
    private Context mContext;

    private RecyclerView mRecyclerView;//视图
    private SessionAdapter mAdapter;//适配器

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        mView = inflater.inflate(R.layout.fragment_session, container);
        findView();
        EventBus.getDefault().register(this); //注册EventBus
        return mView;
    }


    private void findView() {
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.recylerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setAdapter(mAdapter = new SessionAdapter(mContext));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
    }

    /**
     * 订阅接收消息
     * Subscribe，其含义为订阅者。
     * 在其内传入了threadMode，我们定义为ThreadMode.MAIN，其含义是该方法在UI线程完成。
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void recSessionEventBus(SessionModel sessionModel) {
        mAdapter.insertSessionItem(sessionModel);
    }

    /**
     * 订阅接收消息
     * Subscribe，其含义为订阅者。
     * 在其内传入了threadMode，我们定义为ThreadMode.MAIN，其含义是该方法在UI线程完成。
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void recMsgEventBus(MsgModel msg) {
        SessionModel sessionModel = new SessionModel();
        sessionModel.setType(msg.getType());
        sessionModel.setForm(msg.getFromUser());
        sessionModel.setContent(msg.getContent());
        mAdapter.insertSessionItem(sessionModel);
    }

}
