package com.tsegaab.dynamic;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ActionBar;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ShareActionProvider;
import android.widget.Toast;

import com.tsegaab.dynamic.objects.Article;

import database.DbHandler;
import fragments.ScreenSlidePageFragment;

public class SingleArticleActivity extends FragmentActivity {

	private ShareActionProvider provider;

	private static int NUM_PAGES;
	private ViewPager mPager;
	private PagerAdapter mPagerAdapter;
	private ArrayList<Article> articles_to_display;
	private int article_index;
	private DbHandler db;
	private int brightnessPercent = 0;
	private int font_size;
	private String bg_color;
	private ActionBar actionBar;
	private boolean actionBarShowed;
	private HashMap<String, Article> indexedArticle;
	public int temp_article_index;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_screen_slide);

		indexedArticle = new HashMap<String, Article>();
		db = Consts.db;
		Intent i = getIntent();
		article_index = i.getIntExtra("article_index", 0);
		if (savedInstanceState != null) {
			article_index = savedInstanceState.getInt("article_index");
		}

		actionBar = getActionBar();
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBarShowed = true;

		articles_to_display = Consts.current_articles;
		NUM_PAGES = articles_to_display.size();
		
		for(int indexo = 0; indexo < NUM_PAGES; indexo++)
			indexedArticle.put(""+indexo, articles_to_display.get(indexo));

		// Instantiate a ViewPager and a PagerAdapter.
		mPager = (ViewPager) findViewById(R.id.pager);
		mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
		mPager.setAdapter(mPagerAdapter);
		mPager.setCurrentItem(article_index);
		mPager.setClickable(true);

		mPager.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				switchActionsBarShow();

			}
		});
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
				// position = article_index;
				Log.d(Consts.Z_TAG, "PPPOOOSSS = " + position);
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
			temp_article_index = position;
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
		font_size = db.getFontSize();
		bg_color = db.getBgColor();
		switch (item.getItemId()) {
		case android.R.id.home:
			// Intent intent = new Intent(this, ArticlesActivity.class);
			// intent.putExtra("source_id", source_id);
			// intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			// startActivity(intent);
			finish();
			return true;

			// action with ID action_refresh was selected
		case R.id.sigle_article_action_share:
			// sdoShare();
			Toast.makeText(this, "Share selected", Toast.LENGTH_SHORT).show();
			doShare();
			return true;
			// action with ID action_settings was selected
		case R.id.sigle_article_action_font_plus:
			Toast.makeText(this, "Font plus selected", Toast.LENGTH_SHORT)
					.show();
			if (font_size < 160) {
				db.updateStyle(String.valueOf(font_size + 20));
				refreshPage();
			}
			return true;
		case R.id.sigle_article_action_font_minus:
			Toast.makeText(this, "Font minus selected", Toast.LENGTH_SHORT)
					.show();
			if (font_size > 80) {
				db.updateStyle(String.valueOf(font_size - 20));
				refreshPage();
			}

			return true;
		case R.id.sigle_article_action_bright_plus:
			Toast.makeText(this, "Brightness plus selected", Toast.LENGTH_SHORT)
					.show();
			if (brightnessPercent < 100)
				brightnessPercent += 25;
			upDateBrightness();
			return true;
		case R.id.sigle_article_action_bright_minus:
			Toast.makeText(this, "Brightness minus selected",
					Toast.LENGTH_SHORT).show();
			if (brightnessPercent > 0)
				brightnessPercent -= 25;
			upDateBrightness();
			return true;
		case R.id.sigle_article_action_mode:
			Toast.makeText(this, "Reading mode selected", Toast.LENGTH_SHORT)
					.show();
			if (bg_color.equalsIgnoreCase("white")) {
				db.updateStyle("white", "black");
				refreshPage();
			} else if (bg_color.equalsIgnoreCase("black")) {
				db.updateStyle("wheat", "gray");
				refreshPage();
			} else if (bg_color.equalsIgnoreCase("gray")) {
				db.updateStyle("black", "white");
				refreshPage();
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void upDateBrightness() {
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.screenBrightness = brightnessPercent / 100.0f;
		getWindow().setAttributes(lp);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.single_activity_actions, menu);
		// Get the ActionProvider for later usage
		// provider = (ShareActionProvider)
		// menu.findItem(R.id.sigle_article_action_share).getActionProvider();
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	protected void onSaveInstanceState(Bundle icicle) {
		super.onSaveInstanceState(icicle);
		icicle.putInt("article_index", temp_article_index);
	}

	protected void refreshPage() {
		// int a = temp_article_index - 1;
		// mPager.setCurrentItem(NUM_PAGES - a);
		// mPager.setCurrentItem(a);
		// mPager.setCurrentItem(NUM_PAGES - (article_index - 1));
		// Bundle bundle = new Bundle();
		// onSaveInstanceState(bundle);
		// onDestroy();
		// finish();
		// onCreate(bundle);

		// mPager.refreshDrawableState();
		Intent invisible_activity = new Intent(getApplicationContext(),
				InvisibleActivity.class);
		invisible_activity.putExtra("article_index", temp_article_index);
		startActivity(invisible_activity);
		finish();

	}

	private void switchActionsBarShow() {
		Log.d(Consts.Z_TAG, "switchActionsBarShow 111 from " + actionBarShowed);
		if (actionBarShowed) {
			actionBar.hide();
			actionBarShowed = false;
		} else {
			actionBar.show();
			actionBarShowed = true;
		}

	}

	public void doShare() {
		// populate the share intent with data
		/*Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_TEXT, "This is a message for you");
		provider.setShareIntent(intent);*/
		Article art = indexedArticle.get(""+temp_article_index);
		String title = art.getTitle() + "\n" + art.getLink();
		String catid = "40";
		String id = "12546";

/*		Uri.Builder b = Uri.parse("http://www.ethiopianreporter.com/" ).buildUpon();
		   b.path("index.php");
		   b.appendQueryParameter("option=com_content&catid", catid);
		b.appendQueryParameter("id", id); // instead of ("*&*id", id);
		b.appendQueryParameter("view=article", null); // instead of ("*&*view=article", null);
		   b.build();

		String url = b.build().toString();*/

		String tweetUrl = "https://twitter.com/intent/tweet?text=" + title; //+ "&url=" + url;

		Uri uri = Uri.parse(tweetUrl);
		startActivity(new Intent(Intent.ACTION_VIEW, uri));

	}
}
