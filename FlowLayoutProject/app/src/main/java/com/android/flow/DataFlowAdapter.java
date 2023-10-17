package com.android.flow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatTextView;

import com.flow.layout.FlowLayoutAdapter;

import java.util.List;

public class DataFlowAdapter extends FlowLayoutAdapter {
    private Context mContext;
    private List<String> dataList;

    public DataFlowAdapter(Context mContext, List<String> dataList) {
        this.mContext = mContext;
        this.dataList = dataList;
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    @Override
    public View getItemView(int position, ViewGroup parent) {
        AppCompatTextView mView = (AppCompatTextView) LayoutInflater.from(mContext).inflate(R.layout.item_flow_view, parent, false);
        mView.setText(dataList.get(position));
        return mView;
    }
}
