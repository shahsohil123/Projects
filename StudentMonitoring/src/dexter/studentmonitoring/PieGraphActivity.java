package dexter.studentmonitoring;
import android.app.Activity;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class PieGraphActivity extends Activity
{
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.piemain);
        ListView lv=(ListView) findViewById(R.id.listView1);
        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        //lv.setSelector(R.drawable.listcolour);
        OnItemClickListener myclick = new MyClick(this,lv);
        lv.setItemsCanFocus(true);
       //startActivity(pie.getIntent(this));
        ListAdapter ladapter=new MyListAdapter();
        lv.setAdapter(ladapter);        
        Button pending= (Button) findViewById(R.id.pending);
        pending.setText(" Pending Tests");
        Button average= (Button) findViewById(R.id.average);
        average.setText(" Average Marks");
        Button good= (Button) findViewById(R.id.good);
        good.setText(" Good Marks");
        Button poor= (Button) findViewById(R.id.poor);
        poor.setText(" Poor Marks");
        lv.setOnItemClickListener(myclick);
    }
    public void repeat(int position,SparseBooleanArray sp)
    {
        PieChart pie = new PieChart();
    	LinearLayout pieChart=(LinearLayout) findViewById(R.id.pie);
    	pieChart.removeAllViews();
    	pie.changePosi(position,sp);
        pieChart.addView(pie.getView(this));
    }
   
    public void pending(View view)
    {
    	Intent i = new Intent(this, Pending.class);
    	startActivity(i);
    	
    }
    public void average(View view)
    {
    	Intent i = new Intent(this, Average.class);
    	startActivity(i);
    	
    }
    public void poor(View view)
    {
    	Intent i = new Intent(this, Poor.class);
    	startActivity(i);
    	
    	
    }
    public void good(View view)
    {
    	Intent i = new Intent(this, Good.class);
    	startActivity(i);
    }
    
   
    class MyListAdapter implements ListAdapter
    {	
    	 String[] subjects={"English","Marathi","Hindi","Gujrati","French","German","Sanskrit"};
    	 
		public int getCount() {
			// TODO Auto-generated method stub
			return subjects.length;
		}

		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return subjects[position];
		}

		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return subjects[position].length();
		}

		public int getItemViewType(int position) {
			
			// TODO Auto-generated method stub
			return subjects[position].length();
			
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			 	AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
	            ViewGroup.LayoutParams.MATCH_PARENT,30);
	            TextView textView = new TextView(PieGraphActivity.this);
	            textView.setBackgroundColor(R.color.unt);
	            textView.setPadding(30, 5, 5, 5);
	            textView.setLayoutParams(lp);
	            textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
	            textView.setText(getItem(position).toString());
	            textView.setTextSize(15);
	            return textView;
		}

		public int getViewTypeCount() {
			// TODO Auto-generated method stub
			return 1;
		}

		public boolean hasStableIds() {
			// TODO Auto-generated method stub
			return true;
		}

		public boolean isEmpty() {
			// TODO Auto-generated method stub
			return false;
		}

		public void registerDataSetObserver(DataSetObserver observer) {
			// TODO Auto-generated method stub
			
		}

		public void unregisterDataSetObserver(DataSetObserver observer) {
			// TODO Auto-generated method stub
			
		}

		public boolean areAllItemsEnabled() {
			// TODO Auto-generated method stub
			return true;
		}

		public boolean isEnabled(int arg0) {
			// TODO Auto-generated method stub
			return true;
		}
   
    }
    
}