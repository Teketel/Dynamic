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
	private String full_content;
	private String image_local_path;

	public Article(int id, String title, String content, String full_content,
			String link, String author, String created_date, String image_link,
			int source_id, byte[] image_byte) {
		this.id = id;
		this.title = title;
		this.content = content;
		this.link = link;
		this.author = author;
		this.created_date = created_date;
		this.image_link = image_link;
		this.source_id = source_id;
		this.image_byte = image_byte;
		this.full_content = full_content;

	}

	public Article(int id, String title, String content, String full_content,
			String link, String author, String created_date, String image_link,
			int source_id, String image_local_path) {
		this.id = id;
		this.title = title;
		this.content = content;
		this.link = link;
		this.author = author;
		this.created_date = created_date;
		this.image_link = image_link;
		this.source_id = source_id;
		this.full_content = full_content;
		this.setImage_local_path(image_local_path);

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
	 * @param image_byte
	 *            the image_byte to set
	 */
	public void setImage_byte(byte[] image_byte) {
		this.image_byte = image_byte;
	}

	public String getFull_content() {
		return full_content;
	}

	public void setFull_content(String full_content) {
		this.full_content = full_content;
	}

	public String getStyled_FullContent() {
		String awapped = "<!-- BEGIN main container -->" + "\n<html>\n<head>"
				+ "\n<style type=\"text/css\" scoped>\n" + "" + "\nbody {\n "
				+ "\n    font-family: Arial, Helvetica, sans-serif;\n"
				+ "\n    font-size: 15px;\n" + "\n   font-weight: normal;\n"
				+ "\n    font-variant: small-caps;\n"
				+ "\n    line-height: 150%;\n" + "\n}\n" + "\ndiv {    \n"
				+ "\nbackground: rgb(240, 246, 255);\n" + "\nfont-family: monospace;\n"
				+ "\nfont-size: 18px;\n" + "\npadding: 5px 9px;\n"
				+ "\nword-wrap: break-word;\n" + "\n}\n"
				+ "\n</style>\n</head>\n" + "\n<body>\n"
				+ this.getFull_content() + "\n</body>\n" + "\n</html>\n"
				+ "<!-- END main container -->";
		return awapped;
	}

	public String getImage_local_path() {
		return image_local_path;
	}

	public void setImage_local_path(String image_local_path) {
		this.image_local_path = image_local_path;
	}
}
