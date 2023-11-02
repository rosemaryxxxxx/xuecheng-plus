package com.xuecheng.base.execption;

/**
 * @Author rosemaryxxxxx
 * @Date 2023/11/1 16:38
 * @PackageName:com.xuecheng.base.execption
 * @ClassName: ResultError
 * @Description: TODO
 * @Version 1.0
 */
public class ResultError {
    private String errCode;
    private String errMessage;

    public ResultError(String errCode, String errMessage) {
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
