package com.eptok.yspay.opensdkjava.util;


import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.*;
import java.util.logging.Logger;

/**
 * @Description 扫码服务商系统签名验签
 * @Date 9:40 2021/9/26
*/
public class YsfSignUtil {

    private static final Logger logger = Logger.getLogger("YsfSignUtil");

    private final static String ALGORITHM = "SHA1WithRSA";

    private final static String CHAR_SET = "UTF-8";

    private final static String KeyStore_PKCS12 = "PKCS12";

    private final static String PARAM_PRIVATE_KEY = "PrivateKey";


    /**
     *  签名
     * @param params 参数
     * @param privateKeyFilePath 私钥证书路径
     * @param privateKeyPassword 证书密码
     * @return
     */
    public static String sign(Map<String, String> params, String privateKeyFilePath,String privateKeyPassword) throws Exception {
        File privateKeyFile = new File(privateKeyFilePath);
        if(!privateKeyFile.exists()){
            throw new Exception("privateKeyFile is not found...");
        }
        String signContent = getSignContent(params);
        String mysign = rsaSign(signContent, CHAR_SET , privateKeyFile, privateKeyPassword);
        return mysign;
    }

    /**
     * 验证签名是否正确，
     * 已经自动去掉params种的sign字段
     * @param params
     * @param sign
     * @param ysPublicKeyFilePath
     * @return   true 成功，false 失败
     * @throws Exception
     */
    public static boolean rsaCheckContent(Map<String, String> params, String sign,String ysPublicKeyFilePath) throws Exception {
        if (sign == null) {
            sign = "";
        }
        File publicKeyFile = new File(ysPublicKeyFilePath);
        if(!publicKeyFile.exists()){
            throw new Exception("publicKeyFile is not found...");
        }
        InputStream publicCertFileInputStream = new FileInputStream(publicKeyFile);
        String content = createLinkString(paraFilter(params));
        return rsaCheckContent(publicCertFileInputStream, content, sign, CHAR_SET);
    }

    /**
     * 验签实现类
     *
     * @param publicCertFileInputStream
     * @param content
     * @param sign
     * @param charset
     * @return
     * @throws Exception
     */
    private static boolean rsaCheckContent(InputStream publicCertFileInputStream, String content, String sign,
                                           String charset) throws Exception {
        boolean bFlag = false;
        Signature signetcheck = Signature.getInstance(ALGORITHM);
        signetcheck.initVerify(getPublicKeyFromCert(publicCertFileInputStream));
        signetcheck.update(content.getBytes(charset));
        if (signetcheck.verify(new BASE64Decoder().decodeBuffer(sign))) {
            // 跑不进条件语句里面
            bFlag = true;
        }
        return bFlag;
    }

    /**
     * 读取公钥，x509格式
     * @param ins
     * @return
     * @throws Exception
     * @see
     */
    private static PublicKey getPublicKeyFromCert(InputStream ins) throws Exception {
        Map<String, Object> certMap = new java.util.concurrent.ConcurrentHashMap<String, Object>();
        PublicKey pubKey = (PublicKey) certMap.get("PublicKey");
        if (pubKey != null) {
            return pubKey;
        }
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            Certificate cac = cf.generateCertificate(ins);
            pubKey = cac.getPublicKey();
            certMap.put("PublicKey", pubKey);
        } catch (Exception e) {
            if (ins != null){
                ins.close();
            }
            throw e;
        } finally {
            if (ins != null) {
                ins.close();
            }
        }
        return pubKey;
    }

    /**
     * RSA签名
     * @param content 签名内容
     * @param charset 字符集
     * @param privateKeyFile 私钥证书
     * @param privateKeyPassword 证书密码
     * @return
     * @throws Exception
     */
    private static String rsaSign(String content, String charset, File privateKeyFile, String privateKeyPassword) throws Exception {
        try {
            InputStream pfxCertFileInputStream = new FileInputStream(privateKeyFile);
            logger.info("进入签名方法：content[" + content + "], charset[" + charset + "]");
            PrivateKey priKey = getPrivateKeyFromPKCS12(privateKeyPassword, pfxCertFileInputStream);
            Signature signature = Signature.getInstance(ALGORITHM);
            signature.initSign(priKey);
            if (StringUtil.isEmpty(charset)) {
                signature.update(content.getBytes());
            } else {
                signature.update(content.getBytes(charset));
            }
            byte[] signed = signature.sign();
            String sign = new BASE64Encoder().encode(signed);
            logger.info("进入签名完：content[" + content + "], charset[" + charset + "], sign[" + sign + "]");
            return sign;
        } catch (Exception e) {
            throw new Exception("支付报文加签异常， RSAcontent = " + content + "; charset = " + charset, e);
        }
    }

    /**
     * 读取PKCS12格式的key（私钥）pfx格式
     * @param password 证书密码
     * @param ins 证书输入流
     * @return
     * @throws Exception
     */
    private static PrivateKey getPrivateKeyFromPKCS12(String password, InputStream ins) throws Exception {
        Map<String, Object> certMap = new java.util.concurrent.ConcurrentHashMap<String, Object>();
        PrivateKey priKey = (PrivateKey) certMap.get(PARAM_PRIVATE_KEY);
        if (priKey != null) {
            return priKey;
        }
        KeyStore keystoreCA = KeyStore.getInstance(KeyStore_PKCS12);
        try {
            // 读取CA根证书
            keystoreCA.load(ins, password.toCharArray());
            Enumeration<?> aliases = keystoreCA.aliases();
            String keyAlias = null;
            if (aliases != null) {
                while (aliases.hasMoreElements()) {
                    keyAlias = (String) aliases.nextElement();
                    // 获取CA私钥
                    priKey = (PrivateKey) (keystoreCA.getKey(keyAlias, password.toCharArray()));
                    if (priKey != null) {
                        certMap.put(PARAM_PRIVATE_KEY, priKey);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            if (ins != null){
                ins.close();
            }
            throw e;
        } finally {
            if (ins != null) {
                ins.close();
            }
        }
        return priKey;
    }

    /**
     * 遍历以及根据重新排序
     * @param sortedParams 需要验签的参数
     * @return
     */
    private static String getSignContent(Map<String, String> sortedParams) {
        StringBuffer content = new StringBuffer();
        List<String> keys = new ArrayList<String>(sortedParams.keySet());
        Collections.sort(keys);
        int index = 0;
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = sortedParams.get(key);
            if (StringUtil.areNotEmpty(key, value)) {
                content.append((index == 0 ? "" : "&") + key + "=" + value);
                index++;
            }
        }
        return content.toString();
    }

    /**
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     * @param params 需要排序并参与字符拼接的参数组
     * @return 拼接后字符串
     */
    private static String createLinkString(Map<String, String> params) {
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);
        String prestr = "";
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);
            if (i == keys.size() - 1) {
                prestr = prestr + key + "=" + value;
            } else {
                prestr = prestr + key + "=" + value + "&";
            }
        }
        return prestr;
    }

    /**
     * 除去数组中的空值和签名参数
     * @param sArray  签名参数组
     * @return 去掉空值与签名参数后的新签名参数组
     */
    private static Map<String, String> paraFilter(Map<String, String> sArray) {
        Map<String, String> result = new HashMap<String, String>();
        if (sArray == null || sArray.size() <= 0) {
            return result;
        }
        for (String key : sArray.keySet()) {
            String value = sArray.get(key);
            if (value == null || StringUtil.isEmpty(value) || key.equalsIgnoreCase("sign")) {
                continue;
            }
            result.put(key, value);
        }
        return result;
    }
}
