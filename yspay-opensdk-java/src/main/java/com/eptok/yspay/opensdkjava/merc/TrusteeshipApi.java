package com.eptok.yspay.opensdkjava.merc;

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

public class TrusteeshipApi {

    private static final Logger logger = Logger.getLogger("TrusteeshipApi");
    private final static String version = "3.0";

    /**
     * @Description 商户签约接口
     * @Date 14:22 2021/9/26
     * @Param
     * @return
     */
    public static String mercSign(OnlineReqDataVo vo) throws Exception {

        /** 1、业务数据转json*/
        String biz_content = JSONObject.toJSONString(vo.getParamData());
        logger.info("TrusteeshipApi-mercSign-biz_content:"+biz_content);

        /** 2、组装入参的数据*/
        Map<String, String> params = new HashMap<String, String>();
        params.put("method","ysepay.trusteeship.sign");
        params.put("partner_id",vo.getPartnerId());
        params.put("timestamp", DateUtil.getDateNow());
        params.put("charset", Constants.CHARSET_UTF_8);
        params.put("sign_type", "RSA");
        params.put("notify_url",vo.getNotifyUrl());
        params.put("version",version);
        params.put("biz_content",biz_content);

        /*3、对数据进行加签，并存入请求数据*/
        String sign = YsOnlineSignUtils.sign(params, vo.getPrivateKeyPassword(),vo.getPrivateKeyFilePath());
        logger.info("TrusteeshipApi-mercSign-signData:"+sign);
        params.put("sign",sign);

        /*4、发送请求*/
        String result = HttpClientUtil.sendPostParam(vo.getReqUrl(), StringUtil.mapToString(params));
        logger.info("TrusteeshipApi-mercSign-resultData:"+result);
        if(StringUtil.isEmpty(result)){
            logger.info("TrusteeshipApi mercSign resultData is empty");
        }
        /*5、结果验签*/
        JSONObject jsonObject= JSON.parseObject(result, Feature.OrderedField);
        // 加签值
        String sinaValue = (String) jsonObject.get("sign");
        // 加签内容
        String content = jsonObject.get("ysepay_trusteeship_sign_response").toString();
        boolean flag = YsOnlineSignUtils.rsaCheckContent(content,sinaValue,Constants.CHARSET_UTF_8,vo.getYsPublicKeyFilePath());
        if(!flag){
            throw new Exception("check sign error");
        }
        return content;
    }

    /**
     * @Description 商户签约确认接口
     * @Date 14:22 2021/9/26
     * @Param
     * @return
     */
    public static String mercSignConfirm(OnlineReqDataVo vo) throws Exception {

        /** 1、业务数据转json*/
        String biz_content = JSONObject.toJSONString(vo.getParamData());
        logger.info("TrusteeshipApi-mercSignConfirm-biz_content:"+biz_content);

        /** 2、组装入参的数据*/
        Map<String, String> params = new HashMap<String, String>();
        params.put("method","ysepay.trusteeship.sign.confirm");
        params.put("partner_id",vo.getPartnerId());
        params.put("timestamp", DateUtil.getDateNow());
        params.put("charset", Constants.CHARSET_UTF_8);
        params.put("sign_type", "RSA");
        params.put("notify_url",vo.getNotifyUrl());
        params.put("version",version);
        params.put("biz_content",biz_content);

        /*3、对数据进行加签，并存入请求数据*/
        String sign = YsOnlineSignUtils.sign(params, vo.getPrivateKeyPassword(),vo.getPrivateKeyFilePath());
        logger.info("TrusteeshipApi-mercSignConfirm-signData:"+sign);
        params.put("sign",sign);

        /*4、发送请求*/
        String result = HttpClientUtil.sendPostParam(vo.getReqUrl(), StringUtil.mapToString(params));
        logger.info("TrusteeshipApi-mercSignConfirm-resultData:"+result);
        if(StringUtil.isEmpty(result)){
            logger.info("TrusteeshipApi mercSignConfirm resultData is empty");
        }
        /*5、结果验签*/
        JSONObject jsonObject= JSON.parseObject(result, Feature.OrderedField);
        // 加签值
        String sinaValue = (String) jsonObject.get("sign");
        // 加签内容
        String content = jsonObject.get("ysepay_trusteeship_sign_confirm_response").toString();
        boolean flag = YsOnlineSignUtils.rsaCheckContent(content,sinaValue,Constants.CHARSET_UTF_8,vo.getYsPublicKeyFilePath());
        if(!flag){
            throw new Exception("check sign error");
        }
        return content;
    }

    /**
     * @Description 协议查询接口
     * @Date 14:22 2021/9/26
     * @Param
     * @return
     */
    public static String queryProtocol(OnlineReqDataVo vo) throws Exception {

        /** 1、业务数据转json*/
        String biz_content = JSONObject.toJSONString(vo.getParamData());
        logger.info("TrusteeshipApi-queryProtocol-biz_content:"+biz_content);

        /** 2、组装入参的数据*/
        Map<String, String> params = new HashMap<String, String>();
        params.put("method","ysepay.trusteeship.fastPay.queryProtocol");
        params.put("partner_id",vo.getPartnerId());
        params.put("timestamp", DateUtil.getDateNow());
        params.put("charset", Constants.CHARSET_UTF_8);
        params.put("sign_type", "RSA");
        params.put("notify_url",vo.getNotifyUrl());
        params.put("version",version);
        params.put("biz_content",biz_content);

        /*3、对数据进行加签，并存入请求数据*/
        String sign = YsOnlineSignUtils.sign(params, vo.getPrivateKeyPassword(),vo.getPrivateKeyFilePath());
        logger.info("TrusteeshipApi-queryProtocol-signData:"+sign);
        params.put("sign",sign);

        /*4、发送请求*/
        String result = HttpClientUtil.sendPostParam(vo.getReqUrl(), StringUtil.mapToString(params));
        logger.info("TrusteeshipApi-queryProtocol-resultData:"+result);
        if(StringUtil.isEmpty(result)){
            logger.info("TrusteeshipApi queryProtocol resultData is empty");
        }
        /*5、结果验签*/
        JSONObject jsonObject= JSON.parseObject(result, Feature.OrderedField);
        // 加签值
        String sinaValue = (String) jsonObject.get("sign");
        // 加签内容
        String content = jsonObject.get("ysepay_trusteeship_fastPay_queryProtocol_response").toString();
        boolean flag = YsOnlineSignUtils.rsaCheckContent(content,sinaValue,Constants.CHARSET_UTF_8,vo.getYsPublicKeyFilePath());
        if(!flag){
            throw new Exception("check sign error");
        }
        return content;
    }

    /**
     * @Description 商户快捷协议解约接口
     * @Date 14:22 2021/9/26
     * @Param
     * @return
     */
    public static String mercSignCancel(OnlineReqDataVo vo) throws Exception {

        /** 1、业务数据转json*/
        String biz_content = JSONObject.toJSONString(vo.getParamData());
        logger.info("TrusteeshipApi-mercSignCancel-biz_content:"+biz_content);

        /** 2、组装入参的数据*/
        Map<String, String> params = new HashMap<String, String>();
        params.put("method","ysepay.trusteeship.sign.cancel");
        params.put("partner_id",vo.getPartnerId());
        params.put("timestamp", DateUtil.getDateNow());
        params.put("charset", Constants.CHARSET_UTF_8);
        params.put("sign_type", "RSA");
        params.put("notify_url",vo.getNotifyUrl());
        params.put("version",version);
        params.put("biz_content",biz_content);

        /*3、对数据进行加签，并存入请求数据*/
        String sign = YsOnlineSignUtils.sign(params, vo.getPrivateKeyPassword(),vo.getPrivateKeyFilePath());
        logger.info("TrusteeshipApi-mercSignCancel-signData:"+sign);
        params.put("sign",sign);

        /*4、发送请求*/
        String result = HttpClientUtil.sendPostParam(vo.getReqUrl(), StringUtil.mapToString(params));
        logger.info("TrusteeshipApi-mercSignCancel-resultData:"+result);
        if(StringUtil.isEmpty(result)){
            logger.info("TrusteeshipApi mercSignCancel resultData is empty");
        }
        /*5、结果验签*/
        JSONObject jsonObject= JSON.parseObject(result, Feature.OrderedField);
        // 加签值
        String sinaValue = (String) jsonObject.get("sign");
        // 加签内容
        String content = jsonObject.get("ysepay_trusteeship_sign_cancel_response").toString();
        boolean flag = YsOnlineSignUtils.rsaCheckContent(content,sinaValue,Constants.CHARSET_UTF_8,vo.getYsPublicKeyFilePath());
        if(!flag){
            throw new Exception("check sign error");
        }
        return content;
    }

    /**
     * @Description 快捷协议支付接口
     * @Date 14:22 2021/9/26
     * @Param
     * @return
     */
    public static String fastPay(OnlineReqDataVo vo) throws Exception {

        /** 1、业务数据转json*/
        String biz_content = JSONObject.toJSONString(vo.getParamData());
        logger.info("TrusteeshipApi-fastPay-biz_content:"+biz_content);

        /** 2、组装入参的数据*/
        Map<String, String> params = new HashMap<String, String>();
        params.put("method","ysepay.trusteeship.fastPay");
        params.put("partner_id",vo.getPartnerId());
        params.put("timestamp", DateUtil.getDateNow());
        params.put("charset", Constants.CHARSET_UTF_8);
        params.put("sign_type", "RSA");
        params.put("notify_url",vo.getNotifyUrl());
        params.put("version",version);
        params.put("return_url",vo.getReturnUrl());
        params.put("biz_content",biz_content);

        /*3、对数据进行加签，并存入请求数据*/
        String sign = YsOnlineSignUtils.sign(params, vo.getPrivateKeyPassword(),vo.getPrivateKeyFilePath());
        logger.info("TrusteeshipApi-fastPay-signData:"+sign);
        params.put("sign",sign);

        /*4、发送请求*/
        String result = HttpClientUtil.sendPostParam(vo.getReqUrl(), StringUtil.mapToString(params));
        logger.info("TrusteeshipApi-fastPay-resultData:"+result);
        if(StringUtil.isEmpty(result)){
            logger.info("TrusteeshipApi fastPay resultData is empty");
        }
        /*5、结果验签*/
        JSONObject jsonObject= JSON.parseObject(result, Feature.OrderedField);
        // 加签值
        String sinaValue = (String) jsonObject.get("sign");
        // 加签内容
        String content = jsonObject.get("ysepay_trusteeship_fastPay_response").toString();
        boolean flag = YsOnlineSignUtils.rsaCheckContent(content,sinaValue,Constants.CHARSET_UTF_8,vo.getYsPublicKeyFilePath());
        if(!flag){
            throw new Exception("check sign error");
        }
        return content;
    }

    /**
     * @Description 快捷协议支付短信确认支付接口
     * @Date 14:22 2021/9/26
     * @Param
     * @return
     */
    public static String fastPayConfirm(OnlineReqDataVo vo) throws Exception {

        /** 1、业务数据转json*/
        String biz_content = JSONObject.toJSONString(vo.getParamData());
        logger.info("TrusteeshipApi-fastPayConfirm-biz_content:"+biz_content);

        /** 2、组装入参的数据*/
        Map<String, String> params = new HashMap<String, String>();
        params.put("method","ysepay.trusteeship.fastPay.confirm");
        params.put("partner_id",vo.getPartnerId());
        params.put("timestamp", DateUtil.getDateNow());
        params.put("charset", Constants.CHARSET_UTF_8);
        params.put("sign_type", "RSA");
        params.put("notify_url",vo.getNotifyUrl());
        params.put("version",version);
        params.put("biz_content",biz_content);

        /*3、对数据进行加签，并存入请求数据*/
        String sign = YsOnlineSignUtils.sign(params, vo.getPrivateKeyPassword(),vo.getPrivateKeyFilePath());
        logger.info("TrusteeshipApi-fastPayConfirm-signData:"+sign);
        params.put("sign",sign);

        /*4、发送请求*/
        String result = HttpClientUtil.sendPostParam(vo.getReqUrl(), StringUtil.mapToString(params));
        logger.info("TrusteeshipApi-fastPayConfirm-resultData:"+result);
        if(StringUtil.isEmpty(result)){
            logger.info("TrusteeshipApi fastPayConfirm resultData is empty");
        }
        /*5、结果验签*/
        JSONObject jsonObject= JSON.parseObject(result, Feature.OrderedField);
        // 加签值
        String sinaValue = (String) jsonObject.get("sign");
        // 加签内容
        String content = jsonObject.get("ysepay_trusteeship_fastPay_confirm_response").toString();
        boolean flag = YsOnlineSignUtils.rsaCheckContent(content,sinaValue,Constants.CHARSET_UTF_8,vo.getYsPublicKeyFilePath());
        if(!flag){
            throw new Exception("check sign error");
        }
        return content;
    }

    /**
     * @Description 快捷支付重新获取短信接口
     * @Date 14:22 2021/9/26
     * @Param
     * @return
     */
    public static String fastPayReachievesSms(OnlineReqDataVo vo) throws Exception {

        /** 1、业务数据转json*/
        String biz_content = JSONObject.toJSONString(vo.getParamData());
        logger.info("TrusteeshipApi-fastPayReachievesSms-biz_content:"+biz_content);

        /** 2、组装入参的数据*/
        Map<String, String> params = new HashMap<String, String>();
        params.put("method","ysepay.trusteeship.fastPay.reachievemsg");
        params.put("partner_id",vo.getPartnerId());
        params.put("timestamp", DateUtil.getDateNow());
        params.put("charset", Constants.CHARSET_UTF_8);
        params.put("sign_type", "RSA");
        params.put("notify_url",vo.getNotifyUrl());
        params.put("version",version);
        params.put("biz_content",biz_content);

        /*3、对数据进行加签，并存入请求数据*/
        String sign = YsOnlineSignUtils.sign(params, vo.getPrivateKeyPassword(),vo.getPrivateKeyFilePath());
        logger.info("TrusteeshipApi-fastPayReachievesSms-signData:"+sign);
        params.put("sign",sign);

        /*4、发送请求*/
        String result = HttpClientUtil.sendPostParam(vo.getReqUrl(), StringUtil.mapToString(params));
        logger.info("TrusteeshipApi-fastPayReachievesSms-resultData:"+result);
        if(StringUtil.isEmpty(result)){
            logger.info("TrusteeshipApi fastPayReachievesSms resultData is empty");
        }
        /*5、结果验签*/
        JSONObject jsonObject= JSON.parseObject(result, Feature.OrderedField);
        // 加签值
        String sinaValue = (String) jsonObject.get("sign");
        // 加签内容
        String content = jsonObject.get("ysepay_trusteeship_fastPay_reachievemsg_response").toString();
        boolean flag = YsOnlineSignUtils.rsaCheckContent(content,sinaValue,Constants.CHARSET_UTF_8,vo.getYsPublicKeyFilePath());
        if(!flag){
            throw new Exception("check sign error");
        }
        return content;
    }
}
