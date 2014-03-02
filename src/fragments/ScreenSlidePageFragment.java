package fragments;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.tsegaab.dynamic.Consts;
import com.tsegaab.dynamic.MyScrollView;
import com.tsegaab.dynamic.MyScrollView.OnScrollViewListener;
import com.tsegaab.dynamic.R;
import com.tsegaab.dynamic.objects.Article;

@SuppressLint("ValidFragment")
public class ScreenSlidePageFragment extends Fragment implements
		Handler.Callback {

	private static final int CLICK_ON_WEBVIEW = 1;
	private static final int CLICK_ON_URL = 2;

	private final Handler handler = new Handler(this);
	public static final String ARTICLE_ID = "ID";
	private int article_index;
	private Article article;
	private boolean actionBarShowed;
	private ActionBar a;
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

		// setHasOptionsMenu(true);
		a = getActivity().getActionBar();
		actionBarShowed = true;
		ViewGroup articleView = (ViewGroup) inflater.inflate(
				R.layout.single_article, container, false);
		
		WebView article_full_content = ((WebView) articleView
				.findViewById(R.id.article_full_content));
		
		MyScrollView scroll = (MyScrollView) articleView.findViewById(R.id.single_article_scroll);
		WebSettings settings = article_full_content.getSettings();
		settings.setAllowFileAccess(true);
		settings.setBuiltInZoomControls(true);
		settings.setLoadWithOverviewMode(true);
		settings.setDefaultTextEncodingName("utf-8");
		article_full_content.loadData(
				this.article.getStyled_FullContentWithTitleAndImage(),
				"text/html; charset=utf-8", "UTF-8");
		scroll.setOnScrollViewListener(new OnScrollViewListener() {
			
			@Override
			public void onScrollChanged(MyScrollView v, int l, int t, int oldl, int oldt) {
				// TODO Auto-generated method stub
				int horxScChange = t - oldt;
				Log.d( "Scroller", "I changed!" + " L = " + l  + " T = " + t + " OL = " + oldl  + " OT = " + oldt );
				if(horxScChange > 10) 
					switchActionsBarShow(a, true);
				if(horxScChange < -15)
					switchActionsBarShow(a, false);
				if (l == 0)
					quit();
			}
		});
		/*article_full_content.setClickable(true);
		list.setClickable(true);
		list.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				switchActionsBarShow(a);				
			}
		});*/

		
		return articleView;
	}

	private void quit() {
		this.onDestroy();
		
	}

	public static ScreenSlidePageFragment create(ArrayList<Article> articles,
			int pageNumber) {
		ScreenSlidePageFragment fragment = new ScreenSlidePageFragment(
				articles.get(pageNumber));
		Bundle args = new Bundle();
		args.putInt(ARTICLE_ID, pageNumber);
		fragment.setArguments(args);
		Log.d(Consts.XML_NAME_SPACE, "Position " + pageNumber);
		return fragment;
	}

	public int getPageNumber() {
		return article_index;
	}


	private void switchActionsBarShow(ActionBar actionBar, boolean show) {
		Log.d(Consts.Z_TAG, "switchActionsBarShow 222 from " + actionBarShowed);
		if (show) {
			actionBar.hide();
			// actionBarShowed = false;
		} else {
			actionBar.show();
			// actionBarShowed = true;
		}

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

	@Override
	public boolean handleMessage(Message msg) {
		if (msg.what == CLICK_ON_URL) {
			handler.removeMessages(CLICK_ON_WEBVIEW);
			return true;
		}
		if (msg.what == CLICK_ON_WEBVIEW) {
			switchActionsBarShow(a);
			return true;
		}
		return false;
	}
}
