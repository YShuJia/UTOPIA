package cn.yshujia.ex;

import cn.yshujia.constant.HttpCode;
import cn.yshujia.domain.vo.ApiVO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author yshujia
 * @description 全局异常处理类
 * @create 2024/4/23
 */

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
	
	// 权限校验异常
	@ExceptionHandler(AccessDeniedException.class)
	public ApiVO<Boolean> handleAccessDeniedException(AccessDeniedException e, HttpServletRequest request) {
		handleException(e, request);
		return ApiVO.failure(HttpCode.FORBIDDEN, "暂无权限访问！");
	}
	
	// 资源未找到异常
	@ExceptionHandler(NoHandlerFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ApiVO<Boolean> handleNoHandlerFoundException(NoHandlerFoundException e, HttpServletRequest request) {
		handleException(e, request);
		return ApiVO.failure(HttpCode.NOT_FOUND, "请求资源不存在！");
	}
	
	// 参数异常
	@ExceptionHandler(value = IllegalArgumentException.class)
	public ApiVO<Boolean> handler(IllegalArgumentException e, HttpServletRequest request) {
		handleException(e, request);
		return ApiVO.failure(HttpCode.BAD_PARAMS, "参数校验失败！");
	}
	
	// 参数异常
	@ExceptionHandler(value = MethodArgumentNotValidException.class)
	public ApiVO<Boolean> handler(MethodArgumentNotValidException e, HttpServletRequest request) {
		handleException(e, request);
		String message = Optional.ofNullable(e.getBindingResult().getFieldError())
				.map(DefaultMessageSourceResolvable::getDefaultMessage)
				.orElse("参数校验失败！");
		return ApiVO.failure(HttpCode.BAD_PARAMS, message);
	}
	
	// 空指针异常
	@ExceptionHandler(value = NullPointerException.class)
	public ApiVO<Boolean> handler(NullPointerException e, HttpServletRequest request) {
		handleException(e, request);
		return ApiVO.failure(HttpCode.SERVER_ERROR, "系统发生空指针异常！");
	}
	
	// 未知的运行时异常
	@ExceptionHandler(RuntimeException.class)
	public ApiVO<Boolean> handleRuntimeException(RuntimeException e, HttpServletRequest request) {
		handleException(e, request);
		return ApiVO.failure(HttpCode.SERVER_ERROR, "系统发生未知异常！");
	}
	
	// 业务异常
	@ExceptionHandler(ServiceException.class)
	public ApiVO<Boolean> handler(ServiceException e, HttpServletRequest request) {
		return ApiVO.failure(e.getCode(), e.getMessage());
	}
	
	// 自定义异常
	@ExceptionHandler(CustomException.class)
	public ApiVO<Boolean> handler(CustomException e, HttpServletRequest request) {
		handleException(e, request);
		return ApiVO.failure(e.getCode(), e.getMessage());
	}
	
	@Async("Task")
	public void handleException(Exception ex, HttpServletRequest request) {
		// 获取最原始的异常（处理包装异常）
		Throwable rootCause = getRootCause(ex);
		// 获取异常堆栈
		StackTraceElement[] stackTrace = rootCause.getStackTrace();
		
		// 找到第一个你自己写的类（排除 JDK 和 Spring 的类）
		Optional<StackTraceElement> targetClass = Arrays.stream(stackTrace)
				// 替换为你的包名
				.filter(element -> element.getClassName().startsWith("cn.yshujia"))
				.findFirst();
		
		String className = "unknown";
		String methodName = "unknown";
		int lineNumber = -1;
		
		if (targetClass.isPresent()) {
			StackTraceElement element = targetClass.get();
			className = element.getClassName();
			methodName = element.getMethodName();
			lineNumber = element.getLineNumber();
		}
		
		// 记录日志（精简格式）
		log.error(
				"请求地址: {}\n执行类: {}\n执行方法: {}\n异常位置: 方法第 {} 行\n异常类型: {}\n异常信息: {}",
				request.getRequestURI(), className, methodName, lineNumber, rootCause.getClass().getSimpleName(),
				Optional.ofNullable(rootCause.getMessage()).orElse("暂无额外描述！")
		);
		
	}
	
	private Throwable getRootCause(Throwable throwable) {
		while (throwable.getCause() != null && throwable.getCause() != throwable) {
			throwable = throwable.getCause();
		}
		return throwable;
	}
	
}
