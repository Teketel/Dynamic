package com.tsegaab.dynamic.bgs;

import server.ServerHandler;
import database.DbHandler;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

public class Syncer extends Service {

	private DbHandler db;
	private ServerHandler serverHandler;

	public Syncer() {

	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO: Return the communication channel to the service.
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public void onCreate() {
		Toast.makeText(this, "The new Service was Created", Toast.LENGTH_LONG)
				.show();
		db = new DbHandler(getApplicationContext());
		serverHandler = new ServerHandler();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// For time consuming an long tasks you can launch a new thread here...

		Toast.makeText(this, " Service Started", Toast.LENGTH_LONG).show();
		Runnable r = new Runnable() {
			public void run() {
				//db.articleListToDb(serverHandler.getArticlesIdGtz(db						.getLatestArticleId()));
				stopSelf();
			}
		};
		Thread t = new Thread(r);
		t.start();
		

	}

	@Override
	public void onDestroy() {
		Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();

	}

}
