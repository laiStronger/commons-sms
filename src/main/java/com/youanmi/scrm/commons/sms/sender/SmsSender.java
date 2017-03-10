/*
 * 文件名：SmsUtils.java
 * 版权：Copyright 2014 youanmi Tech. Co. Ltd. All Rights Reserved. 
 * 描述： SmsUtil.java
 * 修改人：刘红艳
 * 修改时间：2014年12月11日
 * 修改内容：新增
 */
package com.youanmi.scrm.commons.sms.sender;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.youanmi.scrm.commons.sms.conf.SmsConfBuilder;
import com.youanmi.scrm.commons.sms.constants.SmsConstants;
import com.youanmi.scrm.commons.sms.service.ISmsService;
import com.youanmi.scrm.commons.sms.vo.request.SmsInfoVO;
import com.youanmi.scrm.commons.sms.vo.response.SmsSendStatusVO;
import com.youanmi.scrm.commons.util.exception.ExceptionUtils;
import com.youanmi.scrm.commons.util.json.JsonUtils;
import com.youanmi.scrm.commons.util.mail.MailUtils;
import com.youanmi.scrm.commons.util.string.AssertUtils;


/**
 * 短信工具类。
 * <p>
 * 
 * <li>1.发送短信。
 * <p>
 * 
 * 
 * <pre>
 * </pre>
 * 
 * @author 刘红艳
 * @version YouAnMi-OTO 2014年12月11日
 * @since YouAnMi-OTO
 */
public class SmsSender {
    /**
     * 调测日志记录器。
     */
    private static final Logger LOG = LoggerFactory.getLogger(SmsSender.class);
    /** 短信服务商集合 */
    public static Map<String, ISmsService> SmsServiceMap = new HashMap<String, ISmsService>();

    // static {
    // smsServiceMap.put(SmsConstants.GST.GST_SERVER_SN,
    // GSTSmsService.getInstance());
    // smsServiceMap.put(SmsConstants.MD.MD_SERVER_SN,
    // MDSmsService.getInstance());
    // }

    /**
     * 
     * 发送短信。 (使用该方法事物工程请使用 MQClientUtils.start(MQConstants.getMqcGroupName(),
     * MQConstants.getMqcUrl());因为新增短信记录到异步数据库)
     * 
     * @param smsInfo
     * @return
     * @throws HttpException
     * @throws IOException
     */
    public static SmsSendStatusVO sendSms(SmsInfoVO smsInfo) throws Exception {
        String sendCode = SmsConfBuilder.getProperties(SmsConstants.Common.AUTH_CODE_TYPE);
        LOG.info("Is send code:" + sendCode);
        // 2.模拟发送短信成功完成
        if (!SmsConstants.Common.AUTH_CODE_TYPE_1.equals(sendCode)) {
            return new SmsSendStatusVO(SmsConstants.Result.SMS_SEND_SUCESS, "", "100", "111", "1");
        }
        String server = SmsConfBuilder.getProperties(SmsConstants.Common.SMS_API_SERVER);
        List<String> servers = Arrays.asList(server.split(SmsConstants.Common.SERVER_SPLIT));
        // 打乱顺序
        // Collections.shuffle(servers);

        SmsSendStatusVO respVo = null;

        for (int i = 0; i < servers.size(); i++) {
            ISmsService smsService = SmsServiceMap.get(servers.get(i));
            // 服务不存在，抛出异常
            if (smsService == null) {
                throw new RuntimeException("Unkonw sms api server:" + servers.get(i)
                        + ",pls check config[SMS_API_SERVER].");
            }
            else {
                try {
                    respVo = smsService.sendSms(smsInfo);// 发短信
                    if (SmsConstants.Result.SMS_SEND_SUCESS.equals(respVo.getStatus())) {
                        return respVo;// 发成功则返回，退出循环
                    }
                    else {
                        LOG.error("send sms fail , server=" + server + " , send mail ...");
                        sendMail(servers.get(i), null, respVo);// 对方返回错误信息则发邮件
                    }
                    continue;// 否则继续下个服务发短信
                }
                catch (Exception e) {
                    LOG.error("send sms fail , server=" + server + " , send mail ...");
                    sendMail(servers.get(i), e, null);// 异常发送邮件
                    if (i == (servers.size() - 1)) {// 最后一个服务，异常了就抛出异常错误
                        throw e;
                    }
                    continue;// 否则继续下个服务发短信
                }
            }
        }
        return respVo;
    }

    /**
     * 短信服务异常，发邮件通知。
     * 
     * @param e
     * @param respVo
     */
    private static void sendMail(String apiServer, Exception e, SmsSendStatusVO respVo) {
        // 无异常则直接返回不发邮件
        if (e == null && (respVo == null || SmsConstants.Result.SMS_SEND_SUCESS.equals(respVo.getStatus()))) {
            return;
        }
        // // SMTP服务器
        String smtp = SmsConfBuilder.getProperties(SmsConstants.Mail.MAIL_SMTP);
        if (AssertUtils.isNull(smtp)) { // 邮件服务器设置为空则不发邮件直接返回
            return;
        }
        // 发信人
        String from = SmsConfBuilder.getProperties(SmsConstants.Mail.MAIL_FROM);
        String to = SmsConfBuilder.getProperties(SmsConstants.Mail.MAIL_TO);
        String copyto = SmsConfBuilder.getProperties(SmsConstants.Mail.MAIL_COPY_TO);
        String subject = SmsConfBuilder.getProperties(SmsConstants.Mail.MAIL_SUBJECT);
        String content = SmsConstants.Mail.MAIL_CONTENT + "[服务商:" + apiServer + "]<br/>";
        String username = SmsConfBuilder.getProperties(SmsConstants.Mail.MAIL_USERNAME);
        String password = SmsConfBuilder.getProperties(SmsConstants.Mail.MAIL_PASSWORD);
        try {
            if (e != null) {
                content = content + ExceptionUtils.getStackTrace(e);
            }
            if (respVo != null && !SmsConstants.Result.SMS_SEND_SUCESS.equals(respVo.getStatus())) {
                content = content + JsonUtils.toJSON(respVo);
            }
            MailUtils.sendAndCc(smtp, from, to, copyto, subject, content, username, password);
            LOG.info("邮件发完了");
        }
        catch (Exception e1) { // 异常捕获，不外抛
            if (LOG.isErrorEnabled()) {
                LOG.error("Failed to sendMail", e1);
            }
        }
    }

    /**
     * <p>
     * 批量查询用户收到短信的结果。
     * 
     * @return
     * @throws IOException
     * @throws HttpException
     * 
     *             public static List<ReceiveSmsVO> queryReceiveSms() throws
     *             IOException { //return smsService.queryReceiveSms(); return
     *             null; }
     */

}
