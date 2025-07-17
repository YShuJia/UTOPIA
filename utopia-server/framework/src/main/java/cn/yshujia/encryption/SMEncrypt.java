package cn.yshujia.encryption;


import cn.hutool.core.util.HexUtil;
import cn.hutool.crypto.BCUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.SM2;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import cn.yshujia.constant.SecurityConst;
import cn.yshujia.ex.CustomException;
import cn.yshujia.utils.StringUtils;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.crypto.engines.SM2Engine;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.springframework.util.ObjectUtils;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yshujia
 * @create 2024/4/23
 * @description sm 加解密
 */

@Slf4j
public class SMEncrypt {
	
	// 1 - C1C3C2，0 - C1C2C3，默认为1
	private static final SM2Engine.Mode MODE = SM2Engine.Mode.C1C2C3;
	
	public static String enSm2(String publicKey, Object data) throws CustomException {
		String enData;
		
		if (StringUtils.isEmpty(publicKey) || ObjectUtils.isEmpty(data)) {
			return null;
		}
		String json;
		if (data instanceof String) {
			json = (String) data;
		} else {
			json = JSON.toJSONString(data);
		}
		try {
			SM2 sm2 = getSm2(publicKey, null);
			enData = sm2.encryptHex(json, KeyType.PublicKey);
		} catch (Exception e) {
			throw new CustomException(e.getMessage());
		}
		return enData;
	}
	
	public static String deSm2(String privateKey, String data) throws CustomException {
		String deData;
		if (StringUtils.isEmpty(privateKey) || ObjectUtils.isEmpty(data)) {
			return null;
		}
		try {
			SM2 sm2 = getSm2(null, privateKey);
			deData = sm2.decryptStr(data, KeyType.PrivateKey, StandardCharsets.UTF_8);
		} catch (Exception e) {
			throw new CustomException(e.getMessage());
		}
		return deData;
	}
	
	public static String enSm4(String publicKey, String data) throws CustomException {
		String enData;
		if (StringUtils.isEmpty(publicKey) || ObjectUtils.isEmpty(data)) {
			return null;
		}
		try {
			SymmetricCrypto sm4 = SmUtil.sm4(publicKey.getBytes(StandardCharsets.UTF_8));
			enData = sm4.encryptHex(data);
		} catch (Exception e) {
			throw new CustomException(e.getMessage());
		}
		return enData;
	}
	
	public static String deSm4(String privateKey, String data) throws CustomException {
		String deData;
		if (StringUtils.isEmpty(privateKey) || ObjectUtils.isEmpty(data)) {
			return null;
		}
		try {
			SymmetricCrypto sm4 = SmUtil.sm4(privateKey.getBytes(StandardCharsets.UTF_8));
			deData = sm4.decryptStr(data);
		} catch (Exception e) {
			throw new CustomException(e.getMessage());
		}
		return deData;
	}
	
	public static SM2 getSm2(String publicKey, String privateKey) {
		ECPrivateKeyParameters ecPrivateKeyParameters = null;
		ECPublicKeyParameters ecPublicKeyParameters = null;
		if (StringUtils.isNotBlank(privateKey)) {
			ecPrivateKeyParameters = BCUtil.toSm2Params(privateKey);
		}
		
		if (StringUtils.isNotBlank(publicKey)) {
			if (publicKey.length() == 130) {
				publicKey = publicKey.substring(2);
			}
			String xHex = publicKey.substring(0, 64);
			String yHex = publicKey.substring(64, 128);
			ecPublicKeyParameters = BCUtil.toSm2Params(xHex, yHex);
		}
		SM2 sm2 = new SM2(ecPrivateKeyParameters, ecPublicKeyParameters);
		sm2.usePlainEncoding();
		sm2.setMode(MODE);
		return sm2;
	}
	
	public static Map<String, String> getSm2Key() {
		Map<String, String> map = new HashMap<>();
		SM2 sm2 = SmUtil.sm2().setMode(MODE);
		byte[] privateKeyByte = BCUtil.encodeECPrivateKey(sm2.getPrivateKey());
		byte[] publicKeyByte = ((BCECPublicKey) sm2.getPublicKey()).getQ().getEncoded(false);
		String privateKey = HexUtil.encodeHexStr(privateKeyByte);
		String publicKey = HexUtil.encodeHexStr(publicKeyByte);
		map.put(SecurityConst.PRIVATE_KEY, privateKey);
		map.put(SecurityConst.PUBLIC_KEY, publicKey);
		return map;
	}
	
}