package cn.yshujia.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author yshujia
 * @create 2024/4/23
 * @description RedissonConfig
 */

@Configuration
public class RedissonConfig {
	
	@Value("${spring.data.redis.host}")
	private String host;
	
	@Value("${spring.data.redis.port}")
	private String port;
	
	@Value("${spring.data.redis.password}")
	private String password;
	
	@Bean
	public RedissonClient redisson() {
		Config config = new Config();
		//redis服务器地址 //制定redis数据库编号
		config.useSingleServer().setAddress("redis://" + host + ":" + port)
				.setDatabase(0)
				// redis用户名密码
				.setPassword(password)
				//连接池最小空闲连接数
				.setConnectionMinimumIdleSize(10)
				.setKeepAlive(true)
				//连接池最大线程数
				.setConnectionPoolSize(50)
				//线程的超时时间
				.setIdleConnectionTimeout(60000)
				//客户端程序获取redis链接的超时时间
				.setConnectTimeout(6000)
				//响应超时时间
				.setTimeout(60000);
		return Redisson.create(config);
	}
	
}
