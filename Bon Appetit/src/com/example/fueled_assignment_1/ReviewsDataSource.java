package com.example.fueled_assignment_1;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ReviewsDataSource {
	 private SQLiteDatabase database;
	 private Database dbHelper;
	 
	 private String[] allColumns = { Database.COLUMN_ID,
		      Database.COLUMN_REVIEW };
	 
	 
	 public ReviewsDataSource(Context context) {
		    dbHelper = new Database(context);
		  }
	 
	 public void open() throws SQLException {
		    database = dbHelper.getWritableDatabase();
		  }
	 
	 public void close() {
		    dbHelper.close();
		  }
	 public Review createReview(String review,String reference) {
		 	review = review.replace("'", " ");
		 	reference = reference.replace("'", " ");
		    ContentValues values = new ContentValues();
		    values.put(Database.COLUMN_REVIEW, review);
		    values.put(Database.COLUMN_ID, reference);
		    database.insert(Database.TABLE_REVIEWS, null, values);
		    Cursor cursor = database.query(Database.TABLE_REVIEWS,
		        allColumns, Database.COLUMN_ID + " = '" + reference+"'", null,
		        null, null, null);
		   
		    cursor.moveToLast();
		    Review newReview = cursorToReview(cursor);
		   
		    cursor.close();
		    return newReview;
		  }

	 public void deleteReview(Review comment) {
		    String id = comment.getId();
		    System.out.println("Comment deleted with id: " + id);
		    database.delete(Database.TABLE_REVIEWS, Database.COLUMN_ID
		        + " = " + id, null);
		  }
	 public List<Review> getAllReviews(String reference) {
		 	reference = reference.replace("'", " ");
		    List<Review> comments = new ArrayList<Review>();
		    Cursor cursor = database.query(Database.TABLE_REVIEWS,
			        allColumns, Database.COLUMN_ID + " = '" + reference+"'", null,
			        null, null, null);
		    cursor.moveToFirst();
		    while (!cursor.isAfterLast())
		    {
		      Review review = cursorToReview(cursor);
		      comments.add(review);
		      cursor.moveToNext();
		    }
		    cursor.close();
		    return comments;
		    
	 
	 }


	 
	 
	private Review cursorToReview(Cursor cursor) {
		// TODO Auto-generated method stub
		Review review = new Review();
	    review.setId(cursor.getString(0));
	    review.setReview(cursor.getString(1));
	    return review;
	}

}
