package cn.yshujia.config;

import io.minio.MinioClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author yshujia
 * @create 2024/12/28
 * @description MinioConfig
 */

@Data
@Configuration
@ConfigurationProperties(prefix = "minio")
public class MinioConfig {
	
	private String bucketName;
	
	private String endpoint;
	
	private String accessKey;
	
	private String secretKey;

	private long maxImgSize;

	private float compressionQuality;

	private String domain;
	
	@Bean
	public MinioClient minioClient() {
		return MinioClient.builder()
				.endpoint(endpoint)
				.credentials(accessKey, secretKey)
				.build();
	}
	
}
