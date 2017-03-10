/*
 * 文件名：SmsSendStatusVO.java
 * 版权：Copyright 2014 youanmi Tech. Co. Ltd. All Rights Reserved. 
 * 描述： SmsSendStatus.java
 * 修改人：刘红艳
 * 修改时间：2014年12月10日
 * 修改内容：新增
 */
package com.youanmi.scrm.commons.sms.vo.response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 使用短信平台接口发送短信给用户后，返回的发送结果。
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
public class SmsSendStatusVO {
    /**
     * 调测日志记录器。
     */
    private static final Logger LOG = LoggerFactory.getLogger(SmsSendStatusVO.class);

    // 返回状态值：成功返回Success 失败返回：Faild
    private String status;

    // ok提交成功 ,其他的都是失败的提示信息
    private String message;

    // 返回余额
    private String remainpoint;

    // 返回本次任务的序列ID
    private String taskID;

    // 成功短信数：当成功后返回提交成功短信数
    private String successCounts;


    /**
     * 构造函数。
     * 
     */
    public SmsSendStatusVO() {
        super();
    }


    /**
     * 构造函数。
     * 
     * @param status
     * @param message
     * @param remainpoint
     * @param taskID
     * @param successCounts
     */
    public SmsSendStatusVO(String status, String message, String remainpoint, String taskID,
            String successCounts) {
        super();
        this.status = status;
        this.message = message;
        this.remainpoint = remainpoint;
        this.taskID = taskID;
        this.successCounts = successCounts;
    }


    public String getStatus() {
        return status;
    }


    public void setStatus(String status) {
        this.status = status;
    }


    public String getMessage() {
        return message;
    }


    public void setMessage(String message) {
        this.message = message;
    }


    public String getRemainpoint() {
        return remainpoint;
    }


    public void setRemainpoint(String remainpoint) {
        this.remainpoint = remainpoint;
    }


    public String getTaskID() {
        return taskID;
    }


    public void setTaskID(String taskID) {
        this.taskID = taskID;
    }


    public String getSuccessCounts() {
        return successCounts;
    }


    public void setSuccessCounts(String successCounts) {
        this.successCounts = successCounts;
    }
}
