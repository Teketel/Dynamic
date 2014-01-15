package com.tsegaab.dynamic.objects;

public class Article {

	private int id;
	private String title;
	private String content;
	private String link;
	private String author;
	private String created_date;
	private String image_link;
	private int source_id;
	private byte[] image_byte;
	
	
	public Article(int id, String title, String content, String link,
			String author, String created_date, String image_link, int source_id, byte[] image_byte) {
		this.id = id;
		this.title = title;
		this.content = content;
		this.link = link;
		this.author = author;
		this.created_date = created_date;
		this.image_link = image_link;
		this.source_id = source_id;
		this.image_byte = image_byte;

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
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content
	 *            the content to set
	 */
	public void setContent(String content) {
		this.content = content;
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
	 * @return the created_date
	 */
	public String getCreated_date() {
		return created_date;
	}

	/**
	 * @param created_date
	 *            the created_date to set
	 */
	public void setCreated_date(String created_date) {
		this.created_date = created_date;
	}

	/**
	 * @return the author
	 */
	public String getAuthor() {
		return author;
	}

	/**
	 * @param author
	 *            the author to set
	 */
	public void setAuthor(String author) {
		this.author = author;
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
	 * @return the source_id
	 */
	public int getSource_id() {
		return source_id;
	}

	/**
	 * @param source_id
	 *            the source_id to set
	 */
	public void setSource_id(int source_id) {
		this.source_id = source_id;
	}

	/**
	 * @return the image_byte
	 */
	public byte[] getImage_byte() {
		return image_byte;
	}

	/**
	 * @param image_byte the image_byte to set
	 */
	public void setImage_byte(byte[] image_byte) {
		this.image_byte = image_byte;
	}

}
