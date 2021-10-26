package com.eptok.yspay.opensdkjava.pojo.vo;

import java.util.Map;

public class YsfReqDataVo {

    /**
     * 加密数据的aeskey
     */
    private String aeskey;

    /**
     * 请求路径
     */
    private String reqUrl;

    /**
     * 请求ID，扫码服务商接入，改参数不参与加签
     */
    private String reqFlowId;

    /**
     * 发起方(合作伙伴ID)
     */
    private String src;

    /**
     * 私钥证书路径
     */
    private String privateKeyFilePath;

    /**
     * 证书密码
     */
    private String privateKeyPassword;

    /**
     * 银盛公钥证书路径
     */
    private String ysPublicKeyFilePath;

    /**
     * 图片类型 调用图片上传的时候才需要
     */
    private String picType;

    /**
     * 新增扫码服务商的业务数据
     */
    private Map<String,Object> paramData;

    public String getAeskey() {
        return aeskey;
    }

    public void setAeskey(String aeskey) {
        this.aeskey = aeskey;
    }

    public String getReqUrl() {
        return reqUrl;
    }

    public void setReqUrl(String reqUrl) {
        this.reqUrl = reqUrl;
    }

    public String getReqFlowId() {
        return reqFlowId;
    }

    public void setReqFlowId(String reqFlowId) {
        this.reqFlowId = reqFlowId;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public Map<String, Object> getParamData() {
        return paramData;
    }

    public void setParamData(Map<String, Object> paramData) {
        this.paramData = paramData;
    }

    public String getPrivateKeyFilePath() {
        return privateKeyFilePath;
    }

    public void setPrivateKeyFilePath(String privateKeyFilePath) {
        this.privateKeyFilePath = privateKeyFilePath;
    }

    public String getPrivateKeyPassword() {
        return privateKeyPassword;
    }

    public void setPrivateKeyPassword(String privateKeyPassword) {
        this.privateKeyPassword = privateKeyPassword;
    }

    public String getYsPublicKeyFilePath() {
        return ysPublicKeyFilePath;
    }

    public void setYsPublicKeyFilePath(String ysPublicKeyFilePath) {
        this.ysPublicKeyFilePath = ysPublicKeyFilePath;
    }

    public String getPicType() {
        return picType;
    }

    public void setPicType(String picType) {
        this.picType = picType;
    }
}
