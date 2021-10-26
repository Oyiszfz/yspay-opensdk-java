package com.eptok.yspay.opensdkjava.util;

import sun.misc.BASE64Encoder;

/**
 * 注意该类是平台外给商户的des加密类和平台解密商户信息的方法 平台内部用 DesUtil工具类
 * 
 * @author chenliang
 * 
 */
public class SrcDesUtil {

	/**
	 * 加密扩展数据
	 * 
	 * @param extraData
	 * @return
	 */
	public static String encryptExtraData(String src, String extraData)
			throws Exception {
		src = String.format("%8.8s", src);
		
		String out = "";
		if (!StringUtil.isEmpty(extraData)) {
			JDES jd = new JDES();
			jd.SetKey(src.getBytes());
			try {
				out = new String(new BASE64Encoder().encode(jd.doECBEncrypt(
						extraData.getBytes("GBK"),
						extraData.getBytes("GBK").length)));
			} catch (Exception e) {
				throw e;
			}
		} else {
			out = extraData;
		}
		return out;
	}

	/**
	 * 解密扩展数据
	 * 
	 * @param src
	 * @param encryptExtraData
	 * @return
	 */
	public static String decryptExtraData(String src, String encryptExtraData)
			throws Exception {
		src = String.format("%8.8s", src);
		String out = "";
		if (!StringUtil.isEmpty(encryptExtraData)) {
			JDES jd = new JDES();
			jd.SetKey(src.getBytes());
			try {
				byte[] encryptByte = Base64Utils.decode(encryptExtraData);
				out = new String(jd.doECBDecrypt(encryptByte,
						encryptByte.length), "GBK");
			} catch (Exception e) {
				throw new RuntimeException("解密错误"+e.getMessage());
			}
		} else {
			out = encryptExtraData;
		}
		return out;
	}

	/**
	 * DES加密
	 * 
	 * @param
	 * @return
	 */
	public static String encryptData(String key, String data)
			throws Exception {
		key = String.format("%8.8s", key);
		String out = "";
		if (!StringUtil.isEmpty(data)) {
			JDES jd = new JDES();
			jd.SetKey(key.getBytes());
			try {
				out = new String(new BASE64Encoder().encode(jd.doECBEncrypt(
						data.getBytes("GBK"), data.getBytes("GBK").length)));
			} catch (Exception e) {
				throw e;
			}
		} else {
			out = data;
		}
		return out;
	}

	/**
	 * DES解密
	 * 
	 * @param
	 * @return
	 */
	public static String decryptData(String key, String data)
			throws Exception {
		key = String.format("%8.8s", key);
		String out = "";
		if (!StringUtil.isEmpty(data)) {
			JDES jd = new JDES();
			jd.SetKey(key.getBytes());
			try {
				byte[] encryptByte = Base64Utils.decode(data);
				out = new String(jd.doECBDecrypt(encryptByte,
						encryptByte.length), "GBK");
			} catch (Exception e) {
				throw e;
			}
		} else {
			out = data;
		}
		return out;
	}
}
