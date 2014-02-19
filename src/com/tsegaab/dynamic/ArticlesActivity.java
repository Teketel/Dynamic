package com.tsegaab.dynamic;

import java.io.File;
import java.util.ArrayList;

import com.tsegaab.dynamic.objects.Article;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ArticlesActivity extends Activity {

	private ViewGroup articles_container;
	private AsyncTask<Void, Void, Void> connectionTask;
	private ArrayList<Article> articles = null;
	private int source_id;
	private Intent single_article_intent;
	private View aContentView;
	private View aLoadingView;
	private int aShortAnimationDuration;
	private int articles_counter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.articles_layout);

		single_article_intent = new Intent(this, SingleArticleActivity.class);
		articles_container = (ViewGroup) findViewById(R.id.articles_container);
		Intent intent = getIntent();
		source_id = intent.getIntExtra("source_id", 0);

		aContentView = (View) findViewById(R.id.articles_scroll);
		aLoadingView = (View) findViewById(R.id.articles_loading_spinner);
		aContentView.setVisibility(View.GONE);
		aShortAnimationDuration = getResources().getInteger(
				android.R.integer.config_shortAnimTime);
		articles_counter = 0;

		ActionBar ab = getActionBar();
		ab.setDisplayHomeAsUpEnabled(true);

		connectionTask = new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				articles = Consts.db.getArticlesWithSourceId(source_id);
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				showContentOrLoadingIndicator(true);
				Consts.current_articles = articles;
				showArticles(articles);
				connectionTask = null;
			}

		};
		connectionTask.execute();
	}

	private void showArticles(ArrayList<Article> articles) {
		if (articles != null) {
			for (int i = 0; i < articles.size(); i++) {
				addArticle(articles.get(i));
			}
		} else {
			Log.e(Consts.EZ_TAG, "Article object is null");
		}
	}

	private void addArticle(Article a) {

		Log.d(Consts.Z_TAG, "addItem");
		final ViewGroup articleView = (ViewGroup) LayoutInflater.from(this)
				.inflate(R.layout.article_layout, articles_container, false);

		TextView article_title = ((TextView) articleView
				.findViewById(R.id.article_title));
		Typeface article_title_font = Typeface.createFromAsset(getAssets(),
				"fonts/NYALA.TTF");
		article_title.setTypeface(article_title_font);
		article_title.setText(a.getTitle());

		ImageView article_image = (ImageView) articleView
				.findViewById(R.id.article_image);
		if (a.getImage_local_path() != null) {
			File imgFile = new File(a.getImage_local_path());
			if (imgFile.exists()) {
				Bitmap bmp = BitmapFactory
						.decodeFile(imgFile.getAbsolutePath());
				article_image.setImageBitmap(bmp);
			}

		}

		WebView article_content = ((WebView) articleView
				.findViewById(R.id.article_highlite));
		WebSettings settings = article_content.getSettings();
		settings.setDefaultTextEncodingName("utf-8");
		article_content.loadData(a.getContent(), "text/html; charset=utf-8",
				"UTF-8");

		((TextView) articleView.findViewById(R.id.article_author)).setText(a
				.getAuthor());

		articleView.setClickable(true);
		// articleView.setId(a.getId());
		articleView.setId(articles_counter);

		articleView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), "View " + v.getId(),
						Toast.LENGTH_SHORT).show();
				single_article_intent.putExtra("article_index", v.getId());
				single_article_intent.putExtra("source_id", source_id);
				v.setBackgroundColor(Color.DKGRAY);
				startActivity(single_article_intent);
				v.setBackgroundColor(Color.WHITE);

			}
		});
		articles_counter++;
		articles_container.addView(articleView, 0);

	}

	private void showContentOrLoadingIndicator(boolean contentLoaded) {
		// Decide which view to hide and which to show.
		final View showView = contentLoaded ? aContentView : aLoadingView;
		final View hideView = contentLoaded ? aLoadingView : aContentView;

		// Set the "show" view to 0% opacity but visible, so that it is visible
		// (but fully transparent) during the animation.
		showView.setAlpha(0f);
		showView.setVisibility(View.VISIBLE);

		// Animate the "show" view to 100% opacity, and clear any animation
		// listener set on
		// the view. Remember that listeners are not limited to the specific
		// animation
		// describes in the chained method calls. Listeners are set on the
		// ViewPropertyAnimator object for the view, which persists across
		// several
		// animations.
		showView.animate().alpha(1f).setDuration(aShortAnimationDuration)
				.setListener(null);

		// Animate the "hide" view to 0% opacity. After the animation ends, set
		// its visibility
		// to GONE as an optimization step (it won't participate in layout
		// passes, etc.)
		hideView.animate().alpha(0f).setDuration(aShortAnimationDuration)
				.setListener(new AnimatorListenerAdapter() {
					@Override
					public void onAnimationEnd(Animator animation) {
						hideView.setVisibility(View.GONE);
					}
				});
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent intent = new Intent(this, FristActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
