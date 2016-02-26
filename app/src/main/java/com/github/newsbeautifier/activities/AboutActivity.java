package com.github.newsbeautifier.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.github.newsbeautifier.BuildConfig;
import com.github.newsbeautifier.R;
import com.github.newsbeautifier.adapters.AboutAdapter;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        AboutAdapter aboutAdapter = new AboutAdapter(this);
        ((ListView)findViewById(R.id.list_view)).setAdapter(aboutAdapter);

        // add version
        aboutAdapter.add(new AboutAdapter.AboutModel(getString(R.string.about_version), getVersionName()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private String getVersionName() {
        return "v" + BuildConfig.VERSION_NAME + "-r" + BuildConfig.VERSION_CODE;
    }
}
