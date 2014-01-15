package com.tsegaab.dynamic;

import java.io.IOException;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParserException;
import com.tsegaab.dynamic.objects.Source;
import com.tsegaab.dynamic.parser.ServerHandler;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private ServerHandler sHandler;
	private AsyncTask<Void, Void, Void> connectionTask;
	private ArrayList<Source> sources = null;
	private ViewGroup source_container;
	private Intent articles_activity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		/*
		 * first time = local recent source id = 0 if ( first time ) { fetch
		 * sources from uri save fetched sources to local file/db } else fetch
		 * sources from local add them to layout
		 */
		source_container = (ViewGroup) findViewById(R.id.source_container);
		articles_activity = new Intent(this, ArticlesActivity.class);

		connectionTask = new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				sHandler = new ServerHandler();
				try {
					sources = sHandler.getAllSources();
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
				showSources(sources);
				connectionTask = null;
			}

		};
		connectionTask.execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private void showSources(ArrayList<Source> sources) {
		if (sources != null) {
			for (int i = 0; i < sources.size(); i++) {
				addSource(sources.get(i));
			}
		} else {
			Log.e(Consts.EZ_TAG, "Source object is null");
		}
	}

	private void addSource(Source s) {
		// Instantiate a new "row" view.

		Log.d(Consts.Z_TAG, "addItem");
		final ViewGroup sourceView = (ViewGroup) LayoutInflater.from(this)
				.inflate(R.layout.source_layout, source_container, false);
		
		TextView source_title = ((TextView) sourceView
				.findViewById(R.id.source_title));
		/*Typeface Source_font = Typeface.createFromAsset(getAssets(),
				"fonts/NYALA.TTF");
		source_title.setTypeface(Source_font);*/
		source_title.setText(s.getName());
		ImageView source_image = (ImageView) sourceView
				.findViewById(R.id.source_image);
		Bitmap bmp = BitmapFactory.decodeByteArray(s.getImage_byte(), 0,
				s.getImage_byte().length);
		source_image.setImageBitmap(bmp);
		sourceView.setClickable(true);
		sourceView.setId(s.getId());
		sourceView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				 v.setBackgroundColor(Color.GRAY);
				Toast.makeText(getApplicationContext(), "View " + v.getId(),
						Toast.LENGTH_SHORT).show();
				articles_activity.putExtra("source_id", v.getId());
				startActivity(articles_activity);
			}
		});

		source_container.addView(sourceView, 0);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// showSources(sources);
	}
}
