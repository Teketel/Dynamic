package com.tsegaab.dynamic;

import java.io.File;
import java.util.ArrayList;

import com.tsegaab.dynamic.objects.Source;

import database.DbHandler;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SourcesFragment extends Fragment{

	public static String ARG_CATEG_ID = "source_id";
	private Intent articlesIntent;
	public SourcesFragment() {
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
        
        //int i = getArguments().getInt(ARG_CATEG_ID);
		articlesIntent = new Intent(Consts.context, ArticlesActivity.class);
		DbHandler dbhandler = new DbHandler(Consts.context);
		Consts.db = dbhandler;
        ArrayList<Source> sources = dbhandler.getAllSources();
        Source s;
        View sourcesView = inflater.inflate(R.layout.sources_layout, container, false);
        LinearLayout sources_container = (LinearLayout) sourcesView.findViewById(R.id.new_linear);
        
        for (int i = 0; i < sources.size(); i++) {
        	s = sources.get(i);
			View sourceView = inflater.inflate(R.layout.source_layout,
					container, false);
			TextView source_title = ((TextView) sourceView
					.findViewById(R.id.source_title));
			source_title.setText(s.getName());
			ImageView source_image = (ImageView) sourceView
					.findViewById(R.id.source_image);
			if (s.getImage_local_path() != null) {
				File imgFile = new File(s.getImage_local_path());
				if (imgFile.exists()) {
					Bitmap myBitmap = BitmapFactory.decodeFile(imgFile
							.getAbsolutePath());
					source_image.setImageBitmap(myBitmap);
				}
				//Bitmap bmp = BitmapFactory.decodeByteArray(s.getImage_byte(), 0, s.getImage_byte().length);

			}
			sourceView.setClickable(true);
			sourceView.setId(s.getId());
			sourceView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					v.setBackgroundColor(Color.GRAY);
					Toast.makeText(Consts.context, "View " + v.getId(),
							Toast.LENGTH_SHORT).show();
					articlesIntent.putExtra("source_id", v.getId());
					startActivity(articlesIntent);
				}
			});
			sources_container.addView(sourceView);
		}
		return sourcesView;
    }
}
