package cn.yshujia.handler.domain;

import cn.yshujia.utils.MinioUtils;
import cn.yshujia.utils.StringUtils;
import com.baomidou.mybatisplus.extension.handlers.AbstractJsonTypeHandler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author yshujia
 * @create 2025/3/5
 * @description DomainContentHandler 为文章中的资源地址添加域名
 */


public class DomainContentHandler extends AbstractJsonTypeHandler<String> {
	
	public DomainContentHandler(Class<?> type) {
		super(type);
	}
	
	@Override
	public String parse(String json) {
		if (StringUtils.isEmpty(json)) {
			return "";
		}
		// 匹配 src 属性 添加静态资源域名
		Pattern srcPattern = Pattern.compile("src='((?!http).*?)'");
		String replacement = "src='" + MinioUtils.STATIC_DOMAIN + "$1'";
		Matcher matcher = srcPattern.matcher(json);
		StringBuilder text = new StringBuilder();
		// 替换匹配
		while (matcher.find()) {
			matcher.appendReplacement(text, replacement);
		}
		// 将剩余部分添加到结果中
		matcher.appendTail(text);
		return text.toString();
	}
	
	@Override
	public String toJson(String obj) {
		if (StringUtils.isEmpty(obj)) {
			return "";
		}
		// 处理域名前缀
		obj = obj.replaceAll(MinioUtils.STATIC_DOMAIN, "");
		return obj;
	}
	
}