package com.tsegaab.dynamic.parser;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.apache.http.util.ByteArrayBuffer;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.os.Environment;
import android.util.Log;
import android.util.Xml;

import com.tsegaab.dynamic.Consts;
import com.tsegaab.dynamic.objects.Article;

public class Rss2Article {
	
	public Rss2Article() {
	}

	public ArrayList<Article> parse(InputStream in)
			throws XmlPullParserException, IOException {
		try {
			Log.d(Consts.Z_TAG, "Rss Parssing .. " + in.toString());
			XmlPullParser parser = Xml.newPullParser();
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			parser.setInput(in, null);
			parser.nextTag();
			Log.d(Consts.Z_TAG, parser.toString());
			return getParsedArticles(parser);
		} finally {
			in.close();
		}
	}

	private ArrayList<Article> getParsedArticles(XmlPullParser parser)
			 {
		ArrayList<Article> Articlewoch = new ArrayList<Article>();
		try {
		parser.require(XmlPullParser.START_TAG, Consts.XML_NAME_SPACE, "xml");
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			Log.d(Consts.Z_TAG, "Frist tag name = " + name);
			// Starts by looking for the entry tag
			if (name.equals("item")) {
				Articlewoch.add(readSingleArticle(parser));
			} else {
				skip(parser);
			}
		}
		} catch (XmlPullParserException xe) {
			xe.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Articlewoch;
	}

	private Article readSingleArticle(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, Consts.XML_NAME_SPACE, "item");
		int id = 0;
		String title = null;
		String content = null;
		String full_content = null;
		String link = null;
		String author = null;
		String created_date = null;
		String image_link = null;
		int source_id = 0;
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String tag_name = parser.getName();
			if (tag_name.equals("id")) {
				Log.d(Consts.Z_TAG, "inside item id found");
				id = Integer.parseInt(readTagValue(parser, "id"));
			} else if (tag_name.equals("title")) {
                Log.d(Consts.Z_TAG, "inside item title found");
                title = readTagValue(parser, "title");
            } else if (tag_name.equals("content")) {
                Log.d(Consts.Z_TAG, "inside item content found");
                content = readTagValue(parser, "content");
            } else if (tag_name.equals("full_content")) {
                Log.d(Consts.Z_TAG, "inside item full_content found");
                full_content = readTagValue(parser, "full_content");
            } else if (tag_name.equals("author")) {
                Log.d(Consts.Z_TAG, "inside item author found");
                author = readTagValue(parser, "author");
            } else if (tag_name.equals("created_date")) {
                Log.d(Consts.Z_TAG, "inside item created_date found");
                created_date = readTagValue(parser, "created_date");
            } else if (tag_name.equals("image_link")) {
                Log.d(Consts.Z_TAG, "inside item image_link found");
                image_link = readTagValue(parser, "image_link");
            } else if (tag_name.equals("source_id")) {
                Log.d(Consts.Z_TAG, "inside item source_id found");
                source_id = Integer.parseInt(readTagValue(parser, "source_id"));
            } else {
				Log.d(Consts.Z_TAG, "inside item skiping");
				skip(parser);
			}
		}
		return new Article(id, title, content, full_content, link, author, created_date, image_link, source_id, downloadPicture(getImageByte(image_link), image_link));
	}

	// Processes title tags in the feed.
	private String readTagValue(XmlPullParser parser, String tag_name)
			throws IOException, XmlPullParserException {
		parser.require(XmlPullParser.START_TAG, Consts.XML_NAME_SPACE, tag_name);
		String value = readText(parser);
		parser.require(XmlPullParser.END_TAG, Consts.XML_NAME_SPACE, tag_name);
		return value;
	}

	// For the tags title and summary, extracts their text values.
	private String readText(XmlPullParser parser) throws IOException,
			XmlPullParserException {
		String result = "";
		if (parser.next() == XmlPullParser.TEXT) {
			result = parser.getText();
			parser.nextTag();
		}
		return result;
	}

	private void skip(XmlPullParser parser) throws XmlPullParserException,
			IOException {
		if (parser.getEventType() != XmlPullParser.START_TAG) {
			throw new IllegalStateException();
		}
		int depth = 1;
		while (depth != 0) {
			switch (parser.next()) {
			case XmlPullParser.END_TAG:
				depth--;
				break;
			case XmlPullParser.START_TAG:
				depth++;
				break;
			}
		}
	}
	
	
	private byte[] getImageByte(String url) {
		try {
			URL imageUrl = new URL(url);
			URLConnection ucon = imageUrl.openConnection();

			InputStream is = ucon.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is);

			ByteArrayBuffer baf = new ByteArrayBuffer(500);
			int current = 0;
			while ((current = bis.read()) != -1) {
				baf.append((byte) current);
			}

			return baf.toByteArray();
		} catch (Exception e) {
			Log.d("ImageManager", "Error: " + e.toString());
		}
		return null;
	}
	
	public String downloadPicture(byte[] image_byte, String image_link) {
		String path = null;
		int splitSize =image_link.split("/").length;
		File imageDir = new File(Environment.getExternalStorageDirectory() + "/com/tsegaab/dynamic/images/");
		if(!imageDir.exists()){
			imageDir.mkdirs();
			//Log.d(Consts.EZ_TAG, "Xmlparser Unable to create " + imageDir.getAbsolutePath().toString());
		}
		File photo = new File(imageDir.getAbsolutePath(), image_link.split("/")[splitSize - 1]);
		//photo.mkdirs();
		if (photo.exists()) {
			return photo.getPath();
		}

		try {
			FileOutputStream fos = new FileOutputStream(photo.getPath());
			if (image_byte != null) {
				fos.write(image_byte);
				path = photo.getPath();
			}
			fos.close();
			Log.d(Consts.Z_TAG, "Image path = " + path);
		} catch (java.io.IOException e) {
			Log.e(Consts.EZ_TAG, "Exception in photoCallback", e);
		}
		return path;

	}

}
