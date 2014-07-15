package com.househelper.contentprovider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class StoreContentProvider extends ContentProvider {
	
	private static final UriMatcher URI_MATCHER;
	 private SQLiteDatabase db;
	 public static final Uri CONTENT_URI = 
			 	         Uri.withAppendedPath(
			 	               Auth.CONTENT_URI,
			 	               "items");
	 
	// prepare the UriMatcher
	 static {
	 	   URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
	 	   URI_MATCHER.addURI(Auth.AUTHORITY,
	 	         "items",
	 	         1);

	 }

	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		Context context = getContext();
	      DbHelper dbHelper = new DbHelper(context);
	      /**
	       * Create a write able database which will trigger its 
	       * creation if it doesn't already exist.
	       */
	      db = dbHelper.getWritableDatabase();
	      return (db == null)? false:true;
		
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		switch (URI_MATCHER.match(uri)){
	     
	      case 1:
	         return "vnd.android.cursor.dir/vnd.example.items";
	      /** 
	       * Get a particular student
	       */
	     
	      default:
	         throw new IllegalArgumentException("Unsupported URI: " + uri);
	      }
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
