package trr.manthan;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		try{
			int count=getCount();
		HttpClient httpclient = new DefaultHttpClient(); // Create HTTP Client
		HttpGet httpget = new HttpGet("http://www.c7.comlu.com/upload/"); // Set the action you want to do
		HttpResponse response = httpclient.execute(httpget); // Executeit
		HttpEntity entity = response.getEntity(); 
		InputStream is = entity.getContent(); // Create an InputStream with the response
		BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
		StringBuilder sb = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null){
			// Read line by line
			
			if(line.contains("a href"))
			{	int x=line.indexOf("=")+2;
			int y=line.indexOf("> ")-1;
			int z=line.indexOf("</a>");
			line=line.substring(x, y)+" "+line.substring(y+3, z);
		    sb.append(line + "\n");}
		}
		
	
		String resString = sb.toString();
		resString=resString+"\n"+count;// Result is here
		TextView tv=(TextView) findViewById(R.id.Text);
		tv.setText(resString);
Log.d("Text", resString);
		is.close(); // Close the stream
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		
		
	}

	public int getCount() throws IOException{
		
		HttpClient httpclient = new DefaultHttpClient(); // Create HTTP Client
		HttpGet httpget = new HttpGet("http://www.c7.comlu.com/upload/"); // Set the action you want to do
		HttpResponse response = httpclient.execute(httpget); // Executeit
		HttpEntity entity = response.getEntity(); 
		InputStream is = entity.getContent(); // Create an InputStream with the response
		BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
		StringBuilder sb = new StringBuilder();
		String line = null;
		int count=0;
		while ((line = reader.readLine()) != null){
			// Read line by line
			
			if(line.contains("a href"))
			{	count++;}
		}
		is.close();
		
		return count;
		
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
