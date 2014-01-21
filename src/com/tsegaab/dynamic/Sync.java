package com.tsegaab.dynamic;

import database.DbHandler;
import server.ServerHandler;
import android.os.AsyncTask;

public class Sync extends AsyncTask<DbHandler, Void, Void>{

	private ServerHandler serverHandler;
	private DbHandler db;
	@Override
	protected Void doInBackground(DbHandler... args) {
		serverHandler = new ServerHandler();
		db = args[0];
		db.sourceListToDb(serverHandler.getSourcesIdGtz(db.getLatestSourceId()));
		db.articleListToDb(serverHandler.getArticlesIdGtz(db.getLatestArticleId()));
		return null;
	}

}
