package com.tsegaab.dynamic.objects;

public class Source {

	private int id;
	private String name;
	private String link;
	private String image_link;
	private byte[] image_byte;
	private String image_local_path;
	private String category_id;
	
	
	
	public Source(int id, String name, String link, String image_link,
			byte[] image_byte) {

		this.id = id;
		this.name = name;
		this.link = link;
		this.image_link = image_link;
		this.image_byte = image_byte;
	}

	public Source(int id, String name, String link, String image_link, String image_local_path, String category_id) {

		this.id = id;
		this.name = name;
		this.link = link;
		this.image_link = image_link;
		this.setImage_local_path(image_local_path);
		this.category_id = category_id;
	}
	
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the link
	 */
	public String getLink() {
		return link;
	}

	/**
	 * @param link
	 *            the link to set
	 */
	public void setLink(String link) {
		this.link = link;
	}

	/**
	 * @return the image_link
	 */
	public String getImage_link() {
		return image_link;
	}

	/**
	 * @param image_link
	 *            the image_link to set
	 */
	public void setImage_link(String image_link) {
		this.image_link = image_link;
	}

	/**
	 * @return the image_byte
	 */
	public byte[] getImage_byte() {
		return image_byte;
	}

	/**
	 * @param image_byte
	 *            the image_byte to set
	 */
	public void setImage_byte(byte[] image_byte) {
		this.image_byte = image_byte;
	}

	public String getImage_local_path() {
		return image_local_path;
	}

	public void setImage_local_path(String image_local_path) {
		this.image_local_path = image_local_path;
	}

	public String getCategory_id() {
		return category_id;
	}

	public void setCategory_id(String category_id) {
		this.category_id = category_id;
	}
}