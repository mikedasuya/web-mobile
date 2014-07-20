package com.househelper.contentprovider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
	
	   public static final String DATABASE_NAME = "records";
	   public static final String TABLE_NAME = "items";
	   public static final int DATABASE_VERSION = 1;
	   static final String CREATE_DB_TABLE = 
	      " CREATE TABLE " + TABLE_NAME +
	      " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " + 
	      Auth.ItemsColumn.COL_USERNAME + " TEXT NOT NULL," +		  
	      Auth.ItemsColumn.COL_AGENT_NAME +"  TEXT NOT NULL, " +
	      Auth.ItemsColumn.COL_HOUSE_AREA +"  INTEGER NOT NULL," +
	      Auth.ItemsColumn.COL_PRICE + "  INTEGER NOT NULL," +
	      Auth.ItemsColumn.COL_NOTES + " TEXT,"+
	      Auth.ItemsColumn.COL_FOLDERNAME + " TEXT," +
	      Auth.ItemsColumn.COL_SYNCED + "  INTGER NOT NULL);";
	   
	   
	   public DbHelper(Context context){
		   super(context, DATABASE_NAME, null, DATABASE_VERSION);
	   }
	   
	   public DbHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	   }
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		  db.execSQL(CREATE_DB_TABLE);
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS " +  TABLE_NAME);
        onCreate(db);
	}
 
}
