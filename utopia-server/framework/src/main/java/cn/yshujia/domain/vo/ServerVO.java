package cn.yshujia.domain.vo;

import cn.yshujia.domain.vo.server.*;
import cn.yshujia.utils.ArithUtils;
import cn.yshujia.utils.RequestUtils;
import lombok.Data;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.CentralProcessor.TickType;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;
import oshi.util.Util;

import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

/**
 * @author yshujia
 * @create 2024/4/23
 * @description 服务器信息响应对象
 */

@Data
public class ServerVO {
	
	private static final int OSHI_WAIT_SECOND = 1000;
	
	/**
	 * CPU相关信息
	 */
	private CpuVO cpuVO = new CpuVO();
	
	/**
	 * 內存相关信息
	 */
	private MemVO memVO = new MemVO();
	
	/**
	 * JVM相关信息
	 */
	private JvmVO jvmVO = new JvmVO();
	
	/**
	 * 服务器相关信息
	 */
	private SysVO sysVO = new SysVO();
	
	/**
	 * 磁盘相关信息
	 */
	private List<SysFileVO> sysFilesVO = new LinkedList<SysFileVO>();
	
	
	public void copyTo() throws Exception {
		SystemInfo si = new SystemInfo();
		HardwareAbstractionLayer hal = si.getHardware();
		setCpuInfo(hal.getProcessor());
		
		setMemInfo(hal.getMemory());
		
		setSysInfo();
		
		setJvmInfo();
		
		setSysFiles(si.getOperatingSystem());
	}
	
	/**
	 * 设置CPU信息
	 */
	private void setCpuInfo(CentralProcessor processor) {
		// CPU信息
		long[] prevTicks = processor.getSystemCpuLoadTicks();
		Util.sleep(OSHI_WAIT_SECOND);
		long[] ticks = processor.getSystemCpuLoadTicks();
		long nice = ticks[TickType.NICE.getIndex()] - prevTicks[TickType.NICE.getIndex()];
		long irq = ticks[TickType.IRQ.getIndex()] - prevTicks[TickType.IRQ.getIndex()];
		long softirq = ticks[TickType.SOFTIRQ.getIndex()] - prevTicks[TickType.SOFTIRQ.getIndex()];
		long steal = ticks[TickType.STEAL.getIndex()] - prevTicks[TickType.STEAL.getIndex()];
		long cSys = ticks[TickType.SYSTEM.getIndex()] - prevTicks[TickType.SYSTEM.getIndex()];
		long user = ticks[TickType.USER.getIndex()] - prevTicks[TickType.USER.getIndex()];
		long iowait = ticks[TickType.IOWAIT.getIndex()] - prevTicks[TickType.IOWAIT.getIndex()];
		long idle = ticks[TickType.IDLE.getIndex()] - prevTicks[TickType.IDLE.getIndex()];
		long totalCpu = user + nice + cSys + idle + iowait + irq + softirq + steal;
		cpuVO.setCpuNum(processor.getLogicalProcessorCount());
		cpuVO.setTotal(totalCpu);
		cpuVO.setSysUsed(cSys);
		cpuVO.setUserUsed(user);
		cpuVO.setWait(iowait);
		cpuVO.setFree(idle);
	}
	
	/**
	 * 设置内存信息
	 */
	private void setMemInfo(GlobalMemory memory) {
		memVO.setTotal(memory.getTotal());
		memVO.setUsed(memory.getTotal() - memory.getAvailable());
		memVO.setFree(memory.getAvailable());
	}
	
	/**
	 * 设置服务器信息
	 */
	private void setSysInfo() {
		Properties props = System.getProperties();
		sysVO.setComputerName(RequestUtils.getHostName());
		sysVO.setComputerIp(RequestUtils.getHostIp());
		sysVO.setOsName(props.getProperty("os.name"));
		sysVO.setOsArch(props.getProperty("os.arch"));
		sysVO.setUserDir(props.getProperty("user.dir"));
	}
	
	/**
	 * 设置Java虚拟机
	 */
	private void setJvmInfo() throws UnknownHostException {
		Properties props = System.getProperties();
		jvmVO.setTotal(Runtime.getRuntime().totalMemory());
		jvmVO.setMax(Runtime.getRuntime().maxMemory());
		jvmVO.setFree(Runtime.getRuntime().freeMemory());
		jvmVO.setVersion(props.getProperty("java.version"));
		jvmVO.setHome(props.getProperty("java.home"));
	}
	
	/**
	 * 设置磁盘信息
	 */
	private void setSysFiles(OperatingSystem os) {
		FileSystem fileSystem = os.getFileSystem();
		List<OSFileStore> fsArray = fileSystem.getFileStores();
		for (OSFileStore fs : fsArray) {
			long free = fs.getUsableSpace();
			long total = fs.getTotalSpace();
			long used = total - free;
			SysFileVO sysFile = new SysFileVO();
			sysFile.setDirName(fs.getMount());
			sysFile.setSysTypeName(fs.getType());
			sysFile.setTypeName(fs.getName());
			sysFile.setTotal(convertFileSize(total));
			sysFile.setFree(convertFileSize(free));
			sysFile.setUsed(convertFileSize(used));
			sysFile.setUsage(ArithUtils.mul(ArithUtils.div(used, total, 4), 100));
			sysFilesVO.add(sysFile);
		}
	}
	
	/**
	 * 字节转换
	 *
	 * @param size 字节大小
	 * @return 转换后值
	 */
	public String convertFileSize(long size) {
		long kb = 1024;
		long mb = kb * 1024;
		long gb = mb * 1024;
		if (size >= gb) {
			return String.format("%.1f GB", (float) size / gb);
		} else if (size >= mb) {
			float f = (float) size / mb;
			return String.format(f > 100 ? "%.0f MB" : "%.1f MB", f);
		} else if (size >= kb) {
			float f = (float) size / kb;
			return String.format(f > 100 ? "%.0f KB" : "%.1f KB", f);
		} else {
			return String.format("%d B", size);
		}
	}
	
}
