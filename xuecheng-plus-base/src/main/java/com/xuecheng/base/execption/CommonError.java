package com.xuecheng.base.execption;

/**
 * @description 通用错误信息
 */
public enum CommonError {

    UNKOWN_ERROR("120401","执行过程异常，请重试。"),
    PARAMS_ERROR("120402","非法参数"),
    OBJECT_NULL("120403","对象为空"),
    QUERY_NULL("120404","查询结果为空"),
    REQUEST_NULL("120405","请求参数为空"),
    DELETE_ERROR("120409","课程计划还有子级信息，无法操作");

    private String errCode;
    private String errMessage;

    public String getErrMessage() {
        return errMessage;
    }

    public String getErrCode() {
        return errCode;
    }

    private CommonError(String errCode ,String errMessage) {
        this.errCode = errCode;
        this.errMessage = errMessage;
    }

}