package cn.yshujia.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author yshujia
 * @create 2024/4/23
 * @description 配置跨域
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {
	
	private static final String[] ORIGINS = new String[]{"GET", "POST", "PUT", "DELETE", "OPTIONS"};
	
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		// 所有的当前站点的请求地址，都支持跨域访问。
		registry.addMapping("/**")
				// 所有的外部域都可跨域访问
				.allowedOriginPatterns("*")
				// 是否支持跨域用户凭证
				.allowCredentials(true)
				// 当前站点支持的跨域请求类型是什么
				.allowedMethods(ORIGINS)
				// 超时时长设置为1小时。 时间单位是秒。
				.maxAge(3600);
	}
	
}
