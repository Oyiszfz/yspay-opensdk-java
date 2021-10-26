package com.eptok.yspay.opensdkjava.merc;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
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
 * 商户扫码报备SDK
 */
public class MercChannelReportApi {

    private static final Logger logger = Logger.getLogger("MercChannelReportApi");
    private final static String version = "3.0";

    /**
     * @Description 商户扫码报备接口
     * @Date 14:22 2021/9/26
     * @Param
     * @return
     */
    public static String mercScanReport(OnlineReqDataVo vo) throws Exception {

        /** 1、对业务数据进行加密*/
        String biz_content = JSONObject.toJSONString(vo.getParamData());
        logger.info("MercChannelReportApi-mercScanReport-bizContent:"+biz_content);

        /** 2、组装入参的数据*/
        Map<String, String> params = new HashMap<String, String>();
        params.put("method","ysepay.merchant.scan.report");
        params.put("partner_id",vo.getPartnerId());
        params.put("timestamp", DateUtil.getDateNow());
        params.put("charset", Constants.CHARSET_UTF_8);
        params.put("sign_type", "RSA");
        params.put("notify_url",vo.getNotifyUrl());
        params.put("version",version);
        params.put("biz_content",biz_content);

        /*3、对数据进行加签，并存入请求数据*/
        try{
            String sign = YsOnlineSignUtils.sign(params, vo.getPrivateKeyPassword(),vo.getPrivateKeyFilePath());
            logger.info("MercChannelReportApi-mercScanReport-signValue:"+sign);
            params.put("sign",sign);
        }catch (Exception e){
            logger.info("签名异常："+e);
        }

        /*4、发送请求*/
        String result = HttpClientUtil.sendPostParam(vo.getReqUrl(),StringUtil.mapToString(params));
        logger.info("MercChannelReportApi-mercScanReport-remoteResult:"+result);
        if(StringUtil.isEmpty(result)){
            logger.info("mercScanReport result is null");
        }
        /*5、结果验签*/
        JSONObject jsonObject= JSON.parseObject(result, Feature.OrderedField);
        // 加签值
        String sinaValue = jsonObject.getString("sign");
        // 加签内容
        String content = JSONObject.toJSONString(jsonObject.get("ysepay_merchant_scan_report_response"),SerializerFeature.WriteMapNullValue);
        boolean flag = YsOnlineSignUtils.rsaCheckContent(content,sinaValue,Constants.CHARSET_UTF_8,vo.getYsPublicKeyFilePath());
        if(!flag){
            throw new Exception("check sign error");
        }
        return content;
    }


    /**
     * @Description 商户扫码报备接口
     * @Date 14:22 2021/9/26
     * @Param
     * @return
     */
    public static String queryMercScanReport(OnlineReqDataVo vo) throws Exception {

        /** 1、对业务数据进行加密*/
        String biz_content = JSONObject.toJSONString(vo.getParamData());
        logger.info("MercChannelReportApi-queryMercScanReport-bizContent:"+biz_content);

        /** 2、组装入参的数据*/
        Map<String, String> params = new HashMap<String, String>();
        params.put("method","ysepay.merchant.scan.report.query");
        params.put("partner_id",vo.getPartnerId());
        params.put("timestamp", DateUtil.getDateNow());
        params.put("charset", Constants.CHARSET_UTF_8);
        params.put("sign_type", "RSA");
        params.put("notify_url",vo.getNotifyUrl());
        params.put("version",version);
        params.put("biz_content",biz_content);

        /*3、对数据进行加签，并存入请求数据*/
        try{
            String sign = YsOnlineSignUtils.sign(params, vo.getPrivateKeyPassword(),vo.getPrivateKeyFilePath());
            logger.info("MercChannelReportApi-queryMercScanReport-signValue:"+sign);
            params.put("sign",sign);
        }catch (Exception e){
           logger.info("sign error");
           throw e;
        }

        /*4、发送请求*/
        String result = HttpClientUtil.sendPostParam(vo.getReqUrl(),StringUtil.mapToString(params));
        logger.info("MercChannelReportApi-queryMercScanReport-remoteResult:"+result);
        if(StringUtil.isEmpty(result)){
            logger.info("result is null");
        }
        /*5、结果验签 */
        JSONObject jsonObject= JSON.parseObject(result, Feature.OrderedField);
        // 加签值
        String sinaValue = jsonObject.getString("sign");
        // 加签内容
        String content = JSONObject.toJSONString(jsonObject.get("ysepay_merchant_scan_report_query_response"),SerializerFeature.WriteMapNullValue);
        boolean flag = YsOnlineSignUtils.rsaCheckContent(content,sinaValue,Constants.CHARSET_UTF_8,vo.getYsPublicKeyFilePath());
        if(!flag){
            logger.info("check sign error");
            throw new Exception("check sign error");
        }
        return content;
    }


    /**
     * @Description 商户活动报名接口
     * @Date 14:22 2021/9/26
     * @Param
     * @return
     */
    public static String scanReportActivity(OnlineReqDataVo vo) throws Exception {

        /** 1、对业务数据进行加密*/
        String biz_content = JSONObject.toJSONString(vo.getParamData());
        logger.info("MercChannelReportApi-scanReportActivity-bizContent:"+biz_content);

        /** 2、组装入参的数据*/
        Map<String, String> params = new HashMap<String, String>();
        params.put("method","ysepay.merchant.scan.report.activity");
        params.put("partner_id",vo.getPartnerId());
        params.put("timestamp", DateUtil.getDateNow());
        params.put("charset", Constants.CHARSET_UTF_8);
        params.put("sign_type", "RSA");
        params.put("notify_url",vo.getNotifyUrl());
        params.put("version",version);
        params.put("biz_content",biz_content);

        /*3、对数据进行加签，并存入请求数据*/
        try{
            String sign = YsOnlineSignUtils.sign(params, vo.getPrivateKeyPassword(),vo.getPrivateKeyFilePath());
            logger.info("MercChannelReportApi-scanReportActivity-signValue:"+sign);
            params.put("sign",sign);
        }catch (Exception e){
            logger.info("签名异常："+e);
        }

        /*4、发送请求*/
        String result = HttpClientUtil.sendPostParam(vo.getReqUrl(),StringUtil.mapToString(params));
        logger.info("MercChannelReportApi-scanReportActivity-remoteResult:"+result);
        if(StringUtil.isEmpty(result)){
            logger.info("MercChannelReportApi scanReportActivity result is null");
        }
        /*5、结果验签 */
        JSONObject jsonObject= JSON.parseObject(result, Feature.OrderedField);
        // 加签值
        String sinaValue = jsonObject.getString("sign");
        // 加签内容
        String content = JSONObject.toJSONString(jsonObject.get("ysepay_merchant_scan_report_activity_response"),SerializerFeature.WriteMapNullValue);
        boolean flag = YsOnlineSignUtils.rsaCheckContent(content,sinaValue,Constants.CHARSET_UTF_8,vo.getYsPublicKeyFilePath());
        if(!flag){
            logger.info("check sign error");
            throw new Exception("check sign error");
        }
        return content;
    }

    /**
     * @Description 商户扫码报备修改
     * @Date 14:22 2021/9/26
     * @Param
     * @return
     */
    public static String mercScanReportUpdate(OnlineReqDataVo vo) throws Exception {

        /** 1、对业务数据进行加密*/
        String biz_content = JSONObject.toJSONString(vo.getParamData());
        logger.info("MercChannelReportApi-mercScanReportUpdate-bizContent:"+biz_content);

        /** 2、组装入参的数据*/
        Map<String, String> params = new HashMap<String, String>();
        params.put("method","ysepay.merchant.scan.report.update");
        params.put("partner_id",vo.getPartnerId());
        params.put("timestamp", DateUtil.getDateNow());
        params.put("charset", Constants.CHARSET_UTF_8);
        params.put("sign_type", "RSA");
        params.put("notify_url",vo.getNotifyUrl());
        params.put("version",version);
        params.put("biz_content",biz_content);

        /*3、对数据进行加签，并存入请求数据*/
        try{
            String sign = YsOnlineSignUtils.sign(params, vo.getPrivateKeyPassword(),vo.getPrivateKeyFilePath());
            logger.info("MercChannelReportApi-mercScanReportUpdate-signValue"+sign);
            params.put("sign",sign);
        }catch (Exception e){
            throw e;
        }

        /*4、发送请求*/
        String result = HttpClientUtil.sendPostParam(vo.getReqUrl(),StringUtil.mapToString(params));
        logger.info("MercChannelReportApi-mercScanReportUpdate-remoteResute:"+result);
        if(StringUtil.isEmpty(result)){
            logger.info("MercChannelReportApi mercScanReportUpdate result is null");
        }
        /*5、结果验签 */
        JSONObject jsonObject= JSON.parseObject(result, Feature.OrderedField);
        // 加签值
        String sinaValue = jsonObject.getString("sign");
        // 加签内容
        String content = JSONObject.toJSONString(jsonObject.get("ysepay_merchant_scan_report_update_response"),SerializerFeature.WriteMapNullValue);
        boolean flag = YsOnlineSignUtils.rsaCheckContent(content,sinaValue,Constants.CHARSET_UTF_8,vo.getYsPublicKeyFilePath());
        if(!flag){
            logger.info("check sign error");
            throw new Exception("check sign erro");
        }
        return content;
    }

    /**
     * @Description 扫码报备异步通知
     * @Date 14:22 2021/9/26
     * @Param
     * @return
     */
    public static String sacnReportUpdateNotify(OnlineReqDataVo vo) throws Exception {
        /** 1、获取异步通知数据*/
        Map<String,Object> notifyContent = vo.getParamData();
        logger.info("MercChannelReportApi-sacnReportUpdateNotify-notifyData:"+JSONObject.toJSONString(notifyContent));

        /*2、对异步通知数据进行验签 */
        // 加签值
        String sinaValue = (String) notifyContent.get("sign");
        // 加签内容
        String content = JSONObject.toJSONString(notifyContent.get("ysepay_merchant_scan_report_update_response"),SerializerFeature.WriteMapNullValue);
        boolean flag = YsOnlineSignUtils.rsaCheckContent(content,sinaValue,Constants.CHARSET_UTF_8,vo.getYsPublicKeyFilePath());
        if(!flag){
            logger.info("check sign error");
            throw new Exception("check sign error");
        }
        return content;
    }
}
