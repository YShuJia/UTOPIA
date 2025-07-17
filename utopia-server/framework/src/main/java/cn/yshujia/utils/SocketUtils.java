package cn.yshujia.utils;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yshujia
 * @create 2024/12/16
 * @description SocketUtils
 */

@Component
public class SocketUtils {
	
	private static final Map<String, HashMap<UUID, SocketIOClient>> CONCURRENT_HASH_MAP = new ConcurrentHashMap<>();
	
	@Resource
	private SocketIOServer socketIoServer;
	
	/* 广播评论 */
	public void sendComment(Object message) {
		if (!ObjectUtils.isEmpty(message)) {
			// 广播评论
			socketIoServer.getNamespace("/comment")
					.getBroadcastOperations()
					.sendEvent("commentMessage", message);
		}
	}
	
}
