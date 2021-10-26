package com.eptok.yspay.opensdkjava.merc;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.eptok.yspay.opensdkjava.common.Constants;
import com.eptok.yspay.opensdkjava.pojo.vo.OnlineReqDataVo;
import com.eptok.yspay.opensdkjava.pojo.vo.YsfReqDataVo;
import com.eptok.yspay.opensdkjava.util.*;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * 线上商户进件接口
 */
public class OnlineMercApi {

    private static final Logger logger = Logger.getLogger("OnlineMercApi");
    private final static String version = "3.0";

    /**
     * @Description 线上商户获取图片上传口令接口
     * @Date 14:22 2021/9/26
     * @Param
     * @return
     */
    public static String getToken(OnlineReqDataVo vo) throws Exception {

        /** 2、组装入参的数据*/
        Map<String, String> params = new HashMap<String, String>();
        params.put("method","ysepay.merchant.register.token.get");
        params.put("partner_id",vo.getPartnerId());
        params.put("timestamp", DateUtil.getDateNow());
        params.put("charset", Constants.CHARSET_UTF_8);
        params.put("sign_type", "RSA");
        params.put("notify_url",vo.getNotifyUrl());
        params.put("version",version);
        //获取token没有业务参数，因此写成{}
        params.put("biz_content","{}");

        /*3、对数据进行加签，并存入请求数据*/
        String sign = YsOnlineSignUtils.sign(params, vo.getPrivateKeyPassword(),vo.getPrivateKeyFilePath());
        logger.info("OnlineMercApi-getToken-signData:"+sign);
        params.put("sign",sign);

        /*4、发送请求*/
        String result = HttpClientUtil.sendPostParam(vo.getReqUrl(),StringUtil.mapToString(params));
        logger.info("OnlineMercApi-getToken-resultData:"+result);
        if(StringUtil.isEmpty(result)){
            logger.info("OnlineMercApi getToken resultData is empty");
        }
        /*5、结果验签*/
        JSONObject jsonObject= JSON.parseObject(result, Feature.OrderedField);
        // 加签值
        String sinaValue = (String) jsonObject.get("sign");
        // 加签内容
        String content = jsonObject.get("ysepay_merchant_register_token_get_response").toString();
        boolean flag = YsOnlineSignUtils.rsaCheckContent(content,sinaValue,Constants.CHARSET_UTF_8,vo.getYsPublicKeyFilePath());
        if(!flag){
            throw new Exception("check sign error");
        }
        return content;
    }

    /**
     * @Description 线上商户图片上传接口
     * @Date 14:22 2021/9/26
     * @Param
     * @return
     */
    public static String uploadPicture(OnlineReqDataVo vo, byte[] fileByte, String fileName) throws Exception {

        /** 1、组装需要加签的入参的数据*/
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("picType", (String) vo.getParamData().get("picType"));
        param.put("token", (String) vo.getParamData().get("token"));
        param.put("superUsercode", (String) vo.getParamData().get("superUsercode"));

        /*3、发送请求*/
        String result = HttpClientUtil.sendPostFile(vo.getReqUrl(),param,fileByte,fileName,"picFile");
        logger.info("OnlineMercApi-uploadPicture-resultData:"+result);
        return result;
    }

    /**
     * @Description 线上商户注册接口
     * @Date 14:22 2021/9/26
     * @Param
     * @return
     */
    public static String merchantRegister(OnlineReqDataVo vo) throws Exception {

        /** 1、业务数据转json*/
        String biz_content = JSONObject.toJSONString(vo.getParamData());
        logger.info("OnlineMercApi-merchantRegister-biz_content:"+biz_content);

        /** 2、组装入参的数据*/
        Map<String, String> params = new HashMap<String, String>();
        params.put("method","ysepay.merchant.register.accept");
        params.put("partner_id",vo.getPartnerId());
        params.put("timestamp", DateUtil.getDateNow());
        params.put("charset", Constants.CHARSET_UTF_8);
        params.put("sign_type", "RSA");
        params.put("notify_url",vo.getNotifyUrl());
        params.put("version",version);
        params.put("biz_content",biz_content);

        /*3、对数据进行加签，并存入请求数据*/
        String sign = YsOnlineSignUtils.sign(params, vo.getPrivateKeyPassword(),vo.getPrivateKeyFilePath());
        logger.info("OnlineMercApi-merchantRegister-signData:"+sign);
        params.put("sign",sign);

        /*4、发送请求*/
        String result = HttpClientUtil.sendPostParam(vo.getReqUrl(),StringUtil.mapToString(params));
        logger.info("OnlineMercApi-merchantRegister-resultData:"+result);
        if(StringUtil.isEmpty(result)){
            logger.info("OnlineMercApi merchantRegister resultData is empty");
        }
        /*5、结果验签*/
        JSONObject jsonObject= JSON.parseObject(result, Feature.OrderedField);
        // 加签值
        String sinaValue = (String) jsonObject.get("sign");
        // 加签内容
        String content = jsonObject.get("ysepay_merchant_register_accept_response").toString();
        boolean flag = YsOnlineSignUtils.rsaCheckContent(content,sinaValue,Constants.CHARSET_UTF_8,vo.getYsPublicKeyFilePath());
        if(!flag){
            throw new Exception("check sign error");
        }
        return content;
    }

    /**
     * @Description 主动查询商户注册信息
     * @Date 14:22 2021/9/26
     * @Param
     * @return
     */
    public static String queryMerchant(OnlineReqDataVo vo) throws Exception {

        /** 1、业务数据转json*/
        String biz_content = JSONObject.toJSONString(vo.getParamData());
        logger.info("OnlineMercApi-queryMerchant-biz_content:"+biz_content);

        /** 2、组装入参的数据*/
        Map<String, String> params = new HashMap<String, String>();
        params.put("method","ysepay.merchant.register.query");
        params.put("partner_id",vo.getPartnerId());
        params.put("timestamp", DateUtil.getDateNow());
        params.put("charset", Constants.CHARSET_UTF_8);
        params.put("sign_type", "RSA");
        params.put("notify_url",vo.getNotifyUrl());
        params.put("version",version);
        params.put("biz_content",biz_content);

        /*3、对数据进行加签，并存入请求数据*/
        String sign = YsOnlineSignUtils.sign(params, vo.getPrivateKeyPassword(),vo.getPrivateKeyFilePath());
        logger.info("OnlineMercApi-queryMerchant-signData:"+sign);
        params.put("sign",sign);

        /*4、发送请求*/
        String result = HttpClientUtil.sendPostParam(vo.getReqUrl(),StringUtil.mapToString(params));
        logger.info("OnlineMercApi-queryMerchant-resultData:"+result);
        if(StringUtil.isEmpty(result)){
            logger.info("OnlineMercApi queryMerchant resultData is empty");
        }
        /*5、结果验签*/
        JSONObject jsonObject= JSON.parseObject(result, Feature.OrderedField);
        // 加签值
        String sinaValue = (String) jsonObject.get("sign");
        // 加签内容
        String content = jsonObject.get("ysepay_merchant_register_query_response").toString();
        boolean flag = YsOnlineSignUtils.rsaCheckContent(content,sinaValue,Constants.CHARSET_UTF_8,vo.getYsPublicKeyFilePath());
        if(!flag){
            throw new Exception("check sign error");
        }
        return content;
    }

    public static void main(String[] args) {
        /**1、获取需要的参数*/
        //商户进件请求路径,建议配置在项目的配置文件里面
        String reqUrl = "https://register.ysepay.com:2443/register_gateway/gateway.do";

        //私钥证书存放路径,建议配置在项目的配置文件里面
        String privateKeyFilePath = "D:\\openRSA\\hyfz_test2.pfx";

        //ys公钥证书存放地址 建议配置在项目的配置文件里面
        String publicKeyFilePath = "D:\\openRSA\\businessgate.cer";


        //私钥证书密钥,建议配置在项目的配置文件里面
        String privateKeyPassworde = "123456";

        //商户在银盛支付平台开设的用户号[商户号],接入时需要替换成自己的
        String partnerId = "hyfz_test2";

        String notifyUrl = "http://127.0.0.1";
        /**2、组装需要的参数*/
        OnlineReqDataVo req = new OnlineReqDataVo();
        //设置私钥证书路径
        req.setPrivateKeyFilePath(privateKeyFilePath);
        //设置私钥密钥
        req.setPrivateKeyPassword(privateKeyPassworde);
        //设置ys公钥证书路径
        req.setYsPublicKeyFilePath(publicKeyFilePath);
        //设置请求路径
        req.setReqUrl(reqUrl);
        req.setPartnerId(partnerId);
        req.setNotifyUrl(notifyUrl);
        logger.info("线上商户上传图片获取token请求入参为:"+ JSONObject.toJSONString(req));

        /**2、调用API的方法*/
        String result = null;
        try{
            result = OnlineMercApi.getToken(req);
            JSONObject jsonObject = JSON.parseObject(result, Feature.OrderedField);
            String token = jsonObject.get("token").toString();
            //根据返回结果处理自己的业务逻辑,result内容详见接口文档
        }catch (Exception e){
            logger.info("线上商户上传图片获取token失败:"+e.getCause().getMessage());
            //根据自己要求处理进件失败的业务逻辑
        }
    }
}
