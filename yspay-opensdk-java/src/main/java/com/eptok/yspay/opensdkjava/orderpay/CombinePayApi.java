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
 * 合单支付
 */
public class CombinePayApi {

    private static final Logger logger = Logger.getLogger("CombinePayApi");

    /**
     * 合单支付 - 微信公众号、小程序
     * @param vo
     * @return
     * @throws Exception
     */
    public static String combineWeixinPay(OnlineReqDataVo vo) throws Exception {
        /** 1、获取业务参数 */
        String biz_content = JSONObject.toJSONString(vo.getParamData());
        logger.info("CombinePayApi-combineWeixinPay-bizContent:"+biz_content);

        /** 2、组装入参的数据*/
        Map<String, String> params = new HashMap<>();
        //接口名称
        params.put("method","ysepay.online.combine.weixin.pay");
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
        logger.info("CombinePayApi-combineWeixinPay-signData:"+sign);
        params.put("sign",sign);

        /**4、发送请求*/
        String result = HttpClientUtil.sendPostParam(vo.getReqUrl(), StringUtil.mapToString(params));
        logger.info("CombinePayApi-combineWeixinPay-remoteResultData:"+result);

        /**5、对接口回执进行验签*/
        JSONObject resultJson = JSONObject.parseObject(result, Feature.OrderedField);
        //加签内容
        String content = resultJson.getString("ysepay_online_combine_weixin_pay_response");
        //加签值
        String sinaValue = resultJson.getString("sign");
        boolean flag = YsOnlineSignUtils.rsaCheckContent(content,sinaValue,Constants.CHARSET_GBK,vo.getYsPublicKeyFilePath());
        if(!flag){
            throw new Exception("check sign error");
        }
        return content;
    }

    /**
     * 合单支付-支付宝生活号
     * @param vo
     * @return
     * @throws Exception
     */
    public static String combineAlijsapiPay(OnlineReqDataVo vo) throws Exception {
        /** 1、获取业务参数 */
        String biz_content = JSONObject.toJSONString(vo.getParamData());
        logger.info("CombinePayApi-combineAlijsapiPay-bizContent:"+biz_content);

        /** 2、组装入参的数据*/
        Map<String, String> params = new HashMap<>();
        //接口名称
        params.put("method","ysepay.online.combine.alijsapi.pay");
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
        logger.info("CombinePayApi-combineAlijsapiPay-signData:"+sign);
        params.put("sign",sign);

        /**4、发送请求*/
        String result = HttpClientUtil.sendPostParam(vo.getReqUrl(), StringUtil.mapToString(params));
        logger.info("CombinePayApi-combineAlijsapiPay-remoteResultData:"+result);

        /**5、对接口回执进行验签*/
        JSONObject resultJson = JSONObject.parseObject(result, Feature.OrderedField);
        //加签内容
        String content = resultJson.getString("ysepay_online_combine_alijsapi_pay_response");
        //加签值
        String sinaValue = resultJson.getString("sign");
        boolean flag = YsOnlineSignUtils.rsaCheckContent(content,sinaValue,Constants.CHARSET_GBK,vo.getYsPublicKeyFilePath());
        if(!flag){
            throw new Exception("check sign error");
        }
        return content;
    }

    /**
     * 合单支付-订单及明细查询
     * @param vo
     * @return
     * @throws Exception
     */
    public static String tradeOrderCombineQuery(OnlineReqDataVo vo) throws Exception {
        /** 1、获取业务参数 */
        String biz_content = JSONObject.toJSONString(vo.getParamData());
        logger.info("CombinePayApi-tradeOrderCombineQuery-bizContent:"+biz_content);

        /** 2、组装入参的数据*/
        Map<String, String> params = new HashMap<>();
        //接口名称
        params.put("method","ysepay.online.trade.order.combine.query");
        //商户在银盛支付平台开设的用户号[商户号]
        params.put("partner_id",vo.getPartnerId());
        //发送请求的时间，格式"yyyy-MM-dd HH:mm:ss"
        params.put("timestamp", DateUtil.getDateNow());
        //商户网站使用的编码格式，如utf-8、gbk、gb2312等。
        params.put("charset", Constants.CHARSET_GBK);
        params.put("sign_type", "RSA");
        params.put("version","3.0");
        //业务参数
        params.put("biz_content",biz_content);

        /** 3、对数据进行加签，并存入请求数据*/
        String sign = YsOnlineSignUtils.sign(params, vo.getPrivateKeyPassword(),vo.getPrivateKeyFilePath());
        logger.info("CombinePayApi-tradeOrderCombineQuery-signData:"+sign);
        params.put("sign",sign);

        /**4、发送请求*/
        String result = HttpClientUtil.sendPostParam(vo.getReqUrl(), StringUtil.mapToString(params));
        logger.info("CombinePayApi-tradeOrderCombineQuery-remoteResultData:"+result);

        /**5、对接口回执进行验签*/
        JSONObject resultJson = JSONObject.parseObject(result, Feature.OrderedField);
        //加签内容
        String content = resultJson.getString("ysepay_online_trade_order_combine_query_response");
        //加签值
        String sinaValue = resultJson.getString("sign");
        boolean flag = YsOnlineSignUtils.rsaCheckContent(content,sinaValue,Constants.CHARSET_GBK,vo.getYsPublicKeyFilePath());
        if(!flag){
            throw new Exception("check sign error");
        }
        return content;
    }


    /**
     * 关闭订单接口
     * @param vo
     * @return
     * @throws Exception
     */
    public static String tradeCombineClose(OnlineReqDataVo vo) throws Exception {
        /** 1、获取业务参数 */
        String biz_content = JSONObject.toJSONString(vo.getParamData());
        logger.info("CombinePayApi-tradeCombineClose-bizContent:"+biz_content);

        /** 2、组装入参的数据*/
        Map<String, String> params = new HashMap<>();
        //接口名称
        params.put("method","ysepay.online.trade.combine.close");
        //商户在银盛支付平台开设的用户号[商户号]
        params.put("partner_id",vo.getPartnerId());
        //发送请求的时间，格式"yyyy-MM-dd HH:mm:ss"
        params.put("timestamp", DateUtil.getDateNow());
        //商户网站使用的编码格式，如utf-8、gbk、gb2312等。
        params.put("charset", Constants.CHARSET_GBK);
        params.put("sign_type", "RSA");
        params.put("version","3.0");
        //业务参数
        params.put("biz_content",biz_content);

        /** 3、对数据进行加签，并存入请求数据*/
        String sign = YsOnlineSignUtils.sign(params, vo.getPrivateKeyPassword(),vo.getPrivateKeyFilePath());
        logger.info("CombinePayApi-tradeCombineClose-signData:"+sign);
        params.put("sign",sign);

        /**4、发送请求*/
        String result = HttpClientUtil.sendPostParam(vo.getReqUrl(), StringUtil.mapToString(params));
        logger.info("CombinePayApi-tradeCombineClose-remoteResultData:"+result);

        /**5、对接口回执进行验签*/
        JSONObject resultJson = JSONObject.parseObject(result, Feature.OrderedField);
        //加签内容
        String content = resultJson.getString("ysepay_online_trade_combine_close_response");
        //加签值
        String sinaValue = resultJson.getString("sign");
        boolean flag = YsOnlineSignUtils.rsaCheckContent(content,sinaValue,Constants.CHARSET_GBK,vo.getYsPublicKeyFilePath());
        if(!flag){
            throw new Exception("check sign error");
        }
        return content;
    }
}
