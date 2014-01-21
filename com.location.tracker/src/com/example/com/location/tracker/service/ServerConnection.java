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

import android.os.Handler;
import android.os.Message;

import com.example.com.location.common.Common;

public class ServerConnection {
	
	public ServerConnection() {
		
		// TODO Auto-generated constructor stub
	}

	public boolean sendEmailLatLong(String email,String lat,String longi) throws IOException{
		System.out.println("tracker ---- " + lat);
		HttpClient httpclient = new DefaultHttpClient();
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
		 	formEntity = new UrlEncodedFormEntity(postParameters);
		 	request.setEntity(formEntity);
		 	response = httpclient.execute(request);
		 	// Get hold of the response entity
	       // HttpEntity entity = response.getEntity();
	        
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
		return true;
	}
};