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
		String regex = "(?i)\\b(src|poster)\\s*=\\s*(['\"])(.*?)\\2";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(json);

		StringBuilder result = new StringBuilder();
		while (matcher.find()) {
			String attribute = matcher.group(1); // src 或 poster
			String quote = matcher.group(2);     // 引号类型
			String url = matcher.group(3);       // 原始 URL
			// 如果URL已经是http或https开头，则保持不变
			if (!StringUtils.isEmpty(url) &&
					(url.toLowerCase().startsWith("http:") || url.toLowerCase().startsWith("https:"))) {
				matcher.appendReplacement(result, Matcher.quoteReplacement(matcher.group(0)));
			} else {
				// 添加域名前缀，如果URL不以/开头，则添加/
				String newUrl = MinioUtils.STATIC_DOMAIN;
				if (url != null && !url.isEmpty()) {
					if (!url.startsWith("/")) {
						newUrl += "/";
					}
					newUrl += url;
				}
				// 构造新的属性字符串
				String replacement = attribute + "=" + quote + newUrl + quote;
				matcher.appendReplacement(result, Matcher.quoteReplacement(replacement));
			}
		}
		return matcher.appendTail(result).toString();

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