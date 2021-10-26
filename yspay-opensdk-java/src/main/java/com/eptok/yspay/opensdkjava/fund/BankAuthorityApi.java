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
 * 鉴权实现类
 */
public class BankAuthorityApi {

    private static final Logger logger = Logger.getLogger("BankAuthorityApi");

    /**
     * @Description 银行实名认证三、四要素下单接口
     * @Date 14:22 2021/9/26
     * @Param 
     * @return 
    */
    public static String sendBankAuthFourKey(OnlineReqDataVo vo) throws Exception {
        /** 2、组装入参的数据*/
        Map<String, String> params = new HashMap<>();
        params.put("method", "ysepay.authenticate.four.key.element");
        params.put("partner_id", vo.getPartnerId());
        params.put("timestamp", DateUtil.getDateNow());
        params.put("charset", Constants.CHARSET_UTF_8);
        params.put("sign_type", "RSA");
        params.put("version", "3.0");
        params.put("biz_content", JSONObject.toJSONString(vo.getParamData()));

        /*3、对数据进行加签，并存入请求数据*/
        String sign = YsOnlineSignUtils.sign(params, vo.getPrivateKeyPassword(),vo.getPrivateKeyFilePath());
        logger.info("authenticate.four.key.element-sign："+sign);
        params.put("sign",sign);

        /*4、发送请求*/
        String result = HttpClientUtil.sendPostParam(vo.getReqUrl(),StringUtil.mapToString(params));
        logger.info("authenticate.four.key.element-result:"+result);

        /*5、结果验签*/
        JSONObject jsonObject= JSON.parseObject(result, Feature.OrderedField);
        // 加签值
        String sinaValue = (String) jsonObject.get("sign");
        // 加签内容
        String content = JSONObject.toJSONString(jsonObject.get("ysepay_authenticate_four_key_element_response"),SerializerFeature.WriteMapNullValue);
        boolean flag = YsOnlineSignUtils.rsaCheckContent(content,sinaValue,Constants.CHARSET_UTF_8,vo.getYsPublicKeyFilePath());
        if(!flag){
            throw new Exception("check sign error");
        } else {
            logger.info("check sign success");
        }
        return content;
    }

    /**
     * @Description 银行实名认证三要素下单
     * @Date 14:22 2021/9/26
     * @Param
     * @return
     */
    public static String sendBankAuthThreePrecise(OnlineReqDataVo vo) throws Exception {
        /** 2、组装入参的数据*/
        Map<String, String> params = new HashMap<>();
        params.put("method", "ysepay.authenticate.three.key.element.precise");
        params.put("partner_id", vo.getPartnerId());
        params.put("timestamp", DateUtil.getDateNow());
        params.put("charset", Constants.CHARSET_UTF_8);
        params.put("sign_type", "RSA");
        params.put("version", "3.0");

        //DES加密身份证号码，密钥为商户号前8位，不足8位在商户号前补空格
        String idCard = vo.getParamData().get("id_card").toString();
        String proxyPassword = vo.getPartnerId();
        if (proxyPassword.length() <8) {
            proxyPassword = "        " + proxyPassword;
            proxyPassword = proxyPassword.substring(proxyPassword.length()-8,proxyPassword.length());
        } else {
            proxyPassword = proxyPassword.substring(0,8);
        }
        idCard = DesUtil.encryptExtraData(proxyPassword, Constants.CHARSET_UTF_8, idCard);
        vo.getParamData().put("id_card", idCard);
        params.put("biz_content", JSONObject.toJSONString(vo.getParamData()));

        /*3、对数据进行加签，并存入请求数据*/
        String sign = YsOnlineSignUtils.sign(params, vo.getPrivateKeyPassword(),vo.getPrivateKeyFilePath());
        logger.info("authenticate.three.key.element.precise-sign："+sign);
        params.put("sign",sign);

        /*4、发送请求*/
        String result = HttpClientUtil.sendPostParam(vo.getReqUrl(),StringUtil.mapToString(params));
        logger.info("authenticate.three.key.element.precise-result:"+result);

        /*5、结果验签*/
        JSONObject jsonObject= JSON.parseObject(result, Feature.OrderedField);
        // 加签值
        String sinaValue = (String) jsonObject.get("sign");
        // 加签内容
        String content = JSONObject.toJSONString(jsonObject.get("ysepay_authenticate_three_key_element_precise_response"),SerializerFeature.WriteMapNullValue);
        boolean flag = YsOnlineSignUtils.rsaCheckContent(content,sinaValue, Constants.CHARSET_UTF_8,vo.getYsPublicKeyFilePath());
        if(!flag){
            throw new Exception("check sign error");
        } else {
            logger.info("check sign success");
        }
        return content;
    }

    /**
     * @Description 银行实名认证四要素下单
     * @Date 14:22 2021/9/26
     * @Param
     * @return
     */
    public static String sendBankAuthFourPrecise(OnlineReqDataVo vo) throws Exception {
        /** 2、组装入参的数据*/
        Map<String, String> params = new HashMap<>();
        params.put("method", "ysepay.authenticate.four.key.element.precise");
        params.put("partner_id", vo.getPartnerId());
        params.put("timestamp", DateUtil.getDateNow());
        params.put("charset", Constants.CHARSET_UTF_8);
        params.put("sign_type", "RSA");
        params.put("version", "3.0");

        //DES加密身份证号码
        String idCard = vo.getParamData().get("id_card").toString();
        String proxyPassword = vo.getPartnerId();
        if (proxyPassword.length() <8) {
            proxyPassword = "        " + proxyPassword;
            proxyPassword = proxyPassword.substring(proxyPassword.length()-8,proxyPassword.length());
        } else {
            proxyPassword = proxyPassword.substring(0,8);
        }
        idCard = DesUtil.encryptExtraData(proxyPassword, Constants.CHARSET_UTF_8, idCard);
        vo.getParamData().put("id_card", idCard);
        params.put("biz_content", JSONObject.toJSONString(vo.getParamData()));

        /*3、对数据进行加签，并存入请求数据*/
        String sign = YsOnlineSignUtils.sign(params, vo.getPrivateKeyPassword(),vo.getPrivateKeyFilePath());
        logger.info("authenticate.four.key.element.precise-sign："+sign);
        params.put("sign",sign);

        /*4、发送请求*/
        String result = HttpClientUtil.sendPostParam(vo.getReqUrl(),StringUtil.mapToString(params));
        logger.info("authenticate.four.key.element.precise-result:"+result);

        /*5、结果验签*/
        JSONObject jsonObject= JSON.parseObject(result, Feature.OrderedField);
        // 加签值
        String sinaValue = (String) jsonObject.get("sign");
        // 加签内容
        String content = JSONObject.toJSONString(jsonObject.get("ysepay_authenticate_four_key_element_precise_response"),SerializerFeature.WriteMapNullValue);
        boolean flag = YsOnlineSignUtils.rsaCheckContent(content,sinaValue, Constants.CHARSET_UTF_8,vo.getYsPublicKeyFilePath());
        if(!flag){
            throw new Exception("check sign error");
        } else {
            logger.info("check sign success");
        }
        return content;
    }

}
