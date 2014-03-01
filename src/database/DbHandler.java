package database;

import java.util.ArrayList;
import java.util.HashMap;

import com.tsegaab.dynamic.Consts;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.tsegaab.dynamic.objects.Category;
import com.tsegaab.dynamic.objects.Source;
import com.tsegaab.dynamic.objects.Article;


public class DbHandler extends SQLiteOpenHelper {

	private String sourceTbName = "sources";
	private String articleTbName = "articles";
	private String categoriesTbName = "categories";
	private String stylesTbName = "styles";
	
	private String createSourceTableQuery = "CREATE TABLE "
			+ sourceTbName
			+ " (id INTEGER, "
			+ "name  VARCHAR(200), link VARCHAR(400), image_link VARCHAR(400), image_local_path VARCHAR(400), category_id INT);";
	private String createArticlesTableQuery = "CREATE TABLE "
			+ articleTbName
			+ " (id INTEGER, "
			+ "title  VARCHAR(200), content TEXT, full_content MEDIUMTEXT, link VARCHAR(200), author VARCHAR(100),"
			+ " created_date VARCHAR(100), image_link VARCHAR(400), source_id INTEGER, image_local_path VARCHAR(400));";
	private String createCategoriesTableQuery = "CREATE TABLE "
			+ categoriesTbName
			+ " (id INTEGER, "
			+ "name  VARCHAR(200), image_local_path VARCHAR(200));";
	private String createStylesTableQuery = "CREATE TABLE "
			+ stylesTbName
			+ " (id INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ "name  VARCHAR(200), bg_color VARCHAR(20), text_color VARCHAR(20), font_size VARCHAR(20));";
	private  String insertStylesQuery = String
			.format("INSERT INTO %s( %s, %s, %s, %s) VALUES('%s', '%s', '%s', '%s');",
					stylesTbName, "name", "bg_color", "text_color", "font_size",
					"mode",
					"white",
					"black",
					"100");
	private static String dbName = "dynamic";

	public DbHandler(Context con) {
		super(con, dbName, null, 1);
		Log.d(Consts.Z_TAG, dbName + " CREATED.");
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		Log.d(Consts.Z_TAG, "Creating table...");
		db.execSQL(createCategoriesTableQuery);
		db.execSQL(createSourceTableQuery);
		db.execSQL(createArticlesTableQuery);
		db.execSQL(createStylesTableQuery);
		db.execSQL(insertStylesQuery);
		Log.d(Consts.Z_TAG, "onCreate methode of DBController"
				+ createSourceTableQuery + ", " + createArticlesTableQuery + createStylesTableQuery + " **AND** " + createCategoriesTableQuery);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int version_old, int version_new) {
		Log.d(Consts.Z_TAG, "Updating database...");
		db.execSQL("DROP TABLE IT EXISTS " + sourceTbName);
		db.execSQL("DROP TABLE IT EXISTS " + articleTbName);
		db.execSQL("DROP TABLE IT EXISTS " + categoriesTbName);
		db.execSQL("DROP TABLE IT EXISTS " + stylesTbName);
		onCreate(db);
	}

	public void updateStyle(String txt_color, String bg_color) {
		String updateStylesQuery = String
				.format("UPDATE %s SET bg_color = '%s', text_color = '%s' WHERE name ='%s';",
						stylesTbName, bg_color, txt_color, "mode");
		SQLiteDatabase db = this.getWritableDatabase();
		Log.d(Consts.Z_TAG, "Excuting .. " + updateStylesQuery);
		db.execSQL(updateStylesQuery);
	}
	public void updateStyle(String font_size) {
		String updateStylesQuery = String
				.format("UPDATE %s SET font_size = '%s' WHERE name = '%s';",
						stylesTbName, font_size, "mode");
		SQLiteDatabase db = this.getWritableDatabase();
		Log.d(Consts.Z_TAG, "Excuting .. " + updateStylesQuery);
		db.execSQL(updateStylesQuery);
	}
	public String getStyle() {
		String selectStylesQuery = String
				.format("SELECT * FROM '%s' WHERE name = '%s';",
						stylesTbName, "mode");
		SQLiteDatabase db = this.getWritableDatabase();
		Log.d(Consts.Z_TAG, "getStyle  Excuting .. " + selectStylesQuery);
		Cursor c = db.rawQuery(selectStylesQuery, null);
		String bg_color = "white";
		String text_color = "black";
		String font_size = "100";
		if (c.moveToFirst()) {
			bg_color = c.getString(2);
			text_color = c.getString(3);
			font_size = c.getString(4);
		}
		String style = "<!-- BEGIN main container -->" 
		+ "\n<html>" 
		+ "\n<head>"
		+ "\n	<style type=\"text/css\" scoped>" 
		+ "\n	body {\n "
		+ "\n   	font-family: Arial, Helvetica, sans-serif;"
		+ "\n   	font-size: 20px;" 
		+ "\n   	font-weight: normal;"
		+ "\n 		font-size: " + font_size + "%;"
		+ "\n   	line-height: 150%;" 
		+ "\n		background: " + bg_color + ";"
		+ "\n		padding: 3px 6px;\n"
		+ "\n		color: " + text_color + ";"
		+ "\n	}\n" 
		+ "\n	div { "
		+ "\n		word-wrap: break-word;" 
		+ "\n	}"
		+ "\n	</style>" 
		+ "\n</head>" ;
		Log.d(Consts.Z_TAG, bg_color + text_color + font_size);
		return style;
				
	}
	public ArrayList<Source> getAllSources() {

		Log.d(Consts.Z_TAG, "getting Sources from db. . . ");
		ArrayList<Source> sourcesFromDb = new ArrayList<Source>();
		Source s;
		String query = "SELECT * FROM " + sourceTbName;
		SQLiteDatabase db = this.getWritableDatabase();
		Log.d(Consts.Z_TAG, "Excuting .. " + query);
		Cursor c = db.rawQuery(query, null);
		Log.d(Consts.Z_TAG, "Ecuting: " + query);
		if (c.moveToFirst()) {
			do {
				s = new Source(Integer.parseInt(c.getString(0)),
						c.getString(1), c.getString(2), c.getString(3),
						c.getString(4), c.getString(5));
				sourcesFromDb.add(s);
			} while (c.moveToNext());
		}
		return sourcesFromDb;
	}
	
	public ArrayList<Source> getSourcesWithCategId(int id) {

		Log.d(Consts.Z_TAG, "getting Sources with CATEGORY ID from db. . . ");
		ArrayList<Source> sourcesFromDb = new ArrayList<Source>();
		Source s;
		String query = "SELECT * FROM " + sourceTbName + " WHERE category_id = " + id;
		SQLiteDatabase db = this.getWritableDatabase();
		Log.d(Consts.Z_TAG, "Excuting .. " + query);
		Cursor c = db.rawQuery(query, null);
				if (c.moveToFirst()) {
			do {
				s = new Source(Integer.parseInt(c.getString(0)),
						c.getString(1), c.getString(2), c.getString(3),
						c.getString(4), c.getString(5));
				sourcesFromDb.add(s);
			} while (c.moveToNext());
		}
		return sourcesFromDb;
	}
	
	public ArrayList<Category> getAllCategories() {

		Log.d(Consts.Z_TAG, "getting Categories from db. . . ");
		ArrayList<Category> categoriesFromDb = new ArrayList<Category>();
		Category categ;
		String query = "SELECT * FROM " + categoriesTbName;
		SQLiteDatabase db = this.getWritableDatabase();
		Log.d(Consts.Z_TAG, "Excuting .. " + query);
		Cursor c = db.rawQuery(query, null);
		Log.d(Consts.Z_TAG, "Ecuting: " + query);
		if (c.moveToFirst()) {
			do {
				categ = new Category(Integer.parseInt(c.getString(0)),
						c.getString(1), c.getString(2));
				categoriesFromDb.add(categ);
			} while (c.moveToNext());
		}
		return categoriesFromDb;
	}

	public void sourceListToDb(ArrayList<Source> sources) {

		Log.d(Consts.Z_TAG, "Adding Sources to database....");
		Source s;
		SQLiteDatabase db = getWritableDatabase();
		if (sources != null) {
			for (int i = 0; i < sources.size(); i++) {
				s = sources.get(i);
				String query = String
						.format("INSERT INTO %s(%s, %s, %s, %s, %s , %s) VALUES('%s', '%s', '%s', '%s', '%s', '%s');",
								sourceTbName, "id", "name", "link",
								"image_link", "image_local_path", "category_id", s.getId(),
								s.getName(), s.getLink(), s.getImage_link(),
								s.getImage_local_path(), s.getCategory_id());

				db.execSQL(query);
			}
		}
	}
	
	public void categoryListToDb(ArrayList<Category> categories) {

		Log.d(Consts.Z_TAG, "Adding categories to database....");
		Category c;
		SQLiteDatabase db = getWritableDatabase();
		if (categories != null) {
			for (int i = 0; i < categories.size(); i++) {
				c = categories.get(i);
				String query = String
						.format("INSERT INTO %s(%s, %s, %s) VALUES('%s', '%s', '%s');",
								categoriesTbName, "id", "name", "image_local_path", c.getId(),
								c.getName(), c.getImage_local_path());

				db.execSQL(query);
			}
		}
	}

	public ArrayList<Article> getArticlesWithSourceId(int s_id) {

		Log.d(Consts.Z_TAG, "getArticlesWithSourceId");
		ArrayList<Article> articelsFromDb = new ArrayList<Article>();
		Article a;
		String query = "SELECT * FROM " + articleTbName + " WHERE source_id = "
				+ s_id;
		SQLiteDatabase db = this.getWritableDatabase();
		Log.d(Consts.Z_TAG, "Excuting .. " + query);
		Cursor c = db.rawQuery(query, null);
		if (c.moveToFirst()) {
			do {
				a = new Article(Integer.parseInt(c.getString(0)),
						c.getString(1), c.getString(2), c.getString(3),
						c.getString(4), c.getString(5), c.getString(6),
						c.getString(7), Integer.parseInt(c.getString(8)),
						c.getString(9));
				articelsFromDb.add(a);
			} while (c.moveToNext());
		}
		return articelsFromDb;
	}
	
	public ArrayList<Article> getArticlesSummaryOfCategoryId(int c_id) {

		ArrayList<Article> categArticles = new  ArrayList<Article>();
		Log.d(Consts.Z_TAG, "getArticlesSummaryOfCategoryId = " + c_id);
		ArrayList<Integer> sourcesId = new ArrayList<Integer>();
		
		int s_id;
		
		String getSourcesQuery = "SELECT * FROM " + sourceTbName + " WHERE category_id = "
				+ c_id;
		SQLiteDatabase db = this.getWritableDatabase();
		Log.d(Consts.Z_TAG, "Excuting .. " + getSourcesQuery);
		Cursor getSourcesCursor = db.rawQuery(getSourcesQuery, null);
		if (getSourcesCursor.moveToFirst()) {
			do {
				s_id = Integer.parseInt(getSourcesCursor.getString(0));
				sourcesId.add(s_id);
			} while (getSourcesCursor.moveToNext());
		}
		
		for (int i = 0; i< sourcesId.size(); i++) {
			categArticles.addAll(this.getArticlesWithSourceId(sourcesId.get(i)));
		}
		return categArticles;
	}

	public Source getSourceWithId(int s_id) {

		Source s = null;
		String query = "SELECT * FROM " + sourceTbName + " WHERE id = " + s_id;
		SQLiteDatabase db = this.getWritableDatabase();
		Log.d(Consts.Z_TAG, "Excuting .. " + query);
		Cursor c = db.rawQuery(query, null);
		if (c.moveToFirst()) {
			s = new Source(Integer.parseInt(c.getString(0)), c.getString(1),
					c.getString(2), c.getString(3), c.getString(4), c.getString(5));
		}
		return s;
	}

	public Article getArticlesWithId(int a_id) {

		Article a = null;
		String query = "SELECT * FROM " + articleTbName + " WHERE id = " + a_id;
		SQLiteDatabase db = this.getWritableDatabase();
		Log.d(Consts.Z_TAG, "Excuting .. " + query);
		Cursor c = db.rawQuery(query, null);
		if (c.moveToFirst()) {
			a = new Article(Integer.parseInt(c.getString(0)), c.getString(1),
					c.getString(2), c.getString(3), c.getString(4),
					c.getString(5), c.getString(6), c.getString(7),
					Integer.parseInt(c.getString(8)), c.getString(9));
		}
		return a;
	}

	public void articleListToDb(ArrayList<Article> articles) {

		Log.d(Consts.Z_TAG, "Adding Articles to database....");
		Article a;
		SQLiteDatabase db = getWritableDatabase();
		if (articles != null) {
			for (int i = 0; i < articles.size(); i++) {
				a = articles.get(i);
				String query = String
						.format("INSERT INTO %s(%s, %s, %s, %s, %s, %s, %s, %s, %s, %s) VALUES('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s');",
								articleTbName, "id", "title", "content",
								"full_content", "link", "image_link", "author",
								"created_date", "source_id",
								"image_local_path", a.getId(), (a.getTitle().replace("\"", "")).replace("\'", ""),
								(a.getContent().replace("\"", "")).replace("\'", ""), (a.getFull_content().replace("\"", "")).replace("\'", ""),
								a.getLink(), a.getImage_link(), a.getAuthor(),
								a.getCreated_date(), a.getSource_id(),
								a.getImage_local_path());

				db.execSQL(query);
			}
		}
	}

	public int getLatestArticleId() {
		int id = 0;
		SQLiteDatabase db = getWritableDatabase();
		Cursor cursor = db.rawQuery("Select * FROM " + articleTbName, null);

		if (cursor != null && cursor.getCount() != 0) {
			cursor.moveToLast();
			id = cursor.getInt(0);
		}
		return id;
	}

	public int getLatestSourceId() {
		int id = 0;
		SQLiteDatabase db = getWritableDatabase();
		Cursor cursor = db.rawQuery("Select * FROM " + sourceTbName, null);
		if (cursor != null && cursor.getCount() != 0) {
			cursor.moveToLast();
			id = cursor.getInt(0);
		}
		return id;
	}
	
	public int getLatestCategoryId() {
		int id = 0;
		SQLiteDatabase db = getWritableDatabase();
		Cursor cursor = db.rawQuery("Select * FROM " + categoriesTbName, null);
		if (cursor != null && cursor.getCount() != 0) {
			cursor.moveToLast();
			id = cursor.getInt(0);
		}
		return id;
	}

	public int getFontSize() {
		int f_size = 100;
		SQLiteDatabase db = getWritableDatabase();
		String query = "Select * FROM " + stylesTbName;
		Log.d(Consts.Z_TAG, "Excuting ...." + query);
		Cursor cursor = db.rawQuery(query, null);
		if (cursor.moveToFirst()) {
			f_size = Integer.parseInt(cursor.getString(4));
		}
		Log.d(Consts.Z_TAG, "" + f_size);
		return f_size;
	}

	public String getBgColor() {
		String bg = "white";
		SQLiteDatabase db = getWritableDatabase();
		String query = "Select * FROM " + stylesTbName;
		Log.d(Consts.Z_TAG, "Excuting ...." + query);
		Cursor cursor = db.rawQuery(query, null);
		if (cursor.moveToFirst()) {
			bg = cursor.getString(2);
		}
		Log.d(Consts.Z_TAG, bg);
		return bg;
	}
}
