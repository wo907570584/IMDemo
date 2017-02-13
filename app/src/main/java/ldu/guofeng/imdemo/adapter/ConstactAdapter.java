package ldu.guofeng.imdemo.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mcxtzhang.swipemenulib.SwipeMenuLayout;

import java.util.List;

import ldu.guofeng.imdemo.R;
import ldu.guofeng.imdemo.activity.ChatActivity;
import ldu.guofeng.imdemo.base.Constant;
import ldu.guofeng.imdemo.bean.Friend;
import ldu.guofeng.imdemo.im.SmackUtils;
import ldu.guofeng.imdemo.util.ToastUtils;

/**
 * 联系人页适配器
 *
 * @author GUOFENG
 */

public class ConstactAdapter extends RecyclerView.Adapter<ConstactAdapter.ViewHolder> {

    private Context mContext;
    private List<Friend> mDatas;

    public ConstactAdapter(Context mContext, List<Friend> mDatas) {
        this.mContext = mContext;
        this.mDatas = mDatas;
    }

    public ConstactAdapter setDatas(List<Friend> datas) {
        mDatas.clear();
        mDatas = datas;
        return this;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.item_constact_friend, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Friend friend = mDatas.get(position);
        holder.iv_touxiang.setImageResource(R.mipmap.ic_launcher);
        holder.tv_name.setText(friend.getName());
        holder.content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //打开聊天界面
                Intent intent = new Intent(mContext, ChatActivity.class);
                //用Bundle携带数据
                Bundle bundle = new Bundle();
                bundle.putString("to_user", mDatas.get(position).getName());
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
        holder.itemView.findViewById(R.id.btnDel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SwipeMenuLayout) holder.itemView).quickClose();
                /**
                 * 删除逻辑
                 */
                final Handler mHandler = new Handler() {

                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        mDatas.remove(holder.getAdapterPosition());
                        notifyDataSetChanged();
                        ToastUtils.showShortToast("删除成功");
                    }
                };
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SmackUtils.getInstance()
                                .deleteFriend(mDatas.get(position).getName() + "@" + Constant.IM_HOST);
                        mHandler.sendEmptyMessage(0);
                    }
                }).start();

            }//end onclick
        });
    }


    @Override
    public int getItemCount() {
        return mDatas != null ? mDatas.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_touxiang;
        TextView tv_name;
        View content;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            iv_touxiang = (ImageView) itemView.findViewById(R.id.iv_touxiang);
            content = itemView.findViewById(R.id.content);
        }
    }

}
