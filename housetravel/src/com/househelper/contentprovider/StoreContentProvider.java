package com.househelper.contentprovider;

import java.util.Arrays;
import java.util.HashSet;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class StoreContentProvider extends ContentProvider {
	
	private static final UriMatcher URI_MATCHER;
	 private SQLiteDatabase db;
	 
	 
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
	
	private void checkColumns(String[] projection) {
	    String[] available = { Auth.ItemsColumn.COL_AGENT_NAME,
	        Auth.ItemsColumn.COL_NOTES,
	        Auth.ItemsColumn.COL_HOUSE_AREA,
	        Auth.ItemsColumn.COL_PRICE,
	        Auth.ItemsColumn.COL_USERNAME,
	        Auth.ItemsColumn.COL_SYNCED };
	    if (projection != null) {
	      HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));
	      HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(available));
	      // check if all columns which are requested are available
	      if (!availableColumns.containsAll(requestedColumns)) {
	        throw new IllegalArgumentException("Unknown columns in projection");
	      }
	    }
	  }

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		// TODO Auto-generated method stub
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

	    // check if the caller has requested a column which does not exists
	   // checkColumns(projection);

	    // Set the table
	    queryBuilder.setTables(DbHelper.TABLE_NAME);

	    int uriType = URI_MATCHER.match(uri);
	    switch (uriType) {
	    
	    case 1:
	      // adding the ID to the original query
	    //  queryBuilder.appendWhere(Auth.ItemsColumn.COL_ID  + "="
	    //      + uri.getLastPathSegment());
	      break;
	    default:
	      throw new IllegalArgumentException("Unknown URI: " + uri);
	    }

	    
	    Cursor cursor = queryBuilder.query(db, projection, selection,
	        selectionArgs, null, null, sortOrder);
	    // make sure that potential listeners are getting notified
	    cursor.setNotificationUri(getContext().getContentResolver(), uri);

	    return cursor;
	
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
		int uriType = URI_MATCHER.match(uri);
		long id = 0;
		switch (uriType) {
			case 1:
			 id = db.insert(DbHelper.TABLE_NAME, 
					null, values);
			break;
			default:
			 throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		return uri;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		int uriType = URI_MATCHER.match(uri);
		
		int rowsDeleted = 0;

		switch (uriType) {
		    case 1:
		      rowsDeleted = db.delete(DbHelper.TABLE_NAME,
	              selection,
		        selectionArgs);
		        break;
		      
		    default:
		      throw new IllegalArgumentException("Unknown URI: " + uri);
		    }
		    getContext().getContentResolver().notifyChange(uri, null);
		    return rowsDeleted;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		int uriType = URI_MATCHER.match(uri);
		
		int rowsUpdated = 0;

		switch (uriType) {
		  case 1:
		    rowsUpdated = db.update(DbHelper.TABLE_NAME, 
		        values, 
		        selection,
		        selectionArgs);
		    break;
		    
		  default:
		    throw new IllegalArgumentException("Unknown URI: " + uri);
		  }
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsUpdated;
		
	}
	
}
