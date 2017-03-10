/*
 * 文件名：SmsConfig.java
 * 版权：深圳柚安米科技有限公司版权所有
 * 修改人：tanguojun
 * 修改时间：2016年12月2日
 * 修改内容：新增
 */
package com.youanmi.scrm.commons.sms.conf;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

import com.youanmi.scrm.commons.sms.constants.SmsConstants;
import com.youanmi.scrm.commons.sms.sender.SmsSender;
import com.youanmi.scrm.commons.sms.service.ISmsService;
import com.youanmi.scrm.commons.sms.service.impl.GSTSmsService;
import com.youanmi.scrm.commons.sms.service.impl.MDSmsService;


/**
 * 短信配置入口
 * 
 * @author tanguojun 2016年12月2日
 * @since 2.2.4
 */
public class SmsConfBuilder {

    private static Properties Props = new Properties();

    /**
     * 配置短信配置
     *
     * @param props
     * @author tanguojun on 2016年12月3日
     */
    public static void buildSmsConf(Properties props) {

        // 校验邮箱
        validateSmsMail(props);

        // 获取短信服务商
        String serverStr = props.getProperty(SmsConstants.Common.SMS_API_SERVER);
        if (StringUtils.isBlank(serverStr)) {
            throw new NullPointerException(SmsConstants.Common.SMS_API_SERVER + " is null");
        }

        String[] servers = serverStr.split(SmsConstants.Common.SERVER_SPLIT);

        // 必须有短信商
        if (servers.length == 0) {
            throw new NullPointerException(SmsConstants.Common.SMS_API_SERVER + " error , "
                    + SmsConstants.Common.SMS_API_SERVER + "=" + serverStr);
        }

        Map<String, ISmsService> serviceMap = new HashMap<String, ISmsService>();

        // 循环实例化短信服务商
        for (String server : servers) {

            if (SmsConstants.MD.MD_SERVER_SN.equals(server)) {
                // 校验md参数
                validateSmsToMD(props);
                serviceMap.put(SmsConstants.MD.MD_SERVER_SN, MDSmsService.getInstance());
            }
            else if (SmsConstants.GST.GST_SERVER_SN.equals(server)) {
                // 校验gst参数
                validateSmsToGST(props);
                serviceMap.put(SmsConstants.GST.GST_SERVER_SN, GSTSmsService.getInstance());
            }
            else {
                // 未知的短信供应商
                throw new RuntimeException("unknow sms server : " + server);
            }
        }
        // 传递配置
        SmsConfBuilder.Props = props;
        // 传递服务商
        SmsSender.SmsServiceMap = serviceMap;
    }

    /**
     * 获取配置
     *
     * @param key
     * @return
     * @author tanguojun on 2016年12月2日
     */
    public static String getProperties(String key) {
        return Props.getProperty(key);

    }

    /**
     * 校验秒嘀短信
     *
     * @author tanguojun on 2016年12月2日
     */
    private static void validateSmsToMD(Properties props) {
        if (StringUtils.isBlank(props.getProperty(SmsConstants.MD.SMSSERVER_RETRY))) {
            throw new NullPointerException(SmsConstants.MD.SMSSERVER_RETRY + " is null");
        }
        else if (StringUtils.isBlank(props.getProperty(SmsConstants.MD.SMSSERVER_HOST))) {
            throw new NullPointerException(SmsConstants.MD.SMSSERVER_HOST + " is null");
        }
        else if (StringUtils.isBlank(props.getProperty(SmsConstants.MD.SMSSERVER_TIMEOUT))) {
            throw new NullPointerException(SmsConstants.MD.SMSSERVER_TIMEOUT + " is null");
        }
        else if (StringUtils.isBlank(props.getProperty(SmsConstants.MD.SMS_ACCOUNT))) {
            throw new NullPointerException(SmsConstants.MD.SMS_ACCOUNT + " is null");
        }
        else if (StringUtils.isBlank(props.getProperty(SmsConstants.MD.SMS_TOKEN))) {
            throw new NullPointerException(SmsConstants.MD.SMS_TOKEN + " is null");
        }
    }

    /**
     * 校验高斯通
     *
     * @author tanguojun on 2016年12月2日
     */
    private static void validateSmsToGST(Properties props) {

        if (StringUtils.isBlank(props.getProperty(SmsConstants.GST.SMS_GST_FROM))) {
            throw new NullPointerException(SmsConstants.GST.SMS_GST_FROM + " is null");
        }
        else if (StringUtils.isBlank(props.getProperty(SmsConstants.GST.SMS_GST_PASSWORD))) {
            throw new NullPointerException(SmsConstants.GST.SMS_GST_PASSWORD + " is null");
        }
        else if (StringUtils.isBlank(props.getProperty(SmsConstants.GST.SMS_GST_USERNAME))) {
            throw new NullPointerException(SmsConstants.GST.SMS_GST_USERNAME + " is null");
        }
        else if (StringUtils.isBlank(props.getProperty(SmsConstants.GST.SMS_GST_RETRY))) {
            throw new NullPointerException(SmsConstants.GST.SMS_GST_RETRY + " is null");
        }
        else if (StringUtils.isBlank(props.getProperty(SmsConstants.GST.SMS_GST_TIMEOUT))) {
            throw new NullPointerException(SmsConstants.GST.SMS_GST_TIMEOUT + " is null");
        }
        else if (StringUtils.isBlank(props.getProperty(SmsConstants.GST.SMS_GST_HOST))) {
            throw new NullPointerException(SmsConstants.GST.SMS_GST_HOST + " is null");
        }
        else if (StringUtils.isBlank(props.getProperty(SmsConstants.GST.SMS_GST_EXPAND_PREFIX))) {
            // do nothing 允许为空
        }

    }

    /**
     * 校验短信
     *
     * @author tanguojun on 2016年12月2日
     */
    private static void validateSmsMail(Properties props) {
        if (StringUtils.isBlank(props.getProperty(SmsConstants.Mail.MAIL_SMTP))) {
            throw new NullPointerException(SmsConstants.Mail.MAIL_SMTP + " is null");
        }
        else if (StringUtils.isBlank(props.getProperty(SmsConstants.Mail.MAIL_FROM))) {
            throw new NullPointerException(SmsConstants.Mail.MAIL_FROM + " is null");
        }
        else if (StringUtils.isBlank(props.getProperty(SmsConstants.Mail.MAIL_TO))) {
            throw new NullPointerException(SmsConstants.Mail.MAIL_TO + " is null");
        }
        else if (StringUtils.isBlank(props.getProperty(SmsConstants.Mail.MAIL_SUBJECT))) {
            throw new NullPointerException(SmsConstants.Mail.MAIL_SUBJECT + " is null");
        }
        else if (StringUtils.isBlank(props.getProperty(SmsConstants.Mail.MAIL_USERNAME))) {
            throw new NullPointerException(SmsConstants.Mail.MAIL_USERNAME + " is null");
        }
        else if (StringUtils.isBlank(props.getProperty(SmsConstants.Mail.MAIL_PASSWORD))) {
            throw new NullPointerException(SmsConstants.Mail.MAIL_PASSWORD + " is null");
        }
        else if (StringUtils.isBlank(props.getProperty(SmsConstants.Mail.MAIL_COPY_TO))) {
            // do nothing 允许空
        }
    }

}
