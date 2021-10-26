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
 * 订单查询
 */
public class OrderQueryApi {

    private static final Logger logger = Logger.getLogger("OrderQueryApi");


    /**
     * 单笔订单支付查询接口
     * @param vo
     * @return
     * @throws Exception
     */
    public static String singleOrderQuery(OnlineReqDataVo vo) throws Exception {
        /** 1、获取业务参数 */
        String biz_content = JSONObject.toJSONString(vo.getParamData());
        logger.info("单笔订单支付查询接口业务参数biz_content:"+biz_content);

        /** 2、组装入参的数据*/
        Map<String, String> params = new HashMap<>();
        //接口名称
        params.put("method","ysepay.online.trade.query");
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
        logger.info("单笔订单支付查询接口签名sign："+sign);
        params.put("sign",sign);

        /**4、发送请求*/
        String result = HttpClientUtil.sendPostParam(vo.getReqUrl(), StringUtil.mapToString(params));
        logger.info("单笔订单支付查询接口-结果返回为:"+result);

        /**5、对接口回执进行验签*/
        JSONObject resultJson = JSONObject.parseObject(result, Feature.OrderedField);
        //加签内容
        String content = resultJson.getString("ysepay_online_trade_query_response");
        //加签值
        String sinaValue = resultJson.getString("sign");
        boolean flag = YsOnlineSignUtils.rsaCheckContent(content,sinaValue,Constants.CHARSET_GBK,vo.getYsPublicKeyFilePath());
        if(!flag){
            throw new Exception("check sign error");
        }
        return content;
    }

    /**
     * 订单及明细查询接口
     * @param vo
     * @return
     * @throws Exception
     */
    public static String orderAndDetailQuery(OnlineReqDataVo vo) throws Exception {
        /** 1、获取业务参数 */
        String biz_content = JSONObject.toJSONString(vo.getParamData());
        logger.info("订单及明细查询接口业务参数biz_content:"+biz_content);

        /** 2、组装入参的数据*/
        Map<String, String> params = new HashMap<>();
        //接口名称
        params.put("method","ysepay.online.trade.order.query");
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
        logger.info("订单及明细查询接口签名sign："+sign);
        params.put("sign",sign);

        /**4、发送请求*/
        String result = HttpClientUtil.sendPostParam(vo.getReqUrl(), StringUtil.mapToString(params));
        logger.info("订单及明细查询接口-结果返回为:"+result);

        /**5、对接口回执进行验签*/
        JSONObject resultJson = JSONObject.parseObject(result, Feature.OrderedField);
        //加签内容
        String content = resultJson.getString("ysepay_online_trade_order_query_response");
        //加签值
        String sinaValue = resultJson.getString("sign");
        boolean flag = YsOnlineSignUtils.rsaCheckContent(content,sinaValue,Constants.CHARSET_GBK,vo.getYsPublicKeyFilePath());
        if(!flag){
            throw new Exception("check sign error");
        }
        return content;
    }
}
