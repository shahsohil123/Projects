package dexter.studentmonitoring;


import android.app.Activity;
import android.app.ExpandableListActivity;
import android.app.WallpaperManager;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.os.Bundle;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView.OnChildClickListener;

public class PatentActivity extends ExpandableListActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      
        ExpandableListAdapter wadapter,xadapter;
        xadapter=new MyParentAdapter(this);
        
        setListAdapter(xadapter);
        
        getExpandableListView().setDividerHeight(0);
        getExpandableListView().setGroupIndicator(null);
        
        OnChildClickListener ck1= new MyChildClick();
       	getExpandableListView().setOnChildClickListener(ck1);
    }
}