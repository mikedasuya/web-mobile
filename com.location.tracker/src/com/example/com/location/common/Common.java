package com.example.com.location.common;

import java.net.URI;

public class Common {
	public static final int time = 500;
	public static final int MAXIMUM_COUNT = 10;
	public static final int ERROR_SERVER_PROBLEM = 11;
	public static final int START_TRACKING = 12;
	public static final int GEO_NOT_FOUND = 13;
	public static final int STOP_TRACKING = 14;
	public static final int STOP_WORKER_THREAD = 15;
	public static CharSequence text = "No Wifi/data, restart location tracker";
	public static String url = "http://162.243.3.123:8080/location/Main";
	public enum EVENT {
		EVENT_START_TRACKING(0),
	    EVENT_STOP_TRACKING(1),
	    EVENT_ERROR (2);
		
	    private int id;
		EVENT(int val) {
			id = val;
		}
		public int getVal() {
			return id;
		}
	    
	};
	public enum STATE {
		STATE_INITIAL(0),
	    STATE_TRACKING(1),
	    STATE_ERROR(2);
	    
		
	    private int id;
		STATE(int val) {
			id = val;
		}
		public int getVal() {
			return id;
		}
			    
	};
	/*                  EVENT_START_TRACKING  EVENT_STOP_TRACKING    EVENT_STATE_ERROR EVENT_STATE_EXIT
	 *                  *******************************************************************************
	 * STATE_INITIAL    STATE_TRACKING        STATE_INITIAL          STATE_INITIAL      STATE_EXIT    
	 * STATE_TRACKING   STATE_TRACKING        STATE_INITIAL          STATE_INITIAL      STATE_EXIT
	 * STATE_EXIT       STATE_EXIT            STATE_EXIT             STATE_EXIT         STATE_EXIT                 
	 */
	public static int ar[][] = {{ 1,0,2},
			        { 1,0,2},
			        {2,2,2}
				};
	
}