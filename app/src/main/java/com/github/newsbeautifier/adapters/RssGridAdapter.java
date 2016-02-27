package com.github.newsbeautifier.adapters;

import android.app.Activity;
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
import com.github.newsbeautifier.MyApplication;
import com.github.newsbeautifier.R;
import com.github.newsbeautifier.fragments.FeedListFragment;
import com.github.newsbeautifier.models.RSSFeed;
import com.github.newsbeautifier.models.User;

import java.util.List;

/**
 * Created by james_000 on 2/16/2016.
 */
public class RssGridAdapter extends ArrayAdapter<RSSFeed> {

    private User mUser;
    private Activity mActivity;
    private List<RSSFeed> mRssList;
    private int mLayoutResourceId;

    public RssGridAdapter(Activity activity, int layoutResourceId, List<RSSFeed> rssList){
        super(activity, layoutResourceId, rssList);

        mActivity = activity;
        mUser = ((MyApplication)mActivity.getApplication()).mUser;
        mLayoutResourceId = layoutResourceId;
        mRssList = rssList;
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
            LayoutInflater inflater = (LayoutInflater) mActivity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(mLayoutResourceId, parent, false);
            holder.rssName = (TextView) convertView.findViewById(R.id.feedTitleTextView);
            holder.rssImage = (ImageView) convertView.findViewById(R.id.rssImage);
            holder.stateIcon = (ImageView) convertView.findViewById(R.id.stateIcon);
            holder.filter = convertView.findViewById(R.id.rssStateFilter);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }

        convertView.setOnClickListener(new OnRssFeedClick(holder));
        if (mRssList.get(position) != null) {
            holder.pos = position;
            Glide.with(mActivity).load(mRssList.get(position).getImage()).into(holder.rssImage);
            holder.rssName.setText(mRssList.get(position).getTitle());
            holder.stateIcon.setVisibility(mRssList.get(position).getUserId() == mUser.getId() ? View.VISIBLE : View.GONE);
            holder.filter.setVisibility(mRssList.get(position).getUserId() == mUser.getId() ? View.GONE : View.VISIBLE);

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
            RSSFeed feed = mRssList.get(mViewHolder.pos);
            List<RSSFeed> tmpFeeds = mUser.getFeeds();
            if (mViewHolder.stateIcon.getVisibility() == View.GONE) {
                mUser.addFeed(feed);
                feed.setUserId(mUser.getId());

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
                feed.setUserId(null);
                mUser.removeFeed(feed);
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
            feed.update();

            try {
                ((FeedListFragment.OnMyFeedsChanged)mActivity).onMyFeedsChanged();
            } catch (ClassCastException e){
                e.printStackTrace();
            }
        }
    }
}