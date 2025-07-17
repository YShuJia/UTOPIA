package cn.yshujia.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.task.DelegatingSecurityContextAsyncTaskExecutor;

import java.util.concurrent.Executor;

/**
 * @author yshujia
 * @description 多线程配置
 * @create 2024/4/23
 */

@Configuration
@EnableAsync // 开启多线程
public class ThreadConfig {
	
	@Bean("Task")
	public Executor asyncExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		//设置核心线程数
		executor.setCorePoolSize(8);
		//设置最大线程数
		executor.setMaxPoolSize(20);
		//配置队列大小
		executor.setQueueCapacity(1024);
		//设置线程活跃时间
		executor.setKeepAliveSeconds(60);
		//设置线程名
		executor.setThreadNamePrefix("副线程");
		//等待所有任务完成后再关闭线程池
		executor.setWaitForTasksToCompleteOnShutdown(true);
		//初始化线程池
		executor.initialize();
		return new DelegatingSecurityContextAsyncTaskExecutor(executor);
	}
	
}
