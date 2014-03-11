package dexter.studentmonitoring;
import org.achartengine.ChartFactory;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;
import android.content.Context;
import android.graphics.Color;
import android.util.SparseBooleanArray;
import android.view.View;

public class PieChart 
{	double val[]={0,0,0,0};
	int position=0;
	
	public PieChart(){}
	public void changePosi(int position, SparseBooleanArray sp)
	{		
		double data[][]={{20,30,40,10},{31,23,4,5},{31,4,5,63},{31,42,54,65},{4,25,64,41},{31,42,54,76},{31,42,54,65}};
		this.position = position;
		
		for(int i=0;i<sp.size();i++)
		{
			if(sp.valueAt(i)==true)
			{
				for(int j=0;j<4;j++)
				{
					val[j]+=data[i][j];
				}
			}
		}
	}
	public View getView(Context context) 
	{
		
		
        int[] colors = new int[] {Color.YELLOW, Color.GREEN, Color.BLUE, Color.RED};
        //String[] labels={"Pending","Average","Good","Poor"};
        DefaultRenderer renderer = buildCategoryRenderer(colors,position);
        View v;
        v=ChartFactory.getPieChartView(context, buildCategoryDataset("Project budget", val), renderer);
        return v;
      }
    protected DefaultRenderer buildCategoryRenderer(int[] colors,int position) {
        DefaultRenderer renderer = new DefaultRenderer();
        String[] subjects={"English","Marathi","Hindi","Gujrati","French","German","Sanskrit"};
        renderer.setChartTitleTextSize(50);
        renderer.setChartTitle(subjects[position]);
        renderer.setBackgroundColor(Color.WHITE);
        renderer.setPanEnabled(false);
        renderer.setClickEnabled(true);
        renderer.setShowLabels(false);
        renderer.setAntialiasing(true);
        renderer.setApplyBackgroundColor(true);
        renderer.setShowLegend(false);
        renderer.setLabelsColor(R.color.black);
        for (int color : colors) {
          SimpleSeriesRenderer r = new SimpleSeriesRenderer();
          r.setColor(color);
          r.setDisplayChartValues(false);
          renderer.addSeriesRenderer(r);
        }
        return renderer;
      }
    
    protected CategorySeries buildCategoryDataset(String title, double[] values) {
        CategorySeries series = new CategorySeries(title);
        int k = 0;
        for (double value : values) {
          series.add("Project " + ++k, value);
          
        }
              
        return series;

	}
	
}
