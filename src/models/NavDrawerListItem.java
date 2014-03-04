package models;

public class NavDrawerListItem {
	
	private String title;
	private int icon;
	private String iconPath;
	private String count = "0";
	// boolean to set visiblity of the counter
	private boolean isCounterVisible = false;

	public NavDrawerListItem() {
	}

	public NavDrawerListItem(String title, int icon, String iconPath) {
		this.title = title;
		this.icon = icon;
		this.iconPath = iconPath;
	}

	public NavDrawerListItem(String title, int icon, String iconPath, boolean isCounterVisible,
			String count) {
		this.title = title;
		this.icon = icon;
		this.iconPath = iconPath;
		this.isCounterVisible = isCounterVisible;
		this.count = count;
	}

	public String getTitle() {
		return this.title;
	}

	public int getIcon() {
		return this.icon;
	}

	public String getCount() {
		return this.count;
	}

	public boolean getCounterVisibility() {
		return this.isCounterVisible;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setIcon(int icon) {
		this.icon = icon;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public void setCounterVisibility(boolean isCounterVisible) {
		this.isCounterVisible = isCounterVisible;
	}

	public String getIconPath() {
		return iconPath;
	}

	public void setIconPath(String iconPath) {
		this.iconPath = iconPath;
	}

}
