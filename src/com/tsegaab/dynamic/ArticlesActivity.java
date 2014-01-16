package com.tsegaab.dynamic;

import java.io.IOException;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParserException;

import com.tsegaab.dynamic.objects.Article;
import com.tsegaab.dynamic.parser.ServerHandler;

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
	private ServerHandler sHandler;
	private AsyncTask<Void, Void, Void> connectionTask;
	private ArrayList<Article> articles = null;
	private int source_id;
	private Intent single_article_intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.articles_layout);

		single_article_intent = new Intent(this, SingleArticle.class);
		articles_container = (ViewGroup) findViewById(R.id.articles_container);
		Intent intent = getIntent();
		source_id = intent.getIntExtra("source_id", 0);
		connectionTask = new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				sHandler = new ServerHandler();
				try {
					articles = sHandler.getAllArticlesWithSourceId(source_id);
				} catch (XmlPullParserException e) {
					Log.e(Consts.EZ_TAG, e.toString());
					e.printStackTrace();
				} catch (IOException e) {
					Log.e(Consts.EZ_TAG, e.toString());
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
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
		if (a.getImage_byte() != null) {
			Bitmap bmp = BitmapFactory.decodeByteArray(a.getImage_byte(), 0,
					a.getImage_byte().length);
			article_image.setImageBitmap(bmp);
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
		articleView.setId(a.getId());

		articleView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), "View " + v.getId(),
						Toast.LENGTH_SHORT).show();
				single_article_intent.putExtra("article_id", v.getId());
				v.setBackgroundColor(Color.DKGRAY);
				startActivity(single_article_intent);

			}
		});

		articles_container.addView(articleView, 0);

	}
}
