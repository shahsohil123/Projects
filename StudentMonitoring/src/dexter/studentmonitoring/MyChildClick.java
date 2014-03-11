package dexter.studentmonitoring;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;

public class MyChildClick implements OnChildClickListener {
	
	AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, 288);
	AbsListView.LayoutParams lq = new AbsListView.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, 48);

	@Override
	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
		int old=v.getLayoutParams().height;
		
		
		if(old<240)
		parent.updateViewLayout(v, lp);
		else
			parent.updateViewLayout(v, lq);	
		return true;
	}

}
