package cn.yshujia.config;


import cn.yshujia.constant.SecurityConst;
import cn.yshujia.filter.JwtAuthenticationTokenFilter;
import cn.yshujia.filter.PasswordAuthenticationFilter;
import cn.yshujia.handler.JwtAccessDeniedHandler;
import cn.yshujia.handler.JwtAuthenticationHandler;
import cn.yshujia.handler.LogoutSuccessHandle;
import cn.yshujia.service.UserLoginService;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


/**
 * @author yshujia
 * @create 2024/4/23
 * @description security认证
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
	
	@Resource
	private UserLoginService userLoginService;
	
	@Resource
	private JwtAuthenticationHandler jwtAuthenticationHandler;
	
	@Resource
	private JwtAccessDeniedHandler jwtAccessDeniedHandler;
	
	@Resource
	private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;
	
	@Resource
	private LogoutSuccessHandle logoutSuccessHandle;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		// 密码加密 默认 10，太大会导致密码匹配时间过长
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public AuthenticationManager authenticationManager() throws Exception {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userLoginService);
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		return new ProviderManager(authenticationProvider);
	}
	
	@Bean
	public PasswordAuthenticationFilter customAuthFilter(AuthenticationManager authenticationManager) {
		PasswordAuthenticationFilter filter = new PasswordAuthenticationFilter(authenticationManager);
		filter.setAuthenticationManager(authenticationManager);
		return filter;
	}
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
		
		// 放行，由注解控制权限
		return http.authorizeHttpRequests(auth ->
				                                  auth.anyRequest().permitAll())
				// 登录
				.formLogin(auth ->
						           auth.loginProcessingUrl(SecurityConst.LOGIN).permitAll())
				.addFilterAt(customAuthFilter(authenticationManager), UsernamePasswordAuthenticationFilter.class)
				//  认证失败处理
				.exceptionHandling(auth ->
						                   auth.authenticationEntryPoint(jwtAuthenticationHandler).accessDeniedHandler(jwtAccessDeniedHandler))
				// 禁用 session
				.sessionManagement(auth ->
						                   auth.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				// 添加 jwt 过滤器
				.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class)
				// 登出
				.logout(auth ->
						        auth.logoutUrl(SecurityConst.LOGIN_OUT).logoutSuccessHandler(logoutSuccessHandle))
				// CSRF（防止伪造请求攻击）禁用，不使用 session
				.csrf(AbstractHttpConfigurer::disable)
				// CORS（控制跨域请求）配置跨域资源共享
				.cors(auth -> auth.configure(http))
				.build();
	}
	
}