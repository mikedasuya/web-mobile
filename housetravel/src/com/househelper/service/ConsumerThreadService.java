package com.househelper.service;

import java.util.concurrent.BlockingQueue;

import com.househelper.common.HouseConstants;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class ConsumerThreadService implements Runnable {
	BlockingQueue mQueue;
	Handler mServiceHandler = null;
	boolean mKeepRunning = true;
	
	ConsumerThreadService(BlockingQueue qu, Handler mHandler) {
		mQueue = qu;
		mServiceHandler = mHandler;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
		       while (mKeepRunning == true) { 
		    	 consumer(mQueue.take()); 
		       }
		     } catch (InterruptedException ex) { 
		    	 
		     }
		   

	}
	
    void consumer(Object obj) {
    	Request req = (Request) obj;
    	req.setCallBackHandler(handleRequestResult);
    	req.run();
    	
    }
    
    Handler handleRequestResult = new Handler() {
    	
    	 public void 	handleMessage(Message msg) {
    		 mServiceHandler.sendMessage(msg); 
    	 }
    };
    
    void stopRunning() {
    	mKeepRunning = false;
    }
}