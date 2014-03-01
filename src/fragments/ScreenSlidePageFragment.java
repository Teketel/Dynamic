package fragments;


import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.tsegaab.dynamic.Consts;
import com.tsegaab.dynamic.R;
import com.tsegaab.dynamic.objects.Article;
 

@SuppressLint("ValidFragment")
public class ScreenSlidePageFragment extends Fragment {

	
	public static final String ARTICLE_ID = "ID";
	private int article_index;
	private Article article;
	private boolean actionBarShowed;
	
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

		//setHasOptionsMenu(true);
		
		actionBarShowed = true;
		ViewGroup articleView = (ViewGroup) inflater.inflate(
				R.layout.single_article, container, false);
		
		WebView article_full_content = ((WebView) articleView
				.findViewById(R.id.article_full_content));
		
		WebSettings settings = article_full_content.getSettings();
		settings.setAllowFileAccess(true);
		settings.setBuiltInZoomControls(true);
        settings.setLoadWithOverviewMode(true);
		settings.setDefaultTextEncodingName("utf-8");
		article_full_content.loadData(this.article.getStyled_FullContentWithTitleAndImage(), "text/html; charset=utf-8",
				"UTF-8");
		article_full_content.setClickable(true);
		article_full_content.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ActionBar a = getActivity().getActionBar();
				switchActionsBarShow(a);
				
			}
		});
		return articleView;
	}
	
	public static ScreenSlidePageFragment create(ArrayList<Article> articles, int pageNumber) {
        ScreenSlidePageFragment fragment = new ScreenSlidePageFragment(articles.get(pageNumber));
        Bundle args = new Bundle();
        args.putInt(ARTICLE_ID, pageNumber);
        fragment.setArguments(args);
        Log.d(Consts.XML_NAME_SPACE, "Position " + pageNumber);
        return  fragment;
    }
	
	public int getPageNumber() {
        return article_index;
    }
	
	private void switchActionsBarShow(ActionBar actionBar) {
		Log.d(Consts.Z_TAG, "switchActionsBarShow 222 from " + actionBarShowed);
		if (actionBarShowed) {
		actionBar.hide();
		actionBarShowed = false;
		} else {
			actionBar.show();
			actionBarShowed = true;
		}
		
	}
}
