package com.househelper.service;

import android.os.Handler;

public interface Request extends Runnable {
	public int getType(); 
	abstract void run();
	abstract String getUrl();
	abstract String getFolderName();
	abstract String getFileName();
	abstract ICallBack getCallBack();
	abstract void setCallBackHandler(Handler mhandler);
	
}