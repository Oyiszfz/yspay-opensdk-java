package com.eptok.yspay.opensdkjava.util;


import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.util.logging.Logger;

/**
 * =================================================================
 * All rights reserved by SHENZHEN YINSHENG E-PAY CO.,LTD 2010-2020 
 * -----------------------------------------------------------------
 * This is not a free software! 
 * You cannot modify and use the program code without permission；
 * No reissue of program code is allowed for any purpose in any form
 * =================================================================
 * @ClassName: DesUtil
 * @Description: TODO
 * @author wx
 * @date 2018年9月11日15:45:35
 */
public class DesUtil {

	private static Logger log = Logger.getLogger("DesUtil");

	/**
	 * 备注：DES加密key只能是8位
	 * 加密扩展数据
	 * @param extraData
	 * @return
	 */
	public static String encryptExtraData(String key,String charset,String extraData) throws Exception {
		String out = "";
		log.info("before-encryptExtraData:" + extraData);
		log.info("encryptExtraData-key:" + key);
		try {
			if(!StringUtil.isEmpty(key) && key.length()>8){
				key = key.substring(0,8);
			}
			JDES jd = new JDES();
			jd.SetKey(key.getBytes(charset));
			out = new String(new BASE64Encoder().encode(jd.doECBEncrypt(
					extraData.getBytes(charset),
					extraData.getBytes(charset).length)));
		} catch (Exception e) {
			throw e;
		}
		return out;
	}

	/**
	 * 解密扩展数据
	 * 
	 * @return
	 */
	public static String decryptExtraData(String key,String charset,String encryptExtraData)
			throws Exception {
		String out = "";
		log.info("befor-decryptExtraData:" + encryptExtraData);
		log.info("decryptExtraData-key:" + key);
		try {
			if(!StringUtil.isEmpty(key) && key.length()>8){
				key = key.substring(0,8);
			}
			JDES jd = new JDES();
			jd.SetKey(key.getBytes(charset));
			byte[] encryptByte = new BASE64Decoder().decodeBuffer(encryptExtraData);
			out = new String(jd.doECBDecrypt(encryptByte, encryptByte.length), charset);
		} catch (Exception e) {
			throw e;
		}
		return out;
	}
}
