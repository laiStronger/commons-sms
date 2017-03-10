/*
 * 文件名：GSTSmsService.java
 * 版权：深圳柚安米科技有限公司版权所有
 * 描述： GaoSiTongSmsService.java
 * 修改人：刘红艳
 * 修改时间：2015年11月11日
 * 修改内容：新增
 */
package com.youanmi.scrm.commons.sms.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.youanmi.scrm.commons.sms.communcation.SmsClient;
import com.youanmi.scrm.commons.sms.communcation.vo.ReqParameterVO;
import com.youanmi.scrm.commons.sms.communcation.vo.SmsHostConfigVO;
import com.youanmi.scrm.commons.sms.conf.SmsConfBuilder;
import com.youanmi.scrm.commons.sms.constants.SmsConstants;
import com.youanmi.scrm.commons.sms.service.ISmsService;
import com.youanmi.scrm.commons.sms.vo.request.SmsInfoVO;
import com.youanmi.scrm.commons.sms.vo.response.ReceiveSmsVO;
import com.youanmi.scrm.commons.sms.vo.response.SmsReceiveStatusVO;
import com.youanmi.scrm.commons.sms.vo.response.SmsSendStatusVO;
import com.youanmi.scrm.commons.util.number.NumberUtils;
import com.youanmi.scrm.commons.util.string.AssertUtils;


/**
 * 高斯通短信对接。
 * <p>
 * 
 * 
 * @author 刘红艳
 * @since 2.2.4
 */
public class GSTSmsService implements ISmsService {
    /**
     * 调测日志记录器。
     */
    private static final Logger LOG = LoggerFactory.getLogger(GSTSmsService.class);

    // 与SMS通信对象
    private SmsClient smsClient;

    private static GSTSmsService instance = new GSTSmsService();

    /**
     * 
     * 构造函数。 需要从配置文件中初始化配置参数。
     */
    private GSTSmsService() {
        smsClient = SmsClient.getInstance();
        initSmsHostConfig();
        initSmsAcct();
    }

    /**
     * 获取smsService实例
     * 
     * @return
     */
    public static GSTSmsService getInstance() {
        return instance;
    }

    /**
     * 发送短信URl配置信息。
     * 
     * @return
     */
    private SmsHostConfigVO initSmsHostConfig() {
        int iRetry = NumberUtils.parseInt(SmsConfBuilder.getProperties(SmsConstants.GST.SMS_GST_RETRY));
        int iTimeout = NumberUtils.parseInt(SmsConfBuilder.getProperties(SmsConstants.GST.SMS_GST_TIMEOUT));
        String strHost = SmsConfBuilder.getProperties(SmsConstants.GST.SMS_GST_HOST);
        SmsHostConfigVO smsSendConfig = new SmsHostConfigVO();
        smsSendConfig.setUrl(String.format(SmsConstants.GST.SMS_URL_SEND, new Object[] {strHost}));
        smsSendConfig.setRetry(iRetry);
        smsSendConfig.setTimeout(iTimeout);
        return smsSendConfig;
    }

    /**
     * 
     * 添加方法注释。
     *
     */
    private SmsGstAcctVO initSmsAcct() {
        SmsGstAcctVO smsAcct = new SmsGstAcctVO();
        smsAcct.setUsername(SmsConfBuilder.getProperties(SmsConstants.GST.SMS_GST_USERNAME));
        smsAcct.setPassword(SmsConfBuilder.getProperties(SmsConstants.GST.SMS_GST_PASSWORD));
        smsAcct.setFrom(SmsConfBuilder.getProperties(SmsConstants.GST.SMS_GST_FROM));
        smsAcct.setExpandPrefix(SmsConfBuilder.getProperties(SmsConstants.GST.SMS_GST_EXPAND_PREFIX));
        return smsAcct;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SmsSendStatusVO sendSms(SmsInfoVO smsInfo) throws IOException {
        SmsSendStatusVO statusVO = null;

        String strXmlResp =
                smsClient.requestByGet(initSmsHostConfig(), makeSendSmsParams(smsInfo),
                    SmsConstants.GST.CHARSET);
        statusVO = readSendStatusVO(strXmlResp);
        return statusVO;
    }

    /**
     * 
     * 发短信时的请求参数，内容自动加上签名信息。
     * 
     * @param smsInfo
     * @return
     */
    private List<ReqParameterVO> makeSendSmsParams(SmsInfoVO smsInfo) {
        if (smsInfo == null) {
            throw new RuntimeException("SmsInfo is null.");
        }
        List<ReqParameterVO> parameterVOs = makeCommonReqParams();
        parameterVOs.add(new ReqParameterVO(SmsConstants.GST.REQ_PARAM_TO, smsInfo.getMobile()));
        // 应商家需求，签名放短信内容前面
        parameterVOs.add(new ReqParameterVO(SmsConstants.GST.REQ_PARAM_CONTENT, smsInfo.getContent()));
        // 非空定时发送
        if (!AssertUtils.isNull(smsInfo.getSendTime())) {
            parameterVOs
                .add(new ReqParameterVO(SmsConstants.GST.REQ_PARAM_PRESENDTIME, smsInfo.getSendTime()));
        }
        else {
            parameterVOs.add(new ReqParameterVO(SmsConstants.GST.REQ_PARAM_PRESENDTIME, ""));
        }
        return parameterVOs;
    }

    /**
     * 
     * 组合公用的请求参数,企业账户信息。
     * 
     * @return
     */
    private List<ReqParameterVO> makeCommonReqParams() {
        SmsGstAcctVO smsAcct = initSmsAcct();
        List<ReqParameterVO> parameterVOs = new ArrayList<ReqParameterVO>();
        parameterVOs.add(new ReqParameterVO(SmsConstants.GST.REQ_PARAM_USERNAME, smsAcct.getUsername()));
        parameterVOs.add(new ReqParameterVO(SmsConstants.GST.REQ_PARAM_PASSWORD, smsAcct.getPassword()));
        parameterVOs.add(new ReqParameterVO(SmsConstants.GST.REQ_PARAM_FROM, smsAcct.getFrom()));
        parameterVOs.add(new ReqParameterVO(SmsConstants.GST.REQ_PARAM_EXPANDPREFIX, smsAcct
            .getExpandPrefix()));
        return parameterVOs;
    }

    /**
     * 解析返回值。<li>
     * 例1-OK:325689 表示短信发送成功 发送成功的最后一条短信ID为325689<li>
     * 例2-ERROR:eUser 表示短信发送不成功，出错原因是因为用户名称有误<li>
     * 
     * @param respString
     * @return
     */
    private SmsSendStatusVO readSendStatusVO(String resp) {
        if (AssertUtils.isNull(resp)) {
            return null;
        }
        String respString = resp.replace("\r\n", "");// 替换对方的换行符号
        LOG.info("sms gst respString is " + respString);
        SmsSendStatusVO statusVO = null;
        // 返回状态值：成功返回Success 失败返回：Faild
        String status = "Faild";
        // ok提交成功 ,其他的都是失败的提示信息
        String message = null;
        // 返回本次任务的序列ID
        String taskID = null;
        // 成功短信数：当成功后返回提交成功短信数,默认为0
        String successCounts = "0";
        String resps[] = respString.split(":");
        if (resps.length < 2) {
            status = SmsConstants.Result.SMS_SEND_FAILD;
            message = respString;
        }
        else {
            if (resps[0].equals(SmsConstants.GST.SEND_SUCCESS)) {
                status = SmsConstants.Result.SMS_SEND_SUCESS;
                message = SmsConstants.GST.SEND_SUCCESS.toLowerCase();
                taskID = resps[1];
                successCounts = "1";
            }
            else {
                status = "Faild";
                message = resps[1];
            }
        }
        statusVO = new SmsSendStatusVO(status, message, null, taskID, successCounts);
        return statusVO;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ReceiveSmsVO> queryReceiveSms() throws IOException {
        // Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SmsReceiveStatusVO> querySmsReceiveStatus() throws IOException {
        // Auto-generated method stub
        return null;
    }

}


/***
 * 企业账户（对方分配）
 * 
 * @author nn
 *
 */
class SmsGstAcctVO {
    /** ISP登陆名(公司分配给ISP) 非空，如果为机构版，则应以“机构ID:登陆名”方式赋值 */
    private String username;
    /** 发送帐号密码 */
    private String password;
    /** 发短信的端口扩展号 */
    private String from;
    /**
     * 用户下行短信自扩展端口，可为空，只接受数字。<br/>
     * 其该端口的使用需要相关机构的指定配置。 （该字段单独最大长度为14位，但下行短信实际发送后会根据SP号进行相关的截取操作）
     */
    private String expandPrefix;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getExpandPrefix() {
        return expandPrefix;
    }

    public void setExpandPrefix(String expandPrefix) {
        this.expandPrefix = expandPrefix;
    }
}