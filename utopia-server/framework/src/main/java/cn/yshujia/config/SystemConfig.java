package cn.yshujia.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author: yshujia
 * @create: 2025/6/20 14:16
 * @description: SystemConfig
 */

@Data
@Configuration
@ConfigurationProperties(prefix = "system")
public class SystemConfig {
	
	// 限流缓存 key
	private final String limitKey = "rate_limit:ip:";
	
	// 限流封禁缓存 key
	private final String limitBanKey = "rate_limit:ban:ip:";
	
	// 密码试错缓存 key
	private final String passwordErrorKey = "password:error:ip:";
	
	// 密码封禁缓存 key
	private final String passwordBanKey = "password:ban:ip:";
	
	private String author;
	
	private Integer errorTime;
	
	private Integer errorTimes;
	
	private Integer limitTime;
	
	private Integer limitTimes;
	
	private Integer banTime;
	
	private String replaceChar;
	
	private Integer maxExperience;
	
	private List<String> tables;
	
}