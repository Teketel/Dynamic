package database;

import java.util.ArrayList;

import com.tsegaab.dynamic.Consts;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.tsegaab.dynamic.objects.Source;
import com.tsegaab.dynamic.objects.Article;

;

public class DbHandler extends SQLiteOpenHelper {

	private String sourceTbName = "sources";
	private String articleTbName = "articles";

	private String createSourceTableQuery = "CREATE TABLE "
			+ sourceTbName
			+ " (id INTEGER, "
			+ "name  VARCHAR(200), link VARCHAR(400), image_link VARCHAR(400), image_local_path VARCHAR(400));";
	private String createArticlesTableQuery = "CREATE TABLE "
			+ articleTbName
			+ " (id INTEGER, "
			+ "title  VARCHAR(200), content TEXT, full_content MEDIUMTEXT, link VARCHAR(200), author VARCHAR(100),"
			+ " created_date VARCHAR(100), image_link VARCHAR(400), source_id INTEGER, image_local_path VARCHAR(400));";
	private static String dbName = "dynamic";

	public DbHandler(Context con) {
		super(con, dbName, null, 1);
		Log.d(Consts.Z_TAG, dbName + " CREATED.");
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		Log.d(Consts.Z_TAG, "Creating table...");
		db.execSQL(createSourceTableQuery);
		db.execSQL(createArticlesTableQuery);
		Log.d(Consts.Z_TAG, "onCreate methode of DBController"
				+ createSourceTableQuery + " AND " + createArticlesTableQuery);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int version_old, int version_new) {
		Log.d(Consts.Z_TAG, "Updating database...");
		db.execSQL("DROP TABLE IT EXISTS " + sourceTbName);
		db.execSQL("DROP TABLE IT EXISTS " + articleTbName);
		onCreate(db);
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
						c.getString(4));
				sourcesFromDb.add(s);
			} while (c.moveToNext());
		}
		return sourcesFromDb;
	}

	public void sourceListToDb(ArrayList<Source> sources) {

		Log.d(Consts.Z_TAG, "Adding Sources to database....");
		Source s;
		SQLiteDatabase db = getWritableDatabase();
		if (sources != null) {
			for (int i = 0; i < sources.size(); i++) {
				s = sources.get(i);
				String query = String
						.format("INSERT INTO %s(%s, %s, %s, %s, %s) VALUES('%s', '%s', '%s', '%s', '%s');",
								sourceTbName, "id", "name", "link",
								"image_link", "image_local_path", s.getId(),
								s.getName(), s.getLink(), s.getImage_link(),
								s.getImage_local_path());

				db.execSQL(query);
			}
		}
	}

	public ArrayList<Article> getArticlesWithSourceId(int s_id) {

		Log.d(Consts.Z_TAG, "getZennawochFromDbTable");
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

	public Source getSourceWithId(int s_id) {

		Source s = null;
		String query = "SELECT * FROM " + sourceTbName + " WHERE id = " + s_id;
		SQLiteDatabase db = this.getWritableDatabase();
		Log.d(Consts.Z_TAG, "Excuting .. " + query);
		Cursor c = db.rawQuery(query, null);
		if (c.moveToFirst()) {
			s = new Source(Integer.parseInt(c.getString(0)), c.getString(1),
					c.getString(2), c.getString(3), c.getString(4));
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
								"image_local_path", a.getId(), a.getTitle(),
								a.getContent(), a.getFull_content(),
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
}
