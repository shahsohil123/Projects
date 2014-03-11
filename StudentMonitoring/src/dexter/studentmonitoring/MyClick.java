package dexter.studentmonitoring;

import android.graphics.Color;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class MyClick implements OnItemClickListener {
	PieGraphActivity pgs;
	PieChart pieView;
	int flag=0;
	ListView lv1;
	public MyClick(PieGraphActivity pieGraphActivity, ListView lv) {
		lv1=lv;
		pgs=pieGraphActivity;

		// TODO Auto-generated constructor stub
	}

	public MyClick(PieChart pieChart) {
		pieView=pieChart;
		// TODO Auto-generated constructor stub
	}
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		 SparseBooleanArray sp=lv1.getCheckedItemPositions();
		 
		 sp=lv1.getCheckedItemPositions();
		 pgs.repeat(position,sp);
	}

}
