package com.example.fueled_assignment_1;

import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

public class ReviewList extends ListActivity {
	private ReviewsDataSource datasource;
	// Place Details
	PlaceDetails placeDetails;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.review);
		Button b = (Button) findViewById(R.id.reviewbutton);
		datasource = new ReviewsDataSource(this);
		datasource.open();
		Intent i = getIntent();
		String x = "ourid";
		final String reference = i.getStringExtra(x);
		List<Review> values = datasource.getAllReviews(reference);

		// Use the SimpleCursorAdapter to show the
		// elements in a ListView
		ArrayAdapter<Review> adapter = new ArrayAdapter<Review>(this,
				android.R.layout.simple_list_item_1, values);
		setListAdapter(adapter);

		b.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				@SuppressWarnings("unchecked")
				ArrayAdapter<Review> adapter = (ArrayAdapter<Review>) getListAdapter();
				Review review = null;
				EditText et = (EditText) findViewById(R.id.reviewtext);
				String text = et.getText().toString();
				review = datasource.createReview(text, reference);
				adapter.add(review);
				adapter.notifyDataSetChanged();
				et.clearComposingText();
			}
		});
	}

	@Override
	protected void onResume() {
		datasource.open();
		super.onResume();
	}

	@Override
	protected void onPause() {
		datasource.close();
		super.onPause();
	}

}
