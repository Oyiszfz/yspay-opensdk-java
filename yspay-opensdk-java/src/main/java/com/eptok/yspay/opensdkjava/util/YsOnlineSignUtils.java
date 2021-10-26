package com.eptok.yspay.opensdkjava.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class YsOnlineSignUtils {

	private static Logger log = Logger.getLogger("YsOnlineSignUtils");

	// 缓存公钥和私钥
	public static Map<String, Object> certMap = new java.util.concurrent.ConcurrentHashMap<String, Object>();
	/**
	 * 文件读取缓冲区大小
	 */
	private static final int CACHE_SIZE = 2048;

	public static final String ALLCHAR = "0123456789ABCDEF";
	public static final String ALGORITHM = "SHA1withRSA";
	/**
	 * 除去数组中的空值和签名参数
	 * 
	 * @param sArray 签名参数组
	 * @return 去掉空值与签名参数后的新签名参数组
	 */
	public static Map<String, String> paraFilter(Map<String, String> sArray) {
		Map<String, String> result = new TreeMap<String, String>();
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

	/**
	 * 
	 * @param sortedParams
	 * @return
	 */
	public static String getSignContent(Map<String, String> sortedParams) {
		StringBuffer content = new StringBuffer();
		List<String> keys = new ArrayList<String>(sortedParams.keySet());
		Collections.sort(keys);
		int index = 0;
		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			if ("sign".equals(key)) {
				continue;
			}
			String value = sortedParams.get(key);
			if (StringUtil.areNotEmpty(key, value)) {
				content.append((index == 0 ? "" : "&") + key + "=" + value);
				index++;
			}
		}
		return content.toString();
	}

	public static String rsaSign(String content, String charset, String privateCerPwd, String privateCerPath,
                                 String rsaType) throws Exception {
		try {
			PrivateKey priKey = getPrivateKeyFromPKCS12(privateCerPwd, privateCerPath);

			Signature signature = Signature.getInstance(rsaType);

			signature.initSign(priKey);

			if (StringUtil.isEmpty(charset)) {
				signature.update(content.getBytes());
			} else {
				signature.update(content.getBytes(charset));
			}

			byte[] signed = signature.sign();

			String sign = new String(Base64Utils.encode(signed));

			return sign;
		} catch (Exception e) {
			log.info("数据签名异常"+e);
			throw new Exception("RSAcontent = " + content + "; charset = " + charset, e);
		}
	}

	/**
	 * 把参数签名
	 *
	 * @param params
	 * @param charset
	 * @return
	 * @throws Exception
	 *
	 *             public static String rsaSign(Map<String, String> params, String charset,
	 *             InputStream pfxCertFileInputStream) throws Exception { String signContent =
	 *             getSignContent(params);
	 *
	 *             return rsaSign(signContent, charset, pfxCertFileInputStream); }
	 */

	public static boolean rsaCheckContent(Map<String, String> params, String sign, String charset, String publicKeyPath) throws Exception {
		String content = StringUtil.createLinkString(YsOnlineSignUtils.paraFilter(params));
		return rsaCheckContent(content, sign, charset, publicKeyPath);
	}

	public static boolean rsaCheckContent(String content, String sign, String charset, String publicKeyPath) throws Exception {
		boolean bFlag = false;
		Signature signetcheck = Signature.getInstance(ALGORITHM);
		signetcheck.initVerify(getPublicKeyFromCert(publicKeyPath));
		signetcheck.update(content.getBytes(charset));
		if (signetcheck.verify(Base64Utils.decode(sign))) {
			bFlag = true;
		}
		return bFlag;
	}

	public static boolean rsaCheckContent(String content, String sign, String charset, PublicKey publicKey,
                                          String rsaType) throws Exception {
		boolean bFlag = false;
		Signature signetcheck = Signature.getInstance(rsaType);
		signetcheck.initVerify(publicKey);
		signetcheck.update(content.getBytes(charset));
		if (signetcheck.verify(Base64Utils.decode(sign))) {
			bFlag = true;
		}
		return bFlag;
	}

	public static boolean validateFileSign(FileInputStream fileInputStream, String sign,
                                           InputStream publicCertFileInputStream, String publicKeyPath) throws Exception {
		boolean result = false;
		// 获得公钥
		PublicKey publicKey = getPublicKeyFromCert(publicKeyPath);
		// 构建签名
		Signature signature = Signature.getInstance(ALGORITHM);
		signature.initVerify(publicKey);
		byte[] decodedSign = Base64Utils.decode(sign);
		byte[] cache = new byte[CACHE_SIZE];
		int nRead = 0;
		while ((nRead = fileInputStream.read(cache)) != -1) {
			signature.update(cache, 0, nRead);
		}
		fileInputStream.close();
		result = signature.verify(decodedSign);
		return result;
	}

	/**
	 * 读取公钥，x509格式
	 * 
	 * @param publicKeyPath 公钥证书路径
	 * @return
	 * @throws Exception
	 * @see
	 */
	public static PublicKey getPublicKeyFromCert(String publicKeyPath) throws Exception {
		PublicKey pubKey = (PublicKey) certMap.get(publicKeyPath);
		if (pubKey != null) {
			return pubKey;
		}
		File privateKeyFile = new File(publicKeyPath);
		if(!privateKeyFile.exists()){
			throw new Exception("publickeyFile is not found...");
		}
		FileInputStream ins = null;
		try {
			CertificateFactory cf = CertificateFactory.getInstance("X.509");
			ins = new FileInputStream(publicKeyPath);
			Certificate cac = cf.generateCertificate(ins);
			pubKey = cac.getPublicKey();
			certMap.put(publicKeyPath, pubKey);
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
	 * 读取PKCS12格式的key（私钥）pfx格式
	 * 
	 * @param password
	 * @param privateCerPath
	 * @return
	 * @throws Exception
	 * @see
	 */
	public static PrivateKey getPrivateKeyFromPKCS12(String password, String privateCerPath) throws Exception {
		PrivateKey priKey = null;
		if (certMap.get(privateCerPath) != null) {
			priKey = (PrivateKey) certMap.get(privateCerPath);
			if (priKey != null) {
				return priKey;
			}
		}

		KeyStore keystoreCA = KeyStore.getInstance("PKCS12");
		File privateKeyFile = new File(privateCerPath);
		if(!privateKeyFile.exists()){
			throw new Exception("privateKeyFile is not found...");
		}
		InputStream ins = new FileInputStream(privateKeyFile);
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
						certMap.put(privateCerPath, priKey);
						break;
					}
				}
			}
		} catch (Exception e) {
			if (ins != null)
				ins.close();
			throw e;
		} finally {
			if (ins != null) {
				ins.close();
			}
		}

		return priKey;
	}

	public static byte[] encrypt(Key key, byte[] data) throws Exception {
		try {
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			return cipher.doFinal(data);
		} catch (Exception e) {
			throw e;
		}
	}

	public static byte[] decrypt(Key Key, byte[] data) throws Exception {
		try {
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.DECRYPT_MODE, Key);
			return cipher.doFinal(data);
		} catch (Exception e) {
			throw e;
		}
	}

	private final static String ENCODE = "UTF-8";
	// private final static String DES = "DES";

	/**
	 * Description 根据键值进行加密
	 *
	 * @param data
	 * @param key 加密键byte数组
	 * @return
	 * @throws Exception
	 */
	public static String encrypt(String data, String key) throws Exception {
		byte[] bt = encrypt(data.getBytes(ENCODE), key.getBytes(ENCODE));
		String strs = Base64Utils.encode(bt);
		return strs;
	}

	/**
	 * Description 根据键值进行加密
	 *
	 * @param data
	 * @param key 加密键byte数组
	 * @return
	 * @throws Exception
	 */
	private static byte[] encrypt(byte[] data, byte[] key) throws Exception {
		// 生成一个可信任的随机数源
		SecureRandom sr = new SecureRandom();
		// 从原始密钥数据创建DESKeySpec对象
		DESKeySpec dks = new DESKeySpec(key);
		// 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey securekey = keyFactory.generateSecret(dks);
		// Cipher对象实际完成加密操作
		Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
		// 用密钥初始化Cipher对象
		cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);
		return cipher.doFinal(data);
	}

	public static String getSignStr(Map<String, String> map) {
		List<String> keys = new ArrayList<String>(map.keySet());
		Collections.sort(keys);
		StringBuilder sb = new StringBuilder();
		for (String key : keys) {
			if ("sign".equals(key)) {
				continue;
			}
			sb.append(key).append("=");
			sb.append(map.get(key));
			sb.append("&");
		}
		if (sb.length() > 0) {
			sb.setLength(sb.length() - 1);
		}
		return sb.toString();
	}

	public static String getPublicStrKeyFromCert(String publicKeyPath) throws Exception {
		FileInputStream ins = null;
		try {
			CertificateFactory cf = CertificateFactory.getInstance("X.509");
			ins = new FileInputStream(publicKeyPath);
			Certificate cac = cf.generateCertificate(ins);
			PublicKey pubKey = cac.getPublicKey();
			return Base64Utils.encode(pubKey.getEncoded());
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
	}

	/**
	 * 签名生成
	 * @param params
	 * @param privateCerPwd
	 * @param privateCerPath
	 * @return
	 * @throws Exception
	 */
	public static String sign(Map<String, String> params, String privateCerPwd, String privateCerPath) throws Exception {

		Map<String, String> stringStringMap = paraFilter(params);
		String signContent = getSignContent(stringStringMap);
		String sign = rsaSign(signContent, params.get("charset"), privateCerPwd, privateCerPath, ALGORITHM);

		return sign;
	}

}
