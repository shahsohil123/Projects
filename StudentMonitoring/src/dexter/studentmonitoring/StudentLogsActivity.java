package dexter.studentmonitoring;

import android.app.Activity;
import android.app.ExpandableListActivity;
import android.os.Bundle;
import android.widget.ExpandableListAdapter;

public class StudentLogsActivity extends ExpandableListActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      
        ExpandableListAdapter studadapter = new MyStudAdapter(this);
        setListAdapter(studadapter);
        getExpandableListView().setGroupIndicator(null);
    }
}