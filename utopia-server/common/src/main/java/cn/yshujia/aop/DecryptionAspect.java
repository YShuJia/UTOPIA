package cn.yshujia.aop;


import cn.yshujia.annotation.DecryptFields;
import cn.yshujia.constant.HttpCode;
import cn.yshujia.constant.SecurityConst;
import cn.yshujia.domain.vo.ApiVO;
import cn.yshujia.encryption.SMEncrypt;
import cn.yshujia.ex.CustomException;
import cn.yshujia.ex.ServiceException;
import cn.yshujia.repository.SMRepository;
import cn.yshujia.utils.RequestUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Field;
import java.util.Objects;

/**
 * @author yshujia
 * @create 2024/4/23
 * @description 加解密切面
 */

@Aspect
@Component
public class DecryptionAspect {
	
	@Resource
	SMRepository smRepository;
	
	@Around(value = "@annotation(decryptFields)", argNames = "joinPoint, decryptFields")
	public Object process(ProceedingJoinPoint joinPoint, DecryptFields decryptFields) throws ServiceException, IllegalAccessException {
		HttpServletRequest req = RequestUtils.getReq();
		
		String publicKey = req.getHeader(SecurityConst.PUBLIC_KEY);
		String privateKey = smRepository.getPrivateKey(req);
		String[] value = decryptFields.value();
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		String[] parameterNames = signature.getParameterNames();
		Object[] args = joinPoint.getArgs();
		for (int i = 0; i < args.length; i++) {
			if (ObjectUtils.isEmpty(args[i])) {
				break;
			}
			Class<?> argType = args[i].getClass();
			if (argType.isPrimitive() || argType.getName().startsWith("java")) {
				for (String v : value) {
					if (parameterNames[i].equals(v)) {
						args[i] = SMEncrypt.deSm2(privateKey, args[i].toString());
						break;
					}
				}
			} else {
				Field[] field = args[i].getClass().getDeclaredFields();
				for (Field item : field) {
					item.setAccessible(true);
					String fieldName = item.getName();
					Object fieldValue = item.get(args[i]);
					for (String v : value) {
						if (Objects.equals(fieldName, v) && !ObjectUtils.isEmpty(fieldValue)) {
							item.set(args[i], SMEncrypt.deSm2(privateKey, fieldValue.toString()));
							break;
						}
					}
				}
			}
		}
		Object result;
		try {
			result = joinPoint.proceed(args);
		} catch (Throwable e) {
			smRepository.remove(req);
			if (e instanceof CustomException) {
				throw (CustomException) e;
			}
			if (e instanceof ServiceException) {
				throw (ServiceException) e;
			}
			throw new ServiceException("数据解密失败，请刷新页面后重试！");
		}
		if (result instanceof ApiVO) {
			if (((ApiVO<?>) result).getData() == null || ((ApiVO<?>) result).getData().equals(false) ||
					((ApiVO<?>) result).getData().equals(true)) {
				return result;
			}
			// 返回加密数据
			ApiVO<String> apiResponse = new ApiVO<>();
			apiResponse.setCode(HttpCode.NEED_DECRYPTION);
			apiResponse.setMsg(((ApiVO<?>) result).getMsg());
			apiResponse.setData(SMEncrypt.enSm2(publicKey, ((ApiVO<?>) result).getData()));
			result = apiResponse;
		}
		return result;
	}
	
}