package com.eptok.yspay.opensdkjava.dsf;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.eptok.yspay.opensdkjava.common.Constants;
import com.eptok.yspay.opensdkjava.pojo.vo.OnlineReqDataVo;
import com.eptok.yspay.opensdkjava.util.DateUtil;
import com.eptok.yspay.opensdkjava.util.HttpClientUtil;
import com.eptok.yspay.opensdkjava.util.StringUtil;
import com.eptok.yspay.opensdkjava.util.YsOnlineSignUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @author 王想
 * @description: 代收
 * 单笔代收受理接口（银行卡）-普通 {@link  #singleNormalDs(OnlineReqDataVo)}
 * 单笔代收受理接口（银行卡）-加急 {@link  #singleQuickDs(OnlineReqDataVo)}
 * 单笔代收加急受理(协议)接口 {@link  #singleProtocolQuickDs(OnlineReqDataVo)}
 * 单笔代收（平台内）受理接口 {@link  #singleQuickInnerDs(OnlineReqDataVo)}
 * 单笔代收查询接口 {@link  #singleQueryDs(OnlineReqDataVo)}
 * 单笔代收协议查询接口请求 {@link  #singleProtocolQueryDs(OnlineReqDataVo)}
 * 单笔代收协议签约 {@link  #singleProtocolAcceptDs(OnlineReqDataVo)}
 * 单笔代收协议签约授权接口请求 {@link  #singleProtocolAcceptGetAuthorizeMsgDs(OnlineReqDataVo)}
 *
 * 批量代收（银行卡）普通受理接口 {@link  #batchNormalDs(OnlineReqDataVo)}
 * 批量代收（平台内）普通受理接口 {@link  #batchNormalInnerDs(OnlineReqDataVo)}
 * 批量代收汇总查询接口 {@link  #batchSummaryQueryDs(OnlineReqDataVo)}
 * 批量代收明细查询接口 {@link  #batchDetailQueryDs(OnlineReqDataVo)}
 * @date 2021/10/11 13:45
 */
public class DsApi {
    private static final Logger logger = Logger.getLogger(DsApi.class.getName());
    private final static String version = "3.0";
    /**
     * 单笔代收受理接口（银行卡）-普通
     * @param vo
     * @return
     * @throws Exception
     */
    public static String singleNormalDs(OnlineReqDataVo vo) throws Exception {

        /** 1、对业务数据进行加密*/
        String biz_content = JSONObject.toJSONString(vo.getParamData());
        logger.info("DsApi.singleQuickDs param biz_content:"+biz_content);

        /** 2、组装入参的数据*/
        Map<String, String> params = new HashMap<String, String>();
        params.put("method","ysepay.ds.single.normal.accept");
        //商户在银盛支付平台开设的用户号[商户号]
        params.put("partner_id",vo.getPartnerId());
        //代理密码，加密传输
        params.put("proxy_password",vo.getParamData()==null?"":(String)vo.getParamData().get("proxy_password"));
        //真实商户用户号
        params.put("merchant_usercode",vo.getParamData()==null?"":(String)vo.getParamData().get("merchant_usercode"));
        //发送请求的时间，格式"yyyy-MM-dd HH:mm:ss"
        params.put("timestamp", DateUtil.getDateNow());
        params.put("charset", Constants.CHARSET_UTF_8);
        params.put("sign_type", "RSA");
        //银盛支付服务器主动通知商户网站里指定的页面http路径。 不可空
        params.put("notify_url",vo.getNotifyUrl());
        params.put("version",version);
         //业务参数
        params.put("biz_content",biz_content);

        /*3、对数据进行加签，并存入请求数据*/
        try{
            String sign = YsOnlineSignUtils.sign(params, vo.getPrivateKeyPassword(),vo.getPrivateKeyFilePath());
            logger.info("DsApi.singleNormalDs params  signValue:"+sign);
            params.put("sign",sign);
        }catch (Exception e){
           throw e;
        }

        /*4、发送请求*/
        String result = HttpClientUtil.sendPostParam(vo.getReqUrl(), StringUtil.mapToString(params));
        logger.info("DsApi.singleNormalDs respone  result:"+result);
        if(StringUtil.isEmpty(result)){
            logger.info("DsApi.singleNormalDs respone  result is empty");
        }
        /*5、结果验签*/
        JSONObject jsonObject= JSON.parseObject(result, Feature.OrderedField);
        // 加签值
        String sinaValue = (String) jsonObject.get("sign");
        // 加签内容
        String content = jsonObject.get("ysepay_ds_single_normal_accept_response").toString();
        boolean flag = YsOnlineSignUtils.rsaCheckContent(content,sinaValue,Constants.CHARSET_UTF_8,vo.getYsPublicKeyFilePath());
        if(!flag){
            throw new Exception("check sign error");
        }
        return content;
    }
    /**
     * 单笔代收受理接口（银行卡）-加急
     * @param vo
     * @return
     * @throws Exception
     */
    public static String singleQuickDs(OnlineReqDataVo vo) throws Exception {

        /** 1、对业务数据进行加密*/
        String biz_content = JSONObject.toJSONString(vo.getParamData());
        logger.info("DsApi.singleQuickDs param biz_content:"+biz_content);

        /** 2、组装入参的数据*/
        Map<String, String> params = new HashMap<String, String>();
        params.put("method","ysepay.ds.single.quick.accept");
        //商户在银盛支付平台开设的用户号[商户号]
        params.put("partner_id",vo.getPartnerId());
        //发送请求的时间，格式"yyyy-MM-dd HH:mm:ss"
        params.put("timestamp", DateUtil.getDateNow());
        params.put("charset", Constants.CHARSET_UTF_8);
        params.put("sign_type", "RSA");
        //银盛支付服务器主动通知商户网站里指定的页面http路径。 不可空
        params.put("notify_url",vo.getNotifyUrl());
        params.put("version",version);
         //业务参数
        params.put("biz_content",biz_content);

        /*3、对数据进行加签，并存入请求数据*/
        try{
            String sign = YsOnlineSignUtils.sign(params, vo.getPrivateKeyPassword(),vo.getPrivateKeyFilePath());
            logger.info("DsApi.singleQuickDs params  signValue:"+sign);
            params.put("sign",sign);
        }catch (Exception e){
            throw e;
        }

        /*4、发送请求*/
        String result = HttpClientUtil.sendPostParam(vo.getReqUrl(), StringUtil.mapToString(params));
        logger.info("DsApi.singleQuickDs response result:"+result);
        if(StringUtil.isEmpty(result)){
            logger.info("DsApi.singleQuickDs response result is null");
        }
        /*5、结果验签*/
        JSONObject jsonObject= JSON.parseObject(result, Feature.OrderedField);
        // 加签值
        String sinaValue = (String) jsonObject.get("sign");
        // 加签内容
        String content = jsonObject.get("ysepay_ds_single_quick_accept_response").toString();
        boolean flag = YsOnlineSignUtils.rsaCheckContent(content,sinaValue,Constants.CHARSET_UTF_8,vo.getYsPublicKeyFilePath());
        if(!flag){
            throw new Exception("check sign error");
        }
        return content;
    }
    /**
     * 单笔代收加急受理(协议)接口
     * @param vo
     * @return
     * @throws Exception
     */
    public static String singleProtocolQuickDs(OnlineReqDataVo vo) throws Exception {

        /** 1、对业务数据进行加密*/
        String biz_content = JSONObject.toJSONString(vo.getParamData());
        logger.info("DsApi.singleProtocolQuickDs param biz_content:"+biz_content);

        /** 2、组装入参的数据*/
        Map<String, String> params = new HashMap<String, String>();
        params.put("method","ysepay.ds.single.protocol.quick.accept");
        //商户在银盛支付平台开设的用户号[商户号]
        params.put("partner_id",vo.getPartnerId());
        //发送请求的时间，格式"yyyy-MM-dd HH:mm:ss"
        params.put("timestamp", DateUtil.getDateNow());
        params.put("charset", Constants.CHARSET_UTF_8);
        params.put("sign_type", "RSA");
        //银盛支付服务器主动通知商户网站里指定的页面http路径。 不可空
        params.put("notify_url",vo.getNotifyUrl());
        params.put("version",version);
         //业务参数
        params.put("biz_content",biz_content);

        /*3、对数据进行加签，并存入请求数据*/
        try{
            String sign = YsOnlineSignUtils.sign(params, vo.getPrivateKeyPassword(),vo.getPrivateKeyFilePath());
            logger.info("DsApi.singleProtocolQuickDs params  signValue:"+sign);
            params.put("sign",sign);
        }catch (Exception e){
            throw e;
        }

        /*4、发送请求*/
        String result = HttpClientUtil.sendPostParam(vo.getReqUrl(), StringUtil.mapToString(params));
        logger.info("DsApi.singleProtocolQuickDs response result:"+result);
        if(StringUtil.isEmpty(result)){
            logger.info("DsApi.singleProtocolQuickDs response result is null");
        }
        /*5、结果验签*/
        JSONObject jsonObject= JSON.parseObject(result, Feature.OrderedField);
        // 加签值
        String sinaValue = (String) jsonObject.get("sign");
        // 加签内容
        String content = jsonObject.get("ysepay_ds_single_protocol_quick_accept_response").toString();
        boolean flag = YsOnlineSignUtils.rsaCheckContent(content,sinaValue,Constants.CHARSET_UTF_8,vo.getYsPublicKeyFilePath());
        if(!flag){
            throw new Exception("check sign error");
        }
        return content;
    }
    /**
     * 单笔代收平台内受理接口
     * @param vo
     * @return
     * @throws Exception
     */
    public static String singleQuickInnerDs(OnlineReqDataVo vo) throws Exception {

        /** 1、对业务数据进行加密*/
        String biz_content = JSONObject.toJSONString(vo.getParamData());
        logger.info("DsApi.singleQuickInnerDs param biz_content:"+biz_content);

        /** 2、组装入参的数据*/
        Map<String, String> params = new HashMap<String, String>();
        params.put("method","ysepay.ds.single.quick.inner.accept");
        //商户在银盛支付平台开设的用户号[商户号]
        params.put("partner_id",vo.getPartnerId());
        //发送请求的时间，格式"yyyy-MM-dd HH:mm:ss"
        params.put("timestamp", DateUtil.getDateNow());
        params.put("charset", Constants.CHARSET_UTF_8);
        params.put("sign_type", "RSA");
        //银盛支付服务器主动通知商户网站里指定的页面http路径。 不可空
        params.put("notify_url",vo.getNotifyUrl());
        params.put("version",version);
        //业务参数
        params.put("biz_content",biz_content);

        /*3、对数据进行加签，并存入请求数据*/
        try{
            String sign = YsOnlineSignUtils.sign(params, vo.getPrivateKeyPassword(),vo.getPrivateKeyFilePath());
            logger.info("DsApi.singleQuickInnerDs params  signValue:"+sign);
            params.put("sign",sign);
        }catch (Exception e){
           throw e;
        }

        /*4、发送请求*/
        String result = HttpClientUtil.sendPostParam(vo.getReqUrl(), StringUtil.mapToString(params));
        logger.info("DsApi.singleQuickInnerDs response result:"+result);
        if(StringUtil.isEmpty(result)){
            logger.info("DsApi.singleQuickInnerDs response is null");
        }
        /*5、结果验签*/
        JSONObject jsonObject= JSON.parseObject(result, Feature.OrderedField);
        // 加签值
        String sinaValue = (String) jsonObject.get("sign");
        // 加签内容
        String content = jsonObject.get("ysepay_ds_single_quick_inner_accept_response").toString();
        boolean flag = YsOnlineSignUtils.rsaCheckContent(content,sinaValue,Constants.CHARSET_UTF_8,vo.getYsPublicKeyFilePath());
        if(!flag){
            throw new Exception("check sign error");
        }
        return content;
    }
    /**
     * 单笔代收查询接口
     * @param vo
     * @return
     * @throws Exception
     */
    public static String singleQueryDs(OnlineReqDataVo vo) throws Exception {

        /** 1、对业务数据进行加密*/
        String biz_content = JSONObject.toJSONString(vo.getParamData());
        logger.info("DsApi.singleQueryDs param biz_content:"+biz_content);

        /** 2、组装入参的数据*/
        Map<String, String> params = new HashMap<String, String>();
        params.put("method","ysepay.ds.single.query");
        //商户在银盛支付平台开设的用户号[商户号]
        params.put("partner_id",vo.getPartnerId());
        //发送请求的时间，格式"yyyy-MM-dd HH:mm:ss"
        params.put("timestamp", DateUtil.getDateNow());
        params.put("charset", Constants.CHARSET_UTF_8);
        params.put("sign_type", "RSA");
        //银盛支付服务器主动通知商户网站里指定的页面http路径。 不可空
        params.put("notify_url",vo.getNotifyUrl());
        params.put("version",version);
         //业务参数
        params.put("biz_content",biz_content);

        /*3、对数据进行加签，并存入请求数据*/
        try{
            String sign = YsOnlineSignUtils.sign(params, vo.getPrivateKeyPassword(),vo.getPrivateKeyFilePath());
            logger.info("DsApi.singleQueryDs params  signValue:"+sign);
            params.put("sign",sign);
        }catch (Exception e){
            throw e;
        }

        /*4、发送请求*/
        String result = HttpClientUtil.sendPostParam(vo.getReqUrl(), StringUtil.mapToString(params));
        logger.info("DsApi.singleQueryDs response result:"+result);
        if(StringUtil.isEmpty(result)){
            logger.info("DsApi.singleQueryDs response is empty");
        }
        /*5、结果验签*/
        JSONObject jsonObject= JSON.parseObject(result, Feature.OrderedField);
        // 加签值
        String sinaValue = (String) jsonObject.get("sign");
        // 加签内容
        String content = jsonObject.get("ysepay_ds_single_query_response").toString();
        boolean flag = YsOnlineSignUtils.rsaCheckContent(content,sinaValue,Constants.CHARSET_UTF_8,vo.getYsPublicKeyFilePath());
        if(!flag){
            throw new Exception("check sign error");
        }
        return content;
    }
    /**
     * 单笔代收协议查询接口请求
     * @param vo
     * @return
     * @throws Exception
     */
    public static String singleProtocolQueryDs(OnlineReqDataVo vo) throws Exception {

        /** 1、对业务数据进行加密*/
        String biz_content = JSONObject.toJSONString(vo.getParamData());
        logger.info("DsApi.singleProtocolQueryDs param biz_content:"+biz_content);

        /** 2、组装入参的数据*/
        Map<String, String> params = new HashMap<String, String>();
        params.put("method","ysepay.ds.protocol.single.query");
        //商户在银盛支付平台开设的用户号[商户号]
        params.put("partner_id",vo.getPartnerId());
        //发送请求的时间，格式"yyyy-MM-dd HH:mm:ss"
        params.put("timestamp", DateUtil.getDateNow());
        params.put("charset", Constants.CHARSET_UTF_8);
        params.put("sign_type", "RSA");
        //银盛支付服务器主动通知商户网站里指定的页面http路径。 不可空
        params.put("notify_url",vo.getNotifyUrl());
        params.put("version",version);
         //业务参数
        params.put("biz_content",biz_content);

        /*3、对数据进行加签，并存入请求数据*/
        try{
            String sign = YsOnlineSignUtils.sign(params, vo.getPrivateKeyPassword(),vo.getPrivateKeyFilePath());
            logger.info("DsApi.singleProtocolQueryDs params  signValue:"+sign);
            params.put("sign",sign);
        }catch (Exception e){
            throw e;
        }

        /*4、发送请求*/
        String result = HttpClientUtil.sendPostParam(vo.getReqUrl(), StringUtil.mapToString(params));
        logger.info("DsApi.singleProtocolQueryDs response result:"+result);
        if(StringUtil.isEmpty(result)){
            logger.info("DsApi.singleProtocolQueryDs is empty");
        }
        /*5、结果验签*/
        JSONObject jsonObject= JSON.parseObject(result, Feature.OrderedField);
        // 加签值
        String sinaValue = (String) jsonObject.get("sign");
        // 加签内容
        String content = jsonObject.get("ysepay_ds_protocol_single_query_response").toString();
        boolean flag = YsOnlineSignUtils.rsaCheckContent(content,sinaValue,Constants.CHARSET_UTF_8,vo.getYsPublicKeyFilePath());
        if(!flag){
            throw new Exception("check sign error");
        }
        return content;
    }
    /**
     * 单笔代收协议签约
     * @param vo
     * @return
     * @throws Exception
     */
    public static String singleProtocolAcceptDs(OnlineReqDataVo vo) throws Exception {

        /** 1、对业务数据进行加密*/
        String biz_content = JSONObject.toJSONString(vo.getParamData());
        logger.info("DsApi.singleProtocolAcceptDs.bizContent param:"+biz_content);

        /** 2、组装入参的数据*/
        Map<String, String> params = new HashMap<String, String>();
        params.put("method","ysepay.ds.protocol.single.accept");
        //商户在银盛支付平台开设的用户号[商户号]
        params.put("partner_id",vo.getPartnerId());
        //发送请求的时间，格式"yyyy-MM-dd HH:mm:ss"
        params.put("timestamp", DateUtil.getDateNow());
        params.put("charset", Constants.CHARSET_UTF_8);
        params.put("sign_type", "RSA");
        //银盛支付服务器主动通知商户网站里指定的页面http路径。 不可空
        params.put("notify_url",vo.getNotifyUrl());
        params.put("version",version);
         //业务参数
        params.put("biz_content",biz_content);

        /*3、对数据进行加签，并存入请求数据*/
        try{
            String sign = YsOnlineSignUtils.sign(params, vo.getPrivateKeyPassword(),vo.getPrivateKeyFilePath());
            logger.info("DsApi.singleProtocolAcceptDs params  signValue:"+sign);
            params.put("sign",sign);
        }catch (Exception e){
            throw e;
        }

        /*4、发送请求*/
        String result = HttpClientUtil.sendPostParam(vo.getReqUrl(), StringUtil.mapToString(params));
        logger.info("DsApi.singleProtocolAcceptDs params  reponse  result:"+result);
        if(StringUtil.isEmpty(result)){
            logger.info("DsApi.singleProtocolAcceptDs params  reponse  result is empty");
        }
        /*5、结果验签*/
        JSONObject jsonObject= JSON.parseObject(result, Feature.OrderedField);
        // 加签值
        String sinaValue = (String) jsonObject.get("sign");
        // 加签内容
        String content = jsonObject.get("ysepay_ds_protocol_single_accept_response").toString();
        boolean flag = YsOnlineSignUtils.rsaCheckContent(content,sinaValue,Constants.CHARSET_UTF_8,vo.getYsPublicKeyFilePath());
        if(!flag){
            throw new Exception("check sign error");
        }
        return content;
    }

    /**
     * 单笔代收协议签约获取授权码接口请求
     * @param vo
     * @return
     * @throws Exception
     */
    public static String singleProtocolAcceptGetAuthorizeMsgDs(OnlineReqDataVo vo) throws Exception {

        /** 1、对业务数据进行加密*/
        String biz_content = JSONObject.toJSONString(vo.getParamData());
        logger.info("DsApi.singleProtocolAcceptGetAuthorizeMsgDs.bizContent param:"+biz_content);

        /** 2、组装入参的数据*/
        Map<String, String> params = new HashMap<String, String>();
        params.put("method","ysepay.ds.protocol.single.accept.get.authorize.msg");
        //商户在银盛支付平台开设的用户号[商户号]
        params.put("partner_id",vo.getPartnerId());
        //发送请求的时间，格式"yyyy-MM-dd HH:mm:ss"
        params.put("timestamp", DateUtil.getDateNow());
        params.put("charset", Constants.CHARSET_UTF_8);
        params.put("sign_type", "RSA");
        //银盛支付服务器主动通知商户网站里指定的页面http路径。 不可空
        params.put("notify_url",vo.getNotifyUrl());
        params.put("version",version);
         //业务参数
        params.put("biz_content",biz_content);

        /*3、对数据进行加签，并存入请求数据*/
        try{
            String sign = YsOnlineSignUtils.sign(params, vo.getPrivateKeyPassword(),vo.getPrivateKeyFilePath());
            logger.info("DsApi.singleProtocolAcceptGetAuthorizeMsgDs params  signValue:"+sign);
            params.put("sign",sign);
        }catch (Exception e){
           throw e;
        }

        /*4、发送请求*/
        String result = HttpClientUtil.sendPostParam(vo.getReqUrl(), StringUtil.mapToString(params));
        logger.info("DsApi.singleProtocolAcceptGetAuthorizeMsgDs params  reponse  result:"+result);
        if(StringUtil.isEmpty(result)){
            logger.info("DsApi.singleProtocolAcceptGetAuthorizeMsgDs params  reponse  result is null");
        }
        /*5、结果验签*/
        JSONObject jsonObject= JSON.parseObject(result, Feature.OrderedField);
        // 加签值
        String sinaValue = (String) jsonObject.get("sign");
        // 加签内容
        String content = jsonObject.get("ysepay_ds_protocol_single_accept_get_authorize_msg_response").toString();
        boolean flag = YsOnlineSignUtils.rsaCheckContent(content,sinaValue,Constants.CHARSET_UTF_8,vo.getYsPublicKeyFilePath());
        if(!flag){
            throw new Exception("check sign error");
        }
        return content;
    }
    /**
     * 单笔代收协议签约授权接口请求
     * @param vo
     * @return
     * @throws Exception
     */
    public static String singleProtocolAcceptGetAuthorizeDs(OnlineReqDataVo vo) throws Exception {

        /** 1、对业务数据进行加密*/
        String biz_content = JSONObject.toJSONString(vo.getParamData());
        logger.info("DsApi.singleProtocolAcceptGetAuthorizeDs.bizContent param:"+biz_content);

        /** 2、组装入参的数据*/
        Map<String, String> params = new HashMap<String, String>();
        params.put("method","ysepay.ds.protocol.single.accept.get.authorize");
        //商户在银盛支付平台开设的用户号[商户号]
        params.put("partner_id",vo.getPartnerId());
        //发送请求的时间，格式"yyyy-MM-dd HH:mm:ss"
        params.put("timestamp", DateUtil.getDateNow());
        params.put("charset", Constants.CHARSET_UTF_8);
        params.put("sign_type", "RSA");
        //银盛支付服务器主动通知商户网站里指定的页面http路径。 不可空
        params.put("notify_url",vo.getNotifyUrl());
        params.put("version",version);
        //业务参数
        params.put("biz_content",biz_content);

        /*3、对数据进行加签，并存入请求数据*/
        try{
            String sign = YsOnlineSignUtils.sign(params, vo.getPrivateKeyPassword(),vo.getPrivateKeyFilePath());
            logger.info("DsApi.singleProtocolAcceptGetAuthorizeDs params  signValue:"+sign);
            params.put("sign",sign);
        }catch (Exception e){
            throw e;
        }

        /*4、发送请求*/
        String result = HttpClientUtil.sendPostParam(vo.getReqUrl(), StringUtil.mapToString(params));
        logger.info("DsApi.singleProtocolAcceptGetAuthorizeDs params  reponse  result:"+result);
        if(StringUtil.isEmpty(result)){
            logger.info("DsApi.singleProtocolAcceptGetAuthorizeDs params  reponse  result is null");
        }
        /*5、结果验签*/
        JSONObject jsonObject= JSON.parseObject(result, Feature.OrderedField);
        // 加签值
        String sinaValue = (String) jsonObject.get("sign");
        // 加签内容
        String content = jsonObject.get("ysepay_ds_protocol_single_accept_get_authorize_response").toString();
        boolean flag = YsOnlineSignUtils.rsaCheckContent(content,sinaValue,Constants.CHARSET_UTF_8,vo.getYsPublicKeyFilePath());
        if(!flag){
            throw new Exception("check sign error");
        }
        return content;
    }

    /**
     * 批量代收（银行卡）普通受理接口
     * @param vo
     * @return
     * @throws Exception
     */
    public static String batchNormalDs(OnlineReqDataVo vo) throws Exception {

        /** 1、对业务数据进行加密*/
        String biz_content = JSONObject.toJSONString(vo.getParamData());
        logger.info("DsApi.batchNormalDs.bizContent param:"+biz_content);

        /** 2、组装入参的数据*/
        Map<String, String> params = new HashMap<String, String>();
        params.put("method","ysepay.ds.batch.normal.accept");
        //商户在银盛支付平台开设的用户号[商户号]
        params.put("partner_id",vo.getPartnerId());
        //代理密码，加密传输
        //发送请求的时间，格式"yyyy-MM-dd HH:mm:ss"
        params.put("timestamp", DateUtil.getDateNow());
        params.put("charset", Constants.CHARSET_UTF_8);
        params.put("sign_type", "RSA");
        //银盛支付服务器主动通知商户网站里指定的页面http路径。 不可空
        params.put("notify_url",vo.getNotifyUrl());
        params.put("version",version);
         //业务参数
        params.put("biz_content",biz_content);

        /*3、对数据进行加签，并存入请求数据*/
        try{
            String sign = YsOnlineSignUtils.sign(params, vo.getPrivateKeyPassword(),vo.getPrivateKeyFilePath());
            logger.info("DsApi.batchNormalDs params  signValue:"+sign);
            params.put("sign",sign);
        }catch (Exception e){
            throw e;
        }

        /*4、发送请求*/
        String result = HttpClientUtil.sendPostParam(vo.getReqUrl(), StringUtil.mapToString(params));
        logger.info("DsApi.batchNormalDs reponse  result:"+result);
        if(StringUtil.isEmpty(result)){
            logger.info("DsApi.batchNormalDs reponse  is null");
        }
        /*5、结果验签*/
        JSONObject jsonObject= JSON.parseObject(result, Feature.OrderedField);
        // 加签值
        String sinaValue = (String) jsonObject.get("sign");
        // 加签内容
        String content = jsonObject.get("ysepay_ds_batch_normal_accept_response").toString();
        boolean flag = YsOnlineSignUtils.rsaCheckContent(content,sinaValue,Constants.CHARSET_UTF_8,vo.getYsPublicKeyFilePath());
        if(!flag){
            throw new Exception("check sign error");
        }
        return content;
    }
    /**
     * 批量代收（平台内）普通受理接口
     * @param vo
     * @return
     * @throws Exception
     */
    public static String batchNormalInnerDs(OnlineReqDataVo vo) throws Exception {

        /** 1、对业务数据进行加密*/
        String biz_content = JSONObject.toJSONString(vo.getParamData());
        logger.info("DsApi.batchNormalInnerDs.bizContent param:"+biz_content);

        /** 2、组装入参的数据*/
        Map<String, String> params = new HashMap<String, String>();
        params.put("method","ysepay.ds.batch.normal.inner.accept");
        //商户在银盛支付平台开设的用户号[商户号]
        params.put("partner_id",vo.getPartnerId());
        //代理密码，加密传输
        //发送请求的时间，格式"yyyy-MM-dd HH:mm:ss"
        params.put("timestamp", DateUtil.getDateNow());
        params.put("charset", Constants.CHARSET_UTF_8);
        params.put("sign_type", "RSA");
        //银盛支付服务器主动通知商户网站里指定的页面http路径。 不可空
        params.put("notify_url",vo.getNotifyUrl());
        params.put("version",version);
         //业务参数
        params.put("biz_content",biz_content);

        /*3、对数据进行加签，并存入请求数据*/
        try{
            String sign = YsOnlineSignUtils.sign(params, vo.getPrivateKeyPassword(),vo.getPrivateKeyFilePath());
            logger.info("DsApi.batchNormalInnerDs params  signValue:"+sign);
            params.put("sign",sign);
        }catch (Exception e){
            throw e;
        }

        /*4、发送请求*/
        String result = HttpClientUtil.sendPostParam(vo.getReqUrl(), StringUtil.mapToString(params));
        logger.info("DsApi.batchNormalInnerDs reponse  result:"+result);
        if(StringUtil.isEmpty(result)){
            logger.info("DsApi.batchNormalInnerDs reponse  result is empty");
        }
        /*5、结果验签*/
        JSONObject jsonObject= JSON.parseObject(result, Feature.OrderedField);
        // 加签值
        String sinaValue = (String) jsonObject.get("sign");
        // 加签内容
        String content = jsonObject.get("ysepay_ds_batch_normal_inner_accept_response").toString();
        boolean flag = YsOnlineSignUtils.rsaCheckContent(content,sinaValue,Constants.CHARSET_UTF_8,vo.getYsPublicKeyFilePath());
        if(!flag){
            throw new Exception("check sign error");
        }
        return content;
    }
    /**
     * 批量代收汇总查询接口
     * @param vo
     * @return
     * @throws Exception
     */
    public static String batchSummaryQueryDs(OnlineReqDataVo vo) throws Exception {

        /** 1、对业务数据进行加密*/
        String biz_content = JSONObject.toJSONString(vo.getParamData());
        logger.info("DsApi.batchSummaryQueryDs.bizContent param:"+biz_content);

        /** 2、组装入参的数据*/
        Map<String, String> params = new HashMap<String, String>();
        params.put("method","ysepay.ds.batch.summary.query");
        //商户在银盛支付平台开设的用户号[商户号]
        params.put("partner_id",vo.getPartnerId());
        //代理密码，加密传输
        //发送请求的时间，格式"yyyy-MM-dd HH:mm:ss"
        params.put("timestamp", DateUtil.getDateNow());
        params.put("charset", Constants.CHARSET_UTF_8);
        params.put("sign_type", "RSA");
        //银盛支付服务器主动通知商户网站里指定的页面http路径。 不可空
        params.put("notify_url",vo.getNotifyUrl());
        params.put("version",version);
         //业务参数
        params.put("biz_content",biz_content);

        /*3、对数据进行加签，并存入请求数据*/
        try{
            String sign = YsOnlineSignUtils.sign(params, vo.getPrivateKeyPassword(),vo.getPrivateKeyFilePath());
            logger.info("DsApi.batchSummaryQueryDs params  signValue:"+sign);
            params.put("sign",sign);
        }catch (Exception e){
           throw e;
        }

        /*4、发送请求*/
        String result = HttpClientUtil.sendPostParam(vo.getReqUrl(), StringUtil.mapToString(params));
        logger.info("DsApi.batchSummaryQueryDs reponse  result:"+result);
        if(StringUtil.isEmpty(result)){
            logger.info("DsApi.batchSummaryQueryDs reponse  result is empty");
        }
        /*5、结果验签*/
        JSONObject jsonObject= JSON.parseObject(result, Feature.OrderedField);
        // 加签值
        String sinaValue = (String) jsonObject.get("sign");
        // 加签内容
        String content = jsonObject.get("ysepay_ds_batch_summary_query_response").toString();
        boolean flag = YsOnlineSignUtils.rsaCheckContent(content,sinaValue,Constants.CHARSET_UTF_8,vo.getYsPublicKeyFilePath());
        if(!flag){
            throw new Exception("check sign error");
        }
        return content;
    }
    /**
     * 批量代收明细查询接口
     * @param vo
     * @return
     * @throws Exception
     */
    public static String batchDetailQueryDs(OnlineReqDataVo vo) throws Exception {

        /** 1、对业务数据进行加密*/
        String biz_content = JSONObject.toJSONString(vo.getParamData());
        logger.info("DsApi.batchDetailQueryDs.bizContent param:"+biz_content);

        /** 2、组装入参的数据*/
        Map<String, String> params = new HashMap<String, String>();
        params.put("method","ysepay.ds.batch.detail.query");
        //商户在银盛支付平台开设的用户号[商户号]
        params.put("partner_id",vo.getPartnerId());
        //代理密码，加密传输
        //发送请求的时间，格式"yyyy-MM-dd HH:mm:ss"
        params.put("timestamp", DateUtil.getDateNow());
        params.put("charset", Constants.CHARSET_UTF_8);
        params.put("sign_type", "RSA");
        //银盛支付服务器主动通知商户网站里指定的页面http路径。 不可空
        params.put("notify_url",vo.getNotifyUrl());
        params.put("version",version);
         //业务参数
        params.put("biz_content",biz_content);

        /*3、对数据进行加签，并存入请求数据*/
        try{
            String sign = YsOnlineSignUtils.sign(params, vo.getPrivateKeyPassword(),vo.getPrivateKeyFilePath());
            logger.info("DsApi.batchDetailQueryDs params  signValue:"+sign);
            params.put("sign",sign);
        }catch (Exception e){
            throw e;
        }

        /*4、发送请求*/
        String result = HttpClientUtil.sendPostParam(vo.getReqUrl(), StringUtil.mapToString(params));
        logger.info("DsApi.batchDetailQueryDs reponse  result:"+result);
        if(StringUtil.isEmpty(result)){
            logger.info("DsApi.batchDetailQueryDs reponse  is empty:");
        }
        /*5、结果验签*/
        JSONObject jsonObject= JSON.parseObject(result, Feature.OrderedField);
        // 加签值
        String sinaValue = (String) jsonObject.get("sign");
        // 加签内容
        String content = jsonObject.get("ysepay_ds_batch_detail_query_response").toString();
        boolean flag = YsOnlineSignUtils.rsaCheckContent(content,sinaValue,Constants.CHARSET_UTF_8,vo.getYsPublicKeyFilePath());
        if(!flag){
            throw new Exception("check sign error");
        }
        return content;
    }
}
