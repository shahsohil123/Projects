package dexter.studentmonitoring;

import android.app.Activity;
import android.app.ExpandableListActivity;
import android.os.Bundle;
import android.widget.ExpandableListAdapter;

public class SyllabusStatusActivity extends ExpandableListActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ExpandableListAdapter ssadapter = new SsAdapter(this);
        setListAdapter(ssadapter);
        getExpandableListView().setGroupIndicator(null);
        getExpandableListView().setDivider(null);
    }
}