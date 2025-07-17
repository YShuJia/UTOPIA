package cn.yshujia.config;

import com.corundumstudio.socketio.SocketConfig;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.SpringAnnotationScanner;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author yshujia
 * @create 2024/12/16
 * @description SocketIOConfig
 */

@Data
@Configuration
@ConfigurationProperties(prefix = "socketio")
public class SocketIOConfig {
	
	private String host;
	
	private int port;
	
	private int bossCount;
	
	private int workCount;
	
	private boolean allowCustomRequests;
	
	private int upgradeTimeout;
	
	private int pingTimeout;
	
	private int pingInterval;
	
	private int maxFramePayloadLength;
	
	private int maxHttpContentLength;
	
	private String[] namespaces;
	
	@Bean
	public SocketIOServer socketIOServer() {
		SocketConfig socketConfig = new SocketConfig();
		socketConfig.setReuseAddress(true);
		socketConfig.setTcpNoDelay(true);
		socketConfig.setSoLinger(0);
		com.corundumstudio.socketio.Configuration configuration = new com.corundumstudio.socketio.Configuration();
		configuration.setSocketConfig(socketConfig);
		// host在本地测试可以设置为localhost或者本机IP，在Linux服务器跑可换成服务器IP
		configuration.setHostname(host);
		configuration.setPort(port);
		// socket连接数大小（如只监听一个端口boss线程组为1即可）
		configuration.setBossThreads(bossCount);
		configuration.setWorkerThreads(workCount);
		configuration.setAllowCustomRequests(allowCustomRequests);
		// 协议升级超时时间（毫秒），默认10秒。HTTP握手升级为ws协议超时时间
		configuration.setUpgradeTimeout(upgradeTimeout);
		// Ping消息超时时间（毫秒），默认60秒，这个时间间隔内没有接收到心跳消息就会发送超时事件
		configuration.setPingTimeout(pingTimeout);
		// Ping消息间隔（毫秒），默认25秒。客户端向服务器发送一条心跳消息间隔
		configuration.setPingInterval(pingInterval);
		// 单条消息最大长度
		configuration.setMaxFramePayloadLength(maxFramePayloadLength);
		// http最大长度
		configuration.setMaxHttpContentLength(maxHttpContentLength);
		
		SocketIOServer socketIOServer = new SocketIOServer(configuration);
		
		// 添加自定义的namespace
		Optional.ofNullable(namespaces).ifPresent(nss ->
				                                          Arrays.stream(nss).forEach(socketIOServer::addNamespace));
		
		return socketIOServer;
	}
	
	/**
	 * 注入 OnConnect，OnDisconnect，OnEvent 注解。否则 Spring 无法扫描 OnConnect，OnDisconnect 等注解
	 */
	@Bean
	public SpringAnnotationScanner springAnnotationScanner(SocketIOServer socketIOServer) {
		return new SpringAnnotationScanner(socketIOServer);
	}
	
}