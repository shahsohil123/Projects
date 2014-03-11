package dexter.studentmonitoring;

import android.R.color;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SsAdapter implements ExpandableListAdapter {
public String groups[]={"For Upcoming Test : Term Test II","For Semester II"};
public String children[][]={{"Total","English","History","Geography","Marathi"},{"Total","English","History","Geography","Marathi"}};
public int childpercent[][]={{30,50,60,40,80},{35,50,65,45,100}};
SyllabusStatusActivity ssaobj;
	public SsAdapter(SyllabusStatusActivity syllabusStatusActivity) {
		// TODO Auto-generated constructor stub
		ssaobj= syllabusStatusActivity;
	}

	@Override
	public boolean areAllItemsEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return children[groupPosition][childPosition];
	}
	
	public Object getChildPercent(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childpercent[groupPosition][childPosition];
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
		View row = convertView;
		AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 48);
      	 if(row==null){
      	  LayoutInflater inflater = ssaobj.getLayoutInflater();
      	  row=inflater.inflate(R.layout.ssa_row, parent, false);
      	  row.setLayoutParams(lp);
      	  TextView tv1 =(TextView) row.findViewById(R.id.textView1);
      	  tv1.setText(getChild(groupPosition, childPosition).toString());
      	  TextView tv2 =(TextView) row.findViewById(R.id.textView2);
      	tv2.setTextColor(Color.argb(250, 0, 100, 0));
	int y=childpercent[groupPosition][childPosition];
		if(y<75)
		{
			tv2.setTextColor(Color.BLACK);
		}
		if(y<40)
		{
			tv2.setTextColor(Color.RED);
		}
    	  tv2.setText(getChildPercent(groupPosition, childPosition).toString()+" %");
    	  LinearLayout ll=(LinearLayout) row.findViewById(R.id.linearLayout3);
    	  ImageView img=(ImageView) row.findViewById(R.id.ssimg);
       	  img.setImageResource(R.drawable.bar2);
       	  img.setPivotX(new Float(0));
          img.setScaleX(new Float((float)childpercent[groupPosition][childPosition]/100));
         // img.setPivotY(new Float(1.0));
      	 }
      	 
       return row;
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
		 TextView textView = new TextView(ssaobj);
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

	@Override
	public void onGroupCollapsed(int groupPosition) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGroupExpanded(int groupPosition) {
		// TODO Auto-generated method stub

	}

	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
		// TODO Auto-generated method stub

	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		// TODO Auto-generated method stub

	}
}
