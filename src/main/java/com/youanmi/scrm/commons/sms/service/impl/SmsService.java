/*
 * 文件名：SmsService.java
 * 版权：Copyright 2014 youanmi Tech. Co. Ltd. All Rights Reserved. 
 * 描述： SmsService.java
 * 修改人：刘红艳
 * 修改时间：2014年12月10日
 * 修改内容：新增
 */
package com.youanmi.scrm.commons.sms.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.HttpException;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.youanmi.scrm.commons.sms.communcation.SmsClient;
import com.youanmi.scrm.commons.sms.communcation.vo.ReqParameterVO;
import com.youanmi.scrm.commons.sms.communcation.vo.SmsHostConfigVO;
import com.youanmi.scrm.commons.sms.conf.SmsConfBuilder;
import com.youanmi.scrm.commons.sms.service.ISmsService;
import com.youanmi.scrm.commons.sms.vo.request.SmsAcctVO;
import com.youanmi.scrm.commons.sms.vo.request.SmsInfoVO;
import com.youanmi.scrm.commons.sms.vo.response.ReceiveSmsVO;
import com.youanmi.scrm.commons.sms.vo.response.SmsReceiveStatusVO;
import com.youanmi.scrm.commons.sms.vo.response.SmsSendStatusVO;
import com.youanmi.scrm.commons.util.number.NumberUtils;
import com.youanmi.scrm.commons.util.string.AssertUtils;
import com.youanmi.scrm.commons.util.xml.XmlReaderUtils;


/**
 * 与短信平台通信交互。
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
public class SmsService implements ISmsService {
    /**
     * 调测日志记录器。
     */
    private static final Logger LOG = LoggerFactory.getLogger(SmsService.class);

    /** 短信平台的配置信息参数名 */
    /** 短信服务器主机（IP or 网址） */
    private static final String SMSSERVER_HOST = "smsServer.host";

    /** 请求超时时长（毫秒为单位） */
    private static final String SMSSERVER_TIMEOUT = "smsServer.timeout";

    /** 请求失败后最大尝试次数 */
    private static final String SMSSERVER_RETRY = "smsServer.retry";

    /** 企业id */
    private static final String SMSSERVER_LOGIN_USERID = "smsServer.login.userId";

    /** 发送用户帐号 */
    private static final String SMSSERVER_LOGIN_ACCTNO = "smsServer.login.acctNo";

    /** 发送帐号密码 */
    private static final String SMSSERVER_LOGIN_PASSWORD = "smsServer.login.password";

    /** 短信签名 */
    private static final String SMSSERVER_LOGIN_SIGNATURE = "smsServer.login.signature";

    /** 短信平台的配置信息 END */

    /** 短信平台交互的编码格式 */
    private static final String SMS_SERVER_ENCODING = "UTF-8";

    /** 发送短信的URl */
    private static final String SMS_URL_SEND = "http://%s/sms.aspx";

    /** 查询用户接收短信状态的URl */
    private static final String SMS_URL_RECEIVE_STATUS = "http://%s/statusApi.aspx";

    /** 查询用户发送短信的URl */
    private static final String SMS_URL_CALL = "http://%s/callApi.aspx";

    /** 企业id */
    private static final String REQ_PARAM_USERID = "userid";

    /** 发送用户帐号 */
    private static final String REQ_PARAM_ACCOUNT = "account";

    /** 发送帐号密码 */
    private static final String REQ_PARAM_PASSWORD = "password";

    /** 全部被叫号码 */
    private static final String REQ_PARAM_MOBILE = "mobile";

    /** 发送内容 */
    private static final String REQ_PARAM_CONTENT = "content";

    /** 定时发送时间 */
    private static final String REQ_PARAM_SEND_TIME = "sendTime";

    /** 发送任务命令 */
    private static final String REQ_PARAM_ACTION = "action";

    /*
     * 扩展子号 unuse private static final String REQ_PARAM_EXTNO = "extno";
     */
    /** 查询请求 */
    private static final String REQ_ACTION_QUERY = "query";

    /** 发送短信请求 */
    private static final String REQ_ACTION_SEND = "send";

    /* 响应XML元素 */
    /* 发短信响应 */
    private static final String XML_ELE_RETURNSTATUS = "returnstatus";

    private static final String XML_ELE_MESSAGE = "message";

    private static final String XML_ELE_REMAINPOINT = "remainpoint";

    private static final String XML_ELE_TASKID = "taskID";

    private static final String XML_ELE_SUCCESSCOUNTS = "successCounts";

    /* 发短信响应 END */
    /* 查询错误响应 */
    private static final String XML_ELE_ERRORSTATUS_PARENT = "errorstatus";

    private static final String XML_ELE_ERROR_REMARK = "remark";

    private static final String XML_ELE_ERROR_STATUS = "error";

    /* 查询错误响应END */
    /* 查询用户接收短信状态响应 */
    private static final String XML_ELE_STATUSBOX = "statusbox";

    private static final String XML_ELE_STATUSBOX_MOBILE = "mobile";

    private static final String XML_ELE_STATUSBOX_TASKID = "taskid";

    private static final String XML_ELE_STATUSBOX_STATUS = "status";

    private static final String XML_ELE_STATUSBOX_RECEIVETIME = "receivetime";

    private static final String XML_ELE_STATUSBOX_ERRORCODE = "errorcode";

    /* 查询用户接收短信状态响应END */

    /* 查询用户上行发送短信响应 */
    private static final String XML_ELE_CALLBOX = "callbox";

    private static final String XML_ELE_CALLBOX_MOBILE = "mobile";

    private static final String XML_ELE_CALLBOX_TASKID = "taskid";

    private static final String XML_ELE_CALLBOX_CONTENT = "content";

    private static final String XML_ELE_CALLBOX_RECEIVETIME = "receivetime";

    private static SmsService instance = new SmsService();

    /* 查询用户上行发送短信响应END */
    /* 响应XML元素 END */

    // 与SMS通信对象
    private SmsClient smsClient;

    /*
     * // 登录短信平台的账户信息 private SmsAcctVO smsAcct;
     * 
     * // 发送短信URl配置信息 private SmsHostConfigVO smsSendConfig;
     * 
     * // 状态报告URL配置信息 private SmsHostConfigVO smsStatusConfig;
     * 
     * // 上行短信URL配置信息 private SmsHostConfigVO smsCallApiConfig;
     * 
     * // 短信签名 private String smSignature;
     */

    /**
     * 
     * 构造函数。 需要从配置文件中初始化配置参数。
     */
    private SmsService() {
        smsClient = SmsClient.getInstance();
        // initSmsHostConfig();
        // initSmsAcct();
    }

    /**
     * 获取smsService实例
     * 
     * @return
     */
    public static SmsService getInstance() {
        return instance;
    }

    /**
     * {@inheritDoc}
     * 
     * @throws IOException
     * @throws HttpException
     */
    @Override
    public SmsSendStatusVO sendSms(SmsInfoVO smsInfo) throws IOException {
        SmsSendStatusVO statusVO = null;
        String strXmlResp = smsClient.request(getSendSmsHostConfig(), makeSendSmsParams(smsInfo));
        statusVO = readSendStatusVO(strXmlResp);
        return statusVO;
    }

    /**
     * {@inheritDoc}
     * 
     * @throws IOException
     * @throws HttpException
     */
    @Override
    public List<ReceiveSmsVO> queryReceiveSms() throws IOException {
        List<ReceiveSmsVO> alSmsVOs = null;
        String strXmlResp = smsClient.request(getCallApiSmsHostConfig(), makeQueryReceiveSmsParams());
        alSmsVOs = readReceiveSmsVOs(strXmlResp);
        return alSmsVOs;
    }

    /**
     * {@inheritDoc}
     * 
     * @throws IOException
     * @throws HttpException
     */
    @Override
    public List<SmsReceiveStatusVO> querySmsReceiveStatus() throws IOException {
        List<SmsReceiveStatusVO> alReceiveStatus = null;
        String strXmlResp = smsClient.request(getStatusSmsHostConfig(), makeQuerySmsReceiveStatusParams());
        alReceiveStatus = readReceiveStatusVOs(strXmlResp);
        return alReceiveStatus;
    }

    private SmsHostConfigVO getSendSmsHostConfig() {
        int iRetry = NumberUtils.parseInt(SmsConfBuilder.getProperties(SMSSERVER_RETRY));
        int iTimeout = NumberUtils.parseInt(SmsConfBuilder.getProperties(SMSSERVER_TIMEOUT));
        String strHost = SmsConfBuilder.getProperties(SMSSERVER_HOST);
        SmsHostConfigVO smsSendConfig = new SmsHostConfigVO();
        smsSendConfig.setUrl(String.format(SmsService.SMS_URL_SEND, new Object[] {strHost}));
        smsSendConfig.setRetry(iRetry);
        smsSendConfig.setTimeout(iTimeout);
        return smsSendConfig;
    }

    private SmsHostConfigVO getStatusSmsHostConfig() {
        int iRetry = NumberUtils.parseInt(SmsConfBuilder.getProperties(SMSSERVER_RETRY));
        int iTimeout = NumberUtils.parseInt(SmsConfBuilder.getProperties(SMSSERVER_TIMEOUT));
        String strHost = SmsConfBuilder.getProperties(SMSSERVER_HOST);
        SmsHostConfigVO smsStatusConfig = new SmsHostConfigVO();
        smsStatusConfig.setUrl(String.format(SmsService.SMS_URL_RECEIVE_STATUS, new Object[] {strHost}));
        smsStatusConfig.setRetry(iRetry);
        smsStatusConfig.setTimeout(iTimeout);
        return smsStatusConfig;
    }

    private SmsHostConfigVO getCallApiSmsHostConfig() {
        int iRetry = NumberUtils.parseInt(SmsConfBuilder.getProperties(SMSSERVER_RETRY));
        int iTimeout = NumberUtils.parseInt(SmsConfBuilder.getProperties(SMSSERVER_TIMEOUT));
        String strHost = SmsConfBuilder.getProperties(SMSSERVER_HOST);
        SmsHostConfigVO smsCallApiConfig = new SmsHostConfigVO();
        smsCallApiConfig.setUrl(String.format(SmsService.SMS_URL_CALL, new Object[] {strHost}));
        smsCallApiConfig.setRetry(iRetry);
        smsCallApiConfig.setTimeout(iTimeout);
        return smsCallApiConfig;
    }

    /**
     * 
     * 添加方法注释。
     *
     */
    private SmsAcctVO getSmsAcct() {
        SmsAcctVO smsAcct = new SmsAcctVO();
        smsAcct.setUserid(SmsConfBuilder.getProperties(SMSSERVER_LOGIN_USERID));
        smsAcct.setAccount(SmsConfBuilder.getProperties(SMSSERVER_LOGIN_ACCTNO));
        smsAcct.setPassword(SmsConfBuilder.getProperties(SMSSERVER_LOGIN_PASSWORD));
        return smsAcct;
    }

    /**
     * 
     * 组合公用的请求参数,企业账户信息。
     * 
     * @return
     */
    private List<ReqParameterVO> makeCommonReqParams() {
        List<ReqParameterVO> parameterVOs = new ArrayList<ReqParameterVO>();
        SmsAcctVO smsAcct = getSmsAcct();
        parameterVOs.add(new ReqParameterVO(SmsService.REQ_PARAM_USERID, smsAcct.getUserid()));
        parameterVOs.add(new ReqParameterVO(SmsService.REQ_PARAM_ACCOUNT, smsAcct.getAccount()));
        parameterVOs.add(new ReqParameterVO(SmsService.REQ_PARAM_PASSWORD, smsAcct.getPassword()));
        return parameterVOs;
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
        parameterVOs.add(new ReqParameterVO(SmsService.REQ_PARAM_MOBILE, smsInfo.getMobile()));
        // 应商家需求，签名放短信内容前面
        String smSignature = SmsConfBuilder.getProperties(SMSSERVER_LOGIN_SIGNATURE);
        parameterVOs
            .add(new ReqParameterVO(SmsService.REQ_PARAM_CONTENT, smSignature + smsInfo.getContent()));
        // 非空定时发送
        if (!AssertUtils.isNull(smsInfo.getSendTime())) {
            parameterVOs.add(new ReqParameterVO(SmsService.REQ_PARAM_SEND_TIME, smsInfo.getSendTime()));
        }
        parameterVOs.add(new ReqParameterVO(SmsService.REQ_PARAM_ACTION, SmsService.REQ_ACTION_SEND));
        // parameterVOs.add(new
        // ReqParameterVO(SmsService.REQ_PARAM_EXTNO,null));
        return parameterVOs;
    }

    /**
     * 
     * 批量查询用户收到短信的请求参数。
     * 
     * @return
     */
    private List<ReqParameterVO> makeQueryReceiveSmsParams() {
        List<ReqParameterVO> parameterVOs = makeCommonReqParams();
        parameterVOs.add(new ReqParameterVO(SmsService.REQ_PARAM_ACTION, SmsService.REQ_ACTION_QUERY));
        return parameterVOs;
    }

    /**
     * 
     * 批量查询用户收到短信的请求参数。
     * 
     * @return
     */
    private List<ReqParameterVO> makeQuerySmsReceiveStatusParams() {
        List<ReqParameterVO> parameterVOs = makeCommonReqParams();
        parameterVOs.add(new ReqParameterVO(SmsService.REQ_PARAM_ACTION, SmsService.REQ_ACTION_QUERY));
        return parameterVOs;
    }

    private Document readXmlStr(String strXml) {
        Document document = null;
        try {
            document = XmlReaderUtils.readXMLByStr(strXml, SmsService.SMS_SERVER_ENCODING);
        }
        catch (DocumentException e) {
            if (LOG.isErrorEnabled()) {
                LOG.error("Failed to readXmlStr", e);
            }
            throw new RuntimeException("Sms server response info error.Response:" + strXml);
        }
        return document;
    }

    /**
     * 
     * 从响应报文中读取发送短信后的响应信息。
     * 
     * @param strXml
     * @return
     */
    private SmsSendStatusVO readSendStatusVO(String strXml) {
        if (AssertUtils.isNull(strXml)) {
            return null;
        }
        SmsSendStatusVO statusVO = null;
        Document document = readXmlStr(strXml);
        Element eleRoot = document.getRootElement();
        // 返回状态值：成功返回Success 失败返回：Faild
        String status = XmlReaderUtils.getEleValue(eleRoot, SmsService.XML_ELE_RETURNSTATUS);
        // ok提交成功 ,其他的都是失败的提示信息
        String message = XmlReaderUtils.getEleValue(eleRoot, SmsService.XML_ELE_MESSAGE);
        // 返回余额
        String remainpoint = XmlReaderUtils.getEleValue(eleRoot, SmsService.XML_ELE_REMAINPOINT);
        // 返回本次任务的序列ID
        String taskID = XmlReaderUtils.getEleValue(eleRoot, SmsService.XML_ELE_TASKID);
        // 成功短信数：当成功后返回提交成功短信数
        String successCounts = XmlReaderUtils.getEleValue(eleRoot, SmsService.XML_ELE_SUCCESSCOUNTS);
        statusVO = new SmsSendStatusVO(status, message, remainpoint, taskID, successCounts);
        return statusVO;
    }

    /**
     * 
     * 从响应报文中读取上行短信信息。
     * 
     * @param strXml
     * @return
     */
    private List<ReceiveSmsVO> readReceiveSmsVOs(String strXml) {
        if (AssertUtils.isNull(strXml)) {
            return null;
        }
        Document document = readXmlStr(strXml);
        Element eleRoot = document.getRootElement();
        Element eleError = eleRoot.element(SmsService.XML_ELE_ERRORSTATUS_PARENT);
        if (eleError != null) {
            throw new RuntimeException("SMS server response:[Status:"
                    + XmlReaderUtils.getEleValue(eleError, SmsService.XML_ELE_ERROR_STATUS) + ",remark:"
                    + XmlReaderUtils.getEleValue(eleError, SmsService.XML_ELE_ERROR_REMARK) + "]");
        }
        else {
            List<Element> eleCallboxs = eleRoot.elements(SmsService.XML_ELE_CALLBOX);
            if (!AssertUtils.notNull(eleCallboxs)) {
                return null;
            }
            List<ReceiveSmsVO> alSmsVOs = new ArrayList<ReceiveSmsVO>();
            for (Element ele : eleCallboxs) {
                alSmsVOs.add(readReceiveSmsVO(ele));
            }
            return alSmsVOs;
        }
    }

    /**
     * 
     * 从ELE元素中封装收到的短信对象。
     * 
     * @param strXml
     * @return
     */
    private ReceiveSmsVO readReceiveSmsVO(Element eleCallBox) {
        ReceiveSmsVO objSms = new ReceiveSmsVO();
        objSms.setContent(XmlReaderUtils.getEleValue(eleCallBox, SmsService.XML_ELE_CALLBOX_CONTENT));
        objSms.setMobile(XmlReaderUtils.getEleValue(eleCallBox, SmsService.XML_ELE_CALLBOX_MOBILE));
        objSms.setReceiveTime(XmlReaderUtils.getEleValue(eleCallBox, SmsService.XML_ELE_CALLBOX_RECEIVETIME));
        objSms.setTaskID(XmlReaderUtils.getEleValue(eleCallBox, SmsService.XML_ELE_CALLBOX_TASKID));
        return objSms;
    }

    /**
     * 
     * 从响应报文中读取下行短信接收状态信息。
     * 
     * @param strXml
     * @return
     */
    private List<SmsReceiveStatusVO> readReceiveStatusVOs(String strXml) {
        if (AssertUtils.isNull(strXml)) {
            return null;
        }
        Document document = readXmlStr(strXml);
        Element eleRoot = document.getRootElement();
        Element eleError = eleRoot.element(SmsService.XML_ELE_ERRORSTATUS_PARENT);
        if (eleError != null) {
            throw new RuntimeException("SMS server response:[Status:"
                    + XmlReaderUtils.getEleValue(eleError, SmsService.XML_ELE_ERROR_STATUS) + ",remark:"
                    + XmlReaderUtils.getEleValue(eleError, SmsService.XML_ELE_ERROR_REMARK) + "]");
        }
        else {

            List<Element> eleStatusboxs = eleRoot.elements(SmsService.XML_ELE_STATUSBOX);
            if (AssertUtils.notNull(eleStatusboxs)) {
                return null;
            }
            List<SmsReceiveStatusVO> alReceiveStatus = new ArrayList<SmsReceiveStatusVO>();
            for (Element ele : eleStatusboxs) {
                alReceiveStatus.add(readReceiveStatusVO(ele));
            }
            return alReceiveStatus;

        }
    }

    /**
     * 
     * 从ELE元素中封装收到的短信对象。
     * 
     * @param strXml
     * @return
     */
    private SmsReceiveStatusVO readReceiveStatusVO(Element eleStatusbox) {
        SmsReceiveStatusVO objRecStatus = new SmsReceiveStatusVO();
        objRecStatus.setErrorCode(XmlReaderUtils.getEleValue(eleStatusbox,
            SmsService.XML_ELE_STATUSBOX_ERRORCODE));
        objRecStatus.setStatus(XmlReaderUtils.getEleValue(eleStatusbox, SmsService.XML_ELE_STATUSBOX_STATUS));
        objRecStatus.setMobile(XmlReaderUtils.getEleValue(eleStatusbox, SmsService.XML_ELE_STATUSBOX_MOBILE));
        objRecStatus.setReceiveTime(XmlReaderUtils.getEleValue(eleStatusbox,
            SmsService.XML_ELE_STATUSBOX_RECEIVETIME));
        objRecStatus.setTaskID(XmlReaderUtils.getEleValue(eleStatusbox, SmsService.XML_ELE_STATUSBOX_TASKID));
        return objRecStatus;
    }
}
