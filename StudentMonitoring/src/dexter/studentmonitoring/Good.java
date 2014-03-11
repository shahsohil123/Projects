package dexter.studentmonitoring;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class Good extends Activity{
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.good);
	        TextView tvPending = (TextView) findViewById(R.id.good);
	        tvPending.setText("This is good marks");
	        
	        
	 }
	 protected void OnResume(){
	    	super.onResume();
	    	setContentView(R.layout.main);}



}
