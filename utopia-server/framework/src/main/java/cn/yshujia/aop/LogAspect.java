package cn.yshujia.aop;

import cn.yshujia.annotation.Logger;
import cn.yshujia.domain.vo.ApiVO;
import cn.yshujia.utils.RequestUtils;
import cn.yshujia.utils.SecurityUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;


/**
 * @author yshujia
 * @description 日志记录
 * @create 2024/4/23
 */

@Slf4j
@Aspect
@Component
public class LogAspect {
	
	@AfterReturning(value = "@annotation(logger)", returning = "methodResult")
	public void doAfterReturning(JoinPoint joinPoint, ApiVO<?> methodResult, Logger logger) {
		doAfter(joinPoint, methodResult);
	}
	
	@Async("Task")
	public void doAfter(JoinPoint joinPoint, ApiVO<?> apiVO) {
		HttpServletRequest request = RequestUtils.getReq();
		MethodSignature ms = (MethodSignature) joinPoint.getSignature();
		StringBuilder param = new StringBuilder();
		try {
			ParameterNameDiscoverer pn = new DefaultParameterNameDiscoverer();
			// 获取参数名集合
			String[] arr = pn.getParameterNames(ms.getMethod());
			// 获取参数值
			Object[] args = joinPoint.getArgs();
			if (args != null && arr != null && args.length == arr.length) {
				for (int i = 0; i < arr.length; i++) {
					int length = Math.min(args[i].toString().length(), 1024);
					param.append(arr[i]).append(" = ").append(args[i].toString(), 0, length).append(" ");
				}
			}
		} catch (Exception e) {
			log.error("获取参数: {}", e.getLocalizedMessage());
		} finally {
			String url = request.getServletPath();
			String className = joinPoint.getTarget().getClass().getName();
			String username;
			try {
				username = SecurityUtils.getLoginUser().getUsername();
			} catch (Exception e) {
				username = RequestUtils.getIp(request);
			}
			if (apiVO.getCode() >= 3000 || apiVO.getCode() < 2000) {
				log.error(
						"""
								执行的用户：{}
								执行的类名：{}
								执行的接口：{}
								执行的数据：{}
								执行的结果：{}""",
						username, className, url, param, apiVO
				);
			} else {
				log.info(
						"""
								执行的用户：{}
								执行的类名：{}
								执行的接口：{}
								执行的数据：{}
								执行的结果：{}""",
						username, className, url, param, apiVO
				);
			}
		}
	}
	
}