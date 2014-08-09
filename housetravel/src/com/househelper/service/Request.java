package com.househelper.service;

public interface Request extends Runnable {
	public int getType(); 
	void run();
}