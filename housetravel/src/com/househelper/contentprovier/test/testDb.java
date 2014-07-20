package com.househelper.contentprovier.test;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.househelper.contentprovider.Auth;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

public class testDb {
	Context context;
	public static final Uri LC_CONTENT_URI = 
	         Uri.withAppendedPath(
	               Auth.CONTENT_URI,
	               "items");
	private static final String TAG = "testdb";
	
	public testDb(Context cont) {
		context = cont;
		
	}
	
	public String getNewFolderName(int id) {
		String folderName = null;
		if (id == 0) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
			String currentDateandTime = sdf.format(new Date());
			folderName = "folder" + currentDateandTime;
		}
		return folderName;
		
	}
		
	
	public void insert() {
		ContentValues newValues = new ContentValues();

		// Sets the values of each column and inserts the word.
		newValues.put(Auth.ItemsColumn.COL_AGENT_NAME, "example.user");
		newValues.put(Auth.ItemsColumn.COL_HOUSE_AREA, 1000);
		newValues.put(Auth.ItemsColumn.COL_NOTES, "inserted into notes");
		newValues.put(Auth.ItemsColumn.COL_PRICE, 1000);
		newValues.put(Auth.ItemsColumn.COL_SYNCED, 0);
		newValues.put(Auth.ItemsColumn.COL_USERNAME, "tet");
		newValues.put(Auth.ItemsColumn.COL_FOLDERNAME, getNewFolderName(0));

		context.getContentResolver().insert(
		    LC_CONTENT_URI,   // the user dictionary content URI
		    newValues);  
	}
	
	public void getData() {
		
		ContentResolver resolver = context.getContentResolver();
		String[] projection = new String[]{Auth.ItemsColumn.COL_ID,
										Auth.ItemsColumn.COL_AGENT_NAME,
										Auth.ItemsColumn.COL_FOLDERNAME,
										Auth.ItemsColumn.COL_HOUSE_AREA,
										Auth.ItemsColumn.COL_NOTES,
										Auth.ItemsColumn.COL_PRICE,
										Auth.ItemsColumn.COL_SYNCED,
										Auth.ItemsColumn.COL_USERNAME};
		Cursor cursor = 
		      resolver.query(Auth.CONTENT_URI_TABLE, 
		            projection, 
		            null, 
		            null, 
		            null);
		if (cursor.moveToFirst()) {
		   do {
		      int id = (int) cursor.getInt(0);
		      String word = cursor.getString(1);
		      Log.v(TAG, "----" + id + "----agent"+word);
		      // do something meaningful
		   } while (cursor.moveToNext());
		}

	}
	
	
	public void updateData() {
		ContentResolver resolver = context.getContentResolver();
		String[] projection = new String[]{Auth.ItemsColumn.COL_ID,
										Auth.ItemsColumn.COL_AGENT_NAME,
										Auth.ItemsColumn.COL_FOLDERNAME,
										Auth.ItemsColumn.COL_HOUSE_AREA,
										Auth.ItemsColumn.COL_NOTES,
										Auth.ItemsColumn.COL_PRICE,
										Auth.ItemsColumn.COL_SYNCED,
										Auth.ItemsColumn.COL_USERNAME};
		Cursor cursor = 
		      resolver.query(Auth.CONTENT_URI_TABLE, 
		            projection, 
		            null, 
		            null, 
		            null);
		if (cursor.moveToFirst()) {
			
		   do {
		      int id = (int) cursor.getInt(0);
		      String word = cursor.getString(1);
		      Log.v(TAG, "----" + id + "----agent"+word);
		      ContentValues newValues = new ContentValues();

				// Sets the values of each column and inserts the word.
				newValues.put(Auth.ItemsColumn.COL_AGENT_NAME, "updated val");
				String[] args = {String.valueOf(id)};
		      resolver.update(Auth.CONTENT_URI_TABLE, newValues, Auth.ItemsColumn.COL_ID + "=?", args);
		      break;
		      // do something meaningful
		   } while (cursor.moveToNext());
		}

	
	}
}
