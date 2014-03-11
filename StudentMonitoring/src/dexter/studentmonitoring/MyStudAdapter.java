package dexter.studentmonitoring;

import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class MyStudAdapter extends BaseExpandableListAdapter {
	StudentLogsActivity sla;
	String groups[]={"Suggestions based on current performance","Student has made following mods in the calendar","Other logs on students activities"};
	String children[][]={{"The left fragment consists of an main activity named MainActivity.java. Within the main activity tabhost will be created and corresponding tabs will be made. each tab will consist of an activity with different class for each of them. The details are given below."},{""},{" "}};
	String column1[]={"1","2","3","4"};
	String column2[]={"ppt 1","ppt 2","ppt 3","ppt 4"};
	String column3[]={"item 1 hello","item 2 bonjour","item 3 namaste","item 4 f_off"};
	String column4[]={"ppt 1 eng","ppt 2 french","ppt 3 hindi ","ppt 4 slang"};
	String column5[]={"item 1","item 2","item 3","item 4"};
	String row21[]={"this is just to test the activity 1","this is just to test the activity 2","this is just to test the activity 3","this is just to test the activity 4"};
	String row22[]={"This is the students log 1","This is the students log 2","This is the students log 3","This is the students log 4"};
	public MyStudAdapter(StudentLogsActivity studentLogsActivity) {
		// TODO Auto-generated constructor stub
		sla=studentLogsActivity;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return children[groupPosition][childPosition];
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}


	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LinearLayout ll_container = new LinearLayout(sla);
		ll_container.setBackgroundColor(Color.WHITE);
		LinearLayout.LayoutParams lp5 = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		lp5.setMargins(10,10,10,10);
		LinearLayout sv = new LinearLayout(sla);
		sv.setBackgroundColor(Color.argb(255, 236,236,236));
		sv.setPadding(5,5,5,5);
		LinearLayout lv =new LinearLayout(sla);
		lv.setBackgroundColor(Color.WHITE);
		lv.setLayoutParams(lp5);
		
		sv.addView(lv);
		ll_container.addView(sv);
		if(groupPosition==1)
		{
		
			
			LinearLayout ll = new LinearLayout(sla);
			ll.setOrientation(LinearLayout.VERTICAL);
			ll.setPadding(10,10,10,10);
			
			lv.addView(ll);
			for(int j=0;j<4;j++)
			{	
				
				LinearLayout ll1 = new LinearLayout(sla);
				ll1.setOrientation(LinearLayout.HORIZONTAL);
				
				if(j%2==0)
					ll1.setBackgroundColor(Color.argb(255, 244, 244, 244));
				if(j==0)
					ll1.setBackgroundColor(Color.argb(255, 213,235,248));
				ll.setFadingEdgeLength(100);
				ll.addView(ll1);
					LayoutParams lp = new LayoutParams(300, ViewGroup.LayoutParams.WRAP_CONTENT);
					LayoutParams lp2 = new LayoutParams(80, ViewGroup.LayoutParams.WRAP_CONTENT);
					TextView tv1 = new TextView(sla);
					tv1.setText(column1[j]);
					tv1.setLayoutParams(lp2);
					tv1.setPadding(10, 10, 10, 10);
					ll1.addView(tv1);
					
					TextView tv2 = new TextView(sla);
					tv2.setText(column2[j]);
					tv2.setPadding(80, 10, 80, 10);
					tv2.setLayoutParams(lp);
					ll1.addView(tv2);
					
					TextView tv3 = new TextView(sla);
					tv3.setText(column3[j]);
					tv3.setPadding(80, 10, 80, 10);
					tv3.setLayoutParams(lp);
					ll1.addView(tv3);
					
					TextView tv4 = new TextView(sla);
					tv4.setText(column4[j]);
					tv4.setPadding(80, 10, 80, 10);
					tv4.setLayoutParams(lp);
					ll1.addView(tv4);
					
					
					TextView tv5 = new TextView(sla);
					tv5.setText(column5[j]);
					tv5.setPadding(80, 10, 80, 10);
					tv5.setLayoutParams(lp);
					ll1.addView(tv5);
			}
			
			
		
		
		}
		
		
		else if(groupPosition==0)
		{
			
			AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
	                ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
			 TextView textView = new TextView(sla);
	        textView.setLayoutParams(lp);
	        textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
	        textView.setPadding(48,20, 0, 0);
	        textView.setText(getChild(groupPosition, childPosition).toString());
	        textView.setLineSpacing(20, 1);
	       
	       return textView;
		}
		else
		{	
			
			
			LinearLayout ll = new LinearLayout(sla);
			ll.setOrientation(LinearLayout.VERTICAL);
			ll.setPadding(10,10,10,10);
			lv.addView(ll);
			for(int j=0;j<4;j++)
			{	
				
				LinearLayout ll1 = new LinearLayout(sla);
				if(j%2==0)
					ll1.setBackgroundColor(Color.argb(255, 244, 244, 244));
				if(j==0)
					ll1.setBackgroundColor(Color.argb(255, 213,235,248));
				ll1.setOrientation(LinearLayout.HORIZONTAL);
				
				ll.addView(ll1);
					LayoutParams lp = new LayoutParams(500, ViewGroup.LayoutParams.WRAP_CONTENT);
					TextView tv1 = new TextView(sla);
					tv1.setText(row21[j]);
					tv1.setLayoutParams(lp);
					tv1.setPadding(40, 10, 10, 10);
					ll1.addView(tv1);
					
					TextView tv2 = new TextView(sla);
					tv2.setText(row22[j]);
					tv2.setPadding(40, 10, 10, 10);
					tv2.setLayoutParams(lp);
					tv2.setGravity(1);
					ll1.addView(tv2);
					
				
			}
		
		}
		 
		return ll_container; 
		
		
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return children[groupPosition].length;
	}

	@Override
	public long getCombinedChildId(long groupId, long childId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getCombinedGroupId(long groupId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return groups[groupPosition];
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return groups.length;
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}


	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 48);
		 TextView textView = new TextView(sla);
        textView.setLayoutParams(lp);
        textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        textView.setPadding(48, 0, 0, 0);
        textView.setText(getGroup(groupPosition).toString());
        textView.setBackgroundResource(R.drawable.group_back_final);
       
       return textView;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

}
