package com.fangwolf.smartexp;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Loneweolf on 2018/3/5.
 */

public class GeneratrQRAdapter extends RecyclerView.Adapter {

    public static final  int TYPE_Sname = 0;
    public static final  int TYPE_Sadress = 2;
    private List<MoreTypeBean> mData;

    public GeneratrQRAdapter(List<MoreTypeBean> data) {
        this.mData = data;
    }

    @Override
    public int getItemViewType(int position) {
        MoreTypeBean moreTypeBean = mData.get(position);
        if (moreTypeBean.type == 0) {
            return TYPE_Sname;
        } else {
            return TYPE_Sadress;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        if (viewType == TYPE_Sname) {
            view = View.inflate(parent.getContext(),R.layout.edit_item_1,null);
            return new SnameHolder(view);
        } else {
            view = View.inflate(parent.getContext(),R.layout.edit_item_2,null);
            return new SadressHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        if (mData != null) {
            return mData.size();
        }
        return 0;
    }

    private class SnameHolder extends RecyclerView.ViewHolder {
        public SnameHolder(View itemView) {
            super(itemView);
        }
    }

    private class SadressHolder extends RecyclerView.ViewHolder {
        public SadressHolder(View itemView) {
            super(itemView);
        }
    }
}
