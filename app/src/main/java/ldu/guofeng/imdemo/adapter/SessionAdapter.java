package ldu.guofeng.imdemo.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ldu.guofeng.imdemo.R;

public class SessionAdapter extends RecyclerView.Adapter<SessionAdapter.MyViewHolder> {

    private List<String> dataList = new ArrayList<>();
    private Context mContext;

    public SessionAdapter(Context mContext, List<String> mDatas) {
        this.mContext = mContext;
        this.dataList = mDatas;
    }

    public SessionAdapter setDatas(List<String> mDatas) {
        dataList = mDatas;
        return this;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_session_friend, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.user.setText(dataList.get(position));
    }

    @Override
    public int getItemCount() {
        return dataList != null ? dataList.size() : 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView user;

        public MyViewHolder(View itemView) {
            super(itemView);
            user = (TextView) itemView.findViewById(R.id.ic_user_name);

        }
    }
}
