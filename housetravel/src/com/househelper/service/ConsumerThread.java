package com.househelper.service;

import java.util.concurrent.BlockingQueue;

public class ConsumerThread implements Runnable {
	BlockingQueue q;
	ConsumerThread(BlockingQueue qu) {
		q = qu;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
		       while (true) { 
		    	   consumer(q.take()); 
		       }
		     } catch (InterruptedException ex) { 
		    	 
		     }
		   

	}
	
    void consumer(Object obj) {
    	Request req = (Request) obj;
    	req.run();
    }
}