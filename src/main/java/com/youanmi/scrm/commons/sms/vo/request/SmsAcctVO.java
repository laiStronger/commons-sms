/*
 * 文件名：SmsAcctVO.java
 * 版权：Copyright 2014 youanmi Tech. Co. Ltd. All Rights Reserved. 
 * 描述： SmsAcct.java
 * 修改人：刘红艳
 * 修改时间：2014年12月10日
 * 修改内容：新增
 */
package com.youanmi.scrm.commons.sms.vo.request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 登录短信平台的账户。
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
public class SmsAcctVO {
    /**
     * 调测日志记录器。
     */
    private static final Logger LOG = LoggerFactory.getLogger(SmsAcctVO.class);

    // 企业id
    private String userid;

    // 发送用户帐号
    private String account;

    // 发送帐号密码
    private String password;


    public String getUserid() {
        return userid;
    }


    public void setUserid(String userid) {
        this.userid = userid;
    }


    public String getAccount() {
        return account;
    }


    public void setAccount(String account) {
        this.account = account;
    }


    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        this.password = password;
    }
}
