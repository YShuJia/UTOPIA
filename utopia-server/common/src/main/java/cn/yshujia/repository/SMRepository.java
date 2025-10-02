package cn.yshujia.repository;

import cn.yshujia.constant.SecurityConst;
import cn.yshujia.encryption.SMEncrypt;
import cn.yshujia.utils.CollectionUtils;
import cn.yshujia.utils.RequestUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;

/**
 * @author yshujia
 */
@Slf4j
@Component
public class SMRepository {

	private static final Duration OVERDUE = Duration.ofDays(1L);

	private static final String PUBLIC_KEY = "sm2:public:";

	private static final String PRIVATE_KEY = "sm2:private:";

	// 备用密钥
	private static final String PUBLIC_SECRET = "0437eb7b089007bbf70471cd59a21f84bd2e9616922fb339b05a8639466c262b36682d257a6e318e800a63348789d2b58482cb53ccda6701838be1ae1ca2223c29";

	private static final String PRIVATE_SECRET = "aca4d4a7ee4edced1ef5d290b21b979159a263f04e290d5c1fb09c05958ed04e";

	private static final Map<String, String> MAP = Map.of(SecurityConst.PUBLIC_KEY, PUBLIC_SECRET, SecurityConst.PRIVATE_KEY, PRIVATE_SECRET);

	@Resource
	RedisTemplate<String, String> redis;

	public String getPublicKey(HttpServletRequest request) {
		String ip = RequestUtils.getIp(request);
		String pubKey = redis.opsForValue().get(PUBLIC_KEY + ip);
		if (pubKey == null) {
			Map<String, String> map = SMEncrypt.getSm2Key();
			if (CollectionUtils.isEmpty(map)) {
				set(MAP, ip);
				return PUBLIC_SECRET;
			}
			set(map, ip);
			return map.get(SecurityConst.PUBLIC_KEY);
		}
		return pubKey;
	}

	public String getPrivateKey(HttpServletRequest request) {
		String ip = RequestUtils.getIp(request);
		String privateKey = redis.opsForValue().get(PRIVATE_KEY + ip);
		if (privateKey == null) {
			Map<String, String> map = SMEncrypt.getSm2Key();
			// 获取密钥对失败， 使用备用密钥对
			if (CollectionUtils.isEmpty(map)) {
				set(MAP, ip);
				return PRIVATE_SECRET;
			}
			set(map, ip);
			return map.get(SecurityConst.PRIVATE_KEY + ip);
		}
		return privateKey;
	}

	@Async("Task")
	public void remove(HttpServletRequest request) {
		String ip = RequestUtils.getIp(request);
		redis.delete(PUBLIC_KEY + ip);
		redis.delete(PRIVATE_KEY + ip);
	}


	@Async("Task")
	protected void set(Map<String, String> map, String ip) {
		redis.opsForValue().set(PUBLIC_KEY + ip, map.get(SecurityConst.PUBLIC_KEY), OVERDUE);
		redis.opsForValue().set(PRIVATE_KEY + ip, map.get(SecurityConst.PRIVATE_KEY), OVERDUE);
	}

}