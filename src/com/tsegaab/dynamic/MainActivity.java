package com.tsegaab.dynamic;

import java.io.File;
import java.util.ArrayList;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tsegaab.dynamic.objects.Source;

import database.DbHandler;

public class MainActivity extends Activity {

	private Sync dataSyncer;
	private DbHandler dbHandler;
	private AsyncTask<Void, Void, Void> connectionTask;
	private ArrayList<Source> sources = null;
	private ViewGroup source_container;
	private Intent articles_activity;
	private View mContentView;
	private View mLoadingView;
	private int mShortAnimationDuration;

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
		mContentView = (View) findViewById(R.id.sources_scroll);
		mLoadingView = (View) findViewById(R.id.sources_loading_spinner);
		mContentView.setVisibility(View.GONE);
		mShortAnimationDuration = getResources().getInteger(android.R.integer.config_shortAnimTime);
		
		dataSyncer = new Sync();
		dbHandler = new DbHandler(getApplicationContext());
		Consts.db = dbHandler;
		dataSyncer.execute(new DbHandler(getApplicationContext()));
		connectionTask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... params) {
				
				sources = dbHandler.getAllSources();
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				showContentOrLoadingIndicator(true);
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
		if (s.getImage_local_path() != null) {
			File imgFile = new  File(s.getImage_local_path());
			if(imgFile.exists()){
			    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
			    source_image.setImageBitmap(myBitmap);
			}
			//Bitmap bmp = BitmapFactory.decodeByteArray(s.getImage_byte(), 0, s.getImage_byte().length);
			
		}
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
	
	private void showContentOrLoadingIndicator(boolean contentLoaded) {
        // Decide which view to hide and which to show.
        final View showView = contentLoaded ? mContentView : mLoadingView;
        final View hideView = contentLoaded ? mLoadingView : mContentView;

        // Set the "show" view to 0% opacity but visible, so that it is visible
        // (but fully transparent) during the animation.
        showView.setAlpha(0f);
        showView.setVisibility(View.VISIBLE);

        // Animate the "show" view to 100% opacity, and clear any animation listener set on
        // the view. Remember that listeners are not limited to the specific animation
        // describes in the chained method calls. Listeners are set on the
        // ViewPropertyAnimator object for the view, which persists across several
        // animations.
        showView.animate()
                .alpha(1f)
                .setDuration(mShortAnimationDuration)
                .setListener(null);

        // Animate the "hide" view to 0% opacity. After the animation ends, set its visibility
        // to GONE as an optimization step (it won't participate in layout passes, etc.)
        hideView.animate()
                .alpha(0f)
                .setDuration(mShortAnimationDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        hideView.setVisibility(View.GONE);
                    }
                });
    }
}
