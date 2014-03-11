package dexter.studentmonitoring;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class Poor extends Activity{
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.poor);
	        TextView tvPending = (TextView) findViewById(R.id.poor);
	        tvPending.setText("This is Poor Marks");
	        
	        
	 }
	 protected void OnResume(){
	    	super.onResume();
	    	setContentView(R.layout.main);}


}
