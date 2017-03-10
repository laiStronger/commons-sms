package com.youanmi.scrm.commons.sms.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import com.youanmi.scrm.commons.util.date.DateUtils;
import com.youanmi.scrm.commons.util.encryption.MD5Utils;
import com.youanmi.scrm.commons.util.json.JsonUtils;
import com.youanmi.scrm.commons.util.number.NumberUtils;
import com.youanmi.scrm.commons.util.string.AssertUtils;


/**
 * 秒嘀
 * 
 * @author Administrator
 *
 */
public class MDSmsService implements ISmsService {

    private SmsClient smsClient;

    private static MDSmsService instance = new MDSmsService();

    private MDSmsService() {
        smsClient = SmsClient.getInstance();
    }

    public static MDSmsService getInstance() {
        return instance;
    }

    @Override
    public SmsSendStatusVO sendSms(SmsInfoVO smsInfo) throws IOException {
        SmsSendStatusVO result = new SmsSendStatusVO();
        String json = smsClient.request(getSendSmsHostConfig(), makeReqParam(smsInfo));
        if (AssertUtils.notNull(json)) {
            Map<String, Object> map = JsonUtils.toObj(json, Map.class);
            if ("00000".equals(map.get("respCode"))) {
                result.setStatus("Success");
            }
            else {
                result.setStatus("Faild");
                result.setMessage(map.get("respCode").toString());
            }
        }
        return result;
    }

    @Override
    public List<ReceiveSmsVO> queryReceiveSms() throws IOException {
        return null;
    }

    @Override
    public List<SmsReceiveStatusVO> querySmsReceiveStatus() throws IOException {
        return null;
    }

    /**
     * 构建短信服务器配置实例
     * 
     * @return
     */
    private SmsHostConfigVO getSendSmsHostConfig() {
        int iRetry = NumberUtils.parseInt(SmsConfBuilder.getProperties(SmsConstants.MD.SMSSERVER_RETRY));
        int iTimeout = NumberUtils.parseInt(SmsConfBuilder.getProperties(SmsConstants.MD.SMSSERVER_TIMEOUT));
        String strHost = SmsConfBuilder.getProperties(SmsConstants.MD.SMSSERVER_HOST);
        SmsHostConfigVO smsSendConfig = new SmsHostConfigVO();
        smsSendConfig.setUrl(String.format(SmsConstants.MD.SMS_URL_SEND, new Object[] {strHost}));
        smsSendConfig.setRetry(iRetry);
        smsSendConfig.setTimeout(iTimeout);
        return smsSendConfig;
    }

    /**
     * 组装请求参数VO
     * 
     * @param smsInfo
     * @return
     */
    private List<ReqParameterVO> makeReqParam(SmsInfoVO smsInfo) {

        String timeStamp = DateUtils.formatDate(System.currentTimeMillis(), "yyyyMMddHHmmss");
        String account = SmsConfBuilder.getProperties(SmsConstants.MD.SMS_ACCOUNT);
        String token = SmsConfBuilder.getProperties(SmsConstants.MD.SMS_TOKEN);
        List<ReqParameterVO> reqList = new ArrayList<ReqParameterVO>();
        reqList.add(new ReqParameterVO(SmsConstants.MD.REQ_PARAM_ACCOUNT, account));
        reqList.add(new ReqParameterVO(SmsConstants.MD.REQ_PARAM_CONTENT, "【柚安米科技】" + smsInfo.getContent()));
        reqList.add(new ReqParameterVO(SmsConstants.MD.REQ_PARAM_TO, smsInfo.getMobile()));
        reqList.add(new ReqParameterVO(SmsConstants.MD.REQ_PARAM_TIMESTAMP, timeStamp));
        reqList
            .add(new ReqParameterVO(SmsConstants.MD.REQ_PARAM_SIGNAL, getSignal(timeStamp, account, token)));
        return reqList;
    }

    /**
     * 获取加密后的签名
     * 
     * @param timeStamp
     * @return
     */
    private String getSignal(String timeStamp, String account, String token) {
        StringBuilder sb = new StringBuilder();
        sb.append(account).append(token).append(timeStamp);
        return MD5Utils.getMD5Code(sb.toString()).toLowerCase();
    }

    public static void main(String[] args) throws IOException {
        SmsInfoVO smsInfo = new SmsInfoVO();
        smsInfo.setMobile("18826560947");
        smsInfo.setContent("【柚安米科技】您的账户注册验证码为18826560947，90秒内有效。");
        SmsSendStatusVO smsVO = MDSmsService.getInstance().sendSms(smsInfo);
        System.out.println(JsonUtils.toJSON(smsVO));
    }
}
