package cn.yshujia.encryption;

/**
 * @author yshujia
 * @description 数据脱敏
 * @create 2024/4/23
 */

public class Sensitivity {
	
	/*详细地址脱敏*/
	public static String getAddress(String address) {
		if (address.length() >= 10) {
			return address.replaceAll("(\\S{3})\\S*(\\S{3})", "$1******$2");
		}
		return address.replaceAll("(\\S{3})\\S*", "$1******");
	}
	
	/*普通字符串脱敏脱敏*/
	public static String getString(String str) {
		if (str.length() > 2) {
			return str.replaceAll("(\\S)\\S*(\\S)", "$1***$2");
		}
		return str.replaceAll("(\\S)\\S*", "$1***");
	}
	
	/*电话号码脱敏*/
	public static String getTel(String tel) {
		return tel.replaceAll("(\\S{3})\\S{4}(\\S{4})", "$1****$2");
	}
	
	/*身份证脱敏*/
	public static String getIdCard(String idCard) {
		return idCard.replaceAll("(\\S{6})\\S{4}(\\S{4})\\S{4}", "$1****$2****");
	}
	
}
