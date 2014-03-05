package adapters;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import com.tsegaab.dynamic.Consts;
import com.tsegaab.dynamic.R;

import models.NavDrawerItem;
import models.NavDrawerListItem;
import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NavDrawerExpandableListAdapter extends BaseExpandableListAdapter {

	private Context context;
	private ArrayList<NavDrawerItem> _navDrawerItems;
	private HashMap<String, ArrayList<NavDrawerListItem>> _listDataChild;

	public NavDrawerExpandableListAdapter(Context context,
			ArrayList<NavDrawerItem> navDrawerItems,
			HashMap<String, ArrayList<NavDrawerListItem>> listDataChild) {
		this.context = context;
		this._navDrawerItems = navDrawerItems;
		this._listDataChild = listDataChild;
	}

	@Override
	public Object getChild(int groupPostion, int childPostion) {
		return this._listDataChild.get(
				this._navDrawerItems.get(groupPostion).getTitle()).get(
				childPostion);
	}

	@Override
	public long getChildId(int groupPostion, int childPostion) {
		return childPostion;
	}

	@Override
	public View getChildView(int groupPostion, int childPostion,
			boolean isLastChild, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater mInflater = (LayoutInflater) context
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(R.layout.drawer_list_item_child,
					null);
		}

		ImageView imgIcon = (ImageView) convertView
				.findViewById(R.id.nav_child_icon);
		TextView txtTitle = (TextView) convertView
				.findViewById(R.id.nav_child_title);
		TextView txtCount = (TextView) convertView
				.findViewById(R.id.nav_child_counter);
		NavDrawerListItem childNavListItem = (NavDrawerListItem) this.getChild(
				groupPostion, childPostion);
		if (childNavListItem.getIconPath() != null) {
			File imgFile = new File(childNavListItem.getIconPath());
			if (imgFile.exists()) {
				Bitmap myBitmap = BitmapFactory.decodeFile(imgFile
						.getAbsolutePath());
				imgIcon.setImageBitmap(myBitmap);
			}
		} else {
			imgIcon.setImageResource(childNavListItem.getIcon());
		}
		txtTitle.setText(childNavListItem.getTitle());

		// displaying count
		// check whether it set visible or not
		if (childNavListItem.getCounterVisibility()) {
			txtCount.setText(childNavListItem.getCount());
		} else {
			// hide the counter view
			txtCount.setVisibility(View.GONE);
		}

		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPostion) {
		return this._listDataChild.get(
				this._navDrawerItems.get(groupPostion).getTitle()).size();
	}

	@Override
	public Object getGroup(int groupPostion) {
		return this._navDrawerItems.get(groupPostion);
	}

	@Override
	public int getGroupCount() {
		return this._navDrawerItems.size();
	}

	@Override
	public long getGroupId(int groupPostion) {
		return groupPostion;
	}

	@Override
	public View getGroupView(int groupPostion, boolean isExpanded,
			View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater mInflater = (LayoutInflater) context
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(R.layout.drawer_list_item, null);
		}

		ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon);
		TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
		TextView txtCount = (TextView) convertView.findViewById(R.id.counter);
		NavDrawerItem parentNavListItem = (NavDrawerItem) this
				.getGroup(groupPostion);
		if (parentNavListItem.getIconPath() != null) {
			if (parentNavListItem.getIconPath().contains("local/")) {
				AssetManager assetManager = Consts.context.getAssets();
		        InputStream istr = null;
		        try {
		            istr = assetManager.open(parentNavListItem.getIconPath());
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
		        Bitmap bitmap = BitmapFactory.decodeStream(istr);
		        imgIcon.setImageBitmap(bitmap);
				
			} else {
			File imgFile = new File(parentNavListItem.getIconPath());
			if (imgFile.exists()) {
				Bitmap myBitmap = BitmapFactory.decodeFile(imgFile
						.getAbsolutePath());
				imgIcon.setImageBitmap(myBitmap);
			}}
		} else {
		imgIcon.setImageResource(parentNavListItem.getIcon());
		}
		txtTitle.setText(parentNavListItem.getTitle());

		// displaying count
		// check whether it set visible or not
		if (parentNavListItem.getCounterVisibility()) {
			txtCount.setText(parentNavListItem.getCount());
		} else {
			// hide the counter view
			txtCount.setVisibility(View.GONE);
		}

		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return true;
	}

}
