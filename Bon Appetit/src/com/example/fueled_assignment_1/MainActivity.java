package com.example.fueled_assignment_1;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

public class MainActivity extends SwipeExtendedListViewActivity {

	// flag for Internet connection status
	Boolean isInternetPresent = false;
	// Connection detector class
	ConnectionDetector cd;
	PlaceDetails placeDetails;
	// Alert Dialog Manager
	AlertDialogManager alert = new AlertDialogManager();

	// Google Places
	GooglePlaces googlePlaces;

	// Places List
	PlacesList nearPlaces;

	// GPS Location
	GPSTracker gps;

	// Button
	Button btnShowOnMap;

	// Progress dialog
	ProgressDialog pDialog;
	
	SimpleExpandableListAdapter expadap;
	
	// Places ExpandableListview
	ExpandableListView explist;

	ThumbsDownDataSource thumbs;
	
	// ListItems data
	public ArrayList<HashMap<String, String>> placesListItems = new ArrayList<HashMap<String, String>>();
	public ArrayList<ArrayList<HashMap<String, String>>> placesListItems1 = new ArrayList<ArrayList<HashMap<String, String>>>();

	int lastExpandedGroupPosition = 0;
	// KEY Strings
	public static String KEY_REFERENCE = "reference"; // id of the place
	public static String KEY_NAME = "name"; // name of the place
	public static String KEY_VICINITY = "vicinity"; // Place area name
	public int remind;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myexplistview);

		cd = new ConnectionDetector(getApplicationContext());

		// Check if Internet present
		isInternetPresent = cd.isConnectingToInternet();
		if (!isInternetPresent) {
			// Internet Connection is not present
			alert.showAlertDialog(MainActivity.this,
					"Internet Connection Error",
					"Please connect to working Internet connection", false);
			// stop executing code by return
			return;
		}

		// creating GPS Class object
		gps = new GPSTracker(this);

		// check if GPS location can get
		if (gps.canGetLocation()) {
			Log.d("Your Location", "latitude:" + gps.getLatitude()
					+ ", longitude: " + gps.getLongitude());
		} else {
			// Can't get user's current location
			alert.showAlertDialog(MainActivity.this, "GPS Status",
					"Couldn't get location information. Please enable GPS",
					false);
			return;
		}

		explist = (ExpandableListView) findViewById(R.id.expandableListView1);

		// button show on map
		btnShowOnMap = (Button) findViewById(R.id.btn_show_map);
		
		new LoadPlaces().execute();

		btnShowOnMap.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(getApplicationContext(),
						PlacesMapActivity.class);
				
				i.putExtra("user_latitude", Double.toString(gps.getLatitude()));
				i.putExtra("user_longitude",
						Double.toString(gps.getLongitude()));

				// passing near places to map activity
				i.putExtra("near_places", nearPlaces);
				// staring activity
				startActivity(i);
			}
		});
		thumbs = new ThumbsDownDataSource(this);
	}

	public void shareButton(View arg0) {
		// TODO Auto-generated method stub
		View v=(View) arg0.getParent().getParent();
		int p =explist.getPositionForView(v);
		Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
		sharingIntent.setType("text/plain");
		String name = placesListItems1.get(p-1).get(0).get("name");
		String address = placesListItems1.get(p-1).get(0).get("formattedaddress");
		String phone = placesListItems1.get(p-1).get(0).get("formattedphonenumber");
		name = name == null ? "Not present" : name; // if name is null display
													// as "Not present"
		address = address == null ? "Not present" : address;
		phone = phone == null ? "Not present" : phone;
		String sharebody = name + "\n" + address
				+ "\n" + phone;
		sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
				"Details of Restaurant");

		sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, sharebody);
		startActivity(Intent.createChooser(sharingIntent, "Share via"));
	}

	public void thumbsDown(View v) {
		View view=(View) v.getParent().getParent();
		int p =explist.getPositionForView(view);
		String name = placesListItems1.get(p-1).get(0).get("name");
		name = name.substring(7);
		Log.d("name", name);
		String latitude = placesListItems1.get(p-1).get(0).get("latitude");
		latitude = latitude.substring(11);
		Log.d("latitude", latitude);
		String longitude = placesListItems1.get(p-1).get(0).get("longitude");
		longitude = longitude.substring(12);
		Log.d("longitude", longitude);
		
		String ourid = name + latitude + longitude;
		Log.d("ourid", ourid);
		thumbs.open();
		thumbs.createThumbsDown(ourid);
		explist.collapseGroup(remind);
		placesListItems.remove(remind);
		placesListItems1.remove(remind);
		expadap.notifyDataSetChanged();

		thumbs.close();

	}


	class LoadPlaces extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(MainActivity.this);
			pDialog.setMessage(Html
					.fromHtml("<b>Search</b><br/>Loading Places..."));
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		protected String doInBackground(String... args) {
			// creating Places class object
			googlePlaces = new GooglePlaces();

			try {
				String types = "cafe|restaurant"; // Listing places only cafes,
													// restaurants

				double radius = 1000; // 1000 meters

				// get nearest places
				nearPlaces = googlePlaces.search(gps.getLatitude(),
						gps.getLongitude(), radius, types);

			} catch (Exception e) {
				e.printStackTrace();
			}

			thumbs.open();
			for (Place p : nearPlaces.results) {

				String ourid = p.name + p.geometry.location.lat
						+ p.geometry.location.lng;
				if (thumbs.compare(ourid)) {

					HashMap<String, String> map = new HashMap<String, String>();
					placesListItems.add(map);
					map.put(KEY_REFERENCE, p.reference);
					// Place name
					try {
						placeDetails = googlePlaces
								.getPlaceDetails(p.reference);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					map.put(KEY_NAME, p.name);
					map.put("ourid", ourid);
					ArrayList<HashMap<String, String>> map1 = new ArrayList<HashMap<String, String>>();
					for (int i = 0; i < 1; i++) {
						HashMap<String, String> map2 = new HashMap<String, String>();
						map1.add(map2);
						map2.put(KEY_NAME, "Name : " + p.name);
						map2.put("formattedaddress", "Address : "
								+ placeDetails.result.formatted_address);

						map2.put("formattedphonenumber", "Phone no. : "
								+ placeDetails.result.formatted_phone_number);

						map2.put(
								"latitude",
								"Latitude : "
										+ Double.toString(p.geometry.location.lat));
						map2.put(
								"longitude",
								"Longitude : "
										+ Double.toString(p.geometry.location.lng));
						map2.put("vicinity", placeDetails.result.vicinity);

					}
					placesListItems1.add(map1);
				}
			}
			thumbs.close();
			return null;
		}

		protected void onPostExecute(String file_url) {
			// dismiss the dialog after getting all products
			pDialog.dismiss();
			// updating UI from Background Thread
			runOnUiThread(new Runnable() {
				public void run() {
					/**
					 * Updating parsed Places into LISTVIEw
					 * */
					// Get json response status
					String status = nearPlaces.status;

					// Check for all possible status
					if (status.equals("OK")) {
						// Successfully got places details
						if (nearPlaces.results != null) {
							expadap = new SimpleExpandableListAdapter(
									MainActivity.this, placesListItems,
									R.layout.list_item, new String[] {
											KEY_REFERENCE, KEY_NAME, "ourid" },
									new int[] { R.id.reference, R.id.name1,
											R.id.ourid }, placesListItems1,
									R.layout.mychild, new String[] { KEY_NAME,
											"formattedaddress",
											"formattedphonenumber", "latitude",
											"longitude" }, new int[] {
											R.id.name, R.id.address,
											R.id.phone, R.id.latitude,
											R.id.longitude });

							explist.setAdapter(expadap);
							explist.setGroupIndicator(null);
							explist.setOnGroupExpandListener(new OnGroupExpandListener() {

								@Override
								public void onGroupExpand(int groupPosition) {
									//explist.expandGroup(groupPosition, true);
									
									if (groupPosition != lastExpandedGroupPosition) {
										explist.collapseGroup(lastExpandedGroupPosition);
									}
									lastExpandedGroupPosition = groupPosition;

									// TODO Auto-generated method stub

								}
							});
							explist.setOnGroupClickListener(new OnGroupClickListener() {

								@Override
								public boolean onGroupClick(
										ExpandableListView accordion, View v,
										int groupPosition, long arg3) {

									// TODO Auto-generated method stub
									remind = groupPosition;

									return false;
								}
							});

						}
					}

					else if (status.equals("ZERO_RESULTS")) {
						// Zero results found
						alert.showAlertDialog(
								MainActivity.this,
								"Near Places",
								"Sorry no places found. Try to change the types of places",
								false);
					} else if (status.equals("UNKNOWN_ERROR")) {
						alert.showAlertDialog(MainActivity.this,
								"Places Error", "Sorry unknown error occured.",
								false);
					} else if (status.equals("OVER_QUERY_LIMIT")) {
						alert.showAlertDialog(
								MainActivity.this,
								"Places Error",
								"Sorry query limit to google places is reached",
								false);
					} else if (status.equals("REQUEST_DENIED")) {
						alert.showAlertDialog(MainActivity.this,
								"Places Error",
								"Sorry error occured. Request is denied", false);
					} else if (status.equals("INVALID_REQUEST")) {
						alert.showAlertDialog(MainActivity.this,
								"Places Error",
								"Sorry error occured. Invalid Request", false);
					} else {
						alert.showAlertDialog(MainActivity.this,
								"Places Error", "Sorry error occured.", false);
					}
				}
			});

		}

	}

	@Override
	public ExpandableListView getExpandableListView() {
		// TODO Auto-generated method stub
		return explist;
	}

	@Override
	public void getSwipeItem(boolean isRight, int position) {

		for (int i = 0; i < position; i++) {
			if (explist.isGroupExpanded(i)) {
				position--;
				explist.collapseGroup(i);
			}

		}
		// TODO Auto-generated method stub

		if (!isRight) {
			Intent i = new Intent(this, ReviewList.class);
			i.putExtra("ourid", placesListItems.get(position).get("ourid"));
			startActivity(i);
		} else {
			Intent i = new Intent(this, SinglePlaceMapActivity.class);
			i.putExtra("user_latitude", Double.toString(gps.getLatitude()));
			i.putExtra("user_longitude", Double.toString(gps.getLongitude()));
			i.putExtra("latitude",
					placesListItems1.get(position).get(0).get("latitude"));
			i.putExtra("longitude",
					placesListItems1.get(position).get(0).get("longitude"));
			i.putExtra("name",
					placesListItems1.get(position).get(0).get(KEY_NAME));
			i.putExtra("vicinity",
					placesListItems1.get(position).get(0).get("vicinity"));

			startActivity(i);

		}
	}
	@Override
	protected void onResume() {
		
		super.onResume();
	}

	@Override
	protected void onPause() {
		
		super.onPause();
	}
}
