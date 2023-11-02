package com.xuecheng.base.execption;

import java.io.Serializable;

/**
 * 错误响应参数包装
 */
public class RestErrorResponse implements Serializable {

    private String errCode;
    private String errMessage;

//    public RestErrorResponse(String errMessage){
//        this.errMessage= errMessage;
//    }
//
//    public String getErrMessage() {
//        return errMessage;
//    }
//
//    public void setErrMessage(String errMessage) {
//        this.errMessage = errMessage;
//    }


    public RestErrorResponse(String errCode, String errMessage) {
        this.errCode = errCode;
        this.errMessage = errMessage;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrMessage() {
        return errMessage;
    }

    public void setErrMessage(String errMessage) {
        this.errMessage = errMessage;
    }
}
