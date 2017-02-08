package ldu.guofeng.imdemo.adapter;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import ldu.guofeng.imdemo.R;
import ldu.guofeng.imdemo.activity.ChatActivity;
import ldu.guofeng.imdemo.base.Constant;
import ldu.guofeng.imdemo.bean.SessionModel;

/**
 * 会话页适配器
 */
public class SessionAdapter extends RecyclerView.Adapter<SessionAdapter.MsgViewHolder> {

    private ArrayList<SessionModel> dataList = new ArrayList<>();
    private Context mContext;

    public SessionAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void insertSessionItem(SessionModel sessionModel) {
        if (sessionModel != null) {
            int index = -1;
            for (int i = 0; i < dataList.size(); i++) {
                if (sessionModel.getForm().equals(dataList.get(i).getForm())) {
                    index = i;
                }
            }
            if (index != -1) {//已经存在就更新
                notifyItemChanged(index, sessionModel);
            } else {//不存在就插入新的
                dataList.add(0, sessionModel);
                notifyItemInserted(0);
            }
        }
    }

    @Override
    public SessionAdapter.MsgViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MsgViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_session_friend, parent, false));
    }

    @Override
    public void onBindViewHolder(SessionAdapter.MsgViewHolder holder, final int position) {
        holder.setData(dataList.get(position));
        //左滑删除
        holder.itemView.findViewById(R.id.btnDel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataList.remove(position);
                notifyItemRemoved(position);
            }
        });
    }

    @Override
    public void onBindViewHolder(SessionAdapter.MsgViewHolder holder, int position, List<Object> payloads) {
        holder.setData(dataList.get(position));
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position);
        } else {
            holder.setData(payloads.get(0));
        }
    }

    @Override
    public int getItemViewType(int position) {
        return dataList.get(position).getType();
    }

    @Override
    public int getItemCount() {
        return dataList != null ? dataList.size() : 0;
    }


    public class MsgViewHolder extends RecyclerView.ViewHolder {
        private TextView user;
        private ImageView user_img;
        private TextView last_msg;
        private LinearLayout content;

        public MsgViewHolder(View itemView) {
            super(itemView);
            user = (TextView) itemView.findViewById(R.id.ic_user_name);
            user_img = (ImageView) itemView.findViewById(R.id.ic_user);
            last_msg = (TextView) itemView.findViewById(R.id.ic_last_msg);
            content = (LinearLayout) itemView.findViewById(R.id.content);
        }

        void setData(Object object) {
            final SessionModel sessionModel = (SessionModel) object;
            Glide.with(itemView.getContext()).load(R.mipmap.ic_launcher)
                    .placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher)
                    .into(user_img);
            user.setText(sessionModel.getForm());
            if (sessionModel.getType() == Constant.MSG_TYPE_TEXT) {
                last_msg.setText(sessionModel.getContent());
            } else if (sessionModel.getType() == Constant.MSG_TYPE_LOC) {
                last_msg.setText("[位置]");
            }

            content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //打开聊天界面
                    Intent intent = new Intent(mContext, ChatActivity.class);
                    //用Bundle携带数据
                    Bundle bundle = new Bundle();
                    bundle.putString("to_user", sessionModel.getForm());
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                }
            });
        }
    }
}
