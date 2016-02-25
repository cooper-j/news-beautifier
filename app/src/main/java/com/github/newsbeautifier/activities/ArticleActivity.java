package com.github.newsbeautifier.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.newsbeautifier.R;
import com.github.newsbeautifier.models.RSSItem;
import com.github.newsbeautifier.utils.URLImageParser;

public class ArticleActivity extends AppCompatActivity implements View.OnClickListener {

    public static String ARTICLE = "ARTICLE";

    private RSSItem mModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("Article");

        findViewById(R.id.share_fab).setOnClickListener(this);

        mModel = getIntent().getParcelableExtra(ARTICLE);
        initViews();
    }

    private void initViews(){
        TextView title = (TextView) findViewById(R.id.article_title);
        TextView author = (TextView) findViewById(R.id.article_published_author);
        TextView pubDate = (TextView) findViewById(R.id.article_published_date);
        TextView content = (TextView) findViewById(R.id.article_content);
        TextView description = (TextView) findViewById(R.id.article_description);
        ImageView cover = (ImageView) findViewById(R.id.article_cover);
        ImageView image = (ImageView) findViewById(R.id.article_image);

        title.setText(Html.fromHtml(mModel.getTitle() != null ? mModel.getTitle() : ""));
        author.setText(getString(R.string.article_author, mModel.getAuthor()));
        pubDate.setText(getString(R.string.article_pubDate, mModel.getPubDate()));

        // set content
        URLImageParser p = new URLImageParser(content, this);
        Spanned contentSpan = Html.fromHtml(mModel.getContent(), p, null);
        content.setText(contentSpan);

        p = new URLImageParser(description, this);
        Spanned descriptionSpan = Html.fromHtml(mModel.getDescription(), p, null);
        description.setText(descriptionSpan);

        if (!mModel.getImage().isEmpty()) {
            Glide
                    .with(this)
                    .load(mModel.getImage())
                    .centerCrop()
                    .crossFade()
                    .into(cover);
            Glide
                    .with(this)
                    .load(mModel.getImage())
                    .centerCrop()
                    .crossFade()
                    .into(image);
        }
    }

    private void shareArticle(){
        if (mModel != null) {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_TEXT, mModel.getTitle() + "\n" + mModel.getLink());
            shareIntent.setType("text/plain");
            startActivity(Intent.createChooser(shareIntent, getString(R.string.send_to)));
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.share_fab){
            shareArticle();
        }
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
}

