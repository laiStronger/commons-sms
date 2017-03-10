/*
 * 文件名：ISmsService.java
 * 版权：Copyright 2014 youanmi Tech. Co. Ltd. All Rights Reserved. 
 * 描述： ISmsService.java
 * 修改人：刘红艳
 * 修改时间：2014年12月10日
 * 修改内容：新增
 */
package com.youanmi.scrm.commons.sms.service;

import java.io.IOException;
import java.util.List;

import org.apache.commons.httpclient.HttpException;

import com.youanmi.scrm.commons.sms.vo.request.SmsInfoVO;
import com.youanmi.scrm.commons.sms.vo.response.ReceiveSmsVO;
import com.youanmi.scrm.commons.sms.vo.response.SmsReceiveStatusVO;
import com.youanmi.scrm.commons.sms.vo.response.SmsSendStatusVO;


/**
 * 与短信平台通信交互接口。
 * <p>
 * 
 * <li>1.发送短信。
 * <li>2.批量查询用户收到短信的结果。
 * <li>3.批量查询用户发送的短信信息。
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
public interface ISmsService {

    /**
     * 
     * 发送短信。
     * 
     * @param smsInfo
     *            短信参数
     * @return
     * @throws IOException
     * @throws HttpException
     */
    SmsSendStatusVO sendSms(SmsInfoVO smsInfo) throws IOException;


    /**
     * <p>
     * 批量查询用户收到短信的结果。
     * 
     * @return
     * @throws IOException
     * @throws HttpException
     */
    List<ReceiveSmsVO> queryReceiveSms() throws IOException;


    /**
     * <p>
     * 批量查询用户发送的短信信息。
     * 
     * @return
     * @throws IOException
     * @throws HttpException
     */
    List<SmsReceiveStatusVO> querySmsReceiveStatus() throws IOException;
}
