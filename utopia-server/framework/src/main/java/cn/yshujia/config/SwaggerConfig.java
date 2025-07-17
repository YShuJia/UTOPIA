package cn.yshujia.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @author yshujia
 * @create 2024/4/23
 * @description 接口定义
 */
@Profile("test")
@Configuration
public class SwaggerConfig {
	
	@Bean
	public OpenAPI open() {
		return new OpenAPI()
				.info(info())
				.externalDocs(externalDocumentation())
				.addSecurityItem(securityRequirement())
				.components(components());
	}
	
	private License license() {
		return new License()
				.name("开源协议 MIT")
				.url("https://opensource.org/license/MIT");
	}
	
	private Info info() {
		return new Info()
				.contact(new Contact()
						         .name("YShuJia")
						         .email("yshujia2001@163.com")
						         .url("https://utopia.yshujia.cn"))
				.title("UTOPIA API")
				.description("UTOPIA后台API接口说明文档")
				.version("v1.0.0")
				.license(license());
	}
	
	private ExternalDocumentation externalDocumentation() {
		return new ExternalDocumentation()
				.description("转到 knife4j 文档...")
				.url("/doc.html");
	}
	
	private SecurityRequirement securityRequirement() {
		return new SecurityRequirement()
				.addList("Bearer Authorization");
	}
	
	private Components components() {
		return new Components()
				.addSecuritySchemes(
						"Bearer Authorization",
						new SecurityScheme()
								.name("Bearer 认证")
								.type(SecurityScheme.Type.HTTP)
								.scheme("bearer")
								.bearerFormat("JWT")
								.in(SecurityScheme.In.HEADER)
				);
	}
	
}