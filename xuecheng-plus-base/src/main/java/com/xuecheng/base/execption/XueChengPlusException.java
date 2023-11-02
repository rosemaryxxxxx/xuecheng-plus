package com.xuecheng.base.execption;

/**
 * @description 学成在线项目异常类，和前端约定返回的异常信息
 */
public class XueChengPlusException extends RuntimeException {

    private String errCode;
    private String errMessage;

    public XueChengPlusException() {
        super();
    }

//    public XueChengPlusException(String errMessage) {
//        super(errMessage);
//        this.errMessage = errMessage;
//    }
//
//    public String getErrMessage() {
//        return errMessage;
//    }
//
//    public static void cast(CommonError commonError) {
//        throw new XueChengPlusException(commonError.getErrMessage());
//    }
//
//    public static void cast(String errMessage) {
//        throw new XueChengPlusException(errMessage);
//    }


    public XueChengPlusException(String errCode, String errMessage) {
        this.errCode = errCode;
        this.errMessage = errMessage;
    }

    public XueChengPlusException(String message, String errCode, String errMessage) {
        super(message);
        this.errCode = errCode;
        this.errMessage = errMessage;
    }

    public XueChengPlusException(String message, Throwable cause, String errCode, String errMessage) {
        super(message, cause);
        this.errCode = errCode;
        this.errMessage = errMessage;
    }

    public XueChengPlusException(Throwable cause, String errCode, String errMessage) {
        super(cause);
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

        public static void cast(CommonError commonError) {
        throw new XueChengPlusException(commonError.getErrCode(), commonError.getErrMessage());
    }

    public static void cast(String errCode,String errMessage) {
        throw new XueChengPlusException(errCode,errMessage);
    }

}

