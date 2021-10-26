package com.eptok.yspay.opensdkjava.util;

import javax.xml.bind.DatatypeConverter;


/**
 * 
 * <P>BASE64编码解码工具包 </p> <p> 依赖javabase64-1.3.1.jar </P>
 * 
 * @version 1.0
 * @author 黄雄星（13077862552） 2014-1-16 上午8:44:44
 */
public class Base64Utils {

	/**
	 * <p> BASE64字符串解码为二进制数据 </p>
	 * 
	 * @param base64
	 * @return
	 * @throws Exception
	 */
	public static byte[] decode(String base64) {
		return DatatypeConverter.parseBase64Binary(base64);
	}

	/**
	 * <p> 二进制数据编码为BASE64字符串 </p>
	 * 
	 * @param bytes
	 * @return
	 * @throws Exception
	 */
	public static String encode(byte[] bytes) {
		return DatatypeConverter.printBase64Binary(bytes);
	}

}
