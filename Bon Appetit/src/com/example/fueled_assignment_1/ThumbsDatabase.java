package com.example.fueled_assignment_1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ThumbsDatabase extends SQLiteOpenHelper {
	

	public static final String TABLE_THUMBS_DOWN = "thumbs";
	  public static final String COLUMN_THUMBS_DOWN= "thumbs_down";
	  
	  private static final String DATABASE_NAME = "thumbs.db";
	  
	  private static final String DATABASE_CREATE_TD = "create table "
			  + TABLE_THUMBS_DOWN + "(" + COLUMN_THUMBS_DOWN + " text not null);";
	  
	  private static final int DATABASE_VERSION = 1;
	  
	  public ThumbsDatabase(Context context) {
		    super(context, COLUMN_THUMBS_DOWN, null, DATABASE_VERSION);
		  }

	  @Override
		public void onCreate(SQLiteDatabase database) {
			// TODO Auto-generated method stub
			database.execSQL(DATABASE_CREATE_TD);
		}
	  
	  @Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			Log.w(Database.class.getName(),
			        "Upgrading database from version " + oldVersion + " to "
			            + newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_THUMBS_DOWN);
		    onCreate(db);
		}
}
