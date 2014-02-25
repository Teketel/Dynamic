package junck;

import java.io.File;

import server.ServerHandler;

import com.tsegaab.dynamic.Consts;
import com.tsegaab.dynamic.R;
import com.tsegaab.dynamic.R.id;
import com.tsegaab.dynamic.R.layout;
import com.tsegaab.dynamic.objects.Article;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

public class SingleArticle extends Activity {

	private ViewGroup single_article_container;
	private ServerHandler ssHandler;
	private AsyncTask<Void, Void, Void> sconnectionTask;
	private Article article = null;
	private int article_id;
	private View sContentView;
	private View sLoadingView;
	private int sShortAnimationDuration;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.single_article);

		single_article_container = (ViewGroup) findViewById(R.id.single_article);
		Intent intent = getIntent();
		article_id = intent.getIntExtra("article_id", 0);
		
		
		ActionBar actionBar = getActionBar();
	    actionBar.setHomeButtonEnabled(true);
	    
	    
		sContentView = (View) findViewById(R.id.single_article);
		
		sContentView.setVisibility(View.GONE);
		sShortAnimationDuration = getResources().getInteger(android.R.integer.config_shortAnimTime);
		
		
		sconnectionTask = new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				ssHandler = new ServerHandler();
				article = Consts.db.getArticlesWithId(article_id) ;
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				showContentOrLoadingIndicator(true);
				showArticle(article);
				sconnectionTask = null;
			}

		};
		sconnectionTask.execute();
	}

	private void showArticle(Article article) {
		if (article != null)
			addArticle(article);
		else
			Log.e(Consts.EZ_TAG, "Article object is null");
	}

	private void addArticle(Article a) {

		Log.d(Consts.Z_TAG, "addItem");
		final ViewGroup articleView = (ViewGroup) LayoutInflater.from(this)
				.inflate(R.layout.single_article, single_article_container, false);

		TextView article_title = ((TextView) articleView
				.findViewById(R.id.sigle_article_title));
		Typeface article_title_font = Typeface.createFromAsset(getAssets(),
				"fonts/NYALA.TTF");
		article_title.setTypeface(article_title_font);
		article_title.setText(a.getTitle());

		ImageView article_image = (ImageView) articleView
				.findViewById(R.id.single_article_image);
		if (a.getImage_local_path() != null) {
			File imgFile = new  File(a.getImage_local_path());
			if(imgFile.exists()){
			    Bitmap bmp = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
			    article_image.setImageBitmap(bmp);
			}
			
		}
		
		
		WebView article_full_content = ((WebView) articleView
				.findViewById(R.id.article_full_content));
		WebSettings settings = article_full_content.getSettings();
		settings.setDefaultTextEncodingName("utf-8");
		article_full_content.loadData(a.getStyled_FullContent(), "text/html; charset=utf-8",
				"UTF-8");
		single_article_container.addView(articleView, 0);

	}
	
	private void showContentOrLoadingIndicator(boolean contentLoaded) {
        // Decide which view to hide and which to show.
        final View showView = contentLoaded ? sContentView : sLoadingView;
        final View hideView = contentLoaded ? sLoadingView : sContentView;

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
                .setDuration(sShortAnimationDuration)
                .setListener(null);

        // Animate the "hide" view to 0% opacity. After the animation ends, set its visibility
        // to GONE as an optimization step (it won't participate in layout passes, etc.)
        hideView.animate()
                .alpha(0f)
                .setDuration(sShortAnimationDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        hideView.setVisibility(View.GONE);
                    }
                });
    }
	
}
