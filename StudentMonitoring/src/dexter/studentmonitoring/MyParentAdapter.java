package dexter.studentmonitoring;





import android.R.color;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class MyParentAdapter extends BaseExpandableListAdapter {
	
	PatentActivity pa;
	private String[] groups = { "School Test", "System Test", "Concept Test"};
	private String[][] children ={{"Average","Class Test","Final Test"},{"Cass Test 3","Class Test 4"},{"Class Test 5","Class Test 6"}};
	private int[][] childrenpercent ={{70,75,85},{65,78},{90,100}};
	private String [] grandchildrenNames ={"English","Maths","Science","History","Geography"};
    private int [][][] grandchildrenMarks={{{30,40,60,70,80},{80,10,60,70,80},{90,40,90,70,80}},{{30,40,60,70,80},{80,10,60,70,80}},{{30,40,60,70,80},{80,10,60,70,80}}};
    
	public MyParentAdapter(PatentActivity patentActivity) {
		// TODO Auto-generated constructor stub
		pa=patentActivity;
	}


	@Override
	public boolean areAllItemsEnabled() {
		// TODO Auto-generated method stub
		return true;
	}
	public Object getChildPercent(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childrenpercent[groupPosition][childPosition];
	}
	
	public Object getChildMarks(int groupPosition, int childPosition, int grandchildPosition) {
		// TODO Auto-generated method stub
		return grandchildrenMarks[groupPosition][childPosition][grandchildPosition];
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
		int grandchildPosition = 0;
		View row = convertView;
		AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 48);
      	 if(row==null){
      	  LayoutInflater inflater = pa.getLayoutInflater();
      	  row=inflater.inflate(R.layout.row, parent, false);
      	  row.setLayoutParams(lp);
      	 }
      	 int i = (Integer) getChildPercent(groupPosition, childPosition);
      	 TextView tv1=(TextView) row.findViewById(R.id.textView1);
      	 TextView tv7=(TextView) row.findViewById(R.id.textView7);
      
      	 tv1.setText(getChild(groupPosition, childPosition).toString());
      	 tv1.setPadding(48, 0, 0, 12);
      	 ImageView img=(ImageView) row.findViewById(R.id.imageView1);
      	 img.setImageResource(R.drawable.bar2);
      	 img.setPivotX(new Float(0));
       	 img.setScaleX(new Float((float)i/100));
       	 img.setPivotY(new Float(1.0));
     
       	 
     	 tv7.setText(getChildPercent(groupPosition, childPosition).toString()+" %");
     	 tv7.setPadding(10, 0, 0, 12);
     	 LinearLayout ll1=(LinearLayout) row.findViewById(R.id.linearLayout1);
     	AbsListView.LayoutParams lp2 = new AbsListView.LayoutParams(
                400, 48);
     	 for(int j=0;j<5;j++)
     	 {
     	 LinearLayout ll2 = new LinearLayout(pa);
     	 ll2.setLayoutParams(lp);
      	 TextView tv2=new TextView(pa);
      	 TextView tv8=new TextView(pa);
      	 tv2.setLayoutParams(lp2);
      	 if(grandchildrenMarks[groupPosition][childPosition][j]<=40)
      	 {
      		 tv2.setTextColor(Color.argb(255, 150, 0, 0));
      		 tv8.setTextColor(Color.argb(255, 150, 0, 0));
      	 }
      	 else if(grandchildrenMarks[groupPosition][childPosition][j]>40 && grandchildrenMarks[groupPosition][childPosition][j]<=70)
      	 {
      		 tv2.setTextColor(Color.argb(255, 0, 100, 0));
      		 tv8.setTextColor(Color.argb(255, 0, 100, 0));
      	 }
      	 else
      	 {}
      		 tv2.setText(grandchildrenNames[j].toString());
      	 tv2.setPadding(150, 12, 0, 12); 
     	 
     	 
     	 
     	 tv8.setLayoutParams(lp2);
     	 tv8.setPadding(150, 12, 0, 12);
     	 tv8.setText(getChildMarks(groupPosition, childPosition,j).toString()+" %");
     	 
     	 ll2.addView(tv2);
     	 ll2.addView(tv8);
     	 ll1.addView(ll2,j+1);
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
		 TextView textView = new TextView(pa);
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
