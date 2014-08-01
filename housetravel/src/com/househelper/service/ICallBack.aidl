
package com.househelper.service;


interface ICallBack {
    /** Request the process ID of this service, to do evil things with it. */
    int uploadRequest(int requestId, String file, int progress);
    
}