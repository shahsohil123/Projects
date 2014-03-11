package dexter.studentmonitoring;






import android.app.TabActivity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Color;

public class StudentMonitoringActivity extends TabActivity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Resources res = getResources();
		TabHost tabHost = getTabHost();
		TabHost.TabSpec spec;
		Intent intent;
		
		
		intent = new Intent().setClass(this, SyllabusStatusActivity.class);
		spec = tabHost.newTabSpec("SyllabusStatus").setIndicator("Syllabus Status",
                res.getDrawable(R.drawable.syllabus_tab))
            .setContent(intent);
		tabHost.addTab(spec);

		
		intent = new Intent().setClass(this, PatentActivity.class);
		spec = tabHost.newTabSpec("TestResults").setIndicator("Test Results",
                res.getDrawable(R.drawable.result_tab))
            .setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, PieSuccessActivity.class);
		spec = tabHost.newTabSpec("TestPerformance").setIndicator("Test PErformance",
                res.getDrawable(R.drawable.perf_tab))
            .setContent(intent);
		tabHost.addTab(spec);

		
		intent = new Intent().setClass(this, StudentLogsActivity.class);
		spec = tabHost.newTabSpec("ActivityLogs").setIndicator("Activity/Logs",
                res.getDrawable(R.drawable.activity_tab))
            .setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, DexSettings.class);
		spec = tabHost.newTabSpec("Settings").setIndicator("Settings",
                res.getDrawable(R.drawable.settings_tab))
            .setContent(intent);
		tabHost.addTab(spec);

		
		intent = new Intent().setClass(this, DexHelp.class);
		spec = tabHost.newTabSpec("Help").setIndicator("Help",
                res.getDrawable(R.drawable.help_tab))
            .setContent(intent);
		tabHost.addTab(spec);
		TabWidget tabs=(TabWidget) findViewById(android.R.id.tabs);
		for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
			tabHost.getTabWidget().getChildAt(i)
					.setBackgroundResource(R.drawable.ic_tab_back);
			final TextView tv = (TextView) tabs.getChildAt(i).findViewById(android.R.id.title);        
			tv.setTextColor(this.getResources().getColorStateList(android.R.color.background_dark));
		}
		
		tabHost.setCurrentTab(3);
		 ListView lv=(ListView)findViewById(R.id.listView1);
	        MyAdap x=new MyAdap();
	        lv.setAdapter(x);

	}
	class MyAdap implements ListAdapter
    {

    	String pointer[]={"Syllabus Completion","Concept Check Results","System Test Results","School Test Results","Schedule Following"};
    	int perc[]={39,59,74,80,90};
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return pointer.length;
		}

		@Override
		public Object getItem(int arg0) {
			return pointer[arg0];
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public int getItemViewType(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View arg1, ViewGroup arg2) {
			View row=null;
			if(row==null){
	          	  LayoutInflater inflater=getLayoutInflater();
	          	  row=inflater.inflate(R.layout.constantbar, arg2, false);
	          	 }
			ImageView img=(ImageView)row.findViewById(R.id.constbar);
			TextView tv1=(TextView)row.findViewById(R.id.PointerText);
			TextView tv2=(TextView) row.findViewById(R.id.PointerPerc);
			tv1.setText(getItem(position).toString()+": ");
			tv2.setText(getPercentage(position).toString()+"%");
			tv2.setTextColor(Color.argb(250, 0, 100, 0));
			//img.setPivotX(new Float(0));
            // img.setPivotY(new Float(0));
			int y=Integer.parseInt(getPercentage(position).toString());
			if(y<75)
			{
				tv2.setTextColor(Color.BLACK);
			}
			if(y<40)
			{
				tv2.setTextColor(Color.RED);
			}
			
			Float x=(float) (Integer.parseInt(getPercentage(position).toString()));
          	img.setScaleX(new Float(x/100));
          	 img.setPivotY(new Float(1.0));
          	// img.setScaleY(new Float(0.5));
			return row;
		}

		private Object getPercentage(int position) {
			
			return perc[position];
		}

		@Override
		public int getViewTypeCount() {
			// TODO Auto-generated method stub
			return 1;
		}

		@Override
		public boolean hasStableIds() {
			// TODO Auto-generated method stub
			return true;
		}

		@Override
		public boolean isEmpty() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void registerDataSetObserver(DataSetObserver arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void unregisterDataSetObserver(DataSetObserver arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean areAllItemsEnabled() {
			// TODO Auto-generated method stub
			return true;
		}

		@Override
		public boolean isEnabled(int arg0) {
			// TODO Auto-generated method stub
			return false;
		}
    	
    }
}