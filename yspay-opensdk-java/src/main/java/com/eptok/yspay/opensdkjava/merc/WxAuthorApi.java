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

/**
 * 微信实名认证接口
 */
public class WxAuthorApi {
    private static final Logger logger = Logger.getLogger("WxAuthorApi");
    private final static String version = "3.0";
    /**
     * @Description 微信实名认证开户意愿申请接口
     * @Date 14:22 2021/9/26
     * @Param
     * @return
     */
    public static String applyWxAuthor(OnlineReqDataVo vo) throws Exception {

        /** 1、业务数据转json*/
        String biz_content = JSONObject.toJSONString(vo.getParamData());
        logger.info("WxAuthorApi-applyWxAuthor-biz_content:"+biz_content);

        /** 2、组装入参的数据*/
        Map<String, String> params = new HashMap<String, String>();
        params.put("method","ysepay.authenticate.wx.apply");
        params.put("partner_id",vo.getPartnerId());
        params.put("timestamp", DateUtil.getDateNow());
        params.put("charset", Constants.CHARSET_UTF_8);
        params.put("sign_type", "RSA");
        params.put("notify_url",vo.getNotifyUrl());
        params.put("version",version);
        params.put("biz_content",biz_content);

        /*3、对数据进行加签，并存入请求数据*/
        String sign = YsOnlineSignUtils.sign(params, vo.getPrivateKeyPassword(),vo.getPrivateKeyFilePath());
        logger.info("WxAuthorApi-applyWxAuthor-signData:"+sign);
        params.put("sign",sign);

        /*4、发送请求*/
        String result = HttpClientUtil.sendPostParam(vo.getReqUrl(), StringUtil.mapToString(params));
        logger.info("WxAuthorApi-applyWxAuthor-resultData:"+result);
        if(StringUtil.isEmpty(result)){
            logger.info("WxAuthorApi applyWxAuthor resultData is empty");
        }
        /*5、结果验签*/
        JSONObject jsonObject= JSON.parseObject(result, Feature.OrderedField);
        // 加签值
        String sinaValue = (String) jsonObject.get("sign");
        // 加签内容
        String content = jsonObject.get("ysepay_authenticate_wx_apply_response").toString();
        boolean flag = YsOnlineSignUtils.rsaCheckContent(content,sinaValue,Constants.CHARSET_UTF_8,vo.getYsPublicKeyFilePath());
        if(!flag){
            throw new Exception("check sign error");
        }
        return content;
    }

    /**
     * @Description 微信实名认证-撤销申请单
     * @Date 14:22 2021/9/26
     * @Param
     * @return
     */
    public static String cancelApplyWxAuthor(OnlineReqDataVo vo) throws Exception {

        /** 1、业务数据转json**/
        String biz_content = JSONObject.toJSONString(vo.getParamData());
        logger.info("WxAuthorApi-cancelApplyWxAuthor-biz_content:"+biz_content);

        /** 2、组装入参的数据*/
        Map<String, String> params = new HashMap<String, String>();
        params.put("method","ysepay.authenticate.wx.apply.cancel");
        params.put("partner_id",vo.getPartnerId());
        params.put("timestamp", DateUtil.getDateNow());
        params.put("charset", Constants.CHARSET_UTF_8);
        params.put("sign_type", "RSA");
        params.put("notify_url",vo.getNotifyUrl());
        params.put("version",version);
        params.put("biz_content",biz_content);

        /*3、对数据进行加签，并存入请求数据*/
        String sign = YsOnlineSignUtils.sign(params, vo.getPrivateKeyPassword(),vo.getPrivateKeyFilePath());
        logger.info("WxAuthorApi-cancelApplyWxAuthor-signData:"+sign);
        params.put("sign",sign);

        /*4、发送请求*/
        String result = HttpClientUtil.sendPostParam(vo.getReqUrl(), StringUtil.mapToString(params));
        logger.info("WxAuthorApi-cancelApplyWxAuthor-resultData:"+result);
        if(StringUtil.isEmpty(result)){
            logger.info("WxAuthorApi cancelApplyWxAuthor resultData is empty");
        }
        /*5、结果验签*/
        JSONObject jsonObject= JSON.parseObject(result, Feature.OrderedField);
        // 加签值
        String sinaValue = (String) jsonObject.get("sign");
        // 加签内容
        String content = jsonObject.get("ysepay_authenticate_wx_apply_cancel_response").toString();
        boolean flag = YsOnlineSignUtils.rsaCheckContent(content,sinaValue,Constants.CHARSET_UTF_8,vo.getYsPublicKeyFilePath());
        if(!flag){
            throw new Exception("check sign error");
        }
        return content;
    }

    /**
     * @Description 查询微信实名认证申请单状态
     * @Date 14:22 2021/9/26
     * @Param
     * @return
     */
    public static String queryApplyWxAuthor(OnlineReqDataVo vo) throws Exception {

        /** 1、业务数据转json*/
        String biz_content = JSONObject.toJSONString(vo.getParamData());
        logger.info("WxAuthorApi-queryApplyWxAuthor-biz_content:"+biz_content);

        /** 2、组装入参的数据*/
        Map<String, String> params = new HashMap<String, String>();
        params.put("method","ysepay.authenticate.wx.query");
        params.put("partner_id",vo.getPartnerId());
        params.put("timestamp", DateUtil.getDateNow());
        params.put("charset", Constants.CHARSET_UTF_8);
        params.put("sign_type", "RSA");
        params.put("notify_url",vo.getNotifyUrl());
        params.put("version",version);
        params.put("biz_content",biz_content);

        /*3、对数据进行加签，并存入请求数据*/
        String sign = YsOnlineSignUtils.sign(params, vo.getPrivateKeyPassword(),vo.getPrivateKeyFilePath());
        logger.info("WxAuthorApi-queryApplyWxAuthor-signData:"+sign);
        params.put("sign",sign);

        /*4、发送请求*/
        String result = HttpClientUtil.sendPostParam(vo.getReqUrl(), StringUtil.mapToString(params));
        logger.info("WxAuthorApi-queryApplyWxAuthor-resultData:"+result);
        if(StringUtil.isEmpty(result)){
            logger.info("WxAuthorApi queryApplyWxAuthor resultData is empty");
        }
        /*5、结果验签*/
        JSONObject jsonObject= JSON.parseObject(result, Feature.OrderedField);
        // 加签值
        String sinaValue = (String) jsonObject.get("sign");
        // 加签内容
        String content = jsonObject.get("ysepay_authenticate_wx_query_response").toString();
        boolean flag = YsOnlineSignUtils.rsaCheckContent(content,sinaValue,Constants.CHARSET_UTF_8,vo.getYsPublicKeyFilePath());
        if(!flag){
            throw new Exception("check sign error");
        }
        return content;
    }

    /**
     * @Description 查询微信实名认证授权状态
     * @Date 14:22 2021/9/26
     * @Param
     * @return
     */
    public static String queryWxAuthorized(OnlineReqDataVo vo) throws Exception {

        /** 1、业务数据转json*/
        String biz_content = JSONObject.toJSONString(vo.getParamData());
        logger.info("WxAuthorApi-queryWxAuthorized-biz_content:"+biz_content);

        /** 2、组装入参的数据*/
        Map<String, String> params = new HashMap<String, String>();
        params.put("method","ysepay.authenticate.wx.authorized.query");
        params.put("partner_id",vo.getPartnerId());
        params.put("timestamp", DateUtil.getDateNow());
        params.put("charset", Constants.CHARSET_UTF_8);
        params.put("sign_type", "RSA");
        params.put("notify_url",vo.getNotifyUrl());
        params.put("version",version);
        params.put("biz_content",biz_content);

        /*3、对数据进行加签，并存入请求数据*/
        String sign = YsOnlineSignUtils.sign(params, vo.getPrivateKeyPassword(),vo.getPrivateKeyFilePath());
        logger.info("WxAuthorApi-queryWxAuthorized-signData:"+sign);
        params.put("sign",sign);

        /*4、发送请求*/
        String result = HttpClientUtil.sendPostParam(vo.getReqUrl(), StringUtil.mapToString(params));
        logger.info("WxAuthorApi-queryWxAuthorized-resultData:"+result);
        if(StringUtil.isEmpty(result)){
            logger.info("WxAuthorApi queryWxAuthorized resultData is empty");
        }
        /*5、结果验签*/
        JSONObject jsonObject= JSON.parseObject(result, Feature.OrderedField);
        // 加签值
        String sinaValue = (String) jsonObject.get("sign");
        // 加签内容
        String content = jsonObject.get("ysepay_authenticate_wx_authorized_query_response").toString();
        boolean flag = YsOnlineSignUtils.rsaCheckContent(content,sinaValue,Constants.CHARSET_UTF_8,vo.getYsPublicKeyFilePath());
        if(!flag){
            throw new Exception("check sign error");
        }
        return content;
    }
}
