/*
 * 文件名：ReqParameterVO.java
 * 版权：Copyright 2014 youanmi Tech. Co. Ltd. All Rights Reserved. 
 * 描述： RequestParameter.java
 * 修改人：刘红艳
 * 修改时间：2014年12月10日
 * 修改内容：新增
 */
package com.youanmi.scrm.commons.sms.communcation.vo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * HTTP请求参数。
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
public class ReqParameterVO {
    /**
     * 调测日志记录器。
     */
    private static final Logger LOG = LoggerFactory.getLogger(ReqParameterVO.class);

    // 参数名
    private String name;

    // 参数值
    private String value;


    /**
     * 构造函数。
     * 
     */
    public ReqParameterVO() {
        super();
    }


    /**
     * 构造函数。
     * 
     * @param name
     * @param value
     */
    public ReqParameterVO(String name, String value) {
        super();
        this.name = name;
        this.value = value;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getValue() {
        return value;
    }


    public void setValue(String value) {
        this.value = value;
    }
}
