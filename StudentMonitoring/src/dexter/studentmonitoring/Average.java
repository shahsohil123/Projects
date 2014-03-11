package dexter.studentmonitoring;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class Average extends Activity{
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.average);
	        TextView tvPending = (TextView) findViewById(R.id.average);
	        tvPending.setText("This is average marks");
	        
	        
	 }
	 protected void OnResume(){
	    	super.onResume();
	    	setContentView(R.layout.main);}


}
