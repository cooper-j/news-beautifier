package com.github.newsbeautifier.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.github.newsbeautifier.MyApplication;
import com.github.newsbeautifier.R;
import com.github.newsbeautifier.fragments.FeedListFragment;
import com.github.newsbeautifier.fragments.FeedSelectFragment;
import com.github.newsbeautifier.fragments.HomeFragment;
import com.github.newsbeautifier.models.RSSFeed;

public class HomeActivity extends AppCompatActivity implements FeedListFragment.OnMyFeedsChanged{

    private DrawerLayout mDrawerLayout;
    private ListView mHeaderList;
    private ListView mFeedListView;
    private FeedAdapter mFeedAdapter;
    private String[] mTitles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mHeaderList = (ListView) navigationView.getHeaderView(0).findViewById(R.id.left_drawer);
        mFeedListView = (ListView) findViewById(R.id.feed_list_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        if (mDrawerLayout != null) {
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            mDrawerLayout.addDrawerListener(toggle);
            toggle.syncState();
        }
        mTitles = getResources().getStringArray(R.array.nav_items);
        mHeaderList.setAdapter(new ArrayAdapter<>(this,
                R.layout.header_item, mTitles));

        mHeaderList.setOnItemClickListener(new HeaderItemClickListener());

        ((ListView)findViewById(R.id.settings_list_view)).setOnItemClickListener(settingsItemClickListener);

        initMyFeeds();
    }

    private AdapterView.OnItemClickListener settingsItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (position == 0){
                sendFeedBack();
            } else if (position == 1){
                startAboutActivity();
            }
        }
    };

    private void startAboutActivity() {
        startActivity(new Intent(this, AboutActivity.class));
    }

    private void sendFeedBack() {
        Intent Email = new Intent(Intent.ACTION_SEND);
        Email.setType("text/email");
        Email.putExtra(Intent.EXTRA_EMAIL, getResources().getStringArray(R.array.email_contacts));
        Email.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.feedback_email_title));
        startActivity(Intent.createChooser(Email, getString(R.string.feedback_send_title)));
    }

    private void initMyFeeds() {
        mFeedAdapter = new FeedAdapter();
        mFeedListView.setAdapter(mFeedAdapter);
        mFeedListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                RSSFeed item = mFeedAdapter.getItem(position);
                item.setUserId(null);
                item.update();
                mFeedAdapter.remove(mFeedAdapter.getItem(position));
                return false;
            }
        });
        mFeedListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectFeed(mFeedAdapter.getItem(position));
            }
        });
    }

    private void selectFeed(RSSFeed feed) {
        /*RSSFragment feedListFragment = new RSSFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(RSSFragment.FEED, item);
        feedListFragment.setArguments(bundle);

        mDrawerLayout.closeDrawer(GravityCompat.START);
        mHeaderList.clearChoices();
        mHeaderList.requestLayout();

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, feedListFragment)
                .commit();
                */
        if (feed.getItems().size() > 0) {
            Intent intent = new Intent(this, ArticleActivity.class);
            intent.putExtra(ArticleActivity.ARTICLE, feed.getItems().get(0));
            startActivity(intent);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        selectHeaderItem(0);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onMyFeedsChanged() {
            mFeedAdapter.notifyDataSetChanged();
    }

    private class HeaderItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectHeaderItem(position);
        }
    }

    /** Swaps fragments in the main content view */
    private void selectHeaderItem(int position) {
        Fragment fragment = null;

        if (position == 0){
            fragment = new HomeFragment();
        } else if (position == 1){
            fragment = new FeedSelectFragment();
        } else if (position == 2){
            fragment = new FeedListFragment();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();

        // Highlight the selected item, update the title, and close the drawer
        mHeaderList.setItemChecked(position, true);
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
        if (position >= 0 && position < mTitles.length) {
            setTitle(mTitles[position]);
        } else {
            setTitle(getString(R.string.app_name));
        }
    }

    private class FeedAdapter extends ArrayAdapter<RSSFeed>{
        public FeedAdapter(){
            super(HomeActivity.this, R.layout.menu_feed_item, ((MyApplication)getApplication()).mUser.getFeeds());
        }
    }
}
