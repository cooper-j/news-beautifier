package com.github.newsbeautifier.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.newsbeautifier.R;

/**
 * Created by james_000 on 2/25/2016.
 */
public class StaggeredViewHolder  extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView articleTitle;
    public ImageView articlePhoto;

    public StaggeredViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        articleTitle = (TextView) itemView.findViewById(R.id.article_title);
        articlePhoto = (ImageView) itemView.findViewById(R.id.article_photo);
    }

    @Override
    public void onClick(View view) {
        Toast.makeText(view.getContext(), "Clicked Position = " + getPosition(), Toast.LENGTH_SHORT).show();
    }
}
