package com.ydj.pushserver.business.bo;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * @description
 * 
 * @version 1.0
 * @create_time 2010-3-18
 */
@SuppressWarnings("restriction")
public class StringUtils {

	public static void main(String[] args) {
		
	}

	/**
	 * 全角字符转半角
	 * 
	 * @param QJstr
	 * @return
	 */
	public static final String QjToBj(String QJstr) {
		String outStr = "";
		String Tstr = "";
		byte[] b = null;

		for (int i = 0; i < QJstr.length(); i++) {
			try {
				Tstr = QJstr.substring(i, i + 1);
				b = Tstr.getBytes("unicode");
			} catch (java.io.UnsupportedEncodingException e) {
				e.printStackTrace();
			}

			if (b[2] == -1) {
				b[3] = (byte) (b[3] + 32);
				b[2] = 0;

				try {
					outStr = outStr + new String(b, "unicode");
				} catch (java.io.UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			} else
				outStr = outStr + Tstr;
		}

		return outStr;
	}

	/**
	 * 半角字符转全角
	 * 
	 * @param QJstr
	 * @return
	 */
	public static String BjToQj(String QJstr) {
		String outStr = "";
		String Tstr = "";
		byte[] b = null;

		for (int i = 0; i < QJstr.length(); i++) {
			try {
				Tstr = QJstr.substring(i, i + 1);
				b = Tstr.getBytes("unicode");
			} catch (java.io.UnsupportedEncodingException e) {
				e.printStackTrace();
			}

			if (b[2] == 0) {
				b[3] = (byte) (b[3] - 32);
				b[2] = -1;

				try {
					outStr = outStr + new String(b, "unicode");
				} catch (java.io.UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			} else
				outStr = outStr + Tstr;
		}

		return outStr;
	}

	/**
	 * 将字符串转换成BASE64编码
	 * 
	 * @param src
	 * @return
	 */
	public static String base64Encode(String src) {

		return CommonUtils.isEmptyString(src) ? "" : base64Encode(src
				.getBytes());

	}

	/**
	 * 将字符串转换成BASE64编码
	 * 
	 * @param byte[]
	 * @return
	 */
	public static String base64Encode(byte b[]) {

		return (null == b || b.length < 1) ? "" : new BASE64Encoder().encode(b);

	}

	/**
	 * 还原BASE64编码的字符串
	 * 
	 * @param src
	 * @return
	 */
	public static String base64Decode(String src) {

		try {

			return new String(new BASE64Decoder().decodeBuffer(src));

		} catch (Exception e) {

			return "";

		}

	}

	/**
	 * 基于32进制对整数进行简单加密
	 * 
	 * @param input
	 * @return
	 */
	public static String base32Encrypt(long input) {

		int loop = Long.toBinaryString(input).length() / 5 + 1;
		int codeBase = (int) (input % 8); // new Random().nextInt(7);
		StringBuilder ret = new StringBuilder();
		for (int i = 0; i < loop; i++)
			ret.append(base32_array[codeBase].charAt(Integer.parseInt(Long
					.toString((input >> (5 * i) & 31)))));

		return ret.toString() + code_base.charAt(codeBase);
	}

	/**
	 * 提供被32进制加密整数的还原
	 * 
	 * @param input
	 * @return
	 */
	public static long base32Decrypt(String input) {

		long ret = 0l;
		try {
			int codeBase = code_base.indexOf(input.charAt(input.length() - 1));
			for (int i = input.length() - 2; i > -1; i--) {
				ret <<= 5;
				ret += base32_array[codeBase].indexOf(input.charAt(i)) & 31;
			}

		} catch (Exception e) {
			System.err.println(e);
		}
		return ret;
	}

	private static final String[] base32_array = {
			"iq1sb4jgphn026c378rflakdtev9umo5",
			"e02qchj1g84ml9vnaodurktib5fs67p3",
			"9q2vetf3jgnrlibm1ak6d8ohsc540p7u",
			"vslqk0fbdcn83mj7epho51iua42t69rg",
			"n0ip5e7hq98jkdbmc2rvtgas643o1flu",
			"mvkir827dq9jaoucn06getls14pb35hf",
			"gp6c35sfe4u7rn0thkja21dq8bli9vom",
			"abde2k4gl05pqrnc8v19fi67s3mjutoh" };

	private static final String code_base = "ds8fj9er";

}
