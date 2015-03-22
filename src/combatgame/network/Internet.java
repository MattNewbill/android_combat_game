package combatgame.network;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

public class Internet {

	//www.newbillity.com/apitest.php
	
	public static String getJSON(String address) {
		String responseString = "";
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response = httpclient.execute(new HttpGet(address));
			StatusLine statusLine = response.getStatusLine();
			if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				response.getEntity().writeTo(out);
				responseString = out.toString();
				out.close();
			} else {
				// Closes the connection.
				response.getEntity().getContent().close();
				throw new IOException(statusLine.getReasonPhrase());
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return responseString;
	}
	
	public static String postJSON(String wurl, JSONObject jsonobj) {
		String resultString = "";
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
			resultString = convertStreamToString(inputstream);
			inputstream.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return resultString;
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
