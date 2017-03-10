/*
 * 文件名：SmsInfoVO.java
 * 版权：Copyright 2014 youanmi Tech. Co. Ltd. All Rights Reserved. 
 * 描述： SmsInfo.java
 * 修改人：刘红艳
 * 修改时间：2014年12月10日
 * 修改内容：新增
 */
package com.youanmi.scrm.commons.sms.vo.request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 短信消息。
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
public class SmsInfoVO {
    /**
     * 调测日志记录器。
     */
    private static final Logger LOG = LoggerFactory.getLogger(SmsInfoVO.class);

    // 发信发送的目的号码.多个号码之间用半角逗号隔开
    private String mobile;

    // 短信的内容，内容需要UTF-8编码
    private String content;

    // 为空表示立即发送，定时发送格式2010-10-24 09:08:10
    private String sendTime;


    // private String action;//交易类型（send or query）
    //
    // private String extno;//请先询问配置的通道是否支持扩展子号，如果不支持，请填空。子号只能为数字，且最多5位数。

    public String getMobile() {
        return mobile;
    }


    public void setMobile(String mobile) {
        this.mobile = mobile;
    }


    public String getContent() {
        return content;
    }


    public void setContent(String content) {
        this.content = content;
    }


    public String getSendTime() {
        return sendTime;
    }


    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }
}
