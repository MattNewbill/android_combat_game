package combatgame.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import android.util.Log;

public class Internet {

	//www.newbillity.com/apitest.php
	
	public static String getJSON(String address){
    	StringBuilder builder = new StringBuilder();
    	HttpClient client = new DefaultHttpClient();
    	HttpGet httpGet = new HttpGet(address);
    	try{
    		HttpResponse response = client.execute(httpGet);
    		StatusLine statusLine = response.getStatusLine();
    		int statusCode = statusLine.getStatusCode();
    		if(statusCode == 200){
    			HttpEntity entity = response.getEntity();
    			InputStream content = entity.getContent();
    			BufferedReader reader = new BufferedReader(new InputStreamReader(content));
    			String line;
    			while((line = reader.readLine()) != null){
    				builder.append(line);
    			}
    		} else {
    			Log.i("sfg","Failed JSON object");
    		}
    	}catch(ClientProtocolException e){
    		e.printStackTrace();
    	} catch (IOException e){
    		e.printStackTrace();
    	}
    	return builder.toString();
    }
	
	public static void postJSON(String wurl, JSONObject jsonobj) {
		try {
			DefaultHttpClient httpclient = new DefaultHttpClient();
			HttpPost httppostreq = new HttpPost(wurl);
			StringEntity se = new StringEntity(jsonobj.toString());
			se.setContentType("application/json;charset=UTF-8");
			se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json;charset=UTF-8"));
			httppostreq.setEntity(se);
			HttpResponse httpresponse = httpclient.execute(httppostreq);
			
			HttpEntity resultentity = httpresponse.getEntity();
			InputStream inputstream = resultentity.getContent();
			Header contentencoding = httpresponse
					.getFirstHeader("Content-Encoding");
			if (contentencoding != null
					&& contentencoding.getValue().equalsIgnoreCase("gzip")) {
				inputstream = new GZIPInputStream(inputstream);
			}
			String resultstring = convertStreamToString(inputstream);
			inputstream.close();
			resultstring = resultstring.substring(1, resultstring.length() - 1);
			Log.i("combatgame", resultstring + "\n\n" + httppostreq.toString());
			JSONObject recvdjson = new JSONObject(resultstring);
			Log.i("combatgame", recvdjson.toString(2));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private static String convertStreamToString(InputStream is) {
		String line = "";
		StringBuilder total = new StringBuilder();
		BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		try {
			while ((line = rd.readLine()) != null) {
				total.append(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return total.toString();
	}
	
//	public static ArduinoHelper parseJSON(String json) {
//		try {
//			JSONObject jsonObject = new JSONObject(json);
//			String direction = jsonObject.getString("Direction");
//			int distance = Integer.parseInt(jsonObject.getString("Distance"));
//			return new ArduinoHelper(direction, distance);
//			//Log.i(MainActivity.class.getName(), jsonObject.getString("date"));
//		} catch (Exception e) {
//			e.printStackTrace();
//			return new ArduinoHelper("unchanged", -1);
//		}
//	}
	
}
