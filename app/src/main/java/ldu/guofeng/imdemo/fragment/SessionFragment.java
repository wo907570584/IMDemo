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

import java.util.ArrayList;
import java.util.List;

import ldu.guofeng.imdemo.R;
import ldu.guofeng.imdemo.adapter.SessionAdapter;
import ldu.guofeng.imdemo.util.DividerItemDecoration;


public class SessionFragment extends Fragment {
    private View mView;
    private Context mContext;

    private RecyclerView mRecyclerView;//视图
    private SessionAdapter mAdapter;//适配器
    private List<String> mDatas = new ArrayList<>();//数据

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        mView = inflater.inflate(R.layout.fragment_session, container);
        findView();
        return mView;
    }

    private void findView() {
        mRecyclerView= (RecyclerView) mView.findViewById(R.id.recylerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new SessionAdapter(mContext, mDatas);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        for (int i=0;i<10;i++){
            mDatas.add(i+"");
        }
        mAdapter.setDatas(mDatas);

    }
}
