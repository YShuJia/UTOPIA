package cn.yshujia.handler;

import com.corundumstudio.socketio.SocketIOServer;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author yshujia
 * @create 2024/12/16
 * @description SocketIOServerRunner
 */

@Component
@Slf4j
public class SocketIOServerRunner implements CommandLineRunner, DisposableBean {
	
	
	@Resource
	private SocketIOServer socketIOServer;
	
	@Override
	public void run(String... args) throws Exception {
		socketIOServer.getNamespace("/comment");
		socketIOServer.start();
		log.info("评论 SocketIOServer==============================启动成功");
	}
	
	
	@Override
	public void destroy() throws Exception {
		socketIOServer.stop();
		log.info("SocketIOServer==============================关闭成功");
	}
	
}