package com.example.fueled_assignment_1;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleExpandableListAdapter;

public abstract class SwipeExtendedListViewActivity extends Activity {

	private ExpandableListView expList;
	private int REL_SWIPE_MIN_DISTANCE;
	private int REL_SWIPE_MAX_OFF_PATH;
	private int REL_SWIPE_THRESHOLD_VELOCITY;

	/**
	 * 
	 * @return ExpandableListView
	 */
	public abstract ExpandableListView getExpandableListView();

	/**
	 * 
	 * @param isRight
	 *            Swiping direction
	 * @param position
	 *            which item position is swiped
	 */
	public abstract void getSwipeItem(boolean isRight, int position);

	/**
	 * For single tap/Click
	 * 
	 * @param adapter
	 * @param position
	 */
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		DisplayMetrics dm = getResources().getDisplayMetrics();
		REL_SWIPE_MIN_DISTANCE = (int) (120.0f * dm.densityDpi / 160.0f + 0.5);
		REL_SWIPE_MAX_OFF_PATH = (int) (250.0f * dm.densityDpi / 160.0f + 0.5);
		REL_SWIPE_THRESHOLD_VELOCITY = (int) (200.0f * dm.densityDpi / 160.0f + 0.5);
	}

	@Override
	protected void onResume() {
		super.onResume();
		expList = getExpandableListView();
		if (expList == null) {
			new Throwable("ExpandableListview not set exception");
		}

		@SuppressWarnings("deprecation")
		final GestureDetector gestureDetector = new GestureDetector(
				new MyGestureDetector());

		View.OnTouchListener gestureListener = new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				
				return gestureDetector.onTouchEvent(event);
			}
		};
		
		expList.setOnTouchListener(gestureListener);

	}

	

	class MyGestureDetector extends SimpleOnGestureListener {

		private int temp_position = -1;

		// Detect a single-click and call my own handler.
		@Override
		public boolean onSingleTapUp(MotionEvent e) {

			
			return false;
		}

		@Override
		public boolean onDown(MotionEvent e) {

			temp_position = expList
					.pointToPosition((int) e.getX(), (int) e.getY());
			return super.onDown(e);
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			if (Math.abs(e1.getY() - e2.getY()) > REL_SWIPE_MAX_OFF_PATH)
				return false;
			if (e1.getX() - e2.getX() > REL_SWIPE_MIN_DISTANCE
					&& Math.abs(velocityX) > REL_SWIPE_THRESHOLD_VELOCITY) {

				int pos = expList
						.pointToPosition((int) e1.getX(), (int) e2.getY());

				if (pos >= 0 && temp_position == pos)
					getSwipeItem(false, pos);
			} else if (e2.getX() - e1.getX() > REL_SWIPE_MIN_DISTANCE
					&& Math.abs(velocityX) > REL_SWIPE_THRESHOLD_VELOCITY) {

				int pos = expList
						.pointToPosition((int) e1.getX(), (int) e2.getY());
				if (pos >= 0 && temp_position == pos)
					getSwipeItem(true, pos);

			}
			return false;
		}
		
		

	}
}
