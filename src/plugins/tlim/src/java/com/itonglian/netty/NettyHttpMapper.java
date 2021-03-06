package com.itonglian.netty;

import com.itonglian.netty.impl.*;
import com.itonglian.utils.StringConstants;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyHttpMapper {

    private NettyHttpActor nettyHttpActor;

    private String path;

    private String jsonValue;


    public NettyHttpMapper(String path,String jsonValue) {
        this.path = parsePath(path);
        this.jsonValue = jsonValue;
        log.debug("请求已达NettyHttpMapper...,请求方法["+this.path+"]");
        switch (this.path){
            case "systemMessage":
            case "approval":
                nettyHttpActor = new SystemMessageActor();
                break;
            case "message":
                nettyHttpActor = new MessageActor();
                break;
            case "addSession":
                nettyHttpActor = new AddSessionActor();
                break;
            case "clearChatHistory":
                nettyHttpActor = new ClearChatHistoryActor();
                break;
            case "deleteSession":
                nettyHttpActor = new DeleteSessionActor();
                break;
            case "dissolved":
                nettyHttpActor = new DissolvedActor();
                break;
            case "getOffline":
                nettyHttpActor = new GetOfflineActor();
                break;
            case "modifySession":
                nettyHttpActor = new ModifySessionActor();
                break;
            case "registerAppPushCode":
                nettyHttpActor = new RegisterAppPushCodeActor();
                break;
            default:
                break;
        }
    }
    public boolean execute(){
        return nettyHttpActor.execute(jsonValue);
    }

    private String parsePath(String path){
        int start = path.indexOf(StringConstants.NETTY)+StringConstants.NETTY.length();
        int end = path.indexOf("?");
        if(end!=-1){
            return path.substring(start,end);
        }
        return path.substring(start);
    }

}
