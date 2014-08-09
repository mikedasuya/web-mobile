
package com.househelper.service;


interface ICallBack {
    /** Request the process ID of this service, to do evil things with it. */
    int uploadRequest(long requestId, String file, int progress);
    
}