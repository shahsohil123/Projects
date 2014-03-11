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
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class Download extends Activity {
	
int count;


public static final int MESSAGE_DOWNLOAD_STARTED = 1000;
public static final int MESSAGE_DOWNLOAD_COMPLETE = 1001;
public static final int MESSAGE_UPDATE_PROGRESS_BAR = 1002;
public static final int MESSAGE_DOWNLOAD_CANCELED = 1003;
public static final int MESSAGE_CONNECTING_STARTED = 1004;
public static final int MESSAGE_ENCOUNTERED_ERROR = 1005;

// instance variables
private Download thisActivity;
private Thread downloaderThread;
private ProgressDialog progressDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.download);
		downloaderThread = null;
        progressDialog = null;
        try {
			count = getCount();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        String[] list=new String[count-1];
		String[] add=new String[count-1];
		try {
			 
			HttpClient httpclient = new DefaultHttpClient();
			HttpGet httpget = new HttpGet("http://www.c7.comlu.com/upload/");
			HttpResponse response = httpclient.execute(httpget); //
			HttpEntity entity = response.getEntity();
			InputStream is = entity.getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			
		
			String line = null;
			int inc=0;
			while ((line = reader.readLine()) != null) {
				// Read line by line

				if (line.contains("a href")) {
					if(inc==0)
						inc++;
					else{
					int x = line.indexOf("=") + 2;
					int y = line.indexOf("> ") - 1;
					int z = line.indexOf("</a>");
					add[inc-1]=line.substring(x, y);
					list[inc-1]=line.substring(y + 3, z);
					Log.d("lol",list[inc-1]);
					inc++;
					}
				}
			}


			is.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		ListView listView = (ListView) findViewById(R.id.mylist);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.textview, list);
		
		listView.setAdapter(adapter); 
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				String urlInput = "http://www.c7.comlu.com/upload/ron%20sign.jpg";
                downloaderThread = new DownloaderThread(thisActivity, urlInput);
                downloaderThread.start();
				
			}
		});
	}

	public int getCount() throws IOException {

		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet("http://www.c7.comlu.com/upload/");
		HttpResponse response = httpclient.execute(httpget);
		HttpEntity entity = response.getEntity();
		InputStream is = entity.getContent();
		BufferedReader reader = new BufferedReader(new InputStreamReader(is,
				"iso-8859-1"), 8);

		String line = null;
		int count = 0;
		while ((line = reader.readLine()) != null) {

			if (line.contains("a href")) {
				count++;
			}
		}
		is.close();

		return count;

	}
	 public Handler activityHandler = new Handler(){
             public void handleMessage(Message msg)
             {
                     switch(msg.what)
                     {
                             /*
                              * Handling MESSAGE_UPDATE_PROGRESS_BAR:
                              * 1. Get the current progress, as indicated in the arg1 field
                              *    of the Message.
                              * 2. Update the progress bar.
                              */
                             case MESSAGE_UPDATE_PROGRESS_BAR:
                                     if(progressDialog != null)
                                     {
                                             int currentProgress = msg.arg1;
                                             progressDialog.setProgress(currentProgress);
                                     }
                                     break;
                             
                             /*
                              * Handling MESSAGE_CONNECTING_STARTED:
                              * 1. Get the URL of the file being downloaded. This is stored
                              *    in the obj field of the Message.
                              * 2. Create an indeterminate progress bar.
                              * 3. Set the message that should be sent if user cancels.
                              * 4. Show the progress bar.
                              */
                             case MESSAGE_CONNECTING_STARTED:
                                     if(msg.obj != null && msg.obj instanceof String)
                                     {
                                             String url = (String) msg.obj;
                                             // truncate the url
                                             if(url.length() > 16)
                                             {
                                                     String tUrl = url.substring(0, 15);
                                                     tUrl += "...";
                                                     url = tUrl;
                                             }
                                             String pdTitle = thisActivity.getString(R.string.progress_dialog_title_connecting);
                                             String pdMsg = thisActivity.getString(R.string.progress_dialog_message_prefix_connecting);
                                             pdMsg += " " + url;
                                             
                                             dismissCurrentProgressDialog();
                                             progressDialog = new ProgressDialog(thisActivity);
                                             progressDialog.setTitle(pdTitle);
                                             progressDialog.setMessage(pdMsg);
                                             progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                             progressDialog.setIndeterminate(true);
                                             // set the message to be sent when this dialog is canceled
                                             Message newMsg = Message.obtain(this, MESSAGE_DOWNLOAD_CANCELED);
                                             progressDialog.setCancelMessage(newMsg);
                                             progressDialog.show();
                                     }
                                     break;
                                     
                             /*
                              * Handling MESSAGE_DOWNLOAD_STARTED:
                              * 1. Create a progress bar with specified max value and current
                              *    value 0; assign it to progressDialog. The arg1 field will
                              *    contain the max value.
                              * 2. Set the title and text for the progress bar. The obj
                              *    field of the Message will contain a String that
                              *    represents the name of the file being downloaded.
                              * 3. Set the message that should be sent if dialog is canceled.
                              * 4. Make the progress bar visible.
                              */
                             case MESSAGE_DOWNLOAD_STARTED:
                                     // obj will contain a String representing the file name
                                     if(msg.obj != null && msg.obj instanceof String)
                                     {
                                             int maxValue = msg.arg1;
                                             String fileName = (String) msg.obj;
                                             String pdTitle = thisActivity.getString(R.string.progress_dialog_title_downloading);
                                             String pdMsg = thisActivity.getString(R.string.progress_dialog_message_prefix_downloading);
                                             pdMsg += " " + fileName;
                                             
                                             dismissCurrentProgressDialog();
                                             progressDialog = new ProgressDialog(thisActivity);
                                             progressDialog.setTitle(pdTitle);
                                             progressDialog.setMessage(pdMsg);
                                             progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                                             progressDialog.setProgress(0);
                                             progressDialog.setMax(maxValue);
                                             // set the message to be sent when this dialog is canceled
                                             Message newMsg = Message.obtain(this, MESSAGE_DOWNLOAD_CANCELED);
                                             progressDialog.setCancelMessage(newMsg);
                                             progressDialog.setCancelable(true);
                                             progressDialog.show();
                                     }
                                     break;
                             
                             /*
                              * Handling MESSAGE_DOWNLOAD_COMPLETE:
                              * 1. Remove the progress bar from the screen.
                              * 2. Display Toast that says download is complete.
                              */
                             case MESSAGE_DOWNLOAD_COMPLETE:
                                     dismissCurrentProgressDialog();
                                     displayMessage(getString(R.string.user_message_download_complete));
                                     break;
                                     
                             /*
                              * Handling MESSAGE_DOWNLOAD_CANCELLED:
                              * 1. Interrupt the downloader thread.
                              * 2. Remove the progress bar from the screen.
                              * 3. Display Toast that says download is complete.
                              */
                             case MESSAGE_DOWNLOAD_CANCELED:
                                     if(downloaderThread != null)
                                     {
                                             downloaderThread.interrupt();
                                     }
                                     dismissCurrentProgressDialog();
                                     displayMessage(getString(R.string.user_message_download_canceled));
                                     break;
                             
                             /*
                              * Handling MESSAGE_ENCOUNTERED_ERROR:
                              * 1. Check the obj field of the message for the actual error
                              *    message that will be displayed to the user.
                              * 2. Remove any progress bars from the screen.
                              * 3. Display a Toast with the error message.
                              */
                             case MESSAGE_ENCOUNTERED_ERROR:
                                     // obj will contain a string representing the error message
                                     if(msg.obj != null && msg.obj instanceof String)
                                     {
                                             String errorMessage = (String) msg.obj;
                                             dismissCurrentProgressDialog();
                                             displayMessage(errorMessage);
                                     }
                                     break;
                                     
                             default:
                                     // nothing to do here
                                     break;
                     }
             }
     };
    
    /**
     * If there is a progress dialog, dismiss it and set progressDialog to
     * null.
     */
    public void dismissCurrentProgressDialog()
    {
            if(progressDialog != null)
            {
                    progressDialog.hide();
                    progressDialog.dismiss();
                    progressDialog = null;
            }
    }
    
    /**
     * Displays a message to the user, in the form of a Toast.
     * @param message Message to be displayed.
     */
    public void displayMessage(String message)
    {
            if(message != null)
            {
                    Toast.makeText(thisActivity, message, Toast.LENGTH_SHORT).show();
            }
    }

}
