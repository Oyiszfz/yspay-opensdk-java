package com.eptok.yspay.opensdkjava.pojo.vo;

import java.util.Map;

public class OnlineReqDataVo {


    /**
     * 请求路径
     */
    private String reqUrl;

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
     * 商户在银盛支付平台开设的用户号[商户号]
     */
    private String partnerId;

    /**
     * 提现代理密码
     */
    private String proxyPassword;

    private String notifyUrl;

    /** 同步通知地址 */
    private String returnUrl;

    /** 交易类型 */
    private String tranType;

    /**
     * 线上进件服务商的业务数据
     */
    private Map<String,Object> paramData;

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public String getTranType() {
        return tranType;
    }

    public void setTranType(String tranType) {
        this.tranType = tranType;
    }

    public String getReqUrl() {
        return reqUrl;
    }

    public void setReqUrl(String reqUrl) {
        this.reqUrl = reqUrl;
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

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public String getProxyPassword() {return proxyPassword;}

    public void setProxyPassword(String proxyPassword) {this.proxyPassword = proxyPassword;}

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public Map<String, Object> getParamData() {
        return paramData;
    }

    public void setParamData(Map<String, Object> paramData) {
        this.paramData = paramData;
    }
}
