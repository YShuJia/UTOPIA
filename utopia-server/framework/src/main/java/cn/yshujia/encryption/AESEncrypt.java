package cn.yshujia.encryption;


import com.baomidou.mybatisplus.core.toolkit.AES;
import org.springframework.stereotype.Component;

/**
 * @author yshujia
 * @create 2024/4/23
 * @description AES加解密
 */

@Component
public class AESEncrypt extends AES {
	
	public static String key = "AND JUST HOLD ON";
	
	public static String encrypt(String data) {
		if (null == data) {
			return null;
		}
		return AES.encrypt(data, key);
	}
	
	public static String decrypt(String data) {
		if (null == data) {
			return null;
		}
		return AES.decrypt(data, key);
	}
	
}
