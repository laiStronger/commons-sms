/*
 * 文件名：SmsConstants.java
 * 版权：深圳柚安米科技有限公司版权所有
 * 修改人：tanguojun
 * 修改时间：2016年12月2日
 * 修改内容：新增
 */
package com.youanmi.scrm.commons.sms.constants;



/**
 * 短信常量F
 * 
 * @author tanguojun 2016年12月2日
 * @since 2.2.4
 */
public interface SmsConstants {

    interface Result {

        /** 短信发送成功 */
        String SMS_SEND_SUCESS = "Success";
        /** 短信发送失败 */
        String SMS_SEND_FAILD = "Faild";
    }

    /**
     * 
     * @author tanguojun on 2016年12月2日
     *
     */
    interface Common {
        /** 多短信服务商分割符 */
        String SERVER_SPLIT = ",";

        /** 短信服务商配置参数名 */
        String SMS_API_SERVER = "sms.api.server";
        /** 1:真实发送 验证码短信 */
        String AUTH_CODE_TYPE_1 = "1";
        /** 验证码发送类型 */
        String AUTH_CODE_TYPE = "authCodeType";
        /**
         * 验证码长度
         */
        Integer CODE_LENGTH = 6;
        /**
         * 验证码有效时长 10min
         */
        Long EFFECTIVE_TIME = 600000L;

        /**
         * 模拟发短信时,验证码是888888
         */
        String FAKE_CODE = "888888";
    }

    /**
     * 
     * @author tanguojun on 2016年12月2日
     *
     */
    interface GST {

        /** 高斯通：标记 */
        String GST_SERVER_SN = "GST";

        /** 与高斯通服务通信编码格式 */
        String CHARSET = "GBK";

        /** 发送短信的URl */
        String SMS_URL_SEND = "http://%s/GsmsHttp";
        /** 主机 */
        String SMS_GST_HOST = "sms.gst.host";
        /** 请求超时时长（毫秒为单位） */
        String SMS_GST_TIMEOUT = "sms.gst.timeout";
        /** 请求失败后最大尝试次数 */
        String SMS_GST_RETRY = "sms.gst.retry";

        /** ISP登陆名(公司分配给ISP) 非空，如果为机构版，则应以“机构ID:登陆名”方式赋值 */
        String SMS_GST_USERNAME = "sms.gst.username";
        /** 发送帐号密码 */
        String SMS_GST_PASSWORD = "sms.gst.password";
        /** 发短信的端口扩展号 */
        String SMS_GST_FROM = "sms.gst.from";
        /**
         * 用户下行短信自扩展端口，可为空，只接受数字。<br/>
         * 其该端口的使用需要相关机构的指定配置。 （该字段单独最大长度为14位，但下行短信实际发送后会根据SP号进行相关的截取操作）
         */
        String SMS_GST_EXPAND_PREFIX = "sms.gst.expandPrefix";
        /** 短信签名 */
        // String SMSSERVER_LOGIN_SIGNATURE =
        // "smsServer.login.signature";

        /** 参数名：ISP登陆名( */
        String REQ_PARAM_USERNAME = "username";
        /** 参数名：ISP登陆密码(公司分配给ISP)非空 */
        String REQ_PARAM_PASSWORD = "password";
        /** 参数名：发短信的端口扩展号 */
        String REQ_PARAM_FROM = "from";
        /** 参数名：接收短信的手机号 非空 支持多个(<=15)手机号码，中间以“,”分割 */
        String REQ_PARAM_TO = "to";
        /** 参数名：短信内容 不能为空(只支持GBK编码,若使用其它编码需要转换一下) */
        String REQ_PARAM_CONTENT = "content";
        /** 参数名：定时发送时间（格式：YYYY-MM-DD HH24:MI:SS） */
        String REQ_PARAM_PRESENDTIME = "presendTime";
        /** 参数名：用户下行短信自扩展端口 */
        String REQ_PARAM_EXPANDPREFIX = "expandPrefix";
        /** 发送成功 */
        String SEND_SUCCESS = "OK";
    }

    /**
     * 
     * @author tanguojun on 2016年12月2日
     *
     */
    interface MD {

        /** 秒嘀：标记 */
        String MD_SERVER_SN = "MD";

        String SMSSERVER_RETRY = "sms.md.retry";

        String SMSSERVER_HOST = "sms.md.host";

        String SMSSERVER_TIMEOUT = "sms.md.timeout";

        String SMS_URL_SEND = "http://%s/industrySMS/sendSMS";

        String SMS_ACCOUNT = "sms.md.account";

        String SMS_TOKEN = "sms.md.token";

        String REQ_PARAM_ACCOUNT = "accountSid";
        String REQ_PARAM_CONTENT = "smsContent";
        String REQ_PARAM_TO = "to";
        String REQ_PARAM_TIMESTAMP = "timestamp";
        String REQ_PARAM_SIGNAL = "sig";
    }

    interface Mail {
        /** 邮件：服务器 */
        String MAIL_SMTP = "mail.smtp";
        /** 邮件：发送人 */
        String MAIL_FROM = "mail.from";
        /** 邮件：发送给谁 */
        String MAIL_TO = "mail.to";
        /** 邮件：抄送给谁 */
        String MAIL_COPY_TO = "mail.copyTo";
        /** 邮件：主题 */
        String MAIL_SUBJECT = "sms.mail.error.title";
        /** 邮件：内容开头 */
        String MAIL_CONTENT = "Dear all,<br/>短信服务异常，异常信息：<br/>";
        /** 邮件：用户名 */
        String MAIL_USERNAME = "mail.username";
        /** 邮件：密码 */
        String MAIL_PASSWORD = "mail.password";
    }

    /**
     * 短信模版
     */
    interface Template {
        /**
         * 找回密码
         */
        String FIND_PASSWORD = "template.password.find";

        /**
         * 绑定手机
         */
        String BIND_PHONE = "template.phone.bind";

        /**
         * 修改手机号
         */
        String UPDATE_PHONE = "template.phone.update";
    }
}
