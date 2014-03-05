package com.tsegaab.dynamic;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

public class InvisibleActivity extends Activity{

	private int article_index;
	private Intent single_article_intent;
	private int caller_activity;
	private int c_i;
	private int p_i;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loading_layout);
		
		Intent intent = getIntent();
		article_index = intent.getIntExtra("article_index", 0);
		caller_activity = intent.getIntExtra("caller_activity", 0);
		c_i = intent.getIntExtra("child_index", -1);
		p_i = intent.getIntExtra("parent", 0);
		caller_activity = intent.getIntExtra("caller_activity", 0);
		single_article_intent = new Intent(this, SingleArticleActivity.class);
		Log.d(Consts.Z_TAG, "CALLER = " + caller_activity);
		if (caller_activity != 0) {
			single_article_intent = new Intent(this, FristActivity.class);
			single_article_intent.putExtra("child_index", c_i);
			single_article_intent.putExtra("parent_index", c_i);
		}
		single_article_intent.putExtra("article_index", article_index - 1);
		startActivity(single_article_intent);
		finish();
	}
}
