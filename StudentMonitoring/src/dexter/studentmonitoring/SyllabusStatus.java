package dexter.studentmonitoring;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class SyllabusStatus extends Activity{
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView textview = new TextView(this);
        textview.setText("Thise Albumts tab");
        setContentView(textview);
    }

}