package cn.yshujia.utils;

import cn.hutool.core.date.DateTime;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * @author yshujia
 * @description 时间工具
 * @create 2024/4/23
 */

@Component
public class TimeUtils {
	
	private static final String PARALLEL_TIME = "yyyyMMddHHmmssSSS";
	
	private static final String PARALLEL_DATE = "yyyyMMdd";
	
	private static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
	
	private static final String YYYY_MM_DD = "yyyy-MM-dd";
	
	
	private static final SimpleDateFormat PARALLEL_TIME_FORMAT = new SimpleDateFormat(PARALLEL_TIME);
	
	private static final SimpleDateFormat PARALLEL_DATE_FORMAT = new SimpleDateFormat(PARALLEL_DATE);
	
	private static final SimpleDateFormat FORMAT_DATE_FORMAT = new SimpleDateFormat(YYYY_MM_DD);
	
	private static final SimpleDateFormat FORMAT_TIME_FORMAT = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
	
	// 线程安全的 DateTimeFormatter 单例
	private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern(YYYY_MM_DD_HH_MM_SS);
	
	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(YYYY_MM_DD);
	
	
	public static Long getTimestamp() {
		//时间戳
		return new Date().getTime();
	}
	
	public static Date getDateTime(long time) {
		return new Date(time);
	}
	
	public static Date getDateTime(String date) {
		// 参数校验
		if (date == null || date.trim().isEmpty()) {
			throw new IllegalArgumentException("Input date string cannot be null or empty");
		}
		try {
			// 尝试解析完整时间格式
			LocalDateTime dateTime = LocalDateTime.parse(date, DATETIME_FORMATTER);
			return Timestamp.valueOf(dateTime); // 转换为 Date 类型
		} catch (DateTimeParseException e1) {
			try {
				// 尝试解析日期格式
				LocalDate localDate = LocalDate.parse(date, DATE_FORMATTER);
				return java.sql.Date.valueOf(localDate); // 转换为 Date 类型
			} catch (DateTimeParseException e2) {
				// 抛出自定义异常，包含详细信息
				throw new IllegalArgumentException("Invalid date format: " + date + ". Expected formats: " + YYYY_MM_DD_HH_MM_SS + " or " + YYYY_MM_DD, e2);
			}
		}
	}
	
	public static String getParallelTime() {
		//时间并联格式 （精确到毫秒）
		return PARALLEL_TIME_FORMAT.format(new Date());
	}
	
	public static String getParallelDate() {
		//时间并联格式 （精确到毫秒）
		return PARALLEL_DATE_FORMAT.format(new Date());
	}
	
	public static String getParallelTime(String date) {
		return PARALLEL_TIME_FORMAT.format(date);
	}
	
	public static String getParallelTime(Date date) {
		//时间并联格式 （精确到毫秒）
		return PARALLEL_TIME_FORMAT.format(date);
	}
	
	public static String getFormatTime() {
		return FORMAT_TIME_FORMAT.format(new Date());
	}
	
	public static String getFormatTime(long time) {
		return FORMAT_TIME_FORMAT.format(time);
	}
	
	public static String getFormatTime(String date) {
		return FORMAT_TIME_FORMAT.format(date);
	}
	
	public static String getFormatTime(Date date) {
		//时间正常格式
		return FORMAT_TIME_FORMAT.format(date);
	}
	
	public static String getFormatDate() {
		return FORMAT_DATE_FORMAT.format(new Date());
	}
	
	public static String getFormatDate(String date) {
		return FORMAT_DATE_FORMAT.format(date);
	}
	
	public static String getFormatDate(Date date) {
		//时间正常格式
		return FORMAT_DATE_FORMAT.format(date);
	}
	
	/**
	 * 获取指定日期年份
	 */
	public static int geYear(Date date) {
		return new DateTime(date).year();
	}
	
	/**
	 * 获取指定日期年份
	 */
	public static int getYear(Date date) {
		return new DateTime(date).year();
	}
	
	/**
	 * 获取指定日期月份
	 */
	public static int getMonth(Date date) {
		return new DateTime(date).month() + 1;
	}
	
	/**
	 * 获取指定日期天数（号）
	 */
	public static int getDay(Date date) {
		return new DateTime(date).dayOfMonth();
	}
	
	/**
	 * 获取当前年
	 */
	public static int getNowYear() {
		Calendar d = Calendar.getInstance();
		return d.get(Calendar.YEAR);
	}
	
	/**
	 * 获取当前月份
	 */
	public static int getNowMonth() {
		Calendar d = Calendar.getInstance();
		return d.get(Calendar.MONTH) + 1;
	}
	
	/**
	 * 获取当月天数
	 */
	public static int getNowMonthDay() {
		Calendar d = Calendar.getInstance();
		return d.getActualMaximum(Calendar.DATE);
	}
	
	/**
	 * 比较两日期大小
	 */
	public static Boolean compareDate(Date big, Date small) {
		if (null == big || null == small) {
			return false;
		}
		return getParallelTime(big).compareToIgnoreCase(getParallelTime(small)) > 0;
	}
	
	/**
	 * 比较两日期是否相等
	 *
	 * @param date      日期字符串
	 * @param otherDate 另一个日期字符串
	 * @return 比较两日期大小。如果date==otherDate则返回true,否则false
	 */
	public static Boolean compareEqual(String date, String otherDate) {
		if (null == date || null == otherDate) {
			return false;
		}
		return getParallelTime(date).equalsIgnoreCase(getParallelTime(otherDate));
	}
	
	/**
	 * 比较两日期是否相等
	 *
	 * @param date      日期
	 * @param otherDate 另一个日期
	 * @return 比较两日期大小。如果date==otherDate则返回true,否则false
	 */
	public static Boolean compareEqual(Date date, Date otherDate) {
		if (null == date || null == otherDate) {
			return false;
		}
		return getParallelTime(date).equalsIgnoreCase(getParallelTime(otherDate));
	}
	
	/**
	 * 比较两日期是否在同一天
	 *
	 * @param date      日期字符串
	 * @param otherDate 另一个日期字符串
	 * @return 比较两日期大小。如果date==otherDate则返回true,否则false
	 */
	public static Boolean compareEqualDay(String date, String otherDate) {
		int day = new DateTime(date).dayOfYear();
		int day1 = new DateTime(otherDate).dayOfYear();
		return day == day1;
	}
	
	/**
	 * 比较两日期是否在同一天
	 *
	 * @param date      日期
	 * @param otherDate 另一个日期
	 * @return 比较两日期大小。如果date==otherDate则返回true,否则false
	 */
	public static Boolean compareEqualDay(Date date, Date otherDate) {
		int day = new DateTime(date).dayOfYear();
		int day1 = new DateTime(otherDate).dayOfYear();
		return day == day1;
	}
	
	/**
	 * @param monty: [monty] * @return Date
	 * @author yshujia
	 * @description 获取提前多少个月
	 * @create 2024/4/23 12:04
	 */
	public static Date getFirstMonth(int monty) {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, -monty);
		return c.getTime();
	}
	
	/**
	 * 计算时间差
	 *
	 * @param endDate   最后时间
	 * @param startTime 开始时间
	 * @return 时间差（天/小时/分钟）
	 */
	public static String timeDistance(Date endDate, Date startTime) {
		long nd = 1000 * 24 * 60 * 60;
		long nh = 1000 * 60 * 60;
		long nm = 1000 * 60;
		// long ns = 1000;
		// 获得两个时间的毫秒时间差异
		long diff = endDate.getTime() - startTime.getTime();
		// 计算差多少天
		long day = diff / nd;
		// 计算差多少小时
		long hour = diff % nd / nh;
		// 计算差多少分钟
		long min = diff % nd % nh / nm;
		// 计算差多少秒//输出结果
		// long sec = diff % nd % nh % nm / ns;
		return day + "天" + hour + "小时" + min + "分钟";
	}
	
}
