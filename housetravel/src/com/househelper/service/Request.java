package com.househelper.service;

import android.os.Handler;

public interface Request extends Runnable {
	abstract void run();
	abstract String getFolderName();
	abstract String getFileName();
	abstract void setCallBackHandler(Handler mhandler);
	
}