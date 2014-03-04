package fragments;


import java.io.File;
import java.util.ArrayList;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tsegaab.dynamic.Consts;
import com.tsegaab.dynamic.R;
import com.tsegaab.dynamic.SingleArticleActivity;
import com.tsegaab.dynamic.objects.Article;
import database.DbHandler;

public class Articles2Source extends Fragment {

	public static String ARG_CATEG_ID = "category_id";
	public static String ARG_SOURCE_ID = "source_id";
	private int c_id;
	private int articles_counter;
	private int s_id;

	public Articles2Source() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		articles_counter = 0;
		setHasOptionsMenu(true);
		int ci = getArguments().getInt(ARG_CATEG_ID);
		int si = getArguments().getInt(ARG_SOURCE_ID);
		c_id = ci + 1;
		s_id = si;
		//singleArticleIntent = new Intent();
		DbHandler dbhandler = new DbHandler(Consts.context);
		Consts.db = dbhandler;
		Log.d(Consts.Z_TAG, "Articles tobe fetched under Categ = " + c_id);
		Article a;
		ArrayList<Article> articles = dbhandler
				.getArticlesSummaryIndexInCategoryId(s_id, c_id);

		View articles2category = inflater.inflate(R.layout.articles2category,
				container, false);
		ViewGroup articles_container = (ViewGroup) articles2category
				.findViewById(R.id.articles2category_container);

		for (int j = 0; j < articles.size(); j++) {
			Log.d(Consts.Z_TAG, "Articles2Source --- Adding article with id = " + j);
			a = articles.get(j);
			View articleView = inflater
					.inflate(R.layout.article2category_layout,
							articles_container, false);
			TextView article_title = ((TextView) articleView
					.findViewById(R.id.article2category_title));
			article_title.setText(a.getTitle());
			ImageView article_image = (ImageView) articleView
					.findViewById(R.id.article2category_image);
			if (a.getImage_local_path() != null) {
				File imgFile = new File(a.getImage_local_path());
				if (imgFile.exists()) {
					Bitmap myBitmap = BitmapFactory.decodeFile(imgFile
							.getAbsolutePath());
					article_image.setImageBitmap(myBitmap);
				}
				// Bitmap bmp =
				// BitmapFactory.decodeByteArray(s.getImage_byte(), 0,
				// s.getImage_byte().length);

			}
			WebView article_content = ((WebView) articleView
					.findViewById(R.id.article2category_highlite));
			WebSettings settings = article_content.getSettings();
			settings.setDefaultTextEncodingName("utf-8");
			article_content.loadData(a.getStyled_Content(),
					"text/html; charset=utf-8", "UTF-8");

			((TextView) articleView.findViewById(R.id.article2category_author))
					.setText(a.getAuthor());

			articleView.setClickable(true);
			articleView.setId(articles_counter);
			articleView.setOnClickListener(new OnClickListener() {

				private Intent single_article_intent = new Intent(getActivity().getApplicationContext(), SingleArticleActivity.class);

				@Override
				public void onClick(View v) {
					v.setBackgroundColor(Color.DKGRAY);
					Toast.makeText(getActivity().getApplicationContext(), "View " + v.getId(),
							Toast.LENGTH_SHORT).show();
					single_article_intent.putExtra("article_index", v.getId());					
					startActivity(single_article_intent);
					v.setBackgroundColor(Color.WHITE);

				}
			});

			articles_container.addView(articleView);
			articles_counter++;
		}
		Consts.current_articles = articles;
		return articles2category;

	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.articles_activity_actions, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.articles_action_search:
			Toast.makeText(getActivity().getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
			// openSearch();
			return true;
		case R.id.articles_action_refresh:
			Toast.makeText(getActivity().getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
			// refreshView();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
