package com.example.fueled_assignment_1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ThumbsDownDataSource {
	 private SQLiteDatabase database;
	 private ThumbsDatabase dbHelper;
	 
	 private String[] allColumns = { ThumbsDatabase.COLUMN_THUMBS_DOWN};
	 
	 public ThumbsDownDataSource(Context context) {
		    dbHelper = new ThumbsDatabase(context);
		  }
	 
	 public void open() throws SQLException {
		    database = dbHelper.getWritableDatabase();
		  }
	 
	 public void close() {
		    dbHelper.close();
		  }
	 
	 public void createThumbsDown(String reference) {
		 reference=reference.replace("'", " ");
		    ContentValues values = new ContentValues();
		    values.put(ThumbsDatabase.COLUMN_THUMBS_DOWN, reference);
		    database.insert(ThumbsDatabase.TABLE_THUMBS_DOWN, null, values);
		  }
	 public boolean compare(String reference)
	 {
		 reference=reference.replace("'", " ");
		 Cursor cursor = database.query(ThumbsDatabase.TABLE_THUMBS_DOWN,
			        allColumns, ThumbsDatabase.COLUMN_THUMBS_DOWN + " = '" + reference+"'", null,
			        null, null, null);
		 cursor.moveToFirst();
		 if((cursor.moveToFirst()))
		 { cursor.close();
				 return false;}
		 cursor.close();
		return true;
	 }
	 
}
