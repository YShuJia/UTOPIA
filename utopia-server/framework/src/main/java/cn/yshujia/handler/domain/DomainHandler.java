package cn.yshujia.handler.domain;

import cn.yshujia.utils.MinioUtils;
import cn.yshujia.utils.StringUtils;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONException;
import com.baomidou.mybatisplus.extension.handlers.AbstractJsonTypeHandler;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * @author yshujia
 * @create 2024/12/28
 * @description DomainHandler 为静态资源添加域名处理器
 */

public class DomainHandler extends AbstractJsonTypeHandler<Object> {
	
	public DomainHandler(Class<?> type) {
		super(type);
	}
	
	@Override
	public Object parse(String json) {
		try {
			List<String> list = JSON.parseArray(json, String.class);
			list.replaceAll((s) -> MinioUtils.STATIC_DOMAIN + s);
			return list;
		} catch (JSONException e) {
			return StringUtils.isEmpty(json) ? null : (MinioUtils.STATIC_DOMAIN + json);
		}
	}
	
	@Override
	public String toJson(Object obj) {
		String json = "";
		if (ObjectUtils.isEmpty(obj)) {
			return json;
		}
		//集合转为 json
		json = obj instanceof List ? JSON.toJSONString(obj) : obj.toString();
		// 处理域名前缀
		if (json.contains(MinioUtils.STATIC_DOMAIN)) {
			json = json.replaceAll(MinioUtils.STATIC_DOMAIN, "");
		}
		return json;
	}
	
}
