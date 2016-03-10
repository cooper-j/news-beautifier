package com.github.cooperj.newsbeautifier.adapters;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.cooperj.newsbeautifier.R;
import com.github.cooperj.newsbeautifier.activities.ArticleActivity;
import com.github.cooperj.newsbeautifier.models.RSSItem;

/**
 * Created by james_000 on 2/25/2016.
 */
public class StaggeredViewHolder  extends RecyclerView.ViewHolder implements View.OnClickListener {

    private Activity mActivity;

    public RSSItem article;
    public TextView articleTitle;
    public ImageView articlePhoto;

    public StaggeredViewHolder(View itemView, Activity activity) {
        super(itemView);
        mActivity = activity;
        itemView.findViewById(R.id.card_view).setOnClickListener(this);
        articleTitle = (TextView) itemView.findViewById(R.id.article_title);
        articlePhoto = (ImageView) itemView.findViewById(R.id.article_photo);
    }

    @Override
    public void onClick(View view) {
        Context context = view.getContext();
        Intent intent = new Intent(context, ArticleActivity.class);
        intent.putExtra(ArticleActivity.ARTICLE, article);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions
                    .makeSceneTransitionAnimation(mActivity, articlePhoto, "article_image");

            context.startActivity(intent, options.toBundle());
        } else
            context.startActivity(intent);
    }
}
