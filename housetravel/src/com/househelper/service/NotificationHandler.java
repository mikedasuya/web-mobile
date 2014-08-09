package com.househelper.service;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

public class NotificationHandler {
	int notificationId = 0;
	NotificationManager mNotifyManager = null;
	Notification.Builder notificationBuilder = null;
	Context context;
	@SuppressLint("NewApi")
	NotificationHandler(Context cont, int id) {
		notificationId = id;
		context = cont;
		mNotifyManager =
		        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationBuilder = new Notification.Builder(context);
		notificationBuilder.setOngoing(true)
		                   .setContentTitle("Upload Image")
		                   .setContentText("Uploading Image ...")
		                   .setProgress(100, 0, false);
		Notification notification = notificationBuilder.build();
		mNotifyManager.notify(notificationId, notification);	
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
	
	
	
}