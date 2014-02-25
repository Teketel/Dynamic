package com.tsegaab.dynamic;


import java.io.File;
import java.util.ArrayList;

import com.tsegaab.dynamic.objects.Article;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
 

@SuppressLint("ValidFragment")
public class ScreenSlidePageFragment extends Fragment {

	
	public static final String ARTICLE_ID = "ID";
	private int article_index;
	private Article article;
	
	public ScreenSlidePageFragment() {
		
	}
	public ScreenSlidePageFragment(Article article) {
		this.article = article;
    }
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        article_index = getArguments().getInt(ARTICLE_ID);
    }
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup articleView = (ViewGroup) inflater.inflate(
				R.layout.single_article, container, false);
		TextView article_title = ((TextView) articleView
				.findViewById(R.id.sigle_article_title));
		Typeface article_title_font = Typeface.createFromAsset(getActivity().getAssets(),
				"fonts/DroidSerif-Bold.ttf");
		article_title.setTypeface(article_title_font);
		article_title.setText(this.article.getTitle());

		ImageView article_image = (ImageView) articleView
				.findViewById(R.id.single_article_image);
		if (this.article.getImage_local_path() != null) {
			File imgFile = new  File(this.article.getImage_local_path());
			if(imgFile.exists()){
			    Bitmap bmp = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
			    article_image.setImageBitmap(bmp);
			}
			
		}
		
		
		WebView article_full_content = ((WebView) articleView
				.findViewById(R.id.article_full_content));
		WebSettings settings = article_full_content.getSettings();
		settings.setDefaultTextEncodingName("utf-8");
		article_full_content.loadData(this.article.getStyled_FullContent(), "text/html; charset=utf-8",
				"UTF-8");

		return articleView;
	}
	
	public static ScreenSlidePageFragment create(ArrayList<Article> articles, int pageNumber) {
        ScreenSlidePageFragment fragment = new ScreenSlidePageFragment(articles.get(articles.size() - (pageNumber + 1)));
        Bundle args = new Bundle();
        args.putInt(ARTICLE_ID, pageNumber);
        fragment.setArguments(args);
        Log.d(Consts.XML_NAME_SPACE, "Position " + pageNumber);
        return  fragment;
    }
	
	public int getPageNumber() {
        return article_index;
    }
}
