package cn.yshujia.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author yshujia
 * @create 2024/4/23
 * @description jwt配置
 */

@Data
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {
	
	private long expire;
	
	private String secret;
	
	private long refresh;
	
}
