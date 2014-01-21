package com.example.com.location.tracker.service;

import com.example.com.location.tracker.service.ICallBack;

interface IServerConnection {
    boolean startTracking(String email);
    boolean stopTracking(String email);
	boolean registerCallBack(ICallBack cb);
	boolean unregister(ICallBack cb);
	boolean isTracking();
	boolean syncUI();
}