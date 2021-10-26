package com.eptok.yspay.opensdkjava.orderpay;

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
 * 扫码支付接口
 */
public class ScanCodePayApi {

    private static final Logger logger = Logger.getLogger("ScanCodePayApi");

    /**
     * 二维码支付
     * @param vo
     * @return
     * @throws Exception
     */
    public static String qrcodepay(OnlineReqDataVo vo) throws Exception {
        /** 1、获取业务参数 */
        String biz_content = JSONObject.toJSONString(vo.getParamData());
        logger.info("ScanCodePayApi-qrcodepay-bizContent:"+biz_content);

        /** 2、组装入参的数据*/
        Map<String, String> params = new HashMap<>();
        //接口名称
        params.put("method","ysepay.online.qrcodepay");
        //商户在银盛支付平台开设的用户号[商户号]
        params.put("partner_id",vo.getPartnerId());
        //发送请求的时间，格式"yyyy-MM-dd HH:mm:ss"
        params.put("timestamp", DateUtil.getDateNow());
        //商户网站使用的编码格式，如utf-8、gbk、gb2312等。
        params.put("charset", Constants.CHARSET_GBK);
        params.put("sign_type", "RSA");
        //银盛支付服务器主动通知商户网站里指定的页面http路径，支持多个url进行异步通知，多个url用分隔符“,”分开，格式如：url1,url2,url3
        params.put("notify_url",vo.getNotifyUrl());
        //同步通知地址。
        params.put("return_url",vo.getReturnUrl());
        //交易类型，说明：1或者空：即时到账，2：担保交易
        params.put("tran_type",vo.getTranType());
        params.put("version","3.4");
        //业务参数
        params.put("biz_content",biz_content);

        /** 3、对数据进行加签，并存入请求数据*/
        String sign = YsOnlineSignUtils.sign(params, vo.getPrivateKeyPassword(),vo.getPrivateKeyFilePath());
        logger.info("ScanCodePayApi-qrcodepay-signData:"+sign);
        params.put("sign",sign);

        /**4、发送请求*/
        String result = HttpClientUtil.sendPostParam(vo.getReqUrl(), StringUtil.mapToString(params));
        logger.info("ScanCodePayApi-qrcodepay-remoteResultData:"+result);

        /**5、对接口回执进行验签*/
        JSONObject resultJson = JSONObject.parseObject(result, Feature.OrderedField);
        //加签内容
        String content = resultJson.getString("ysepay_online_qrcodepay_response");
        //加签值
        String sinaValue = resultJson.getString("sign");
        boolean flag = YsOnlineSignUtils.rsaCheckContent(content,sinaValue,Constants.CHARSET_GBK,vo.getYsPublicKeyFilePath());
        if(!flag){
            throw new Exception("check sign error");
        }
        return content;
    }

    /**
     * 反扫码支付
     * @param vo
     * @return
     * @throws Exception
     */
    public static String barcodepay(OnlineReqDataVo vo) throws Exception {
        /** 1、获取业务参数 */
        String biz_content = JSONObject.toJSONString(vo.getParamData());
        logger.info("ScanCodePayApi-barcodepay-bizContent:"+biz_content);

        /** 2、组装入参的数据*/
        Map<String, String> params = new HashMap<>();
        //接口名称
        params.put("method","ysepay.online.barcodepay");
        //商户在银盛支付平台开设的用户号[商户号]
        params.put("partner_id",vo.getPartnerId());
        //发送请求的时间，格式"yyyy-MM-dd HH:mm:ss"
        params.put("timestamp", DateUtil.getDateNow());
        //商户网站使用的编码格式，如utf-8、gbk、gb2312等。
        params.put("charset", Constants.CHARSET_GBK);
        params.put("sign_type", "RSA");
        //银盛支付服务器主动通知商户网站里指定的页面http路径，支持多个url进行异步通知，多个url用分隔符“,”分开，格式如：url1,url2,url3
        params.put("notify_url",vo.getNotifyUrl());
        //交易类型，说明：1或者空：即时到账，2：担保交易
        params.put("tran_type",vo.getTranType());
        params.put("version","3.4");
        //业务参数
        params.put("biz_content",biz_content);

        /** 3、对数据进行加签，并存入请求数据*/
        String sign = YsOnlineSignUtils.sign(params, vo.getPrivateKeyPassword(),vo.getPrivateKeyFilePath());
        logger.info("ScanCodePayApi-barcodepay-signData"+sign);
        params.put("sign",sign);

        /**4、发送请求*/
        String result = HttpClientUtil.sendPostParam(vo.getReqUrl(), StringUtil.mapToString(params));
        logger.info("ScanCodePayApi-barcodepay-remoteResultData:"+result);

        /**5、对接口回执进行验签*/
        JSONObject resultJson = JSONObject.parseObject(result, Feature.OrderedField);
        //加签内容
        String content = resultJson.getString("ysepay_online_barcodepay_response");
        //加签值
        String sinaValue = resultJson.getString("sign");
        boolean flag = YsOnlineSignUtils.rsaCheckContent(content,sinaValue,Constants.CHARSET_GBK,vo.getYsPublicKeyFilePath());
        if(!flag){
            throw new Exception("check sign error");
        }
        return content;
    }

    /**
     * 聚合码支付
     * @param vo
     * @return
     * @throws Exception
     */
    public static String polycodepay(OnlineReqDataVo vo) throws Exception {
        /** 1、获取业务参数 */
        String biz_content = JSONObject.toJSONString(vo.getParamData());
        logger.info("ScanCodePayApi-polycodepay-bizContent:"+biz_content);

        /** 2、组装入参的数据*/
        Map<String, String> params = new HashMap<>();
        //接口名称
        params.put("method","ysepay.online.polycodepay");
        //商户在银盛支付平台开设的用户号[商户号]
        params.put("partner_id",vo.getPartnerId());
        //发送请求的时间，格式"yyyy-MM-dd HH:mm:ss"
        params.put("timestamp", DateUtil.getDateNow());
        //商户网站使用的编码格式，如utf-8、gbk、gb2312等。
        params.put("charset", Constants.CHARSET_GBK);
        params.put("sign_type", "RSA");
        //银盛支付服务器主动通知商户网站里指定的页面http路径，支持多个url进行异步通知，多个url用分隔符“,”分开，格式如：url1,url2,url3
        params.put("notify_url",vo.getNotifyUrl());
        //交易类型，说明：1或者空：即时到账，2：担保交易
        params.put("tran_type",vo.getTranType());
        params.put("version","3.0");
        //业务参数
        params.put("biz_content",biz_content);

        /** 3、对数据进行加签，并存入请求数据*/
        String sign = YsOnlineSignUtils.sign(params, vo.getPrivateKeyPassword(),vo.getPrivateKeyFilePath());
        logger.info("ScanCodePayApi-polycodepay-signData:"+sign);
        params.put("sign",sign);

        /**4、发送请求*/
        String result = HttpClientUtil.sendPostParam(vo.getReqUrl(), StringUtil.mapToString(params));
        logger.info("ScanCodePayApi-polycodepay-remoteResultData:"+result);

        /**5、对接口回执进行验签*/
        JSONObject resultJson = JSONObject.parseObject(result, Feature.OrderedField);
        //加签内容
        String content = resultJson.getString("ysepay_online_polycodepay_response");
        //加签值
        String sinaValue = resultJson.getString("sign");
        boolean flag = YsOnlineSignUtils.rsaCheckContent(content,sinaValue,Constants.CHARSET_GBK,vo.getYsPublicKeyFilePath());
        if(!flag){
            throw new Exception("check sign error");
        }
        return content;
    }

    /**
     * 微信公众号，小程序支付接口
     * @param vo
     * @return
     * @throws Exception
     */
    public static String weixinpay(OnlineReqDataVo vo) throws Exception {
        /** 1、获取业务参数 */
        String biz_content = JSONObject.toJSONString(vo.getParamData());
        logger.info("ScanCodePayApi-weixinpay-bizContent:"+biz_content);

        /** 2、组装入参的数据*/
        Map<String, String> params = new HashMap<>();
        //接口名称
        params.put("method","ysepay.online.weixin.pay");
        //商户在银盛支付平台开设的用户号[商户号]
        params.put("partner_id",vo.getPartnerId());
        //发送请求的时间，格式"yyyy-MM-dd HH:mm:ss"
        params.put("timestamp", DateUtil.getDateNow());
        //商户网站使用的编码格式，如utf-8、gbk、gb2312等。
        params.put("charset", Constants.CHARSET_GBK);
        params.put("sign_type", "RSA");
        //银盛支付服务器主动通知商户网站里指定的页面http路径，支持多个url进行异步通知，多个url用分隔符“,”分开，格式如：url1,url2,url3
        params.put("notify_url",vo.getNotifyUrl());
        //交易类型，说明：1或者空：即时到账，2：担保交易
        params.put("tran_type",vo.getTranType());
        params.put("version","3.4");
        //业务参数
        params.put("biz_content",biz_content);

        /** 3、对数据进行加签，并存入请求数据*/
        String sign = YsOnlineSignUtils.sign(params, vo.getPrivateKeyPassword(),vo.getPrivateKeyFilePath());
        logger.info("ScanCodePayApi-weixinpay-signData:"+sign);
        params.put("sign",sign);

        /**4、发送请求*/
        String result = HttpClientUtil.sendPostParam(vo.getReqUrl(), StringUtil.mapToString(params));
        logger.info("ScanCodePayApi-weixinpay-remoteResultData:"+result);

        /**5、对接口回执进行验签*/
        JSONObject resultJson = JSONObject.parseObject(result, Feature.OrderedField);
        //加签内容
        String content = resultJson.getString("ysepay_online_weixin_pay_response");
        //加签值
        String sinaValue = resultJson.getString("sign");
        boolean flag = YsOnlineSignUtils.rsaCheckContent(content,sinaValue,Constants.CHARSET_GBK,vo.getYsPublicKeyFilePath());
        if(!flag){
            throw new Exception("check sign error");
        }
        return content;
    }

    /**
     * 支付宝生活服务窗支付接口
     * @param vo
     * @return
     * @throws Exception
     */
    public static String alipay(OnlineReqDataVo vo) throws Exception {
        /** 1、获取业务参数 */
        String biz_content = JSONObject.toJSONString(vo.getParamData());
        logger.info("ScanCodePayApi-alipay-bizContent:"+biz_content);

        /** 2、组装入参的数据*/
        Map<String, String> params = new HashMap<>();
        //接口名称
        params.put("method","ysepay.online.alijsapi.pay");
        //商户在银盛支付平台开设的用户号[商户号]
        params.put("partner_id",vo.getPartnerId());
        //发送请求的时间，格式"yyyy-MM-dd HH:mm:ss"
        params.put("timestamp", DateUtil.getDateNow());
        //商户网站使用的编码格式，如utf-8、gbk、gb2312等。
        params.put("charset", Constants.CHARSET_GBK);
        params.put("sign_type", "RSA");
        //银盛支付服务器主动通知商户网站里指定的页面http路径，支持多个url进行异步通知，多个url用分隔符“,”分开，格式如：url1,url2,url3
        params.put("notify_url",vo.getNotifyUrl());
        //交易类型，说明：1或者空：即时到账，2：担保交易
        params.put("tran_type",vo.getTranType());
        params.put("version","3.4");
        //业务参数
        params.put("biz_content",biz_content);

        /** 3、对数据进行加签，并存入请求数据*/
        String sign = YsOnlineSignUtils.sign(params, vo.getPrivateKeyPassword(),vo.getPrivateKeyFilePath());
        logger.info("ScanCodePayApi-alipay-signData:"+sign);
        params.put("sign",sign);

        /**4、发送请求*/
        String result = HttpClientUtil.sendPostParam(vo.getReqUrl(), StringUtil.mapToString(params));
        logger.info("ScanCodePayApi-alipay-remoteResultData:"+result);

        /**5、对接口回执进行验签*/
        JSONObject resultJson = JSONObject.parseObject(result, Feature.OrderedField);
        //加签内容
        String content = resultJson.getString("ysepay_online_alijsapi_pay_response");
        //加签值
        String sinaValue = resultJson.getString("sign");
        boolean flag = YsOnlineSignUtils.rsaCheckContent(content,sinaValue,Constants.CHARSET_GBK,vo.getYsPublicKeyFilePath());
        if(!flag){
            throw new Exception("check sign error");
        }
        return content;
    }

    /**
     * 行业码支付纯接口
     * @param vo
     * @return
     * @throws Exception
     */
    public static String cupmulappQrcodepay(OnlineReqDataVo vo) throws Exception {
        /** 1、获取业务参数 */
        String biz_content = JSONObject.toJSONString(vo.getParamData());
        logger.info("ScanCodePayApi-cupmulappQrcodepay-bizContent:"+biz_content);

        /** 2、组装入参的数据*/
        Map<String, String> params = new HashMap<>();
        //接口名称
        params.put("method","ysepay.online.cupmulapp.qrcodepay");
        //商户在银盛支付平台开设的用户号[商户号]
        params.put("partner_id",vo.getPartnerId());
        //发送请求的时间，格式"yyyy-MM-dd HH:mm:ss"
        params.put("timestamp", DateUtil.getDateNow());
        //商户网站使用的编码格式，如utf-8、gbk、gb2312等。
        params.put("charset", Constants.CHARSET_GBK);
        params.put("sign_type", "RSA");
        //银盛支付服务器主动通知商户网站里指定的页面http路径，支持多个url进行异步通知，多个url用分隔符“,”分开，格式如：url1,url2,url3
        params.put("notify_url",vo.getNotifyUrl());
        //同步通知地址
        params.put("return_url",vo.getReturnUrl());
        params.put("version","3.4");
        //业务参数
        params.put("biz_content",biz_content);

        /** 3、对数据进行加签，并存入请求数据*/
        String sign = YsOnlineSignUtils.sign(params, vo.getPrivateKeyPassword(),vo.getPrivateKeyFilePath());
        logger.info("ScanCodePayApi-cupmulappQrcodepay-signData:"+sign);
        params.put("sign",sign);

        /**4、发送请求*/
        String result = HttpClientUtil.sendPostParam(vo.getReqUrl(), StringUtil.mapToString(params));
        logger.info("ScanCodePayApi-cupmulappQrcodepay-remoteResultData:"+result);

        /**5、对接口回执进行验签*/
        JSONObject resultJson = JSONObject.parseObject(result, Feature.OrderedField);
        //加签内容
        String content = resultJson.getString("ysepay_online_cupmulapp_qrcodepay_response");
        //加签值
        String sinaValue = resultJson.getString("sign");
        boolean flag = YsOnlineSignUtils.rsaCheckContent(content,sinaValue,Constants.CHARSET_GBK,vo.getYsPublicKeyFilePath());
        if(!flag){
            throw new Exception("check sign error");
        }
        return content;
    }

}
