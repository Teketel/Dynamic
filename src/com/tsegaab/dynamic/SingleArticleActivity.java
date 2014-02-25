package com.tsegaab.dynamic;

import java.util.ArrayList;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ShareActionProvider;
import android.widget.Toast;

import com.tsegaab.dynamic.objects.Article;

public class SingleArticleActivity extends FragmentActivity {
	
	private ShareActionProvider provider;

	private static int NUM_PAGES;
	private ViewPager mPager;
	private PagerAdapter mPagerAdapter;
	private ArrayList<Article> articles_to_display;
	private int article_index;
	private int source_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_screen_slide);

		Intent i = getIntent();
		article_index = i.getIntExtra("article_index", 0);
		source_id = i.getIntExtra("source_id", 0);

		ActionBar actionBar = getActionBar();
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);

		articles_to_display = Consts.current_articles;
		NUM_PAGES = articles_to_display.size();

		// Instantiate a ViewPager and a PagerAdapter.
		mPager = (ViewPager) findViewById(R.id.pager);
		mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
		mPager.setAdapter(mPagerAdapter);
		mPager.setCurrentItem(NUM_PAGES - (article_index + 1));
		mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				// When changing pages, reset the action bar actions since they
				// are dependent
				// on which page is currently active. An alternative approach is
				// to have each
				// fragment expose actions itself (rather than the activity
				// exposing actions),
				// but for simplicity, the activity provides the actions in this
				// sample.
				position = article_index;
				invalidateOptionsMenu();
			}
		});
	}

	private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
		public ScreenSlidePagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			return ScreenSlidePageFragment
					.create(articles_to_display, position);

		}

		@Override
		public int getCount() {
			return NUM_PAGES;
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent intent = new Intent(this, ArticlesActivity.class);
			intent.putExtra("source_id", source_id);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();
			return true;

			// action with ID action_refresh was selected
		case R.id.sigle_article_action_share:
			//sdoShare();
			Toast.makeText(this, "Share selected", Toast.LENGTH_SHORT).show();
			return true;
			// action with ID action_settings was selected
		case R.id.sigle_article_action_favorite:
			Toast.makeText(this, "Favorite selected", Toast.LENGTH_SHORT)
					.show();

			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.single_activity_actions, menu);
		// Get the ActionProvider for later usage
	    // provider = (ShareActionProvider) menu.findItem(R.id.sigle_article_action_share).getActionProvider();
		return super.onCreateOptionsMenu(menu);
	}
	
	
	
	public void doShare() {
	    // populate the share intent with data
	    Intent intent = new Intent(Intent.ACTION_SEND);
	    intent.setType("text/plain");
	    intent.putExtra(Intent.EXTRA_TEXT, "This is a message for you");
	    provider.setShareIntent(intent);
	  }
}
