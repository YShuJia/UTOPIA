package cn.yshujia.utils;

import cn.yshujia.config.JwtConfig;
import cn.yshujia.constant.SecurityConst;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecureDigestAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

/**
 * @author yshujia
 * @description
 * @create 2024/4/23
 */

@Slf4j
@Component
public class JwtTokenUtils {

	// 加密算法
	private final static SecureDigestAlgorithm<SecretKey, SecretKey> ALGORITHM = Jwts.SIG.HS256;

	// jwt主题
	private final static String SUBJECT = "Peripherals";
	// jwt签发者
	private static final String JWT_ISS = "YSHUJIA";
	private static JwtConfig jwtConfig;

	@Autowired
	public JwtTokenUtils(JwtConfig jwtConfig) {
		JwtTokenUtils.jwtConfig = jwtConfig;
	}

	/*
	这些是一组预定义的声明，它们 不是强制性的，而是推荐的 ，以 提供一组有用的、可互操作的声明 。
	iss: jwt签发者
	sub: jwt所面向的用户
	aud: 接收jwt的一方
	exp: jwt的过期时间，这个过期时间必须要大于签发时间
	nbf: 定义在什么时间之前，该jwt都是不可用的.
	iat: jwt的签发时间
	jti: jwt的唯一身份标识，主要用来作为一次性token,从而回避重放攻击
	*/
	public static String generatorToken(String id) {

		Date exprireDate = Date.from(Instant.now().plusSeconds(jwtConfig.getExpire() * 60L));

		return Jwts.builder()
				// 设置头部信息header
				.header()
				.add("typ", "JWT")
				.add("alg", "HS256")
				.and()
				// 设置自定义负载信息payload
				.claim("id", id)
				// 令牌ID
				.id(id)
				// 过期日期
				.expiration(exprireDate)
				// 签发时间
				.issuedAt(new Date())
				// 主题
				.subject(SUBJECT)
				// 签发者
				.issuer(JWT_ISS)
				// 签名
				.signWith(Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes(StandardCharsets.UTF_8)), ALGORITHM)
				.compact();
	}

	public static String getId(String jwt) {
		jwt = jwt.replace(SecurityConst.TOKEN_PREFIX, "");
		Claims claims = parseClaim(jwt);
		if (null == claims) {
			return null;
		}
		return claims.get("id").toString();
	}

	public static String getId(HttpServletRequest req) {
		String jwt = req.getHeader(SecurityConst.TOKEN_KEY);
		return getId(jwt);
	}

	/**
	 * 解析 token
	 */
	public static Claims parseClaim(String token) {
		try {
			return Jwts.parser()
					.verifyWith(Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes(StandardCharsets.UTF_8)))
					.build()
					.parseSignedClaims(token)
					.getPayload();
		} catch (Exception e) {
			log.error("解析 token 失败: {}", e.getMessage());
		}
		return null;
	}

	//判断是否需要刷新
	public static Boolean isRefresh(String jwt) {
		if (null == jwt) {
			return false;
		}
		jwt = jwt.replace(SecurityConst.TOKEN_PREFIX, "");
		Claims parseClaim = parseClaim(jwt);
		if (null == parseClaim) {
			return false;
		}
		Date expirationDate = parseClaim.getExpiration();
		return expirationDate.getTime() / 1000 - jwtConfig.getRefresh() * 60 <= new Date().getTime() / 1000;
	}

	//判断是否过期
	public static Boolean isExpired(String jwt) {
		jwt = jwt.replace(SecurityConst.TOKEN_PREFIX, "");
		Claims parseClaim = parseClaim(jwt);
		if (null == parseClaim) {
			return true;
		}
		Date expirationDate = parseClaim.getExpiration();
		String date = TimeUtils.getParallelTime(expirationDate);
		return date.compareTo(TimeUtils.getParallelTime()) < 0;
	}

}