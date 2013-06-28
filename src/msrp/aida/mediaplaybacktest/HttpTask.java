package msrp.aida.mediaplaybacktest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.os.AsyncTask;
import android.util.Log;

public class HttpTask extends AsyncTask<String, Integer, String> {

	
	@Override
	protected String doInBackground(String... params) {
		//  Load the webpage with the credentials given by the user
		// params is an array that contains the url of AutoExpreso in [0] , username in [1] and password in [2] 
		
		String urlS = params[0];
	//	final String username = params[1];
	//	final String password = params[2];
		
		try{
			
			HttpClient client = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(urlS);
			
			//List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            //nameValuePairs.add(new BasicNameValuePair("Username", username));
            //nameValuePairs.add(new BasicNameValuePair("Password", password));
            //TODO Login using this crap
            //nameValuePairs.add(new BasicNameValuePair("myLoginForm", "submit()"));
            //httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			
			HttpResponse reponse = client.execute(httppost);
			Log.d("DEBUG", "line "+reponse.getStatusLine().toString());
			HttpEntity entity = reponse.getEntity();
			
			if(entity != null)
			{
				InputStream insStream = entity.getContent();
				readStream(insStream);
			}
			
		}
		catch(Exception e)
		{
			Log.d("DEBUG", "Exploto");
		}
		
		return null;
	}

	
	@Override
	protected void onPostExecute(String result) {
		//TODO Update the UI 
		super.onPostExecute(result);
	}
	
	
	private void readStream(InputStream in) {
		  BufferedReader reader = null;
		  System.out.println("WUUUUUUUUT");
		  try {
		    reader = new BufferedReader(new InputStreamReader(in));
		    String htmlPage = "";
		    String line;
		    while ((line = reader.readLine()) != null) {
		    	htmlPage += line;
		    }
	    	Log.d("DEBUG", htmlPage);

		  } catch (IOException e) {
		    e.printStackTrace();
		  } finally {
		    if (reader != null) {
		      try {
		        reader.close();
		      } catch (IOException e) {
		        e.printStackTrace();
		        }
		    }
		  }
		} 
	

}
/*
 * @Override
	protected String doInBackground(String... params) {
		//  Load the webpage with the credentials given by the user
		// params is an array that contains the url of AutoExpreso in [0] , username in [1] and password in [2] 
		
		String urlS = params[0];
		final String username = params[1];
		final String password = params[2];

		try{
			 
			URL url = new URL(urlS);
			Log.d("DEBUG", "url " + url);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64; rv:12.0) Gecko/20100101 Firefox/12.0");
			readStream(connection.getInputStream());
		
		}
		catch(Exception e)
		{
			Log.d("DEBUG", "Exploto");
		}
		
		return null;
	}
 */
