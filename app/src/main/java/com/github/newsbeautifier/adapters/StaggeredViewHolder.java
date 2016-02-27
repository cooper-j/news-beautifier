package com.github.newsbeautifier.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.newsbeautifier.R;
import com.github.newsbeautifier.activities.ArticleActivity;
import com.github.newsbeautifier.models.RSSItem;

/**
 * Created by james_000 on 2/25/2016.
 */
public class StaggeredViewHolder  extends RecyclerView.ViewHolder implements View.OnClickListener {

    public RSSItem article;
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
        Context context = view.getContext();
        Intent intent = new Intent(context, ArticleActivity.class);
        intent.putExtra(ArticleActivity.ARTICLE, article);
        context.startActivity(intent);
    }
}
