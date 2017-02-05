package ldu.guofeng.imdemo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import ldu.guofeng.imdemo.R;
import ldu.guofeng.imdemo.base.IMApplication;
import ldu.guofeng.imdemo.bean.ItemModel;
import ldu.guofeng.imdemo.bean.MsgModel;

/**
 * 聊天界面适配器
 *
 * @author GUOFENG
 */
public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.BaseAdapter> {

    private ArrayList<ItemModel> dataList = new ArrayList<>();

    public void replaceAll(ArrayList<ItemModel> list) {
        dataList.clear();
        if (list != null && list.size() > 0) {
            dataList.addAll(list);
        }
        notifyDataSetChanged();
    }

    public void addAll(ArrayList<ItemModel> list) {
        if (dataList != null && list != null) {
            dataList.addAll(list);
            notifyItemRangeChanged(dataList.size(), list.size());
        }
    }

    /**
     * 在最后插入一条消息
     *
     * @param itemModel
     */
    public void insertLastItem(ItemModel itemModel) {
        if (itemModel != null) {
            dataList.add(itemModel);
        }
        notifyItemInserted(dataList.size());
    }

    //---------
    @Override
    public ChatAdapter.BaseAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ItemModel.LEFT_TEXT:
                return new LeftTextViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.chat_left_text, parent, false));
            case ItemModel.RIGHT_TEXT:
                return new RightTextViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.chat_right_text, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(ChatAdapter.BaseAdapter holder, int position) {
        holder.setData(dataList.get(position).object);
    }

    @Override
    public int getItemViewType(int position) {
        return dataList.get(position).leftOrRight;
    }

    @Override
    public int getItemCount() {
        return dataList != null ? dataList.size() : 0;
    }

    public class BaseAdapter extends RecyclerView.ViewHolder {


        public BaseAdapter(View itemView) {
            super(itemView);
        }

        void setData(Object object) {

        }
    }

    private class LeftTextViewHolder extends BaseAdapter {
        private ImageView ic_user;
        private TextView tv;

        public LeftTextViewHolder(View view) {
            super(view);
            ic_user = (ImageView) itemView.findViewById(R.id.ic_user);
            tv = (TextView) itemView.findViewById(R.id.tv);
        }

        @Override
        void setData(Object object) {
            super.setData(object);
            MsgModel msgModel = (MsgModel) object;
            Glide.with(IMApplication.getMyAppContext()).load(R.mipmap.ic_launcher)
                    .placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher)
                    .into(ic_user);
            tv.setText(msgModel.getContent());
        }
    }

    private class RightTextViewHolder extends BaseAdapter {
        private ImageView ic_user;
        private TextView tv;

        public RightTextViewHolder(View view) {
            super(view);
            ic_user = (ImageView) itemView.findViewById(R.id.ic_user);
            tv = (TextView) itemView.findViewById(R.id.tv);
        }

        @Override
        void setData(Object object) {
            super.setData(object);
            MsgModel msgModel = (MsgModel) object;
            Glide.with(itemView.getContext()).load(R.mipmap.ic_launcher)
                    .placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher)
                    .into(ic_user);
            tv.setText(msgModel.getContent());
        }
    }
}
