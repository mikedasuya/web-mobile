package com.example.com.location.common;

import java.net.URI;

public class Common {
	public static final int time = 500;
	public static final int MAXIMUM_COUNT = 10;
	public static final int ERROR_SERVER_PROBLEM = 1;
	public static final int START_TRACKING = 2;
	public static final int GEO_NOT_FOUND = 3;
	public static final int STOP_TRACKING = 4;
	public static CharSequence text = "No Wifi/data, restart location tracker";
	public static String url = "http://162.243.3.123:8080/location/Main";
	public enum EVENT {
		EVENT_START_TRACKING(1),
	    EVENT_STOP_TRACKING(2),
	    EVENT_ERROR (3);
		
	    private int id;
		EVENT(int val) {
			id = val;
		}
		public int getVal() {
			return id;
		}
	    
	};
	public enum STATE {
		STATE_INITIAL(1),
	    STATE_TRACKING(2),
	    STATE_ERROR(3);
	    
		
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
	public static int ar[][] = {{ 2,1,3},
			        { 2,1,3},
			        {3,3,3}
				};
	
}