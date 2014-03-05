package server;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParserException;

import android.widget.Toast;

import com.tsegaab.dynamic.Consts;
import com.tsegaab.dynamic.objects.Article;
import com.tsegaab.dynamic.objects.Category;
import com.tsegaab.dynamic.objects.Source;
import com.tsegaab.dynamic.parser.Xml2Article;
import com.tsegaab.dynamic.parser.Xml2Source;
import com.tsegaab.dynamic.parser.Xml2Category;;

public class ServerHandler {

	private Xml2Article xml2Article;
	private Xml2Source xml2Source;
	private Xml2Category xml2Category;
	
	private String host = "http://10.0.0.49";

	public ServerHandler() {
		xml2Source = new Xml2Source();
		xml2Article = new Xml2Article();
		xml2Category = new Xml2Category();
	}

	public ServerHandler(String ip) {
		this.host = ip;
		xml2Source = new Xml2Source();
		xml2Article = new Xml2Article();
		xml2Category = new Xml2Category();
	}

	public ArrayList<Category> getCategoriesIdGtz(int id) {
		try {
			return xml2Category.parse(downloadUrl(host
					+ "/z_articles_handler/index.php/categories/idGtz/"
					+ String.valueOf(id) + "?format=xml"));
		} catch (XmlPullParserException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			Toast.makeText(Consts.context,"No Internet Coneectio", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
			return null;
		}
	}
	
	public ArrayList<Article> getAllArticlesWithSourceId(int id) {
		try {
			return xml2Article.parse(downloadUrl(host
					+ "/z_articles_handler/index.php/articles/withsourceid/"
					+ String.valueOf(id) + "?format=xml"));
		} catch (XmlPullParserException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			Toast.makeText(Consts.context,"No Internet Coneectio", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
			return null;
		}
	}

	public ArrayList<Article> getArticlesIdGtz(int id) {
		try {
			return xml2Article.parse(downloadUrl(host
					+ "/z_articles_handler/index.php/articles/idgtz/"
					+ String.valueOf(id) + "?format=xml"));
		} catch (XmlPullParserException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			Toast.makeText(Consts.context,"No Internet Coneectio", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
			return null;
		}
	}

	public ArrayList<Source> getAllSources() {
		try {
			return xml2Source
					.parse(downloadUrl(host
							+ "/z_articles_handler/index.php/sources/allsources/format/xml"));
		} catch (XmlPullParserException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public ArrayList<Source> getSourcesIdGtz(int id) {
		try {
			return xml2Source.parse(downloadUrl(host
					+ "/z_articles_handler/index.php/sources/idgtz/"
					+ String.valueOf(id) + "?format=xml"));
		} catch (XmlPullParserException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	private InputStream downloadUrl(String urlString) throws IOException {
		URL url = new URL(urlString);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setReadTimeout(7000 /* milliseconds */);
		conn.setConnectTimeout(5000 /* milliseconds */);
		conn.setRequestMethod("GET");
		conn.setDoInput(true);
		// Starts the query
		conn.connect();
		InputStream stream = conn.getInputStream();
		return stream;
	}

	public Article getArticlesWithId(int article_id)
			throws XmlPullParserException, IOException {
		return xml2Article.parse(
				downloadUrl(host
						+ "/z_articles_handler/index.php/articles/withid/"
						+ String.valueOf(article_id) + "?format=xml")).get(0);
	}
}
