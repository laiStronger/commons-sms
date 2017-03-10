/*
 * 文件名：ReceiveSmsVO.java
 * 版权：Copyright 2014 youanmi Tech. Co. Ltd. All Rights Reserved. 
 * 描述： ReceiveSms.java
 * 修改人：刘红艳
 * 修改时间：2014年12月10日
 * 修改内容：新增
 */
package com.youanmi.scrm.commons.sms.vo.response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 上行收到的短信。
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
public class ReceiveSmsVO {
    /**
     * 调测日志记录器。
     */
    private static final Logger LOG = LoggerFactory.getLogger(ReceiveSmsVO.class);

    // 对应的手机号码
    private String mobile;

    // 同一批任务ID
    private String taskID;

    // 上行内容
    private String content;

    // 接收时间
    private String receiveTime;


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


    public String getContent() {
        return content;
    }


    public void setContent(String content) {
        this.content = content;
    }


    public String getReceiveTime() {
        return receiveTime;
    }


    public void setReceiveTime(String receiveTime) {
        this.receiveTime = receiveTime;
    }
}
