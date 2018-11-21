package com.nokia.logtools.date;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.springframework.util.StringUtils;

/**
 * 日期的常用方法类.
 *
 * @author
 */
public class DateUtil {

	public final static String DATE_FORMATE_HHMMSS = "HH:mm:ss";
	public final static String DATE_FORMATE_YYYY = "yyyy";
	public final static String DATE_FORMATE_YYYY_MM = "yyyy_MM";
	public final static String DATE_FORMATE_YYYY_MM_DD = "yyyy-MM-dd";
	public final static String DATE_FORMATE_YYYYMMDDHHMM = "yyyy-MM-dd HH:mm";
	public final static String DATE_FORMATE_YYYYMMDDHHMMSS = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 获得字符串类型的当前系统时间
	 * 
	 * @return 当前时间 String
	 */
	public static String getNow() {
		Date nowDate = new Date();
		SimpleDateFormat sformat = new SimpleDateFormat(DATE_FORMATE_YYYYMMDDHHMMSS);
		return sformat.format(nowDate);
	}

	/**
	 * 日期格式化
	 * 
	 * @param date
	 * @param formatType
	 * @return
	 */
	public static String formatter(Date date, String formatType) {
		if (StringUtils.isEmpty(formatType) || date == null) {
			return "";
		}
		SimpleDateFormat formatter = new SimpleDateFormat(formatType);
		return formatter.format(date);
	}

	/**
	 * 时间戳转日期
	 * 
	 * @param String
	 * @returnType Date
	 * @modification
	 */
	public static Date stampToDate(String s) {
		long lt = new Long(s);
		Date date = new Date(lt * 1000);
		return date;
	}

	/**
	 * 时间戳转日期
	 * 
	 * @param String
	 * @returnType String
	 * @modification
	 */
	public static String stampToString(String s) {
		long lt = new Long(s);
		Date date = new Date(lt * 1000);
		SimpleDateFormat sformat = new SimpleDateFormat(DATE_FORMATE_YYYYMMDDHHMMSS);
		return sformat.format(date);
	}

	/**
	 * 时间转时间戳
	 * 
	 * @param String
	 * @returnType String
	 * @modification
	 */
	public static String stringToStamp(String s) {
		String res;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMATE_YYYYMMDDHHMMSS);
		Date date = null;
		try {
			date = simpleDateFormat.parse(s);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		long ts = date.getTime();
		res = String.valueOf(ts / 1000);
		return res;
	}

	/**
	 * 时间转时间戳
	 * 
	 * @param Date
	 * @returnType String
	 * @modification
	 */
	public static String dateToStamp(Date date) {
		long ts = date.getTime();
		String res = String.valueOf(ts / 1000);
		return res;
	}

	/**
	 * 取得格式化效果的系统日期！ 格式如：yyyy-MM-dd kk:mm:ss
	 */
	public final static String getFormateDate(String formate) {
		SimpleDateFormat f = new SimpleDateFormat(formate, Locale.US);
		return f.format(new Date());
	}

	/**
	 * 获取默认格式的日期和时间.形如：2018-7-4 12:23:54
	 *
	 * @return
	 */
	public final static String getDateTime() {
		return getFormateDate(DATE_FORMATE_YYYYMMDDHHMMSS);
	}

	/**
	 * 获取默认格式的日期.形如：2018-7-4
	 *
	 * @return
	 */
	public final static String getDate() {
		return getFormateDate(DATE_FORMATE_YYYY_MM_DD);
	}

	/**
	 * 获取当前的年份
	 *
	 * @return
	 */
	public final static String getYear() {
		return getFormateDate(DATE_FORMATE_YYYY);
	}

	/**
	 * 获取当前的月份
	 *
	 * @return
	 */
	public final static String getMonth() {
		return getFormateDate("MM");
	}

	/**
	 * 获取当前的日期
	 *
	 * @return
	 */
	public final static String getDay() {
		return getFormateDate("dd");
	}

	/**
	 * 日期运算
	 *
	 * @param date
	 * @param count
	 * @param formatType
	 * @param field
	 *            YEAR = 1 MONTH = 2 WEEK_OF_YEAR = 3 WEEK_OF_MONTH = 4 MINUTE = 12
	 * @return
	 */
	public static String dateOperate(Date date, int count, String formatType, int field) {
		if (StringUtils.isEmpty(formatType) || date == null) {
			return "";
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(field, count);
		Date afterDate = (Date) cal.getTime();
		cal.setTime(new Date(System.currentTimeMillis()));
		return formatter(afterDate, formatType);
	}

	/**
	 * 获取当前日期时间，参数表示在此基础上的偏差，参数依次表示年、月、日、时、分、秒。 为正则表示在此日期上加、为负则表示在此日期上减。
	 *
	 * @param deviation
	 * @return
	 */
	public final static Date getNowDate(int... deviation) {
		return setDate(new Date(), deviation);
	}

	/**
	 * 在某一指定的日期基础上进行日期的偏差设置，参数意义同getNowDate
	 *
	 * @param date
	 * @param deviation
	 * @return
	 */
	public final static Date setDate(Date date, int... deviation) {
		Calendar cal = Calendar.getInstance(Locale.US);
		cal.setTime(date);
		if (deviation.length < 1) {
			return cal.getTime();
		}
		final int[] filed = { Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH, Calendar.HOUR_OF_DAY,
				Calendar.MINUTE, Calendar.SECOND };
		for (int i = 0; i < deviation.length; i++) {
			cal.add(filed[i], deviation[i]);
		}
		return cal.getTime();
	}

	/**
	 * 当月第一天
	 *
	 * @return
	 */
	public static String getFirstDay() {
		SimpleDateFormat df = new SimpleDateFormat(DATE_FORMATE_YYYY_MM_DD);
		Calendar calendar = Calendar.getInstance();
		Date theDate = calendar.getTime();
		GregorianCalendar gcLast = (GregorianCalendar) Calendar.getInstance();
		gcLast.setTime(theDate);
		gcLast.set(Calendar.DAY_OF_MONTH, 1);
		String dayFirst = df.format(gcLast.getTime());
		StringBuffer str = new StringBuffer().append(dayFirst).append(" 00:00:00");
		return str.toString();
	}

	/**
	 * 获取某月的第一天
	 *
	 * @throws ParseException
	 */
	public static String getFirstDay(String date) {
		SimpleDateFormat df = new SimpleDateFormat(DATE_FORMATE_YYYY_MM_DD);
		Date theDate = null;
		try {
			theDate = df.parse(date);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		GregorianCalendar gcLast = (GregorianCalendar) Calendar.getInstance();
		gcLast.setTime(theDate);
		gcLast.set(Calendar.DAY_OF_MONTH, 1);
		String dayFirst = df.format(gcLast.getTime());
		StringBuffer str = new StringBuffer().append(dayFirst).append(" 00:00:00");
		return str.toString();
	}

	/**
	 * 当月最后一天
	 *
	 * @return
	 */
	public static String getLastDay() {
		Calendar calendar = Calendar.getInstance();
		// 设置时间,当前时间不用设置
		// 设置日期为本月最大日期
		calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
		// 打印
		DateFormat format = new SimpleDateFormat(DATE_FORMATE_YYYY_MM_DD);
		return format.format(calendar.getTime());
	}

	/**
	 * 某月的最后一天 首先要获取月份
	 *
	 * @return
	 * @throws ParseException
	 */
	public static String getLastDay(String date) {
		Calendar calendar = Calendar.getInstance();
		// 设置时间,当前时间不用设置
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMATE_YYYY_MM_DD);
		try {
			calendar.setTime(sdf.parse(date));
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		// 设置日期为本月最大日期
		calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
		DateFormat format = new SimpleDateFormat(DATE_FORMATE_YYYY_MM_DD);
		return format.format(calendar.getTime());
	}

	/**
	 * 得到某个日期的前几月或者后几月
	 *
	 * @param dateStr
	 *            格式如 2013-10-17
	 * @param pos
	 *            位移几月
	 */
	public static String getMoveMonth(String dateStr, int pos) {
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMATE_YYYY_MM_DD);
		Date dt = sdf.parse(dateStr, new ParsePosition(0));
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(dt);
		rightNow.add(Calendar.MONTH, pos);
		Date dt1 = rightNow.getTime();
		return sdf.format(dt1);
	}

	/**
	 * 返回到两个时间段所有月份列表
	 *
	 * @throws ParseException
	 */
	public static String[] getMothList(String da1, String da2) throws ParseException {
		StringBuffer sb = new StringBuffer();
		// 定义起始日期
		Date d1 = new SimpleDateFormat(DATE_FORMATE_YYYY_MM).parse(da1);
		// 定义结束日期
		Date d2 = new SimpleDateFormat(DATE_FORMATE_YYYY_MM).parse(da2);
		// 定义日期实例
		Calendar dd = Calendar.getInstance();
		// 设置日期起始时间
		dd.setTime(d1);
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMATE_YYYY_MM);
		// 判断是否到结束日期
		while (dd.getTime().before(d2)) {
			String str = sdf.format(dd.getTime());
			sb.append(str + ",");
			// 进行当前日期月份加1
			dd.add(Calendar.MONTH, 1);
		}
		// 补全最后一个月
		sb.append(sdf.format(dd.getTime()));
		return sb.toString().split(",");
	}

	/**
	 * 获取到两个时间之间的所有周
	 **/
	public static String[] getWeeks(String da1, String da2) throws ParseException {
		Calendar cBegin = new GregorianCalendar();
		Calendar cEnd = new GregorianCalendar();
		StringBuffer sb = new StringBuffer();
		cBegin.setTime(convertToDate(da1));
		cEnd.setTime(convertToDate(da2));
		int count = 1;
		// 结束日期下滚一天是为了包含最后一天
		cEnd.add(Calendar.DAY_OF_YEAR, 1);
		while (cBegin.before(cEnd)) {
			if (cBegin.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
				sb.append("第" + count + "周,");
				count++;
			}
			cBegin.add(Calendar.DAY_OF_YEAR, 1);
		}
		return sb.toString().split(",");
	}

	/**
	 * 两个时间相差多少分钟
	 * 
	 * @param endDate
	 * @param nowDate
	 * @returnType int
	 * @modification
	 */
	public static int dateSubMini(Date endDate, Date nowDate) {
		long nd = 1000 * 60;
		int diff = Math.abs((int) (nowDate.getTime() - endDate.getTime()));
		return (int) (diff / nd);
	}

	/**
	 * 获取一个日期的月份
	 */
	public static int getMonth(Date time) {
		SimpleDateFormat st = new SimpleDateFormat("MM");
		return Integer.parseInt(st.format(time));
	}

	/**
	 * 把日期转换成字符串
	 */
	public static String convertToStr(Date time) {
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMATE_YYYY_MM_DD);
		return sdf.format(time);
	}

	/*** 把日期转换成字符串 指定格式 */
	public static String convertToStrwithformat(Date time, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(time);
	}

	/***
	 * 把字符串转换成 日期
	 *
	 * @throws ParseException
	 */
	public static Date convertToDate(String time) {
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMATE_YYYY_MM_DD);
		try {
			return sdf.parse(time);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	/***
	 * 把字符串转换成 日期 指定格式
	 *
	 * @throws ParseException
	 */
	public static Date convertToDate(String time, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			return sdf.parse(time);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	// 禁止实例化
	private DateUtil() {
	}
}
