
package com.househelper.service;
import com.househelper.service.ICallBack;

interface IUploadRequest {
    
    int uploadFilePath(in String url, in String folderName, in String file, in ICallBack cb );
    int uploadEntry(in String url, in int id, in ICallBack cb );
    int uploadDbEntry(in String url, int Map, in ICallBack cb);

    
}