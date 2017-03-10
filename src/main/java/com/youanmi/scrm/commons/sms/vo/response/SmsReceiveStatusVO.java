/*
 * 文件名：SmsReceiveStatusVO.java
 * 版权：Copyright 2014 youanmi Tech. Co. Ltd. All Rights Reserved. 
 * 描述： SmsReceiveStatus.java
 * 修改人：刘红艳
 * 修改时间：2014年12月10日
 * 修改内容：新增
 */
package com.youanmi.scrm.commons.sms.vo.response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 发送短信后，用户收到短信的状态。
 * <p>
 * 
 * <p>
 * 
 * 
 * <pre>
 * </pre>
 * 
 * @author 刘红艳
 * @version YouAnMi-OTO 2014年12月10日
 * @since YouAnMi-OTO
 */
public class SmsReceiveStatusVO {
    /**
     * 调测日志记录器。
     */
    private static final Logger LOG = LoggerFactory.getLogger(SmsReceiveStatusVO.class);

    // 对应的手机号码
    private String mobile;

    // 同一批任务ID
    private String taskID;

    // 状态报告10：发送成功，20：发送失败
    private String status;

    // 接收时间
    private String receiveTime;

    // 状态返回值
    private String errorCode;


    public String getMobile() {
        return mobile;
    }


    public void setMobile(String mobile) {
        this.mobile = mobile;
    }


    public String getTaskID() {
        return taskID;
    }


    public void setTaskID(String taskID) {
        this.taskID = taskID;
    }


    public String getStatus() {
        return status;
    }


    public void setStatus(String status) {
        this.status = status;
    }


    public String getReceiveTime() {
        return receiveTime;
    }


    public void setReceiveTime(String receiveTime) {
        this.receiveTime = receiveTime;
    }


    public String getErrorCode() {
        return errorCode;
    }


    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
