package dexter.studentmonitoring;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class Pending extends Activity {
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.pending);
	        TextView tvPending = (TextView) findViewById(R.id.pending);
	        tvPending.setText("This is Pending LIST");
	        
	        
	 }
	 


}
