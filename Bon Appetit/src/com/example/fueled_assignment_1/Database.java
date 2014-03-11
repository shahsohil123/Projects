package com.example.fueled_assignment_1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Database extends SQLiteOpenHelper {

	public static final String TABLE_REVIEWS = "reviews";

	  public static final String COLUMN_ID = "_id";
	  public static final String COLUMN_REVIEW = "REVIEW";
	
	  private static final String DATABASE_NAME = "reviews.db";
	  private static final int DATABASE_VERSION = 1;
	  
	  private static final String DATABASE_CREATE = "create table "
		      + TABLE_REVIEWS + "(" + COLUMN_ID
		      + " text not null, " + COLUMN_REVIEW
		      + " text not null);";
	  
	

	  public Database(Context context) {
		    super(context, DATABASE_NAME, null, DATABASE_VERSION);
		  }

	@Override
	public void onCreate(SQLiteDatabase database) {
		// TODO Auto-generated method stub
		database.execSQL(DATABASE_CREATE);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		Log.w(Database.class.getName(),
		        "Upgrading database from version " + oldVersion + " to "
		            + newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_REVIEWS);
	    onCreate(db);
	}
}
