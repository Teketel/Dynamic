package com.tsegaab.dynamic.objects;

public class Category {
	
	private int id;
	private String name;
	private String image_local_path;
	
	public Category(int id, String name) {
		this.id = id; this.name = name;
	}
	public Category(int id, String name, String image_local_path) {
		this.id = id; this.name = name; this.image_local_path = image_local_path;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getImage_local_path() {
		return image_local_path;
	}
	public void setImage_local_path(String image_local_path) {
		this.image_local_path = image_local_path;
	}

}
