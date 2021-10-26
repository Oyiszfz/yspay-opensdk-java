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
 * @description: 代付相关API
 * 单笔代付交易（银行卡）-普通 {@link  #singleNormalDf(OnlineReqDataVo)}
 * 单笔代付交易（银行卡）-加急 {@link  #singleQuickDf(OnlineReqDataVo)}
 * 单笔代付（平台内） {@link  #singleInnerDf(OnlineReqDataVo)}
 * 单笔代付（代收协议） {@link  #singleDsprotocolQuickDf(OnlineReqDataVo)}
 * 单笔代付查询 {@link  #singleQueryDf(OnlineReqDataVo)}
 *
 * 批量代付（银行卡） {@link  #batchNormalDf(OnlineReqDataVo)}
 * 批量代付汇总查询接口 {@link  #batchSummaryQueryDf(OnlineReqDataVo)}
 * 批量代付明细查询接口 {@link  #batchDetailQueryDf(OnlineReqDataVo)}
 *
 * @date 2021/9/27 17:33
 */
public class DfApi {
    private static final Logger logger = Logger.getLogger(DfApi.class.getName());
    private final static String version = "3.0";

    /**
     * 单笔代付交易（银行卡）-普通
     * @param vo
     * @return
     * @throws Exception
     */
    public static String singleNormalDf(OnlineReqDataVo vo) throws Exception {

        /** 1、对业务数据进行加密*/
        String biz_content = JSONObject.toJSONString(vo.getParamData());
       logger.info("DfApi.singleNormalDf param biz_content:"+biz_content);

        /** 2、组装入参的数据*/
        Map<String, String> params = new HashMap<String, String>();
        params.put("method","ysepay.df.single.normal.accept");
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
            logger.info("DfApi.singleNormalDf.sign:"+sign);
            params.put("sign",sign);
        }catch (Exception e){
            throw e;
        }

        /*4、发送请求*/
        String result = HttpClientUtil.sendPostParam(vo.getReqUrl(), StringUtil.mapToString(params));
        logger.info("DfApi.singleNormalDf response result :"+result);
        if(StringUtil.isEmpty(result)){
            logger.info("DfApi.singleNormalDf response result is empty");
        }
        /*5、结果验签*/
        JSONObject jsonObject= JSON.parseObject(result, Feature.OrderedField);
        // 加签值
        String sinaValue = (String) jsonObject.get("sign");
        // 加签内容
        String content = jsonObject.get("ysepay_df_single_normal_accept_response").toString();
        boolean flag = YsOnlineSignUtils.rsaCheckContent(content,sinaValue,Constants.CHARSET_UTF_8,vo.getYsPublicKeyFilePath());
        if(!flag){
            throw new Exception("check sign error");
        }
        return content;
    }

    /**
     * 单笔普代付交易（银行卡）-加急
     * @param vo
     * @return
     * @throws Exception
     */
    public static String singleQuickDf(OnlineReqDataVo vo) throws Exception {

        /** 1、对业务数据进行加密*/
        String biz_content = JSONObject.toJSONString(vo.getParamData());

        logger.info("DfApi.singleQuickDf param biz_content:"+biz_content);

        /** 2、组装入参的数据*/
        Map<String, String> params = new HashMap<String, String>();
        params.put("method","ysepay.df.single.quick.accept");
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
            logger.info("DfApi.singleQuickDf params  signValue:"+sign);
            params.put("sign",sign);
        }catch (Exception e){
            throw e;
        }

        /*4、发送请求*/
        String result = HttpClientUtil.sendPostParam(vo.getReqUrl(), StringUtil.mapToString(params));
        logger.info("DfApi.singleQuickDf response result: "+result);
        if(StringUtil.isEmpty(result)){
            logger.info("DfApi.singleQuickDf response result is empty");
        }
        /*5、结果验签*/
        JSONObject jsonObject= JSON.parseObject(result, Feature.OrderedField);
        // 加签值
        String sinaValue = (String) jsonObject.get("sign");
        // 加签内容
        String content = jsonObject.get("ysepay_df_single_quick_accept_response").toString();
        boolean flag = YsOnlineSignUtils.rsaCheckContent(content,sinaValue,Constants.CHARSET_UTF_8,vo.getYsPublicKeyFilePath());
        if(!flag){
            throw new Exception("check sign error");
        }
        return content;
    }
    /**
     * 单笔代付受理接口（平台内）
     * @param vo
     * @return
     * @throws Exception
     */
    public static String singleInnerDf(OnlineReqDataVo vo) throws Exception {

        /** 1、对业务数据进行加密*/
        String biz_content = JSONObject.toJSONString(vo.getParamData());
        logger.info("DfApi.singleInnerDf param biz_content:"+biz_content);

        /** 2、组装入参的数据*/
        Map<String, String> params = new HashMap<String, String>();
        params.put("method","ysepay.df.single.quick.inner.accept");
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
            logger.info("DfApi.singleInnerDf  params  signValue:"+sign);
            params.put("sign",sign);
        }catch (Exception e){
            throw e;
        }

        /*4、发送请求*/
        String result = HttpClientUtil.sendPostParam(vo.getReqUrl(), StringUtil.mapToString(params));
        logger.info("DfApi.singleInnerDf response result :"+result);
        if(StringUtil.isEmpty(result)){
            logger.info("DfApi.singleInnerDf response is null");
        }
        /*5、结果验签*/
        JSONObject jsonObject= JSON.parseObject(result, Feature.OrderedField);
        // 加签值
        String sinaValue = (String) jsonObject.get("sign");
        // 加签内容
        String content = jsonObject.get("ysepay_df_single_quick_inner_accept_response").toString();
        boolean flag = YsOnlineSignUtils.rsaCheckContent(content,sinaValue,Constants.CHARSET_UTF_8,vo.getYsPublicKeyFilePath());
        if(!flag){
            throw new Exception("check sign error");
        }
        return content;
    }
    /**
     * 单笔代付交易（代收协议）
     * @param vo
     * @return
     * @throws Exception
     */
    public static String singleDsprotocolQuickDf(OnlineReqDataVo vo) throws Exception {

        /** 1、对业务数据进行加密*/
        String biz_content = JSONObject.toJSONString(vo.getParamData());
        logger.info("DfApi.singleDsprotocolQuickDf param biz_content:"+biz_content);

        /** 2、组装入参的数据*/
        Map<String, String> params = new HashMap<String, String>();
        params.put("method","ysepay.df.single.dsprotocol.quick.accept");
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
            logger.info("DfApi.singleDsprotocolQuickDf params  signValue:"+sign);
            params.put("sign",sign);
        }catch (Exception e){
            throw e;
        }

        /*4、发送请求*/
        String result = HttpClientUtil.sendPostParam(vo.getReqUrl(), StringUtil.mapToString(params));
        logger.info("DfApi.singleDsprotocolQuickDf response result :"+result);
        if(StringUtil.isEmpty(result)){
            logger.info("DfApi.singleDsprotocolQuickDf response result is empty");
        }
        /*5、结果验签*/
        JSONObject jsonObject= JSON.parseObject(result, Feature.OrderedField);
        // 加签值
        String sinaValue = (String) jsonObject.get("sign");
        // 加签内容
        String content = jsonObject.get("ysepay_df_single_dsprotocol_quick_accept_response").toString();
        boolean flag = YsOnlineSignUtils.rsaCheckContent(content,sinaValue,Constants.CHARSET_UTF_8,vo.getYsPublicKeyFilePath());
        if(!flag){
            throw new Exception("check sign error");
        }
        return content;
    }

    /**
     * 单笔代付查询
     * @param vo
     * @return
     * @throws Exception
     */
    public static String singleQueryDf(OnlineReqDataVo vo) throws Exception {

        /** 1、对业务数据进行加密*/
        String biz_content = JSONObject.toJSONString(vo.getParamData());
        logger.info("DfApi.singleQueryDf param biz_content:"+biz_content);

        /** 2、组装入参的数据*/
        Map<String, String> params = new HashMap<>();
        params.put("method","ysepay.df.single.query");
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
            logger.info("DfApi.singleQueryDf params  signValue:"+sign);
            params.put("sign",sign);
        }catch (Exception e){
            throw e;
        }

        /*4、发送请求*/
        String result = HttpClientUtil.sendPostParam(vo.getReqUrl(), StringUtil.mapToString(params));
        logger.info("DfApi.singleQueryDf response result :"+result);
        if(StringUtil.isEmpty(result)){
            logger.info("DfApi.singleQueryDf response result is empty");
        }
        /*5、结果验签*/
        JSONObject jsonObject= JSON.parseObject(result, Feature.OrderedField);
        // 加签值
        String sinaValue = (String) jsonObject.get("sign");
        // 加签内容
        String content = jsonObject.get("ysepay_df_single_query_response").toString();
        boolean flag = YsOnlineSignUtils.rsaCheckContent(content,sinaValue,Constants.CHARSET_UTF_8,vo.getYsPublicKeyFilePath());
        if(!flag){
            throw new Exception("check sign error");
        }
        return content;
    }








    /**
     * 批量代付（银行卡）普通受理接口
     * @param vo
     * @return
     * @throws Exception
     */
    public static String batchNormalDf(OnlineReqDataVo vo) throws Exception {

        /** 1、对业务数据进行加密*/
        String biz_content = JSONObject.toJSONString(vo.getParamData());
        logger.info("DfApi.batchNormalDf param biz_content:"+biz_content);

        /** 2、组装入参的数据*/
        Map<String, String> params = new HashMap<String, String>();
        params.put("method","ysepay.df.batch.normal.accept");
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
            logger.info("DfApi.batchNormalDf params  signValue:"+sign);
            params.put("sign",sign);
        }catch (Exception e){
            throw e;
        }

        /*4、发送请求*/
        String result = HttpClientUtil.sendPostParam(vo.getReqUrl(), StringUtil.mapToString(params));
        logger.info("DfApi.batchNormalDf response result :"+result);
        if(StringUtil.isEmpty(result)){
            logger.info("DfApi.batchNormalDf response result is empty");
        }
        /*5、结果验签*/
        JSONObject jsonObject= JSON.parseObject(result, Feature.OrderedField);
        // 加签值
        String sinaValue = (String) jsonObject.get("sign");
        // 加签内容
        String content = jsonObject.get("ysepay_df_batch_normal_accept_response").toString();
        boolean flag = YsOnlineSignUtils.rsaCheckContent(content,sinaValue,Constants.CHARSET_UTF_8,vo.getYsPublicKeyFilePath());
        if(!flag){
            throw new Exception("check sign error");
        }
        return content;
    }

    /**
     * 批量代付汇总查询接口
     * @param vo
     * @return
     * @throws Exception
     */
    public static String batchSummaryQueryDf(OnlineReqDataVo vo) throws Exception {

        /** 1、对业务数据进行加密*/
        String biz_content = JSONObject.toJSONString(vo.getParamData());
        logger.info("DfApi.batchSummaryQueryDf param biz_content:"+biz_content);

        /** 2、组装入参的数据*/
        Map<String, String> params = new HashMap<String, String>();
        params.put("method","ysepay.df.batch.summary.query");
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
            logger.info("DfApi.batchSummaryQueryDf params  signValue:"+sign);
            params.put("sign",sign);
        }catch (Exception e){
            throw e;
        }

        /*4、发送请求*/
        String result = HttpClientUtil.sendPostParam(vo.getReqUrl(), StringUtil.mapToString(params));
        logger.info("DfApi.batchSummaryQueryDf response result:"+result);
        if(StringUtil.isEmpty(result)){
            logger.info("DfApi.batchSummaryQueryDf response result is empty");
        }
        /*5、结果验签*/
        JSONObject jsonObject= JSON.parseObject(result, Feature.OrderedField);
        // 加签值
        String sinaValue = (String) jsonObject.get("sign");
        // 加签内容
        String content = jsonObject.get("ysepay_df_batch_summary_query_response").toString();
        boolean flag = YsOnlineSignUtils.rsaCheckContent(content,sinaValue,Constants.CHARSET_UTF_8,vo.getYsPublicKeyFilePath());
        if(!flag){
            throw new Exception("check sign error");
        }
        return content;
    }
    /**
     * 批量代付明细查询接口
     * @param vo
     * @return
     * @throws Exception
     */
    public static String batchDetailQueryDf(OnlineReqDataVo vo) throws Exception {

        /** 1、对业务数据进行加密*/
        String biz_content = JSONObject.toJSONString(vo.getParamData());
        logger.info("DfApi.batchDetailQueryDf param biz_content:"+biz_content);

        /** 2、组装入参的数据*/
        Map<String, String> params = new HashMap<String, String>();
        params.put("method","ysepay.df.batch.detail.query");
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
            logger.info("DfApi.batchDetailQueryDf params  signValue:"+sign);
            params.put("sign",sign);
        }catch (Exception e){
            throw e;
        }

        /*4、发送请求*/
        String result = HttpClientUtil.sendPostParam(vo.getReqUrl(), StringUtil.mapToString(params));
        logger.info("DfApi.batchDetailQueryDf response result:"+result);
        if(StringUtil.isEmpty(result)){
            logger.info("DfApi.batchDetailQueryDf response result is empty");
        }
        /*5、结果验签*/
        JSONObject jsonObject= JSON.parseObject(result, Feature.OrderedField);
        // 加签值
        String sinaValue = (String) jsonObject.get("sign");
        // 加签内容
        String content = jsonObject.get("ysepay_df_batch_detail_query_response").toString();
        boolean flag = YsOnlineSignUtils.rsaCheckContent(content,sinaValue,Constants.CHARSET_UTF_8,vo.getYsPublicKeyFilePath());
        if(!flag){
            throw new Exception("check sign error");
        }
        return content;
    }



}
