package ldu.guofeng.imdemo.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mcxtzhang.indexlib.IndexBar.widget.IndexBar;
import com.mcxtzhang.indexlib.suspension.SuspensionDecoration;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import ldu.guofeng.imdemo.R;
import ldu.guofeng.imdemo.activity.AddFriendActivity;
import ldu.guofeng.imdemo.adapter.ConstactAdapter;
import ldu.guofeng.imdemo.bean.Friend;
import ldu.guofeng.imdemo.im.SmackUtils;
import ldu.guofeng.imdemo.util.DividerItemDecoration;
import ldu.guofeng.imdemo.view.CustomIndexToolbar;

/**
 * 联系人页
 */
public class ContactsFragment extends Fragment {

    private Context mContext;
    private View mView;

    private RecyclerView mRecyclerView;//视图
    private ConstactAdapter mAdapter;//适配器
    private List<Friend> mDatas = new ArrayList<>();//数据

    private LinearLayoutManager mManager;
    private SuspensionDecoration mDecoration;//分类、悬停的Decoration
    private IndexBar mIndexBar; //右侧边栏导航区域
    private TextView mTvSideBarHint; //显示指示器DialogText
    private CustomIndexToolbar mToolbar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        mView = inflater.inflate(R.layout.fragment_contacts, container);
        init();
        return mView;
    }

    private void init() {
        //添加好友点击监听
        mToolbar = (CustomIndexToolbar) mView.findViewById(R.id.title_bar);
        mToolbar.setRightButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //添加好友
                Intent intent = new Intent(mContext, AddFriendActivity.class);
                mContext.startActivity(intent);
            }
        });
        mToolbar.setLeftButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initData();
            }
        });
        //RecyclerView设置
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.rv);
        mRecyclerView.setLayoutManager(mManager = new LinearLayoutManager(mContext));
        mAdapter = new ConstactAdapter(mContext, mDatas);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(mDecoration = new SuspensionDecoration(mContext, mDatas));
        //如果add两个，那么按照先后顺序，依次渲染。
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
        //使用indexBar和HintTextView
        mTvSideBarHint = (TextView) mView.findViewById(R.id.tvSideBarHint);//HintTextView
        mIndexBar = (IndexBar) mView.findViewById(R.id.indexBar);//IndexBar


    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
    }


    private final MyHandler mHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        private final WeakReference<ContactsFragment> mWeakReference;

        MyHandler(ContactsFragment contactsFragment) {
            mWeakReference = new WeakReference<ContactsFragment>(contactsFragment);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ContactsFragment contactsFragment = mWeakReference.get();
            if (contactsFragment != null) {
                Log.e("更新好友列表", "好友数量=" + contactsFragment.mDatas.size());
                //设置数据、更新适配器
                contactsFragment.mAdapter.setDatas(contactsFragment.mDatas);
                contactsFragment.mAdapter.notifyDataSetChanged();
                //下面代码属于设置集成的：字母索引列表
                contactsFragment.mIndexBar.
                        //设置HintTextView
                        setmPressedShowTextView(contactsFragment.mTvSideBarHint)
                        //设置需要真实的索引
                        .setNeedRealIndex(true)
                        //设置RecyclerView的LayoutManager
                        .setmLayoutManager(contactsFragment.mManager)
                        //设置数据
                        .setmSourceDatas(contactsFragment.mDatas)
                        .invalidate();
                contactsFragment.mDecoration.setmDatas(contactsFragment.mDatas);
            }
        }
    }


    private void initData() {
        /**
         * 加载好友列表
         */
        new Thread(new Runnable() {
            @Override
            public void run() {
                mDatas = SmackUtils.getInstance().getFriendsList();
                if (mDatas.size() > 0) {
                    mHandler.sendEmptyMessage(0);
                }
            }
        }).start();
    }
}
