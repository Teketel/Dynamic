package com.tsegaab.dynamic;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

public class InvisibleActivity extends Activity{

	private int article_index;
	private Intent single_article_intent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loading_layout);
		
		Intent intent = getIntent();
		article_index = intent.getIntExtra("article_index", 0);
		single_article_intent = new Intent(this, SingleArticleActivity.class);
		single_article_intent.putExtra("article_index", article_index - 1);
		startActivity(single_article_intent);
		finish();
	}
}
