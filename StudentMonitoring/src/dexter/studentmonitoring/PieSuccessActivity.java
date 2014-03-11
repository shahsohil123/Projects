package dexter.studentmonitoring;

import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class PieSuccessActivity extends ActivityGroup {
	int total_rows = 3;
	ScrollView sv,sw;
    LinearLayout ll_parent;
    LinearLayout ll_child[]=new LinearLayout[3];
    LinearLayout ll_activitychild[]=new LinearLayout[3];
    LayoutParams lp1 = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,480);
    TextView tv[]=new TextView[total_rows];
    String row_data[] ={"wazzzzzzzzzo 1","wazzzzzzzzzo 2","wazzzzzzzzzo 3"}; 
    int temp=-1;
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adding);
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,48);
             ll_parent= (LinearLayout) findViewById(R.id.linearLayout1);
            for(int i=0;i<3;i++)
            {
             ll_child[i] = new LinearLayout(this);
              ll_child[i].setLayoutParams(lp);
              tv[i]=new TextView(this);
              tv[i].setLayoutParams(lp);
              tv[i].setPadding(40,10, 10,0);
              tv[i].setBackgroundResource(R.drawable.group_back_final);
              tv[i].setText(row_data[i]);
              
              ll_child[i].addView(tv[i]);
              
              ll_parent.addView(ll_child[i]);
              ll_activitychild[i]=new LinearLayout(this);
      	   	  ll_activitychild[i].setLayoutParams(lp1);
            }
      OnClickListener myact =new MyAction(this);
      
      for(int i=0;i<3;i++)
      {
    	  tv[i].setId(i);
    	  tv[i].setOnClickListener(myact);
      }
     
  
  
        
        
    }
   void letsdo(int index)
   {	
	   if(temp==-1)
	   {
		   temp=index;
		   createInnerActivity(ll_activitychild[index], PieGraphActivity.class);
		   ll_parent.addView(ll_activitychild[index], (index+1));
	   }
	   else
	   {
		   destroyInnerActivity(ll_activitychild[temp], PieGraphActivity.class);
		   ll_parent.removeViewAt(temp+1);
		   temp=index;
		   createInnerActivity(ll_activitychild[index], PieGraphActivity.class);
		   ll_parent.addView(ll_activitychild[index], (index+1));
	   }
   }
   public void letsundo(int index)
   {	

	   destroyInnerActivity(ll_activitychild[index], PieGraphActivity.class);
	   ll_parent.removeViewAt(index+1);
	   temp=-1;
   }
  
 
    void createInnerActivity(ViewGroup container, Class<?> activityClass) 
    { 
            if (container.getChildCount() != 0) { 
                container.removeViewAt(0); 
                
            } 
            final Intent intent = new Intent(this, activityClass); 
            final Window window = getLocalActivityManager()
            		.startActivity(activityClass.toString(), intent); 
            container.addView(window.getDecorView(), new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 
                  ViewGroup.LayoutParams.FILL_PARENT)); 
    }
    void destroyInnerActivity(ViewGroup container, Class<?> activityClass) 
    { 
            
            final Window window = getLocalActivityManager()
            		.destroyActivity(activityClass.toString(),true); 
            		container.removeView(window.getDecorView()); 
    }
	
   
}