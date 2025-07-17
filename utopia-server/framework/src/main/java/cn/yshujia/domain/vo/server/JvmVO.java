package cn.yshujia.domain.vo.server;

import cn.yshujia.utils.ArithUtils;
import cn.yshujia.utils.TimeUtils;
import lombok.Setter;

import java.lang.management.ManagementFactory;
import java.util.Date;

@Setter
public class JvmVO {
	
	/**
	 * 当前JVM占用的内存总数(M)
	 */
	private double total;
	
	/**
	 * JVM最大可用内存总数(M)
	 */
	private double max;
	
	/**
	 * JVM空闲内存(M)
	 */
	private double free;
	
	/**
	 * JDK版本
	 */
	private String version;
	
	/**
	 * JDK路径
	 */
	private String home;
	
	private String startTime;
	
	private String runTime;
	
	private String inputArgs;
	
	
	public double getTotal() {
		return ArithUtils.div(total, (1024 * 1024), 2);
	}
	
	
	public double getMax() {
		return ArithUtils.div(max, (1024 * 1024), 2);
	}
	
	
	public double getFree() {
		return ArithUtils.div(free, (1024 * 1024), 2);
	}
	
	
	public double getUsed() {
		return ArithUtils.div(total - free, (1024 * 1024), 2);
	}
	
	public double getUsage() {
		return ArithUtils.mul(ArithUtils.div(total - free, total, 4), 100);
	}
	
	/**
	 * 获取JDK名称
	 */
	public String getName() {
		return ManagementFactory.getRuntimeMXBean().getVmName();
	}
	
	public String getVersion() {
		return version;
	}
	
	
	public String getHome() {
		return home;
	}
	
	/**
	 * JDK启动时间
	 */
	public String getStartTime() {
		return TimeUtils.getFormatTime(ManagementFactory.getRuntimeMXBean().getStartTime());
	}
	
	/**
	 * JDK运行时间
	 */
	public String getRunTime() {
		return TimeUtils.timeDistance(new Date(), TimeUtils.getDateTime(ManagementFactory.getRuntimeMXBean().getStartTime()));
	}
	
	/**
	 * 运行参数
	 */
	public String getInputArgs() {
		return ManagementFactory.getRuntimeMXBean().getInputArguments().toString();
	}
	
}
