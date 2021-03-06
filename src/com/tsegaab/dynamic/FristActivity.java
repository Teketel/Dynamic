package com.tsegaab.dynamic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import server.ServerHandler;

import com.tsegaab.dynamic.bgs.Syncer;
import com.tsegaab.dynamic.objects.Category;
import com.tsegaab.dynamic.objects.Source;

import database.DbHandler;
import fragments.Articles2Category;
import fragments.Articles2Source;
import fragments.LoadingFragment;
import fragments.SourcesFragment;

import models.NavDrawerItem;
import models.NavDrawerListItem;
import adapters.NavDrawerExpandableListAdapter;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ListView;
import android.widget.Toast;

public class FristActivity extends Activity {

	private int lastExpandedPosition = -1;
	private DrawerLayout mDrawerLayout;
	// private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private Sync dataSyncer;

	// nav drawer title
	private CharSequence mDrawerTitle;

	// used to store app title
	private CharSequence mTitle;

	// slide menu items
	private List<String> navMenuTitles;
	private TypedArray navMenuIcons;

	private ArrayList<NavDrawerItem> navDrawerItems;
	private ArrayList<NavDrawerListItem> childnavDrawerListItems;
	private HashMap<String, ArrayList<NavDrawerListItem>> childHashMap;
	// private NavDrawerListAdapter adapter;
	private ExpandableListView expandableListView;
	private NavDrawerExpandableListAdapter expandableListViewAdapter;
	private int mShortAnimationDuration;
	private String[] childNavMenuTitles;
	private TypedArray childNavMenuIcons;
	protected boolean throughCode;
	private DbHandler db;
	private ArrayList<Category> categoriesFromDb;
	private int sent_parent_index;
	private int sent_child_index;
	private int p_i;
	private int c_i;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_frist);
		
		
		Consts.context = getApplicationContext();
		mTitle = mDrawerTitle = getTitle();

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		expandableListView = (ExpandableListView) findViewById(R.id.list_slider_menu);
		navMenuIcons = getResources().obtainTypedArray(
				R.array.nav_drawer_items_icon);
		navMenuTitles = Arrays.asList(getResources().getStringArray(
				R.array.nav_drawer_items));

		db = new DbHandler(getApplicationContext());
		Consts.db = db;

		mShortAnimationDuration = getResources().getInteger(
				android.R.integer.config_shortAnimTime);

		//startService(new Intent(this, Syncer.class));

		dataSyncer = new Sync();
		//dataSyncer.execute(new DbHandler(getApplicationContext()));
		
		categoriesFromDb = db.getAllCategories();

		if ((categoriesFromDb != null) && (categoriesFromDb.size() > 0)) {
			addDbCategoryToExpandableList(categoriesFromDb);
		} else {
			addResourceCategoryToExpandableList();
		}

		expandableListViewAdapter = new NavDrawerExpandableListAdapter(
				getApplicationContext(), navDrawerItems, childHashMap);
		expandableListView.setAdapter(expandableListViewAdapter);
		expandableListView
				.setOnGroupExpandListener(new OnGroupExpandListener() {

					@Override
					public void onGroupExpand(int groupPosition) {
						expandableListView.setItemChecked(groupPosition, true);
						if (lastExpandedPosition != -1
								&& groupPosition != lastExpandedPosition) {
							throughCode = true;
							expandableListView
									.collapseGroup(lastExpandedPosition);
						}
						lastExpandedPosition = groupPosition;
						Toast.makeText(getApplicationContext(),
								"" + groupPosition, Toast.LENGTH_SHORT).show();

					}
				});
		expandableListView
				.setOnGroupCollapseListener(new OnGroupCollapseListener() {

					@Override
					public void onGroupCollapse(int groupPosition) {
						if (!throughCode) {
							displayArticlesOfCategory(groupPosition);
						}
						throughCode = false;

					}
				});
		expandableListView.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				int childIndex = expandableListView
						.getFlatListPosition(ExpandableListView
								.getPackedPositionForChild(groupPosition,
										childPosition));
				expandableListView.setItemChecked(childIndex, true);
				displayArticlesOfCategoryAndSourceId(groupPosition, childPosition);
				Toast.makeText(getApplicationContext(), "" + childIndex,
						Toast.LENGTH_SHORT).show();
				c_i = childPosition;
				p_i = groupPosition;
				return true;
			}
		});

		/*
		 * mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
		 * 
		 * // setting the nav drawer list adapter adapter = new
		 * NavDrawerListAdapter(getApplicationContext(), navDrawerItems);
		 * mDrawerList.setAdapter(adapter);
		 */

		// enabling action bar app icon and behaving it as toggle button
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, // nav menu toggle icon
				R.string.app_name, // nav drawer open - description for
									// accessibility
				R.string.app_name // nav drawer close - description for
									// accessibility
		) 
		{
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				// calling onPrepareOptionsMenu() to show action bar icons
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				// calling onPrepareOptionsMenu() to hide action bar icons
				invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			displayArticlesOfCategory(0);
			
		} else {
			if(savedInstanceState.getInt("child_index") < 0)
			displayArticlesOfCategory(savedInstanceState.getInt("patent_index"));
			else
				displayArticlesOfCategoryAndSourceId(savedInstanceState.getInt("parent_index"), savedInstanceState.getInt("child_index"));
		}
	}
	
	private void refresh() {
		Intent i = new Intent(getApplicationContext(), InvisibleActivity.class);
		i.putExtra("caller_activity", 2);
		i.putExtra("parent_index", p_i);
		i.putExtra("child_index", c_i);
		startActivity(i);
		finish();
		
	}
	private void addDbCategoryToExpandableList(ArrayList<Category> categories) {
		Category c;
		Source s;
		ArrayList<Source> ss;
		navDrawerItems = new ArrayList<NavDrawerItem>();
		childHashMap = new HashMap<String, ArrayList<NavDrawerListItem>>();
		List<String> sNameList = new ArrayList<String>();
		for (int i = 0; i < categories.size(); i++) {
			childnavDrawerListItems = new ArrayList<NavDrawerListItem>();
			c = categories.get(i);
			navDrawerItems.add(new NavDrawerItem(c.getName(), navMenuIcons
					.getResourceId(0, -1), c.getImage_local_path()));
			ss = db.getSourcesWithCategId(c.getId());
			for (int j = 0; j < ss.size(); j++) {
				s = ss.get(j);
				childnavDrawerListItems.add(new NavDrawerListItem(s.getName(),
						navMenuIcons.getResourceId(1, -1), s
								.getImage_local_path()));
			}
			sNameList.add(c.getName());
			childHashMap.put(c.getName(), childnavDrawerListItems);
		}
		navMenuIcons.recycle();
		navMenuTitles = sNameList;
		// navMenuTitles = reverseOf(navMenuTitles);
	}

	private void addResourceCategoryToExpandableList() {

		childNavMenuTitles = getResources().getStringArray(
				R.array.nav_drawer_child_items);

		// nav drawer icons from resources
		childNavMenuIcons = getResources().obtainTypedArray(
				R.array.nav_drawer_child_items_icon);

		navDrawerItems = new ArrayList<NavDrawerItem>();
		childHashMap = new HashMap<String, ArrayList<NavDrawerListItem>>();
		childnavDrawerListItems = new ArrayList<NavDrawerListItem>();
		childnavDrawerListItems.add(new NavDrawerListItem(
				childNavMenuTitles[0], childNavMenuIcons.getResourceId(0, -1),
				null));
		childnavDrawerListItems.add(new NavDrawerListItem(
				childNavMenuTitles[1], childNavMenuIcons.getResourceId(1, -1),
				null));
		childnavDrawerListItems.add(new NavDrawerListItem(
				childNavMenuTitles[2], childNavMenuIcons.getResourceId(2, -1),
				null));
		childnavDrawerListItems.add(new NavDrawerListItem(
				childNavMenuTitles[3], childNavMenuIcons.getResourceId(3, -1),
				null));
		// adding nav drawer items to array
		// Home
		navDrawerItems.add(new NavDrawerItem(navMenuTitles.get(0), navMenuIcons
				.getResourceId(0, -1), null));
		// Find People
		navDrawerItems.add(new NavDrawerItem(navMenuTitles.get(1), navMenuIcons
				.getResourceId(1, -1), null));
		// Photos
		navDrawerItems.add(new NavDrawerItem(navMenuTitles.get(2), navMenuIcons
				.getResourceId(2, -1), null));
		// Communities, Will add a counter here
		navDrawerItems.add(new NavDrawerItem(navMenuTitles.get(3), navMenuIcons
				.getResourceId(3, -1), null, true, "22"));

		childHashMap.put(navDrawerItems.get(0).getTitle(),
				childnavDrawerListItems);
		childHashMap.put(navDrawerItems.get(1).getTitle(),
				childnavDrawerListItems);
		childHashMap.put(navDrawerItems.get(2).getTitle(),
				childnavDrawerListItems);
		childHashMap.put(navDrawerItems.get(3).getTitle(),
				childnavDrawerListItems);
		// Recycle the typed array
		navMenuIcons.recycle();

	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.articles_activity_actions, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		
		switch (item.getItemId()) {
		case R.id.articles_action_search:
			Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
			// openSearch();
			return true;
		case R.id.articles_action_refresh:
			Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
			refresh();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/***
	 * Called when invalidateOptionsMenu() is triggered
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// if nav drawer is opened, hide the action items
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(expandableListView);
		menu.findItem(R.id.articles_action_refresh).setVisible(!drawerOpen);
		menu.findItem(R.id.articles_action_search).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	private void displayArticlesOfCategoryAndSourceId(int groupPosition,
			int childPosition) {
		Fragment fragment = null;
		fragment = new Articles2Source();
		Bundle bundleArg = new Bundle();
		bundleArg.putInt("category_id", groupPosition);
		bundleArg.putInt("source_id", childPosition);
		fragment.setArguments(bundleArg);
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.frame_container, fragment).commit();

		// update selected item and title, then close the drawer
		expandableListView.setItemChecked(groupPosition, true);
		expandableListView.setSelection(groupPosition);
		setTitle(navMenuTitles.get(groupPosition));
		mDrawerLayout.closeDrawer(expandableListView);

	}

	private void displayArticlesOfCategory(int position) {
		// update the main content by replacing fragments
		Fragment fragment = null;
		fragment = new Articles2Category();
		Bundle bundleArg = new Bundle();
		bundleArg.putInt("category_id", position);
		fragment.setArguments(bundleArg);
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.frame_container, fragment).commit();

		// update selected item and title, then close the drawer
		// expandableListView.setItemChecked(position, true);
		// expandableListView.setSelection(position);
		setTitle(navMenuTitles.get(position));
		mDrawerLayout.closeDrawer(expandableListView);
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	/*
	 * private void showContentOrLoadingIndicator(boolean contentLoaded) { //
	 * Decide which view to hide and which to show. final View showView =
	 * contentLoaded ? mContentView : mLoadingView; final View hideView =
	 * contentLoaded ? mLoadingView : mContentView;
	 * 
	 * // Set the "show" view to 0% opacity but visible, so that it is visible
	 * // (but fully transparent) during the animation. showView.setAlpha(0f);
	 * showView.setVisibility(View.VISIBLE);
	 * 
	 * // Animate the "show" view to 100% opacity, and clear any animation //
	 * listener set on // the view. Remember that listeners are not limited to
	 * the specific // animation // describes in the chained method calls.
	 * Listeners are set on the // ViewPropertyAnimator object for the view,
	 * which persists across // several // animations.
	 * showView.animate().alpha(1f).setDuration(mShortAnimationDuration)
	 * .setListener(null);
	 * 
	 * // Animate the "hide" view to 0% opacity. After the animation ends, set
	 * // its visibility // to GONE as an optimization step (it won't
	 * participate in layout // passes, etc.)
	 * hideView.animate().alpha(0f).setDuration(mShortAnimationDuration)
	 * .setListener(new AnimatorListenerAdapter() {
	 * 
	 * @Override public void onAnimationEnd(Animator animation) {
	 * hideView.setVisibility(View.GONE); } }); }
	 */

	public class Sync extends AsyncTask<DbHandler, Void, Void> {

		private ServerHandler serverHandler;
		private DbHandler db;

		@Override
		protected Void doInBackground(DbHandler... args) {
			serverHandler = new ServerHandler();
			db = args[0];
			db.categoryListToDb(serverHandler.getCategoriesIdGtz(db
					.getLatestCategoryId()));
			db.sourceListToDb(serverHandler.getSourcesIdGtz(db
					.getLatestSourceId()));
			Consts.categsAndSourcesSynced = true;
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			LoadingFragment wf = new LoadingFragment();

			FragmentManager manager = FristActivity.this.getFragmentManager();
			FragmentTransaction trans = manager.beginTransaction();
			trans.replace(R.id.frame_container, wf);
			trans.commit();

		}
	}
}