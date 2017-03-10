/*
 * 文件名：SmsClient.java
 * 版权：Copyright 2014 youanmi Tech. Co. Ltd. All Rights Reserved. 
 * 描述： SmsClient.java
 * 修改人：刘红艳
 * 修改时间：2014年12月10日
 * 修改内容：新增
 */
package com.youanmi.scrm.commons.sms.communcation;

import java.io.IOException;
import java.util.List;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.youanmi.scrm.commons.sms.communcation.vo.ReqParameterVO;
import com.youanmi.scrm.commons.sms.communcation.vo.SmsHostConfigVO;


/**
 * 与Sms短信平台通信类。
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
public class SmsClient {
    /**
     * 调测日志记录器。
     */
    private static final Logger LOG = LoggerFactory.getLogger(SmsClient.class);

    private static final String CHARSET = "UTF-8";

    private static SmsClient instance = new SmsClient();


    private SmsClient() {
        super();
    }


    /**
     * 
     * 获取 与Sms短信平台通信实体。
     * 
     * @return
     */
    public static SmsClient getInstance() {
        return instance;
    }


    /**
     * 
     * 向WEB服务器发送POST请求。
     * 
     * @param config
     *            服务器配置信息
     * @param parameters
     *            请求参数集合
     * @return 请求后服务器响应的全部报文信息
     * @throws IOException
     * @throws HttpException
     */
    public String request(SmsHostConfigVO config, List<ReqParameterVO> parameters) throws IOException {
        String responsStr = null;
        HttpClient hc;
        PostMethod pm = null;
        try {
            hc = new HttpClient();
            hc.getHttpConnectionManager().getParams().setConnectionTimeout(config.getTimeout());

            pm = new PostMethod(config.getUrl());
            LOG.info("Request sms server url:" + config.getUrl());
            // Retry when request failed ,default is retry 3 time.
            pm.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
                new DefaultHttpMethodRetryHandler(config.getRetry(), true));
            // 设置编码
            pm.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, CHARSET);
            // post提交的表单信息
            NameValuePair[] nvps = makeParams(parameters);
            // 将键值对放到post请求里
            pm.setRequestBody(nvps);
            // httpclient发送post请求
            int exeStatus = hc.executeMethod(pm);
            if (exeStatus != 200) {
                throw new HttpException("SMS Server error!Response status code:" + exeStatus);
            }
            else {
                // 请求应答封装响应
                responsStr = pm.getResponseBodyAsString();
                LOG.info("Request sms server response:" + responsStr);
            }
        }
        finally {
            if (pm != null) {
                pm.releaseConnection();
            }
        }
        return responsStr;
    }

    /**
     * 
     * 向WEB服务器发送POST请求。
     * 
     * @param config
     *            服务器配置信息
     * @param parameters
     *            请求参数集合
     * @return 请求后服务器响应的全部报文信息
     * @throws IOException
     * @throws HttpException
     */
    public String requestByGet(SmsHostConfigVO config, List<ReqParameterVO> parameters,String charset) throws IOException {
        String responsStr = null;
        HttpClient hc;
        YAMGetMethod pm = null;
        try {
            hc = new HttpClient();
            hc.getHttpConnectionManager().getParams().setConnectionTimeout(config.getTimeout());

            pm = new YAMGetMethod(config.getUrl(),charset);
            LOG.info("Request sms server url:" + config.getUrl());
            // Retry when request failed ,default is retry 3 time.
            pm.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
                new DefaultHttpMethodRetryHandler(config.getRetry(), true));
            // 设置编码
            pm.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, charset);
            // post提交的表单信息
            NameValuePair[] nvps = makeParams(parameters);
            // 将键值对放到post请求里
            pm.setQueryString(nvps);
            // httpclient发送post请求
            int exeStatus = hc.executeMethod(pm);
            if (exeStatus != 200) {
                throw new HttpException("SMS Server error!Response status code:" + exeStatus);
            }
            else {
                // 请求应答封装响应
                responsStr = pm.getResponseBodyAsString();
                LOG.info("Request sms server response:" + responsStr);
            }
        }
        finally {
            if (pm != null) {
                pm.releaseConnection();
            }
        }
        return responsStr;
    }
    /**
     * 
     * 组合请求参数。
     * 
     * @param parameters
     * @return
     */
    private NameValuePair[] makeParams(List<ReqParameterVO> parameters) {
        if (parameters == null) {
            return null;
        }
        int size = parameters.size();
        NameValuePair[] pair = new NameValuePair[size];

        for (int i = 0; i < size; i++) {
            ReqParameterVO pram = parameters.get(i);
            pair[i] = new NameValuePair(pram.getName(), pram.getValue());
        }
        return pair;
    }

}
