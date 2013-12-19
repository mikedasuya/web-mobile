package com.example.com.location.tracker.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.example.com.location.common.Common;

public class ServerConnection {
	
	public void sendEmailLatLong(String email,String lat,String longi) {
		System.out.println("tracker ---- " + lat);
		HttpClient httpclient = new DefaultHttpClient();

    // Prepare a request object
    

    // Execute the request
    HttpResponse response = null;
    try {
    	HttpPost request = new HttpPost(Common.url);
		List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
		postParameters.add(new BasicNameValuePair("email", email));
	    postParameters.add(new BasicNameValuePair("lat", lat));
	    postParameters.add(new BasicNameValuePair("long", longi));
	    Date date = new Date();
	    Calendar c = new GregorianCalendar();
	    int mYear=c.get(Calendar.YEAR);
	    int mMonth=c.get(Calendar.MONTH);
	    int mDay=c.get(Calendar.DAY_OF_MONTH);
	    postParameters.add(new BasicNameValuePair("day",Integer.toString(mDay)));
	    postParameters.add(new BasicNameValuePair("month",Integer.toString(mMonth)));
	    postParameters.add(new BasicNameValuePair("year",Integer.toString(mYear)));
	 // Set your parameters here...
	 		UrlEncodedFormEntity formEntity = null;
	 		try {
	 			formEntity = new UrlEncodedFormEntity(postParameters);
	 		} catch (UnsupportedEncodingException e) {
	 			// TODO Auto-generated catch block
	 			e.printStackTrace();
	 		}
	 		request.setEntity(formEntity);
	 		
	 		try {
	 			response = httpclient.execute(request);
	 		} catch (ClientProtocolException e) {
	 			// TODO Auto-generated catch block
	 			e.printStackTrace();
	 		} catch (IOException e) {
	 			// TODO Auto-generated catch block
	 			e.printStackTrace();
	 		}
        
       /* int code = response.getStatusLine().getStatusCode();
        System.out.println("tracker" + code);
        if (code != 200) {
        	System.out.println("tracker" + code);
        }*/
        // Get hold of the response entity
        HttpEntity entity = response.getEntity();
        // If the response does not enclose an entity, there is no need
        // to worry about connection release

        

    } catch (Exception e) {
    	e.printStackTrace();
    	
    }
}
}