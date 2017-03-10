/*
 * 文件名：SmsHostConfigVO.java
 * 版权：Copyright 2014 youanmi Tech. Co. Ltd. All Rights Reserved. 
 * 描述： SmsHostConfig.java
 * 修改人：刘红艳
 * 修改时间：2014年12月10日
 * 修改内容：新增
 */
package com.youanmi.scrm.commons.sms.communcation.vo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 短信平台URl等相关信息。
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
public class SmsHostConfigVO {
    /**
     * 调测日志记录器。
     */
    private static final Logger LOG = LoggerFactory.getLogger(SmsHostConfigVO.class);
    // 请求的URl 或者IP
    private String url;
    // 请求超时时长（毫秒为单位）
    private int timeout;
    // 请求失败后最大尝试次数
    private int retry;


    public String getUrl() {
        return url;
    }


    public void setUrl(String url) {
        this.url = url;
    }


    public int getTimeout() {
        return timeout;
    }


    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }


    public int getRetry() {
        return retry;
    }


    public void setRetry(int retry) {
        this.retry = retry;
    }
}
