/*
 * 文件名：SmsErrorStatusVO.java
 * 版权：Copyright 2014 youanmi Tech. Co. Ltd. All Rights Reserved. 
 * 描述： SmsErrorStatus.java
 * 修改人：刘红艳
 * 修改时间：2014年12月10日
 * 修改内容：新增
 */
package com.youanmi.scrm.commons.sms.vo.response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 错误信息。
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
public class SmsErrorStatusVO {
    /**
     * 调测日志记录器。
     */
    private static final Logger LOG = LoggerFactory.getLogger(SmsErrorStatusVO.class);

    // 1：用户名或密码不能为空
    // 2：用户名或密码错误
    // 3：该用户不允许查看状态报告
    // 4：参数不正确
    // 错误码
    private String error;

    // 错误描述
    private String remark;


    public String getError() {
        return error;
    }


    public void setError(String error) {
        this.error = error;
    }


    public String getRemark() {
        return remark;
    }


    public void setRemark(String remark) {
        this.remark = remark;
    }
}
