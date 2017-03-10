/*
 * 文件名：YAMGetMethod.java
 * 版权：深圳柚安米科技有限公司版权所有
 * 描述： YAMGetMethod.java
 * 修改人：刘红艳
 * 修改时间：2015年11月12日
 * 修改内容：新增
 */
package com.youanmi.scrm.commons.sms.communcation;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.util.EncodingUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 重写setQueryString 方法。<br/>
 * 
 * 修正<link>GetMethod.setQueryString</link>其编码格式不能用utf-8外的其他编码格式BUG。
 * <p>
 * 
 * @author 刘红艳
 * @since 2.2.4
 */
public class YAMGetMethod extends GetMethod {
    /**
     * 调测日志记录器。
     */
    private static final Logger LOG = LoggerFactory.getLogger(YAMGetMethod.class);

    private String charset;


    /**
     * 构造函数。
     * 
     * @param url
     *            请求地址
     * @param charset
     *            交互编码格式
     */
    public YAMGetMethod(String url, String charset) {
        super(url);
        this.charset = charset;
    }

    /**
     * 重写修正其不能使用UTF-8外的编码
     * {@inheritDoc}
     */
    public void setQueryString(NameValuePair[] params) {
        LOG.trace("enter HttpMethodBase.setQueryString(NameValuePair[])");
        String queryString = EncodingUtil.formUrlEncode(params, charset);
        super.setQueryString(queryString);
    }
}
