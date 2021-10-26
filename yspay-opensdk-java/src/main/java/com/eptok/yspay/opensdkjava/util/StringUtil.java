package com.eptok.yspay.opensdkjava.util;

import java.util.*;

public class StringUtil {

	/**
	 * 判断字符穿是否为空
	 * 当字符串为空或空字符串时返回true 否则返回false
	 * @param str
	 * @return
	 */
	public static final boolean isEmpty(String str)
	{
		if (str == null || str.trim().length() == 0)
		{
			return true;
		}
		return false;

	}
	
	/**
	 * 判断字符数组是否为空
	 */
	public static boolean areNotEmpty(String... values) {
		boolean result = true;
		if (values == null || values.length == 0) {
			result = false;
		} else {
			for (String value : values) {
				result &= !isEmpty(value);
			}
		}
		return result;
	}


	/**
	 * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
	 *
	 * @param params 需要排序并参与字符拼接的参数组
	 * @return 拼接后字符串
	 */
	public static String createLinkString(Map<String, String> params) {
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
	 * map 转成 string
	 * @param map
	 * @return
	 */
	public static String mapToString(Map<String, String> map) {
		SortedMap<String, String> sortedMap = new TreeMap<String, String>(map);

		StringBuilder sb = new StringBuilder();

		for (Map.Entry<String, String> entry : sortedMap.entrySet()) {
			if (StringUtil.isEmpty(entry.getValue())) {
				continue;
			}
			sb.append(entry.getKey()).append('=').append(entry.getValue()).append('&');
		}
		sb.deleteCharAt(sb.length() - 1);

		return sb.length() == 0 ? "" : sb.toString();
	}
}
