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
		Pattern pattern = Pattern.compile("(src|poster)\\s*=\\s*(['\"])(.*?)\\2", Pattern.DOTALL);
		Matcher matcher = pattern.matcher(json);
		StringBuilder text = new StringBuilder();

		while (matcher.find()) {
			// "src" 或 "poster"
			String attr = matcher.group(1);
			String quote = matcher.group(2);
			String url = matcher.group(3);
			
			if (url.startsWith("http")) {
				continue;
			}
			String newUrl = MinioUtils.STATIC_DOMAIN + url;
			matcher.appendReplacement(text, "");
			text.append(attr).append("=").append(quote).append(newUrl).append(quote);
		}
		return matcher.appendTail(text).toString();
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