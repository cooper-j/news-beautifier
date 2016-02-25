package com.github.newsbeautifier.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.github.newsbeautifier.R;
import com.github.newsbeautifier.models.RSSItem;

import java.util.List;

/**
 * Created by james_000 on 2/25/2016.
 */

public class StaggeredRecyclerViewAdapter  extends RecyclerView.Adapter<StaggeredViewHolder> {

    private List<RSSItem> itemList;
    private Context mContext;

    public StaggeredRecyclerViewAdapter(Context context, List<RSSItem> itemList) {
        this.itemList = itemList;
        this.mContext = context;
    }

    @Override
    public StaggeredViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.staggered_list_item, null);
        StaggeredViewHolder rcv = new StaggeredViewHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(StaggeredViewHolder holder, int position) {
        holder.articleTitle.setText(itemList.get(position).getTitle());
        Glide.with(mContext).load(itemList.get(position).getImage()).into(holder.articlePhoto);
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }
}