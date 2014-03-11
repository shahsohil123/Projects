package dexter.studentmonitoring;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class MyAction implements OnClickListener {
	
	
	PieSuccessActivity psa;
	int index;

	public MyAction(PieSuccessActivity pieSuccessActivity) {
		// TODO Auto-generated constructor stub
		psa = pieSuccessActivity;
	}

	@Override
	public void onClick(View ss) {
		// TODO Auto-generated method stub
		index=ss.getId();
		Flagging.setFlag(index);
	 	if(Flagging.myflag[index]==0)
		{	
	    	psa.letsdo(index);
		}
		else
		{
			psa.letsundo(index);
		}
	}
	
	
}
