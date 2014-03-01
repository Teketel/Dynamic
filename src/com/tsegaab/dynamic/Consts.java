package com.tsegaab.dynamic;

import java.util.ArrayList;

import android.content.Context;

import com.tsegaab.dynamic.objects.Article;

import database.DbHandler;

public class Consts {

	public static final String Z_TAG = "Xml_Parser";
	public static final String EZ_TAG = "Error in Xml_Parser";
	public static final String XML_NAME_SPACE = null;
	public static DbHandler db = null;
	public static ArrayList<Article> current_articles = null;
	public static Context context = null;
	public static boolean categsAndSourcesSynced = false;
	
}
