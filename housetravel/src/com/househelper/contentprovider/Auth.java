package com.househelper.contentprovider;

import android.net.Uri;

public class Auth {
	/**
	 * The authority of the lentitems provider.
	 */
	public static final String AUTHORITY = 
	      "com.househelper.contentprovier";
	/**
	 * The content URI for the top-level 
	 * lentitems authority.
	 */
	public static final Uri CONTENT_URI = 
	      Uri.parse("content://" + AUTHORITY);
	
	public static final Uri CONTENT_URI_TABLE = 
	         Uri.withAppendedPath(
	               Auth.CONTENT_URI,
	               "items");
	
	/**
	 * Constants for the Items table 
	 * of the lentitems provider.
	 */
	public static final class ItemsColumn 
	{ 
		public static final String COL_USERNAME= "UserName";
		public static final String COL_AGENT_NAME = "AgentName";
		public static final String COL_HOUSE_AREA = "HouseArea";
		public static final String COL_PRICE = "Price";
		public static final String COL_NOTES = "Notes";
		public static final String COL_SYNCED = "SyncStatus";
		public static final String COL_ID = "_id";
		public static final String COL_FOLDERNAME = "foldername";
		
	}
	
	
}
