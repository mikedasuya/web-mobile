package com.househelper.service;

import com.househelper.common.HouseConstants;
import com.househelper.service.data.NotificationObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

public class NotificationHandler {
	int notificationId = 0;
	NotificationManager mNotifyManager = null;
	Notification.Builder notificationBuilder = null;
	Context mContext;
	@SuppressLint("NewApi")
	NotificationHandler(Context cont) {
			mContext = cont;
	}
	
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@SuppressLint("NewApi")
	void updateProgress(int progress) {
		notificationBuilder.setProgress(100,progress, false);
		Notification notification = notificationBuilder.build();
		mNotifyManager.notify(notificationId, notification);
	}
	
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	void updateNotificationMessage(String message) {
		notificationBuilder.setContentText(message);
		Notification notification = notificationBuilder.build();
		mNotifyManager.notify(notificationId, notification);
		
	}
	
	void deleteNotification() {
		mNotifyManager.cancel(notificationId);
	}
	
	void handleMessage(NotificationObject obj) {
		/*
		notificationId = obj.getId();
		mNotifyManager =
		        (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationBuilder = new Notification.Builder(mContext);
		notificationBuilder.setOngoing(true)
		                   .setContentTitle("Upload Image")
		                   .setContentText("Uploading Image ...")
		                   .setProgress(100, 0, false);
		Notification notification = notificationBuilder.build();
		mNotifyManager.notify(notificationId, notification);*/
	}
	
	public void handleFileUploadResult(Bundle data, int code) {
		  long id = data.getLong(HouseConstants.REQUEST_ID_STRIND);
		 /* mapRequestId.remove(id);
		  if (mNotifyHandler != null && code == HouseConstants.OPERATION_FILE_UPLOAD_FAILURE) {
			  mNotifyHandler.updateNotificationMessage(HouseConstants.OPERATION_FILE_UPLOAD_ERROR_STRING);
		  } else if (mNotifyHandler != null && code == HouseConstants.OPERATION_FILE_UPLOAD_SUCCESS) {
			  mNotifyHandler.updateNotificationMessage(HouseConstants.OPERATION_FILE_UPLOAD_SUCCESS_STRING);
		  }*/
	  }
	  
	  private void handleFileUploadResult(int arg2) {
			// TODO Auto-generated method stub
		  /* if (arg2 == HouseConstants.OPERATION_FILE_UPLOAD_START) {
			   mNotifyHandler =  new NotificationHandler(getApplicationContext(), 1);
		   } else if (arg2 == HouseConstants.OPERATION_FILE_UPLOAD_INPROGRESS) {
			   if (mNotifyHandler != null) {
					mNotifyHandler.updateProgress(arg2);
				}   
		   }*/
			
	  }
	
	
}