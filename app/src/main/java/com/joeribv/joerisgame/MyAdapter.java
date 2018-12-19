package com.joeribv.joerisgame;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private ArrayList<DataBaseSorter> mDataset;
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv, tv1;
        public ViewHolder(View v) {
            super(v);
            tv1 = v.findViewById(R.id.textView2);
            tv = v.findViewById(R.id.textView10);
        }
    }
    public MyAdapter(ArrayList<DataBaseSorter> myDataset) {
        mDataset = myDataset;
    }
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_text_view, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tv.setText(mDataset.get(position).getName());
        holder.tv1.setText(Integer.toString( mDataset.get(position).getScore()));
    }
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
