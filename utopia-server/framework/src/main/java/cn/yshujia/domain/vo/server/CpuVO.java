package cn.yshujia.domain.vo.server;

import cn.yshujia.utils.ArithUtils;
import lombok.Setter;


@Setter
public class CpuVO {
	
	/**
	 * 核心数
	 */
	private int cpuNum;
	
	/**
	 * CPU总的使用率
	 */
	private double total;
	
	/**
	 * CPU系统使用率
	 */
	private double sysUsed;
	
	/**
	 * CPU用户使用率
	 */
	private double userUsed;
	
	/**
	 * CPU当前等待率
	 */
	private double wait;
	
	/**
	 * CPU当前空闲率
	 */
	private double free;
	
	public int getCpuNum() {
		return cpuNum;
	}
	
	
	public double getTotal() {
		return ArithUtils.round(ArithUtils.mul(total, 100), 2);
	}
	
	
	public double getSysUsed() {
		return ArithUtils.round(ArithUtils.mul(sysUsed / total, 100), 2);
	}
	
	
	public double getUserUsed() {
		return ArithUtils.round(ArithUtils.mul(userUsed / total, 100), 2);
	}
	
	
	public double getWait() {
		return ArithUtils.round(ArithUtils.mul(wait / total, 100), 2);
	}
	
	
	public double getFree() {
		return ArithUtils.round(ArithUtils.mul(free / total, 100), 2);
	}
	
}
