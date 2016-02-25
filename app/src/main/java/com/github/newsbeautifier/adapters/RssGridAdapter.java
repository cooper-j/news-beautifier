package com.github.newsbeautifier.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.newsbeautifier.R;
import com.github.newsbeautifier.models.RSSFeed;
import com.github.newsbeautifier.models.User;

import java.util.ArrayList;

/**
 * Created by james_000 on 2/16/2016.
 */
public class RssGridAdapter extends ArrayAdapter<RSSFeed> {

    private Context mContext;
    private User mUser;
    private ArrayList<RSSFeed> mRssList;
    private int mLayoutResourceId;

    public RssGridAdapter(Context context, int layoutResourceId,  ArrayList<RSSFeed> rsssList, User user){
        super(context, layoutResourceId, rsssList);
        mContext = context;
        mUser = user;
        mLayoutResourceId = layoutResourceId;
        mRssList = rsssList;
    }

    @Override
    public int getCount() {
        return mRssList.size();
    }

    @Override
    public RSSFeed getItem(int position) {
        return mRssList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(mLayoutResourceId, parent, false);
            holder.pos = position;
            holder.rssName = (TextView) convertView.findViewById(R.id.feedTitleTextView);
            holder.rssImage = (ImageView) convertView.findViewById(R.id.rssImage);
            holder.stateIcon = (ImageView) convertView.findViewById(R.id.stateIcon);
            holder.filter = convertView.findViewById(R.id.rssStateFilter);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }

        if (mRssList.get(position) != null) {
            Glide.with(mContext).load(mRssList.get(position).getIcon()).into(holder.rssImage);
            holder.rssName.setText(mRssList.get(position).getTitle());
            holder.stateIcon.setVisibility(View.GONE);

            convertView.setOnClickListener(new OnRssFeedClick(holder));
        }
        return convertView;
    }

    private static class ViewHolder {
        public int pos;
        public ImageView rssImage;
        public ImageView stateIcon;
        public View filter;
        public TextView rssName;
    }

    private class OnRssFeedClick implements View.OnClickListener {
        private ViewHolder mViewHolder;
        public OnRssFeedClick(ViewHolder viewHolder){
            mViewHolder = viewHolder;
        }

        @Override
        public void onClick(View v) {
            if (mViewHolder.stateIcon.getVisibility() == View.GONE) {
                mUser.getFeeds().add(mRssList.get(mViewHolder.pos));
                mViewHolder.stateIcon.setVisibility(View.VISIBLE);
                AlphaAnimation fadeIn = new AlphaAnimation(1.0f, 0.0f);
                fadeIn.setDuration(1000);
                fadeIn.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        mViewHolder.filter.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                mViewHolder.filter.startAnimation(fadeIn);
            } else {
                mUser.getFeeds().remove(mRssList.get(mViewHolder.pos));
                mViewHolder.stateIcon.setVisibility(View.GONE);
                AlphaAnimation fadeOut = new AlphaAnimation(0.0f, 1.0f);
                fadeOut.setDuration(1000);
                fadeOut.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        mViewHolder.filter.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                mViewHolder.filter.startAnimation(fadeOut);
            }
        }
    }
}