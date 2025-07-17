package cn.yshujia.utils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author yshujia
 * @create 2024/4/23
 * @description 获取用户IP地址
 */

@Data
public class RequestUtils {
	
	public final static String REGEX_0_255 = "(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]\\d|\\d)";
	
	// 匹配 ip
	public final static String REGEX_IP = "((" + REGEX_0_255 + "\\.){3}" + REGEX_0_255 + ")";
	
	// 匹配网段
	public final static String REGEX_IP_SEGA = "(" + REGEX_IP + "-" + REGEX_IP + ")";
	
	public final static String REGEX_IP_WILDCARD = "(((\\*\\.){3}\\*)|(" + REGEX_0_255 + "(\\.\\*){3})|(" + REGEX_0_255 + "\\." + REGEX_0_255 + ")(\\.\\*){2}" + "|((" + REGEX_0_255 + "\\.){3}\\*))";
	
	private static final String UNKNOWN_IP = "UNKNOWN";
	
	public static String getIp() {
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if (attributes != null) {
			return getIp(attributes.getRequest());
		}
		return null;
	}
	
	public static String getIp(HttpServletRequest request) {
		String[] headersToCheck = {"X-Forwarded-For", "Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_CLIENT_IP", "HTTP_X_FORWARDED_FOR"};
		String ipAddress = "UNKNOWN";
		for (String header : headersToCheck) {
			String currentIp = request.getHeader(header);
			if (currentIp != null && !currentIp.equalsIgnoreCase("UNKNOWN")) {
				ipAddress = currentIp;
				if ("127.0.0.1".equals(ipAddress) || "0:0:0:0:0:0:0:1".equals(ipAddress))
					try {
						ipAddress = InetAddress.getLocalHost().getHostAddress();
					} catch (UnknownHostException e) {
						return UNKNOWN_IP;
					}
				break;
			}
		}
		return ipAddress.equalsIgnoreCase("UNKNOWN") ? request.getRemoteAddr() : ipAddress;
	}
	
	public static String getParameter(String name) {
		return getReq().getParameter(name);
	}
	
	public static HttpServletRequest getReq() {
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = null;
		if (attributes != null) {
			request = attributes.getRequest();
		}
		assert request != null;
		return request;
	}
	
	public static boolean internalIp(String ip) {
		byte[] adder = textToNumericFormatV4(ip);
		return (internalIp(adder) || "127.0.0.1".equals(ip));
	}
	
	private static boolean internalIp(byte[] address) {
		if (StringUtils.isNull(address) || address.length < 2) {
			return true;
		}
		final byte b0 = address[0];
		final byte b1 = address[1];
		// 10.x.x.x/8
		final byte SECTION_1 = 0x0A;
		// 172.16.x.x/12
		final byte SECTION_2 = (byte) 0xAC;
		final byte SECTION_3 = (byte) 0x10;
		final byte SECTION_4 = (byte) 0x1F;
		// 192.168.x.x/16
		final byte SECTION_5 = (byte) 0xC0;
		final byte SECTION_6 = (byte) 0xA8;
		switch (b0) {
			case SECTION_1:
				return true;
			case SECTION_2:
				if (b1 >= SECTION_3 && b1 <= SECTION_4) {
					return true;
				}
			case SECTION_5:
				if (b1 == SECTION_6) {
					return true;
				}
			default:
				return false;
		}
	}
	
	public static byte[] textToNumericFormatV4(String text) {
		if (text.isEmpty())
			return null;
		byte[] bytes = new byte[4];
		String[] elements = text.split("\\.", -1);
		try {
			long l;
			int i;
			switch (elements.length) {
				case 1:
					l = Long.parseLong(elements[0]);
					if (l < 0L || l > 4294967295L)
						return null;
					bytes[0] = (byte) (int) (l >> 24L & 0xFFL);
					bytes[1] = (byte) (int) ((l & 0xFFFFFFL) >> 16L & 0xFFL);
					bytes[2] = (byte) (int) ((l & 0xFFFFL) >> 8L & 0xFFL);
					bytes[3] = (byte) (int) (l & 0xFFL);
					return bytes;
				case 2:
					l = Integer.parseInt(elements[0]);
					if (l < 0L || l > 255L)
						return null;
					bytes[0] = (byte) (int) (l & 0xFFL);
					l = Integer.parseInt(elements[1]);
					if (l < 0L || l > 16777215L)
						return null;
					bytes[1] = (byte) (int) (l >> 16L & 0xFFL);
					bytes[2] = (byte) (int) ((l & 0xFFFFL) >> 8L & 0xFFL);
					bytes[3] = (byte) (int) (l & 0xFFL);
					return bytes;
				case 3:
					for (i = 0; i < 2; i++) {
						l = Integer.parseInt(elements[i]);
						if (l < 0L || l > 255L)
							return null;
						bytes[i] = (byte) (int) (l & 0xFFL);
					}
					l = Integer.parseInt(elements[2]);
					if (l < 0L || l > 65535L)
						return null;
					bytes[2] = (byte) (int) (l >> 8L & 0xFFL);
					bytes[3] = (byte) (int) (l & 0xFFL);
					return bytes;
				case 4:
					for (i = 0; i < 4; i++) {
						l = Integer.parseInt(elements[i]);
						if (l < 0L || l > 255L)
							return null;
						bytes[i] = (byte) (int) (l & 0xFFL);
					}
					return bytes;
			}
			return null;
		} catch (NumberFormatException e) {
			return null;
		}
	}
	
	public static String getHostIp() {
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException unknownHostException) {
			return "127.0.0.1";
		}
	}
	
	public static String getHostName() {
		try {
			return InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException unknownHostException) {
			return "未知";
		}
	}
	
	public static String getMultistageReverseProxyIp(String ip) {
		if (ip != null && ip.indexOf(",") > 0) {
			String[] ips = ip.trim().split(",");
			for (String subIp : ips) {
				if (!isUnknown(subIp)) {
					ip = subIp;
					break;
				}
			}
		}
		return StringUtils.substring(ip, 0, 255);
	}
	
	public static boolean isUnknown(String checkString) {
		return (StringUtils.isBlank(checkString) || "unknown".equalsIgnoreCase(checkString));
	}
	
	public static boolean isIP(String ip) {
		return (StringUtils.isNotBlank(ip) && ip.matches(REGEX_IP));
	}
	
	public static boolean isIpWildCard(String ip) {
		return (StringUtils.isNotBlank(ip) && ip.matches(REGEX_IP_WILDCARD));
	}
	
	public static boolean ipIsInWildCardNoCheck(String ipWildCard, String ip) {
		String[] s1 = ipWildCard.split("\\.");
		String[] s2 = ip.split("\\.");
		boolean isMatchedSeg = true;
		for (int i = 0; i < s1.length && !"*".equals(s1[i]); i++) {
			if (!s1[i].equals(s2[i])) {
				isMatchedSeg = false;
				break;
			}
		}
		return isMatchedSeg;
	}
	
	public static boolean isIpSegment(String ipSeg) {
		return (StringUtils.isNotBlank(ipSeg) && ipSeg.matches(REGEX_IP_SEGA));
	}
	
	public static boolean ipIsInNetNoCheck(String ipArea, String ip) {
		int idx = ipArea.indexOf('-');
		String[] sIps = ipArea.substring(0, idx).split("\\.");
		String[] sIpe = ipArea.substring(idx + 1).split("\\.");
		String[] sIpt = ip.split("\\.");
		long ips = 0L, ipe = 0L, ipt = 0L;
		for (int i = 0; i < 4; i++) {
			ips = ips << 8L | Integer.parseInt(sIps[i]);
			ipe = ipe << 8L | Integer.parseInt(sIpe[i]);
			ipt = ipt << 8L | Integer.parseInt(sIpt[i]);
		}
		if (ips > ipe) {
			long t = ips;
			ips = ipe;
			ipe = t;
		}
		return (ips <= ipt && ipt <= ipe);
	}
	
	public static boolean isMatchedIp(String filter, String ip) {
		if (StringUtils.isEmpty(filter) || StringUtils.isEmpty(ip))
			return false;
		String[] ips = filter.split(";");
		for (String iStr : ips) {
			if (isIP(iStr) && iStr.equals(ip))
				return true;
			if (isIpWildCard(iStr) && ipIsInWildCardNoCheck(iStr, ip))
				return true;
			if (isIpSegment(iStr) && ipIsInNetNoCheck(iStr, ip))
				return true;
		}
		return false;
	}
	
}
