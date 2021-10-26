package com.eptok.yspay.opensdkjava.merc;

import com.alibaba.fastjson.JSONObject;
import com.eptok.yspay.opensdkjava.common.Constants;
import com.eptok.yspay.opensdkjava.pojo.vo.YsfReqDataVo;
import com.eptok.yspay.opensdkjava.util.DesUtil;
import com.eptok.yspay.opensdkjava.util.HttpClientUtil;
import com.eptok.yspay.opensdkjava.util.StringUtil;
import com.eptok.yspay.opensdkjava.util.YsfSignUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * 扫码服务商实现类
 */
public class ScanMercApi {

    private static final Logger logger = Logger.getLogger("ScanMercApi");
    
    /**
     * @Description 扫码服务商进件
     * @Date 14:22 2021/9/26
     * @Param 
     * @return 
    */
    public static String addScanMerc(YsfReqDataVo vo) throws Exception {
        /** 1、对业务数据进行加密*/
        String addMercData = JSONObject.toJSONString(vo.getParamData());
        String creData = DesUtil.encryptExtraData(vo.getAeskey(), "UTF-8", addMercData);
        logger.info("ScanMercApi-addScanMerc-AesData:"+creData);

        /** 2、组装入参的数据*/
        Map<String, String> params = new HashMap<String, String>();
        params.put("ver", "1.0");
        params.put("msgCode", "Merchant");
        params.put("method", "addMerchant");
        params.put("src",vo.getSrc());
        params.put("signType", "RSA");
        params.put("charset", Constants.CHARSET_UTF_8);
        params.put("data", creData);

        /*3、对数据进行加签，并存入请求数据*/
        String sign = YsfSignUtil.sign(params, vo.getPrivateKeyFilePath(), vo.getPrivateKeyPassword());
        logger.info("ScanMercApi-addScanMerc-signData:"+sign);
        params.put("signValue", sign);
        params.put("reqFlowId", vo.getReqFlowId());

        /*4、发送请求*/
        String result = HttpClientUtil.sendPostJson(vo.getReqUrl(),JSONObject.toJSONString(params));
        logger.info("ScanMercApi-addScanMerc-remoteResultData:"+result);

        /*5、结果验签*/
        JSONObject jsonObject = JSONObject.parseObject(result);
        // 加签值
        String sinaValue = jsonObject.getString("signValue");
        // 加签内容
        String respData = jsonObject.getString("data");
        Map<String, String> dataMap = null;
        if(!StringUtil.isEmpty(respData)){
            dataMap = JSONObject.parseObject(respData,Map.class);
        }
        boolean flag = YsfSignUtil.rsaCheckContent(dataMap,sinaValue,vo.getYsPublicKeyFilePath());
        if(!flag){
            throw new Exception("check sign error");
        }
        return result;
    }

    /**
     * @Description 扫码服务商图片上传接口
     * @Date 14:22 2021/9/26
     * @Param
     * @return
     */
    public static String uploadPic(YsfReqDataVo vo, byte[] fileByte,String fileName) throws Exception {
        /** 1、组装需要加签的入参的数据*/
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("picType", (String) vo.getParamData().get("picType"));
        param.put("sysFlowId", (String) vo.getParamData().get("sysFlowId"));

        /*2、对数据进行加签，并存入请求数据*/
        String sign = YsfSignUtil.sign(param, vo.getPrivateKeyFilePath(), vo.getPrivateKeyPassword());
        logger.info("ScanMercApi-uploadPic-signData:"+sign);
        param.put("signValue", sign);

        /*3、发送请求*/
        String result = HttpClientUtil.sendPostFile(vo.getReqUrl(),param,fileByte,fileName,"file");
        logger.info("ScanMercApi-uploadPic-remoteResultData:"+result);

        /*4、结果验签*/
        JSONObject jsonObject = JSONObject.parseObject(result);
        // 加签值
        String sinaValue = jsonObject.getString("signValue");
        // 加签内容
        String respData = jsonObject.getString("data");
        Map<String, String> dataMap = null;
        if(!StringUtil.isEmpty(respData)){
            dataMap = JSONObject.parseObject(respData,Map.class);
        }
        boolean flag = YsfSignUtil.rsaCheckContent(dataMap,sinaValue,vo.getYsPublicKeyFilePath());
        if(!flag){
            throw new Exception("check sign error");
        }
        return result;
    }

    /**
     * @Description 扫码服务商商户信息修改接口
     * @Date 14:22 2021/9/26
     * @Param
     * @return
     */
    public static String updateScanMerc(YsfReqDataVo vo) throws Exception {
        /** 1、对业务数据进行加密*/
        String addMercData = JSONObject.toJSONString(vo.getParamData());
        String creData = DesUtil.encryptExtraData(vo.getAeskey(), Constants.CHARSET_UTF_8, addMercData);
        logger.info("ScanMercApi-updateScanMerc-AesData:"+creData);

        /** 2、组装入参的数据*/
        Map<String, String> params = new HashMap<String, String>();
        params.put("ver", "1.0");
        params.put("msgCode", "Merchant");
        params.put("method", "updateScanMerchant");
        params.put("src",vo.getSrc());
        params.put("signType", "RSA");
        params.put("charset", Constants.CHARSET_UTF_8);
        params.put("data", creData);

        /*3、对数据进行加签，并存入请求数据*/
        String sign = YsfSignUtil.sign(params, vo.getPrivateKeyFilePath(), vo.getPrivateKeyPassword());
        logger.info("ScanMercApi-updateScanMerc-signData:"+sign);
        params.put("signValue", sign);
        params.put("reqFlowId", vo.getReqFlowId());

        /*4、发送请求*/
        String result = HttpClientUtil.sendPostJson(vo.getReqUrl(),JSONObject.toJSONString(params));
        logger.info("ScanMercApi-updateScanMerc-remoteResult:"+result);

        /*5、结果验签*/
        JSONObject jsonObject = JSONObject.parseObject(result);
        // 加签值
        String sinaValue = jsonObject.getString("signValue");
        // 加签内容
        String respData = jsonObject.getString("data");
        Map<String, String> dataMap = null;
        if(!StringUtil.isEmpty(respData)){
            dataMap = JSONObject.parseObject(respData,Map.class);
        }
        boolean flag = YsfSignUtil.rsaCheckContent(dataMap,sinaValue,vo.getYsPublicKeyFilePath());
        if(!flag){
            throw new Exception("check sign error");
        }
        return result;
    }


    /**
     * @Description 扫码服务商商户信息修改接口
     * @Date 14:22 2021/9/26
     * @Param
     * @return
     */
    public static String autoAudit(YsfReqDataVo vo) throws Exception {
        /** 1、对业务数据进行加密*/
        String addMercData = JSONObject.toJSONString(vo.getParamData());
        String creData = DesUtil.encryptExtraData(vo.getAeskey(), Constants.CHARSET_UTF_8, addMercData);
        logger.info("ScanMercApi-autoAudit-AesData:"+creData);

        /** 2、组装入参的数据*/
        Map<String, String> params = new HashMap<String, String>();
        params.put("ver", "1.0");
        params.put("msgCode", "Merchant");
        params.put("method", "autoAuditTerm");
        params.put("src",vo.getSrc());
        params.put("signType", "RSA");
        params.put("charset", Constants.CHARSET_UTF_8);
        params.put("data", creData);

        /*3、对数据进行加签，并存入请求数据*/
        String sign = YsfSignUtil.sign(params, vo.getPrivateKeyFilePath(), vo.getPrivateKeyPassword());
        logger.info("ScanMercApi-autoAudit-signData:"+sign);
        params.put("signValue", sign);
        params.put("reqFlowId", vo.getReqFlowId());

        /*4、发送请求*/
        String result = HttpClientUtil.sendPostJson(vo.getReqUrl(),JSONObject.toJSONString(params));
        logger.info("ScanMercApi-autoAudit-remoteResult:"+result);

        /*5、结果验签*/
        JSONObject jsonObject = JSONObject.parseObject(result);
        // 加签值
        String sinaValue = jsonObject.getString("signValue");
        // 加签内容
        String respData = jsonObject.getString("data");
        Map<String, String> dataMap = null;
        if(!StringUtil.isEmpty(respData)){
            dataMap = JSONObject.parseObject(respData,Map.class);
        }
        boolean flag = YsfSignUtil.rsaCheckContent(dataMap,sinaValue,vo.getYsPublicKeyFilePath());
        if(!flag){
            throw new Exception("check sign error");
        }
        return result;
    }

    /**
     * @Description 扫码服务商 线上交易权限开通
     * @Date 14:22 2021/9/26
     * @Param
     * @return
     */
    public static String onlineTrading(YsfReqDataVo vo) throws Exception {
        /** 1、对业务数据进行加密*/
        String addMercData = JSONObject.toJSONString(vo.getParamData());
        String creData = DesUtil.encryptExtraData(vo.getAeskey(), Constants.CHARSET_UTF_8, addMercData);
        logger.info("ScanMercApi-onlineTrading-AesData:"+creData);

        /** 2、组装入参的数据*/
        Map<String, String> params = new HashMap<String, String>();
        params.put("ver", "1.0");
        params.put("msgCode", "Merchant");
        params.put("method", "onlineTrading");
        params.put("src",vo.getSrc());
        params.put("signType", "RSA");
        params.put("charset", Constants.CHARSET_UTF_8);
        params.put("data", creData);

        /*3、对数据进行加签，并存入请求数据*/
        String sign = YsfSignUtil.sign(params, vo.getPrivateKeyFilePath(), vo.getPrivateKeyPassword());
        logger.info("ScanMercApi-onlineTrading-signData:"+sign);
        params.put("signValue", sign);
        params.put("reqFlowId", vo.getReqFlowId());

        /*4、发送请求*/
        String result = HttpClientUtil.sendPostJson(vo.getReqUrl(),JSONObject.toJSONString(params));
        logger.info("ScanMercApi-onlineTrading-remoteResult:"+result);

        /*5、结果验签*/
        JSONObject jsonObject = JSONObject.parseObject(result);
        // 加签值
        String sinaValue = jsonObject.getString("signValue");
        // 加签内容
        String respData = jsonObject.getString("data");
        Map<String, String> dataMap = null;
        if(!StringUtil.isEmpty(respData)){
            dataMap = JSONObject.parseObject(respData,Map.class);
        }
        boolean flag = YsfSignUtil.rsaCheckContent(dataMap,sinaValue,vo.getYsPublicKeyFilePath());
        if(!flag){
            throw new Exception("check sign error");
        }
        return result;
    }

    /**
     * @Description 扫码服务商 重发签约短信
     * @Date 14:22 2021/9/26
     * @Param
     * @return
     */
    public static String sendScanSmsCode(YsfReqDataVo vo) throws Exception {
        /** 1、对业务数据进行加密*/
        String addMercData = JSONObject.toJSONString(vo.getParamData());
        String creData = DesUtil.encryptExtraData(vo.getAeskey(), Constants.CHARSET_UTF_8, addMercData);
        logger.info("ScanMercApi-sendScanSmsCode-AesDate:"+creData);

        /** 2、组装入参的数据*/
        Map<String, String> params = new HashMap<String, String>();
        params.put("ver", "1.0");
        params.put("msgCode", "Merchant");
        params.put("method", "sendScanSmsCode");
        params.put("src",vo.getSrc());
        params.put("signType", "RSA");
        params.put("charset", Constants.CHARSET_UTF_8);
        params.put("data", creData);

        /*3、对数据进行加签，并存入请求数据*/
        String sign = YsfSignUtil.sign(params, vo.getPrivateKeyFilePath(), vo.getPrivateKeyPassword());
        logger.info("ScanMercApi-sendScanSmsCode-signDate:"+sign);
        params.put("signValue", sign);
        params.put("reqFlowId", vo.getReqFlowId());

        /*4、发送请求*/
        String result = HttpClientUtil.sendPostJson(vo.getReqUrl(),JSONObject.toJSONString(params));
        logger.info("ScanMercApi-sendScanSmsCode-remoteResult:"+result);

        /*5、结果验签*/
        JSONObject jsonObject = JSONObject.parseObject(result);
        // 加签值
        String sinaValue = jsonObject.getString("signValue");
        // 加签内容
        String respData = jsonObject.getString("data");
        Map<String, String> dataMap = null;
        if(!StringUtil.isEmpty(respData)){
            dataMap = JSONObject.parseObject(respData,Map.class);
        }
        boolean flag = YsfSignUtil.rsaCheckContent(dataMap,sinaValue,vo.getYsPublicKeyFilePath());
        if(!flag){
            throw new Exception("check sign error");
        }
        return result;
    }

    /**
     * @Description 扫码服务商 查询签约状态
     * @Date 14:22 2021/9/26
     * @Param
     * @return
     */
    public static String querySignSts(YsfReqDataVo vo) throws Exception {
        /** 1、对业务数据进行加密*/
        String addMercData = JSONObject.toJSONString(vo.getParamData());
        String creData = DesUtil.encryptExtraData(vo.getAeskey(), Constants.CHARSET_UTF_8, addMercData);
        logger.info("ScanMercApi-querySignSts-AesDate:"+creData);

        /** 2、组装入参的数据*/
        Map<String, String> params = new HashMap<String, String>();
        params.put("ver", "1.0");
        params.put("msgCode", "Merchant");
        params.put("method", "querySignSts");
        params.put("src",vo.getSrc());
        params.put("signType", "RSA");
        params.put("charset", Constants.CHARSET_UTF_8);
        params.put("data", creData);

        /*3、对数据进行加签，并存入请求数据*/
        String sign = YsfSignUtil.sign(params, vo.getPrivateKeyFilePath(), vo.getPrivateKeyPassword());
        logger.info("ScanMercApi-querySignSts-signData:"+sign);
        params.put("signValue", sign);
        params.put("reqFlowId", vo.getReqFlowId());

        /*4、发送请求*/
        String result = HttpClientUtil.sendPostJson(vo.getReqUrl(),JSONObject.toJSONString(params));
        logger.info("ScanMercApi-querySignSts-remoteResult:"+result);

        /*5、结果验签*/
        JSONObject jsonObject = JSONObject.parseObject(result);
        // 加签值
        String sinaValue = jsonObject.getString("signValue");
        // 加签内容
        String respData = jsonObject.getString("data");
        Map<String, String> dataMap = null;
        if(!StringUtil.isEmpty(respData)){
            dataMap = JSONObject.parseObject(respData,Map.class);
        }
        boolean flag = YsfSignUtil.rsaCheckContent(dataMap,sinaValue,vo.getYsPublicKeyFilePath());
        if(!flag){
            throw new Exception("check sign error");
        }
        return result;
    }

    /**
     * @Description 扫码服务商 电子合同下载
     * @Date 14:22 2021/9/26
     * @Param
     * @return
     */
    public static byte[] downSign(YsfReqDataVo vo) throws Exception {
        /** 1、对业务数据进行加密*/
        String addMercData = JSONObject.toJSONString(vo.getParamData());
        String creData = DesUtil.encryptExtraData(vo.getAeskey(), Constants.CHARSET_UTF_8, addMercData);
        logger.info("ScanMercApi-downSign-AesDate:"+creData);

        /** 2、组装入参的数据*/
        Map<String, String> params = new HashMap<String, String>();
        params.put("ver", "1.0");
        params.put("msgCode", "Merchant");
        params.put("method", "downSign");
        params.put("src",vo.getSrc());
        params.put("signType", "RSA");
        params.put("charset", Constants.CHARSET_UTF_8);
        params.put("data", creData);

        /*3、对数据进行加签，并存入请求数据*/
        String sign = YsfSignUtil.sign(params, vo.getPrivateKeyFilePath(), vo.getPrivateKeyPassword());
        logger.info("ScanMercApi-downSign-signData:"+sign);
        params.put("signValue", sign);
        params.put("reqFlowId", vo.getReqFlowId());

        /*4、发送请求*/
        byte[] result =HttpClientUtil.sendPostDownFile(vo.getReqUrl(),JSONObject.toJSONString(params));
        return result;
    }


    /**
     * @Description 扫码服务商商户资料变更
     * @Date 14:22 2021/9/26
     * @Param
     * @return
     */
    public static String changeMerInfo(YsfReqDataVo vo) throws Exception {
        /** 1、对业务数据进行加密*/
        String addMercData = JSONObject.toJSONString(vo.getParamData());
        String creData = DesUtil.encryptExtraData(vo.getAeskey(), "UTF-8", addMercData);
        logger.info("ScanMercApi-changeMerInfo-AesData:"+creData);

        /** 2、组装入参的数据*/
        Map<String, String> params = new HashMap<String, String>();
        params.put("ver", "1.0");
        params.put("msgCode", "Merchant");
        params.put("method", "changeMerInfo");
        params.put("src",vo.getSrc());
        params.put("signType", "RSA");
        params.put("charset", Constants.CHARSET_UTF_8);
        params.put("data", creData);

        /*3、对数据进行加签，并存入请求数据*/
        String sign = YsfSignUtil.sign(params, vo.getPrivateKeyFilePath(), vo.getPrivateKeyPassword());
        logger.info("ScanMercApi-changeMerInfo-signData:"+sign);
        params.put("signValue", sign);
        params.put("reqFlowId", vo.getReqFlowId());

        /*4、发送请求*/
        String result = HttpClientUtil.sendPostJson(vo.getReqUrl(),JSONObject.toJSONString(params));
        logger.info("ScanMercApi-changeMerInfo-remoteResultData:"+result);

        /*5、结果验签*/
        JSONObject jsonObject = JSONObject.parseObject(result);
        // 加签值
        String sinaValue = jsonObject.getString("signValue");
        // 加签内容
        String respData = jsonObject.getString("data");
        Map<String, String> dataMap = null;
        if(!StringUtil.isEmpty(respData)){
            dataMap = JSONObject.parseObject(respData,Map.class);
        }
        boolean flag = YsfSignUtil.rsaCheckContent(dataMap,sinaValue,vo.getYsPublicKeyFilePath());
        if(!flag){
            throw new Exception("check sign error");
        }
        return result;
    }


    /**
     * @Description 扫码服务商结算信息变更
     * @Date 14:22 2021/9/26
     * @Param
     * @return
     */
    public static String changeMerAccInfo(YsfReqDataVo vo) throws Exception {
        /** 1、对业务数据进行加密*/
        String addMercData = JSONObject.toJSONString(vo.getParamData());
        String creData = DesUtil.encryptExtraData(vo.getAeskey(), "UTF-8", addMercData);
        logger.info("ScanMercApi-changeMerAccInfo-AesData:"+creData);

        /** 2、组装入参的数据*/
        Map<String, String> params = new HashMap<String, String>();
        params.put("ver", "1.0");
        params.put("msgCode", "Merchant");
        params.put("method", "changeMerAccInfo");
        params.put("src",vo.getSrc());
        params.put("signType", "RSA");
        params.put("charset", Constants.CHARSET_UTF_8);
        params.put("data", creData);

        /*3、对数据进行加签，并存入请求数据*/
        String sign = YsfSignUtil.sign(params, vo.getPrivateKeyFilePath(), vo.getPrivateKeyPassword());
        logger.info("ScanMercApi-changeMerAccInfo-signData:"+sign);
        params.put("signValue", sign);
        params.put("reqFlowId", vo.getReqFlowId());

        /*4、发送请求*/
        String result = HttpClientUtil.sendPostJson(vo.getReqUrl(),JSONObject.toJSONString(params));
        logger.info("ScanMercApi-changeMerAccInfo-remoteResultData:"+result);

        /*5、结果验签*/
        JSONObject jsonObject = JSONObject.parseObject(result);
        // 加签值
        String sinaValue = jsonObject.getString("signValue");
        // 加签内容
        String respData = jsonObject.getString("data");
        Map<String, String> dataMap = null;
        if(!StringUtil.isEmpty(respData)){
            dataMap = JSONObject.parseObject(respData,Map.class);
        }
        boolean flag = YsfSignUtil.rsaCheckContent(dataMap,sinaValue,vo.getYsPublicKeyFilePath());
        if(!flag){
            throw new Exception("check sign error");
        }
        return result;
    }

    /**
     * @Description 扫码服务商-商户扫码费率变更
     * @Date 14:22 2021/9/26
     * @Param
     * @return
     */
    public static String changeMerOnlineFee(YsfReqDataVo vo) throws Exception {
        /** 1、对业务数据进行加密*/
        String addMercData = JSONObject.toJSONString(vo.getParamData());
        String creData = DesUtil.encryptExtraData(vo.getAeskey(), "UTF-8", addMercData);
        logger.info("ScanMercApi-changeMerOnlineFee-AesData:"+creData);

        /** 2、组装入参的数据*/
        Map<String, String> params = new HashMap<String, String>();
        params.put("ver", "1.0");
        params.put("msgCode", "Merchant");
        params.put("method", "changeMerOnlineFee");
        params.put("src",vo.getSrc());
        params.put("signType", "RSA");
        params.put("charset", Constants.CHARSET_UTF_8);
        params.put("data", creData);

        /*3、对数据进行加签，并存入请求数据*/
        String sign = YsfSignUtil.sign(params, vo.getPrivateKeyFilePath(), vo.getPrivateKeyPassword());
        logger.info("ScanMercApi-changeMerOnlineFee-signData:"+sign);
        params.put("signValue", sign);
        params.put("reqFlowId", vo.getReqFlowId());

        /*4、发送请求*/
        String result = HttpClientUtil.sendPostJson(vo.getReqUrl(),JSONObject.toJSONString(params));
        logger.info("ScanMercApi-changeMerOnlineFee-remoteResultData:"+result);

        /*5、结果验签*/
        JSONObject jsonObject = JSONObject.parseObject(result);
        // 加签值
        String sinaValue = jsonObject.getString("signValue");
        // 加签内容
        String respData = jsonObject.getString("data");
        Map<String, String> dataMap = null;
        if(!StringUtil.isEmpty(respData)){
            dataMap = JSONObject.parseObject(respData,Map.class);
        }
        boolean flag = YsfSignUtil.rsaCheckContent(dataMap,sinaValue,vo.getYsPublicKeyFilePath());
        if(!flag){
            throw new Exception("check sign error");
        }
        return result;
    }

    /**
     * @Description 扫码服务商-商户变更图片上传
     * @Date 14:22 2021/9/26
     * @Param
     * @return
     */
    public static String changeMerPic(YsfReqDataVo vo, byte[] fileByte,String fileName) throws Exception {
        /** 1、组装需要加签的入参的数据*/
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("picType", (String) vo.getParamData().get("picType"));
        param.put("changeFlowId", (String) vo.getParamData().get("changeFlowId"));

        /*2、对数据进行加签，并存入请求数据*/
        String sign = YsfSignUtil.sign(param, vo.getPrivateKeyFilePath(), vo.getPrivateKeyPassword());
        logger.info("ScanMercApi-changeMerPic-signData:"+sign);
        param.put("signValue", sign);

        /*3、发送请求*/
        String result = HttpClientUtil.sendPostFile(vo.getReqUrl(),param,fileByte,fileName,"picFile");
        logger.info("ScanMercApi-uploadPic-remoteResultData:"+result);

        /*4、结果验签*/
        JSONObject jsonObject = JSONObject.parseObject(result);
        // 加签值
        String sinaValue = jsonObject.getString("signValue");
        // 加签内容
        String respData = jsonObject.getString("data");
        Map<String, String> dataMap = null;
        if(!StringUtil.isEmpty(respData)){
            dataMap = JSONObject.parseObject(respData,Map.class);
        }
        boolean flag = YsfSignUtil.rsaCheckContent(dataMap,sinaValue,vo.getYsPublicKeyFilePath());
        if(!flag){
            throw new Exception("check sign error");
        }
        return result;
    }
}
