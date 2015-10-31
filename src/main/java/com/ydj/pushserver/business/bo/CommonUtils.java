package com.ydj.pushserver.business.bo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Random;


public class CommonUtils {

	/**
	 * 判断是否为空字符串
	 * 
	 * @param src
	 * @return
	 */
	public static boolean isEmptyString(String src) {

		return src == null || src.trim().length() < 1;

	}

	/**
	 * 判断是否整数
	 * 
	 * @param src
	 * @return
	 */
	public static boolean isInteger(String src) {

		try {

			Integer.parseInt(src);

		} catch (Exception e) {

			return false;

		}

		return true;
	}

	/**
	 * 判断是否Long型
	 * 
	 * @param src
	 * @return
	 */
	public static boolean isLong(String src) {

		try {

			Long.parseLong(src);

		} catch (Exception e) {

			return false;

		}

		return true;
	}

	/**
	 * 将字符串转换为Int型:
	 * 
	 * 此方法将不会抛出NumberFormatException和NullPointerException，出现无法转换时， 则返回-1
	 * 
	 * @param s
	 * @return
	 */
	public static int parseInt(String s) {

		return parseInt(s, -1);

	}

	public static int parseInt(String s, int defaultValue) {

		try {

			return Integer.parseInt(s);

		} catch (Exception e) {

			return defaultValue;

		}
	}

	public static float parseFloat(String s, float defaultValue) {

		try {

			return Float.parseFloat(s);

		} catch (Exception e) {
			return defaultValue;

		}
	}

	/**
	 * 将字符串转换为Long型:
	 * 
	 * 此方法将不会抛出NumberFormatException和NullPointerException，出现无法转换时， 则返回-1
	 * 
	 * @param s
	 * @return
	 */
	public static long parseLong(String s) {

		return parseLong(s, -1l);

	}

	/**
	 * 将字符串转换为Long型:
	 * 
	 * 此方法将不会抛出NumberFormatException和NullPointerException，出现无法转换时，
	 * 则返回defaultValue
	 * 
	 * @param s
	 * @param defaultValue
	 * @return
	 */
	public static long parseLong(String s, long defaultValue) {

		try {

			return Long.parseLong(s);

		} catch (Exception e) {

			return defaultValue;

		}
	}

	/**
	 * 将时间戳转换为格式化的字符串
	 * 
	 * @param format
	 * @param millis
	 * @return
	 */
	public static String getDateString(String format, long millis) {

		Date date = new Date(millis);

		if (isEmptyString(format))
			format = "yyyy-MM-dd HH:mm:ss";

		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.format(date);

	}

	/**
	 * 返回指定日期格式字符串的时间戳
	 * 
	 * @param format
	 *            eg: yyyy-MM-dd HH:mm
	 * @param time
	 * @return
	 */
	public static long parseDateString(String format, String time) {

		try {

			return new SimpleDateFormat(format).parse(time).getTime();

		} catch (Exception e) {

			return 0;

		}

	}

	/**
	 * 判断时间戳是否为今天
	 * 
	 * @param millis
	 * @return
	 * @throws Exception
	 */
	public static boolean isToday(long millis) throws Exception {

		long oneday = 24 * 60 * 60 * 1000l;
		long utc = 8 * 60 * 60 * 1000l;

		return (System.currentTimeMillis() + utc) / oneday - (millis + utc)
				/ oneday == 0;

	}

	/**
	 * 返回n天后的日期时间戳
	 * 
	 * @param days
	 *            可以为任何整数，负数表示前N天，正数表示后N天
	 * @return TimeInMillis
	 */
	public static long getFutureInMillis(long current, int days) {

		current = current > 0 ? current : System.currentTimeMillis();

		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(current);

		c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) + days);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);

		return c.getTimeInMillis();

	}

	public static String getRandomString() {
		return getRandomString(12);
	}

	/**
	 * 获取一个类BASE64编码的随机字符串
	 * 
	 * @param num
	 * @return
	 */
	public static String getRandomString(int num) {
		Random rd = new Random();
		StringBuilder content = new StringBuilder(num);

		for (int i = 0; i < num; i++) {
			int n;
			while (true) {
				n = rd.nextInt('z' + 1);
				if (n >= '0' && n <= '9')
					break;
				if (n >= 'a' && n <= 'z')
					break;
				if (n >= 'A' && n <= 'Z')
					break;
			}
			content.append((char) n);
		}
		return content.toString();
	}

	/**
	 * 获取一个从min到max的随机整数
	 * 
	 * @param min
	 * @param max
	 * @return [min, max]
	 */
	public static int getRandomNumber(int min, int max) {

		if (min >= max)
			return max;

		if (min + 1 == max)
			return min;

		// long to int
		return (int) Math.round(Math.random() * (max - min) + min);

	}




	/**
	 * String数组转化成以','分割的字符串
	 * 
	 * @param str
	 * @return
	 */
	public static String arrayToString(String[] strArray) {

		if (strArray == null || strArray.length < 1)
			return "";

		String s = Arrays.toString(strArray).replaceFirst("\\[", "");

		if (isEmptyString(s))
			return "";

		return s.substring(0, s.length() - 1);

	}

	/**
	 * List转化成以','分割的字符串
	 * 
	 * @param str
	 * @return
	 */
	public static <E> String collectionToString(Collection<E> collection) {

		if (collection == null || collection.isEmpty())
			return "";

		String s = collection.toString().replaceFirst("\\[", "");

		if (isEmptyString(s))
			return "";

		return s.substring(0, s.length() - 1);

	}

	/**
	 * 末尾开始截取字符串
	 * 
	 * @param str
	 * @return
	 */
	public static String subString(String str, int len) {

		if (CommonUtils.isEmptyString(str))
			return "";

		if (str.length() < len)
			throw new IllegalArgumentException(
					"The input string's length must>" + len);

		return str.substring(str.length() - len);

	}

	/**
	 * 按指定长度截取字符串
	 * 
	 * @param src
	 * @param length
	 * @return
	 */
	public static String intercept(String src, int length) {

		if (isEmptyString(src))
			return "";

		int len = src.length();
		int lng = src.getBytes().length;

		return lng < length ? src : src.substring(0,
				(int) ((length - 6) * len / lng)) + "…";
	}

	/**
	 * 获取传入小数的货币表现形式
	 * 
	 * @param money
	 * @return
	 */
	public static String formatMoney(double money) {

		if ((int) money == money)
			return Integer.toString((int) money);

		return formatMoney("0.00", money);

	}

	/**
	 * 获取传入小数的货币表现形式
	 * 
	 * @param format
	 *            指定的表现形式
	 * @param money
	 * @return
	 */
	public static String formatMoney(String format, double money) {

		DecimalFormat decimalFormat = new DecimalFormat(format);

		return decimalFormat.format(money);

	}


	/**
	 * 将内容以UTF8编码方式写入目标文件中,注意:此方法将覆盖原文件.
	 * 
	 * @param file
	 * @param content
	 * @return
	 * @throws IOException
	 */
	public static void writeFile(String file, String content)
			throws IOException {
		writeFile(file, content, "UTF-8");
	}

	/**
	 * 将内容以指定的编码方式写入目标文件中,注意:此方法将覆盖原文件.
	 * 
	 * @param file
	 * @param content
	 * @param charset
	 * @return
	 * @throws IOException
	 */
	public static void writeFile(String file, String content, String charset)
			throws IOException {

		if (isEmptyString(file))
			throw new IOException("Can't write to an empty target file");

		if (isEmptyString(content))
			throw new IOException(
					"Can't write to file for the empty input param content");

		try {
			Charset.isSupported(charset);
		} catch (Exception e) {
			throw new IOException("Unsupported charset name: " + charset);
		}

		File f = new File(file);

		if (!f.exists()) {

			if (f.getParentFile() != null)
				f.getParentFile().mkdirs();

			f.createNewFile();

		}

		FileOutputStream fos = null;

		try {

			fos = new FileOutputStream(f);
			fos.write(content.getBytes(charset));

		} catch (IOException e) {

			throw e;

		} finally {

			if (null != fos)
				fos.close();

		}
	}

	/**
	 * 拷贝文件内容到目标文件中,注意:此方法将覆盖原目标文件.
	 * 
	 * @param sourceFile
	 * @param targetFile
	 * @throws IOException
	 */
	public static void copyFile(String sourceFile, String targetFile)
			throws IOException {

		File s = new File(sourceFile);
		if (!s.exists())
			throw new IOException("source file is not exists: " + sourceFile);

		File f = new File(targetFile);

		if (!f.exists()) {

			if (f.getParentFile() != null)
				f.getParentFile().mkdirs();

			f.createNewFile();

		}

		FileInputStream fis = new FileInputStream(s);
		FileOutputStream fos = new FileOutputStream(f);

		byte[] buf = new byte[1024];
		int i = 0;

		try {
			while ((i = fis.read(buf)) != -1) {
				fos.write(buf, 0, i);
			}
		} finally {
			fis.close();
			fos.close();
		}

	}





	/**
	 * 输入的年月日是否为正确日期
	 * 
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	public static boolean isRealDate(int year, int month, int day) {

		if (year < 0 || month < 1 || day < 1)
			return false;

		if (month > 12 || day > 31)
			return false;

		if (day > maxDay[month - 1])
			return false;

		if (month == 2 && day > 28) {

			if (year % 4 != 0 || year % 100 == 0) {
				return false;
			}
		}

		return true;
	}


	/**
	 * 最大日期
	 */
	private final static int[] maxDay = new int[] { 31, 29, 31, 30, 31, 30, 31,
			31, 30, 31, 30, 31 };

	// TEST
	public static void main(String args[]) {
		/*
		 * System.out.println(getPhoneNumber("+86 0186-1892-0316", true));
		 * System.out.println(getPhoneNumber("+861338160-0316", true));
		 * System.out.println(getPhoneNumber("+86 021 58428042", true));
		 * 
		 * System.err.println(getDateString(null,
		 * getFutureInMillis(System.currentTimeMillis(), 1)));
		 * 
		 * System.out.println(isEmail("xmdlyqc@163.com"));
		 * 
		 * System.err.println(parseDateString("yyyyMMdd", "20120730"));
		 */
	}
}
