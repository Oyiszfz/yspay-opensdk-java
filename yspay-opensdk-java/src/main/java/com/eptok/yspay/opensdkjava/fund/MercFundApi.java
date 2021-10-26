package com.eptok.yspay.opensdkjava.fund;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.eptok.yspay.opensdkjava.common.Constants;
import com.eptok.yspay.opensdkjava.pojo.vo.OnlineReqDataVo;
import com.eptok.yspay.opensdkjava.util.*;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @author
 * @description: 资金实现类
 * 商户余额查询 {@link  #getFundAccount(OnlineReqDataVo)}
 * 商户提现查询 {@link  #withdrawQuery(OnlineReqDataVo)}
 * 实时提现（待结算账户到银行卡） {@link  #withdrawD0(OnlineReqDataVo)}
 * 实时提现（一般户到银行卡） {@link  #withdrawQuick(OnlineReqDataVo)}
 * 一般消费户退款 {@link  #refundGeneralAccount(OnlineReqDataVo)}
 *
 * 担保交易发货通知接口 {@link  #tradeDelivered(OnlineReqDataVo)}
 * 担保交易确认收货接口 {@link  #tradeConfirm(OnlineReqDataVo)}
 *
 * 线上分账登记请求接口 {@link  #divisionOnlineAccept(OnlineReqDataVo)}
 * 线上分账查询接口 {@link  #divisionOnlineQuery(OnlineReqDataVo)}
 * 分账退款交易请求 {@link  #tradeRefundSplit(OnlineReqDataVo)}
 * 分账对账单下载地址接口 {@link  #divisionDownloadUrl(OnlineReqDataVo)}
 *
 * @Date
 */
public class MercFundApi {

    private static final Logger logger = Logger.getLogger("MercFundApi");

    /**
     * @Description 商户余额查询
     * @Date 14:22 2021/9/26
     * @Param
     * @return
     */
    public static String getFundAccount(OnlineReqDataVo vo) throws Exception {
        /** 2、组装入参的数据*/
        Map<String, String> params = new HashMap<>();
        params.put("method", "ysepay.merchant.balance.query");
        params.put("partner_id", vo.getPartnerId());
        params.put("timestamp", DateUtil.getDateNow());
        params.put("charset", Constants.CHARSET_UTF_8);
        params.put("sign_type", "RSA");
        params.put("version", "3.0");
        params.put("biz_content", JSONObject.toJSONString(vo.getParamData()));

        /*3、对数据进行加签，并存入请求数据*/
        String sign = YsOnlineSignUtils.sign(params, vo.getPrivateKeyPassword(),vo.getPrivateKeyFilePath());
        logger.info("merchant.balance.query-sign："+sign);
        params.put("sign",sign);

        /*4、发送请求*/
        String result = HttpClientUtil.sendPostParam(vo.getReqUrl(),StringUtil.mapToString(params));
        logger.info("merchant.balance.query-result:"+result);

        /*5、结果验签*/
        JSONObject jsonObject= JSON.parseObject(result, Feature.OrderedField);
        // 加签值
        String signValue = (String) jsonObject.get("sign");
        // 加签内容
        String content = JSONObject.toJSONString(jsonObject.get("ysepay_merchant_balance_query_response"),SerializerFeature.WriteMapNullValue);
        boolean flag = YsOnlineSignUtils.rsaCheckContent(content, signValue, Constants.CHARSET_UTF_8,vo.getYsPublicKeyFilePath());
        if(!flag){
            throw new Exception("check sign error");
        } else {
            logger.info("check sign success");
        }
        return content;
    }

    /**
     * 商户提现查询
     * @param vo
     * @return
     * @throws Exception
     */
    public static String withdrawQuery(OnlineReqDataVo vo) throws Exception {
        /** 2、组装入参的数据*/
        Map<String, String> params = new HashMap<>();
        params.put("method", "ysepay.merchant.withdraw.quick.query");
        params.put("partner_id", vo.getPartnerId());
        params.put("timestamp", DateUtil.getDateNow());
        params.put("charset", Constants.CHARSET_UTF_8);
        params.put("sign_type", "RSA");
        params.put("version", "3.0");
        params.put("biz_content", JSONObject.toJSONString(vo.getParamData()));

        /*3、对数据进行加签，并存入请求数据*/
        String sign = YsOnlineSignUtils.sign(params, vo.getPrivateKeyPassword(),vo.getPrivateKeyFilePath());
        logger.info("merchant.withdraw.quick.query-sign："+sign);
        params.put("sign",sign);

        /*4、发送请求*/
        String result = HttpClientUtil.sendPostParam(vo.getReqUrl(),StringUtil.mapToString(params));
        logger.info("merchant.withdraw.quick.query-result:"+result);

        /*5、结果验签*/
        JSONObject jsonObject= JSON.parseObject(result, Feature.OrderedField);
        // 加签值
        String sinaValue = (String) jsonObject.get("sign");
        // 加签内容
        String content = JSONObject.toJSONString(jsonObject.get("ysepay_merchant_withdraw_quick_query_response"),SerializerFeature.WriteMapNullValue);
        boolean flag = YsOnlineSignUtils.rsaCheckContent(content,sinaValue, Constants.CHARSET_UTF_8,vo.getYsPublicKeyFilePath());
        if(!flag){
            throw new Exception("check sign error");
        } else {
            logger.info("check sign success");
        }
        return content;
    }

    /**
     * 实时提现（待结算账户到银行卡）
     * @param vo
     * @return
     * @throws Exception
     */
    public static String withdrawD0(OnlineReqDataVo vo) throws Exception {
        /** 2、组装入参的数据*/
        Map<String, String> params = new HashMap<>();
        //使用DES加密，密钥partner_id用户号前8位，不足8位前补空
        if (null != vo.getProxyPassword()) {
            String proxyPassword = vo.getProxyPassword();
            String partnerId = vo.getPartnerId();
            if (partnerId.length() <8) {
                partnerId = "        " + partnerId;
                partnerId = partnerId.substring(partnerId.length()-8,partnerId.length());
            } else {
                partnerId = partnerId.substring(0,8);
            }
            proxyPassword = DesUtil.encryptExtraData(partnerId, Constants.CHARSET_UTF_8, proxyPassword);
            logger.info("merchant.withdraw.d0.accept-Des-proxy_password: "+proxyPassword);
            params.put("proxy_password", proxyPassword);
        }
        /** 1、对业务数据进行加密*/
        String data = JSONObject.toJSONString(vo.getParamData());
        params.put("method", "ysepay.merchant.withdraw.d0.accept");
        params.put("partner_id", vo.getPartnerId());
        params.put("timestamp", DateUtil.getDateNow());
        params.put("charset", Constants.CHARSET_UTF_8);
        params.put("sign_type", "RSA");
        params.put("version", "3.0");
        params.put("notify_url", vo.getNotifyUrl());
        params.put("biz_content", data);

        /*3、对数据进行加签，并存入请求数据*/
        String sign = YsOnlineSignUtils.sign(params, vo.getPrivateKeyPassword(),vo.getPrivateKeyFilePath());
        logger.info("merchant.withdraw.d0.accept-sign："+sign);
        params.put("sign",sign);

        /*4、发送请求*/
        String result = HttpClientUtil.sendPostParam(vo.getReqUrl(), StringUtil.mapToString(params));
        logger.info("merchant.withdraw.d0.accept-result:"+result);

        /*5、结果验签*/
        JSONObject jsonObject= JSON.parseObject(result, Feature.OrderedField);
        // 加签值
        String sinaValue = (String) jsonObject.get("sign");
        // 加签内容
        String content = JSONObject.toJSONString(jsonObject.get("ysepay_merchant_withdraw_d0_accept_response"),SerializerFeature.WriteMapNullValue);
        boolean flag = YsOnlineSignUtils.rsaCheckContent(content,sinaValue, Constants.CHARSET_UTF_8,vo.getYsPublicKeyFilePath());
        if(!flag){
            throw new Exception("check sign error");
        } else {
            logger.info("check sign success");
        }
        return content;
    }

    /**
     * 实时提现（一般户到银行卡）
     * @param vo
     * @return
     * @throws Exception
     */
    public static String withdrawQuick(OnlineReqDataVo vo) throws Exception {
        /** 2、组装入参的数据*/
        Map<String, String> params = new HashMap<>();

        //使用DES加密，密钥partner_id用户号前8位，不足8位前补空
        if (null != vo.getProxyPassword()) {
            String proxyPassword = vo.getProxyPassword();
            String partnerId = vo.getPartnerId();
            if (partnerId.length() <8) {
                partnerId = "        " + partnerId;
                partnerId = partnerId.substring(partnerId.length()-8,partnerId.length());
            } else {
                partnerId = partnerId.substring(0,8);
            }
            proxyPassword = DesUtil.encryptExtraData(partnerId, Constants.CHARSET_UTF_8, proxyPassword);
            logger.info("merchant.withdraw.quick.accept-Des-proxy_password:"+proxyPassword);
            params.put("proxy_password", proxyPassword);
        }

        /** 1、对业务数据进行加密*/
        String data = JSONObject.toJSONString(vo.getParamData());
        params.put("method", "ysepay.merchant.withdraw.quick.accept");
        params.put("partner_id", vo.getPartnerId());
        params.put("timestamp", DateUtil.getDateNow());
        params.put("charset", Constants.CHARSET_UTF_8);
        params.put("sign_type", "RSA");
        params.put("version", "3.0");
        params.put("notify_url", vo.getNotifyUrl());
        params.put("biz_content", data);

        /*3、对数据进行加签，并存入请求数据*/
        String sign = YsOnlineSignUtils.sign(params, vo.getPrivateKeyPassword(),vo.getPrivateKeyFilePath());
        logger.info("merchant.withdraw.quick.accept-sign："+sign);
        params.put("sign",sign);

        /*4、发送请求*/
        String result = HttpClientUtil.sendPostParam(vo.getReqUrl(),StringUtil.mapToString(params));
        logger.info("merchant.withdraw.quick.accept-result:"+result);

        /*5、结果验签*/
        JSONObject jsonObject= JSON.parseObject(result, Feature.OrderedField);
        // 加签值
        String sinaValue = (String) jsonObject.get("sign");
        // 加签内容
        String content = JSONObject.toJSONString(jsonObject.get("ysepay_merchant_withdraw_quick_accept_response"),SerializerFeature.WriteMapNullValue);
        boolean flag = YsOnlineSignUtils.rsaCheckContent(content,sinaValue, Constants.CHARSET_UTF_8,vo.getYsPublicKeyFilePath());
        if(!flag){
            throw new Exception("check sign error");
        } else {
            logger.info("check sign success");
        }
        return content;
    }

    /**
     * 一般消费户退款
     * @param vo
     * @return
     * @throws Exception
     */
    public static String refundGeneralAccount(OnlineReqDataVo vo) throws Exception {
        /** 2、组装入参的数据*/
        Map<String, String> params = new HashMap<>();
        params.put("method", "ysepay.online.trade.refund.general.account");
        params.put("partner_id", vo.getPartnerId());
        params.put("timestamp", DateUtil.getDateNow());
        params.put("charset", Constants.CHARSET_UTF_8);
        params.put("sign_type", "RSA");
        params.put("version", "3.0");
        params.put("tran_type", vo.getTranType());
        params.put("biz_content", JSONObject.toJSONString(vo.getParamData()));

        /*3、对数据进行加签，并存入请求数据*/
        String sign = YsOnlineSignUtils.sign(params, vo.getPrivateKeyPassword(),vo.getPrivateKeyFilePath());
        logger.info("online.trade.refund.general.account-sign："+sign);
        params.put("sign",sign);

        /*4、发送请求*/
        String result = HttpClientUtil.sendPostParam(vo.getReqUrl(), StringUtil.mapToString(params));
        logger.info("online.trade.refund.general.account-result:"+result);

        /*5、结果验签*/
        JSONObject jsonObject= JSON.parseObject(result, Feature.OrderedField);
        // 加签值
        String sinaValue = (String) jsonObject.get("sign");
        // 加签内容
        String content = JSONObject.toJSONString(jsonObject.get("ysepay_online_trade_refund_general_account_response"), SerializerFeature.WriteMapNullValue);
        boolean flag = YsOnlineSignUtils.rsaCheckContent(content,sinaValue, Constants.CHARSET_UTF_8,vo.getYsPublicKeyFilePath());
        if(!flag){
            throw new Exception("check sign error");
        } else {
            logger.info("check sign success");
        }
        return content;
    }

    /**
     * 担保交易发货通知接口
     * @param vo
     * @return
     * @throws Exception
     */
    public static String tradeDelivered(OnlineReqDataVo vo) throws Exception {
        /** 2、组装入参的数据*/
        Map<String, String> params = new HashMap<>();
        params.put("method", "ysepay.online.trade.delivered");
        params.put("partner_id", vo.getPartnerId());
        params.put("timestamp", DateUtil.getDateNow());
        params.put("charset", Constants.CHARSET_UTF_8);
        params.put("sign_type", "RSA");
        params.put("version", "3.0");
        params.put("biz_content", JSONObject.toJSONString(vo.getParamData()));

        /*3、对数据进行加签，并存入请求数据*/
        String sign = YsOnlineSignUtils.sign(params, vo.getPrivateKeyPassword(),vo.getPrivateKeyFilePath());
        logger.info("online.trade.delivered-sign："+sign);
        params.put("sign",sign);

        /*4、发送请求*/
        String result = HttpClientUtil.sendPostParam(vo.getReqUrl(), StringUtil.mapToString(params));
        logger.info("online.trade.delivered-result:"+result);

        /*5、结果验签*/
        JSONObject jsonObject= JSON.parseObject(result, Feature.OrderedField);
        // 加签值
        String sinaValue = (String) jsonObject.get("sign");
        // 加签内容
        String content = JSONObject.toJSONString(jsonObject.get("ysepay_online_trade_delivered_response"), SerializerFeature.WriteMapNullValue);
        boolean flag = YsOnlineSignUtils.rsaCheckContent(content,sinaValue, Constants.CHARSET_UTF_8,vo.getYsPublicKeyFilePath());
        if(!flag){
            throw new Exception("check sign error");
        } else {
            logger.info("check sign success");
        }
        return content;
    }

    /**
     * 担保交易确认收货接口
     * @param vo
     * @return
     * @throws Exception
     */
    public static String tradeConfirm(OnlineReqDataVo vo) throws Exception {
        /** 2、组装入参的数据*/
        Map<String, String> params = new HashMap<>();
        params.put("method", "ysepay.online.trade.confirm");
        params.put("partner_id", vo.getPartnerId());
        params.put("timestamp", DateUtil.getDateNow());
        params.put("charset", Constants.CHARSET_UTF_8);
        params.put("sign_type", "RSA");
        params.put("version", "3.0");
        params.put("biz_content", JSONObject.toJSONString(vo.getParamData()));

        /*3、对数据进行加签，并存入请求数据*/
        String sign = YsOnlineSignUtils.sign(params, vo.getPrivateKeyPassword(),vo.getPrivateKeyFilePath());
        logger.info("online.trade.confirm-sign："+sign);
        params.put("sign",sign);

        /*4、发送请求*/
        String result = HttpClientUtil.sendPostParam(vo.getReqUrl(), StringUtil.mapToString(params));
        logger.info("online.trade.confirm-result:"+result);

        /*5、结果验签*/
        JSONObject jsonObject= JSON.parseObject(result, Feature.OrderedField);
        // 加签值
        String sinaValue = (String) jsonObject.get("sign");
        // 加签内容
        String content = JSONObject.toJSONString(jsonObject.get("ysepay_online_trade_confirm_response"), SerializerFeature.WriteMapNullValue);
        boolean flag = YsOnlineSignUtils.rsaCheckContent(content,sinaValue, Constants.CHARSET_UTF_8,vo.getYsPublicKeyFilePath());
        if(!flag){
            throw new Exception("check sign error");
        } else {
            logger.info("check sign success");
        }
        return content;
    }

    /**
     * 线上分账登记请求接口
     * @param vo
     * @return
     * @throws Exception
     */
    public static String divisionOnlineAccept(OnlineReqDataVo vo) throws Exception {
        /** 2、组装入参的数据*/
        Map<String, String> params = new HashMap<>();
        params.put("method", "ysepay.single.division.online.accept");
        params.put("partner_id", vo.getPartnerId());
        params.put("timestamp", DateUtil.getDateNow());
        params.put("charset", Constants.CHARSET_UTF_8);
        params.put("sign_type", "RSA");
        params.put("version", "3.0");
        params.put("notify_url", vo.getNotifyUrl());
        params.put("biz_content", JSONObject.toJSONString(vo.getParamData()));

        /*3、对数据进行加签，并存入请求数据*/
        String sign = YsOnlineSignUtils.sign(params, vo.getPrivateKeyPassword(),vo.getPrivateKeyFilePath());
        logger.info("single.division.online.accept-sign："+sign);
        params.put("sign",sign);

        /*4、发送请求*/
        String result = HttpClientUtil.sendPostParam(vo.getReqUrl(), StringUtil.mapToString(params));
        logger.info("single.division.online.accept-result:"+result);

        /*5、结果验签*/
        JSONObject jsonObject= JSON.parseObject(result, Feature.OrderedField);
        // 加签值
        String sinaValue = (String) jsonObject.get("sign");
        // 加签内容
        String content = JSONObject.toJSONString(jsonObject.get("ysepay_single_division_online_accept_response"), SerializerFeature.WriteMapNullValue);
        boolean flag = YsOnlineSignUtils.rsaCheckContent(content,sinaValue, Constants.CHARSET_UTF_8,vo.getYsPublicKeyFilePath());
        if(!flag){
            throw new Exception("check sign error");
        } else {
            logger.info("check sign success");
        }
        return content;
    }

    /**
     * 线上分账查询接口
     * @param vo
     * @return
     * @throws Exception
     */
    public static String divisionOnlineQuery(OnlineReqDataVo vo) throws Exception {
        /** 2、组装入参的数据*/
        Map<String, String> params = new HashMap<>();
        params.put("method", "ysepay.single.division.online.query");
        params.put("partner_id", vo.getPartnerId());
        params.put("timestamp", DateUtil.getDateNow());
        params.put("charset", Constants.CHARSET_UTF_8);
        params.put("sign_type", "RSA");
        params.put("version", "3.0");
        params.put("notify_url", vo.getNotifyUrl());
        params.put("biz_content", JSONObject.toJSONString(vo.getParamData()));

        /*3、对数据进行加签，并存入请求数据*/
        String sign = YsOnlineSignUtils.sign(params, vo.getPrivateKeyPassword(),vo.getPrivateKeyFilePath());
        logger.info("single.division.online.query-sign："+sign);
        params.put("sign",sign);

        /*4、发送请求*/
        String result = HttpClientUtil.sendPostParam(vo.getReqUrl(), StringUtil.mapToString(params));
        logger.info("single.division.online.query-result:"+result);

        /*5、结果验签*/
        JSONObject jsonObject= JSON.parseObject(result, Feature.OrderedField);
        // 加签值
        String sinaValue = (String) jsonObject.get("sign");
        // 加签内容
        String content = JSONObject.toJSONString(jsonObject.get("ysepay_single_division_online_query_response"), SerializerFeature.WriteMapNullValue);
        boolean flag = YsOnlineSignUtils.rsaCheckContent(content,sinaValue, Constants.CHARSET_UTF_8,vo.getYsPublicKeyFilePath());
        if(!flag){
            throw new Exception("check sign error");
        } else {
            logger.info("check sign success");
        }
        return content;
    }

    /**
     * 分账退款交易请求
     * @param vo
     * @return
     * @throws Exception
     */
    public static String tradeRefundSplit(OnlineReqDataVo vo) throws Exception {
        /** 2、组装入参的数据*/
        Map<String, String> params = new HashMap<>();
        params.put("method", "ysepay.online.trade.refund.split");
        params.put("partner_id", vo.getPartnerId());
        params.put("timestamp", DateUtil.getDateNow());
        params.put("charset", Constants.CHARSET_UTF_8);
        params.put("sign_type", "RSA");
        params.put("version", "3.0");
        params.put("notify_url", vo.getNotifyUrl());
        params.put("tran_type", vo.getTranType());
        params.put("biz_content", JSONObject.toJSONString(vo.getParamData()));

        /*3、对数据进行加签，并存入请求数据*/
        String sign = YsOnlineSignUtils.sign(params, vo.getPrivateKeyPassword(),vo.getPrivateKeyFilePath());
        logger.info("online.trade.refund.split-sign："+sign);
        params.put("sign",sign);

        /*4、发送请求*/
        String result = HttpClientUtil.sendPostParam(vo.getReqUrl(), StringUtil.mapToString(params));
        logger.info("online.trade.refund.split-result:"+result);

        /*5、结果验签*/
        JSONObject jsonObject= JSON.parseObject(result, Feature.OrderedField);
        // 加签值
        String sinaValue = (String) jsonObject.get("sign");
        // 加签内容
        String content = JSONObject.toJSONString(jsonObject.get("ysepay_online_trade_refund_split_response"), SerializerFeature.WriteMapNullValue);
        boolean flag = YsOnlineSignUtils.rsaCheckContent(content,sinaValue, Constants.CHARSET_UTF_8,vo.getYsPublicKeyFilePath());
        if(!flag){
            throw new Exception("check sign error");
        } else {
            logger.info("check sign success");
        }
        return content;
    }

    /**
     * 分账对账单下载地址接口
     * @param vo
     * @return
     * @throws Exception
     */
    public static String divisionDownloadUrl(OnlineReqDataVo vo) throws Exception {
        /** 2、组装入参的数据*/
        Map<String, String> params = new HashMap<>();
        params.put("method", "ysepay.online.division.downloadurl.get");
        params.put("partner_id", vo.getPartnerId());
        params.put("timestamp", DateUtil.getDateNow());
        params.put("charset", Constants.CHARSET_UTF_8);
        params.put("sign_type", "RSA");
        params.put("version", "3.0");
        params.put("biz_content", JSONObject.toJSONString(vo.getParamData()));

        /*3、对数据进行加签，并存入请求数据*/
        String sign = YsOnlineSignUtils.sign(params, vo.getPrivateKeyPassword(),vo.getPrivateKeyFilePath());
        logger.info("online.division.downloadurl.get-sign："+sign);
        params.put("sign",sign);

        /*4、发送请求*/
        String result = HttpClientUtil.sendPostParam(vo.getReqUrl(), StringUtil.mapToString(params));
        logger.info("online.division.downloadurl.get-result:"+result);

        /*5、结果验签*/
        JSONObject jsonObject= JSON.parseObject(result, Feature.OrderedField);
        // 加签值
        String sinaValue = (String) jsonObject.get("sign");
        // 加签内容
        String content = JSONObject.toJSONString(jsonObject.get("ysepay_online_division_downloadurl_get_response"), SerializerFeature.WriteMapNullValue);
        boolean flag = YsOnlineSignUtils.rsaCheckContent(content,sinaValue, Constants.CHARSET_UTF_8,vo.getYsPublicKeyFilePath());
        if(!flag){
            throw new Exception("check sign error");
        } else {
            logger.info("check sign success");
        }
        return content;
    }

    /**
     * 线下分账登记请求接口
     * @param vo
     * @return
     * @throws Exception
     */
    public static String divisionOfflineAccept(OnlineReqDataVo vo) throws Exception {
        /** 2、组装入参的数据*/
        Map<String, String> params = new HashMap<>();
        params.put("method", "ysepay.single.division.offline.accept");
        params.put("partner_id", vo.getPartnerId());
        params.put("timestamp", DateUtil.getDateNow());
        params.put("charset", Constants.CHARSET_UTF_8);
        params.put("sign_type", "RSA");
        params.put("version", "3.0");
        params.put("notify_url", vo.getNotifyUrl());
        params.put("biz_content", JSONObject.toJSONString(vo.getParamData()));

        /*3、对数据进行加签，并存入请求数据*/
        String sign = YsOnlineSignUtils.sign(params, vo.getPrivateKeyPassword(),vo.getPrivateKeyFilePath());
        logger.info("single.division.offline.accept-sign："+sign);
        params.put("sign",sign);

        /*4、发送请求*/
        String result = HttpClientUtil.sendPostParam(vo.getReqUrl(), StringUtil.mapToString(params));
        logger.info("single.division.offline.accept-result:"+result);

        /*5、结果验签*/
        JSONObject jsonObject= JSON.parseObject(result, Feature.OrderedField);
        // 加签值
        String sinaValue = (String) jsonObject.get("sign");
        // 加签内容
        String content = JSONObject.toJSONString(jsonObject.get("ysepay_single_division_offline_accept_response"), SerializerFeature.WriteMapNullValue);
        boolean flag = YsOnlineSignUtils.rsaCheckContent(content,sinaValue, Constants.CHARSET_UTF_8,vo.getYsPublicKeyFilePath());
        if(!flag){
            throw new Exception("check sign error");
        } else {
            logger.info("check sign success");
        }
        return content;
    }

    /**
     * 线下分账查询接口
     * @param vo
     * @return
     * @throws Exception
     */
    public static String divisionOfflineQuery(OnlineReqDataVo vo) throws Exception {
        /** 2、组装入参的数据*/
        Map<String, String> params = new HashMap<>();
        params.put("method", "ysepay.single.division.offline.query");
        params.put("partner_id", vo.getPartnerId());
        params.put("timestamp", DateUtil.getDateNow());
        params.put("charset", Constants.CHARSET_UTF_8);
        params.put("sign_type", "RSA");
        params.put("version", "3.0");
        params.put("notify_url", vo.getNotifyUrl());
        params.put("biz_content", JSONObject.toJSONString(vo.getParamData()));

        /*3、对数据进行加签，并存入请求数据*/
        String sign = YsOnlineSignUtils.sign(params, vo.getPrivateKeyPassword(),vo.getPrivateKeyFilePath());
        logger.info("single.division.offline.query-sign："+sign);
        params.put("sign",sign);

        /*4、发送请求*/
        String result = HttpClientUtil.sendPostParam(vo.getReqUrl(), StringUtil.mapToString(params));
        logger.info("single.division.offline.query-result:"+result);

        /*5、结果验签*/
        JSONObject jsonObject= JSON.parseObject(result, Feature.OrderedField);
        // 加签值
        String sinaValue = (String) jsonObject.get("sign");
        // 加签内容
        String content = JSONObject.toJSONString(jsonObject.get("ysepay_single_division_offline_query_response"), SerializerFeature.WriteMapNullValue);
        boolean flag = YsOnlineSignUtils.rsaCheckContent(content,sinaValue, Constants.CHARSET_UTF_8,vo.getYsPublicKeyFilePath());
        if(!flag){
            throw new Exception("check sign error");
        } else {
            logger.info("check sign success");
        }
        return content;
    }

    /**
     * 直联分账登记请求接口
     * @param vo
     * @return
     * @throws Exception
     */
    public static String directDivisionAccept(OnlineReqDataVo vo) throws Exception {
        /** 2、组装入参的数据*/
        Map<String, String> params = new HashMap<>();
        params.put("method", "ysepay.direct.division.accept");
        params.put("partner_id", vo.getPartnerId());
        params.put("timestamp", DateUtil.getDateNow());
        params.put("charset", Constants.CHARSET_UTF_8);
        params.put("sign_type", "RSA");
        params.put("version", "3.0");
        params.put("biz_content", JSONObject.toJSONString(vo.getParamData()));

        /*3、对数据进行加签，并存入请求数据*/
        String sign = YsOnlineSignUtils.sign(params, vo.getPrivateKeyPassword(),vo.getPrivateKeyFilePath());
        logger.info("direct.division.accept-sign："+sign);
        params.put("sign",sign);

        /*4、发送请求*/
        String result = HttpClientUtil.sendPostParam(vo.getReqUrl(), StringUtil.mapToString(params));
        logger.info("direct.division.accept-result:"+result);

        /*5、结果验签*/
        JSONObject jsonObject= JSON.parseObject(result, Feature.OrderedField);
        // 加签值
        String sinaValue = (String) jsonObject.get("sign");
        // 加签内容
        String content = JSONObject.toJSONString(jsonObject.get("ysepay_direct_division_accept_response"), SerializerFeature.WriteMapNullValue);
        boolean flag = YsOnlineSignUtils.rsaCheckContent(content,sinaValue, Constants.CHARSET_UTF_8,vo.getYsPublicKeyFilePath());
        if(!flag){
            throw new Exception("check sign error");
        } else {
            logger.info("check sign success");
        }
        return content;
    }

    /**
     * 直联分账查询接口
     * @param vo
     * @return
     * @throws Exception
     */
    public static String directDivisionQuery(OnlineReqDataVo vo) throws Exception {
        /** 2、组装入参的数据*/
        Map<String, String> params = new HashMap<>();
        params.put("method", "ysepay.direct.division.query");
        params.put("partner_id", vo.getPartnerId());
        params.put("timestamp", DateUtil.getDateNow());
        params.put("charset", Constants.CHARSET_UTF_8);
        params.put("sign_type", "RSA");
        params.put("version", "3.0");
        params.put("biz_content", JSONObject.toJSONString(vo.getParamData()));

        /*3、对数据进行加签，并存入请求数据*/
        String sign = YsOnlineSignUtils.sign(params, vo.getPrivateKeyPassword(),vo.getPrivateKeyFilePath());
        logger.info("direct.division.query-sign："+sign);
        params.put("sign",sign);

        /*4、发送请求*/
        String result = HttpClientUtil.sendPostParam(vo.getReqUrl(), StringUtil.mapToString(params));
        logger.info("direct.division.query-result:"+result);

        /*5、结果验签*/
        JSONObject jsonObject= JSON.parseObject(result, Feature.OrderedField);
        // 加签值
        String sinaValue = (String) jsonObject.get("sign");
        // 加签内容
        String content = JSONObject.toJSONString(jsonObject.get("ysepay_direct_division_query_response"), SerializerFeature.WriteMapNullValue);
        boolean flag = YsOnlineSignUtils.rsaCheckContent(content,sinaValue, Constants.CHARSET_UTF_8,vo.getYsPublicKeyFilePath());
        if(!flag){
            throw new Exception("check sign error");
        } else {
            logger.info("check sign success");
        }
        return content;
    }
}
