package com.github.newsbeautifier;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.github.newsbeautifier.fragments.HomeFragment;
import com.github.newsbeautifier.fragments.RSSFragment;
import com.github.newsbeautifier.models.RSSFeed;

public class HomeActivity extends AppCompatActivity implements RSSFragment.OnMyFeedsChanged{

    private DrawerLayout mDrawerLayout;
    private ListView mHeaderList;
    private NavigationView mNavigationView;
    private ListView mFeedListView;
    private FeedAdapter mFeedAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mHeaderList = (ListView) mNavigationView.getHeaderView(0).findViewById(R.id.left_drawer);
        mFeedListView = (ListView) findViewById(R.id.feed_list_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        // Set the adapter for the list view
        mHeaderList.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.nav_items)));

        mHeaderList.setOnItemClickListener(new DrawerItemClickListener());

        initMyFeeds();
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
    }

    @Override
    protected void onStart() {
        super.onStart();
        selectItem(0);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_settings:
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMyFeedsChanged() {
        mFeedAdapter.notifyDataSetChanged();
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    /** Swaps fragments in the main content view */
    private void selectItem(int position) {
        Fragment fragment = null;

        if (position == 0){
            fragment = new HomeFragment();
        } else if (position == 1){
            fragment = new RSSFragment();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();

        // Highlight the selected item, update the title, and close the drawer
        mHeaderList.setItemChecked(position, true);
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

    private class FeedAdapter extends ArrayAdapter<RSSFeed>{
        public FeedAdapter(){
            super(HomeActivity.this, R.layout.menu_feed_item, ((MyApplication)getApplication()).mUser.getFeeds());
        }
    }
}
