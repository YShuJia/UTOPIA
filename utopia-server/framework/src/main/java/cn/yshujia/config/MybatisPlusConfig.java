package cn.yshujia.config;


import cn.yshujia.handler.MetaHandler;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

/**
 * @author yshujia
 * @description mybatis plus配置
 * @create 2024/4/23
 */

@Configuration
public class MybatisPlusConfig {
	
	@Bean("sqlSessionFactory")
	public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
		MybatisSqlSessionFactoryBean mybatisSqlSessionFactoryBean = new MybatisSqlSessionFactoryBean();
		mybatisSqlSessionFactoryBean.setDataSource(dataSource);
		mybatisSqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:mapper/*.xml"));
		mybatisSqlSessionFactoryBean.setGlobalConfig(globalConfig());
//		mybatisSqlSessionFactoryBean.setPlugins(new PaginationInnerInterceptor(DbType.MYSQL));
		return mybatisSqlSessionFactoryBean.getObject();
	}
	
	@Bean
	@ConfigurationProperties(prefix = "mybatis-plus.global-config")
	public GlobalConfig globalConfig() {
		GlobalConfig globalConfig = new GlobalConfig();
		globalConfig.setMetaObjectHandler(new MetaHandler());
		return globalConfig;
	}
	
}
