package cn.yshujia.handler;


import cn.yshujia.utils.CollectionUtils;
import cn.yshujia.utils.MinioUtils;
import cn.yshujia.utils.StringUtils;
import cn.yshujia.utils.WordReplaceUtils;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.extension.handlers.AbstractJsonTypeHandler;
import com.github.houbb.sensitive.word.core.SensitiveWordHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: yshujia
 * @create: 2025/6/24 16:39
 * @description: CommentHandler
 */
public class CommentHandler extends AbstractJsonTypeHandler<List<String>> {
	
	WordReplaceUtils wordReplaceUtils = new WordReplaceUtils();
	
	public CommentHandler(Class<?> type) {
		super(type);
	}
	
	@Override
	public List<String> parse(String json) {
		if (StringUtils.isEmpty(json)) {
			return new ArrayList<>();
		}
		List<String> list = JSON.parseArray(json, String.class);
		if (list.size() == 2 && !StringUtils.isEmpty(list.get(1))) {
			list.set(1, MinioUtils.STATIC_DOMAIN + list.get(1));
		}
		return list;
	}
	
	@Override
	public String toJson(List<String> list) {
		if (CollectionUtils.isEmpty(list)) {
			return JSON.toJSONString(list);
		}
		// 图片去除域名
		if (list.size() == 2) {
			list.set(1, list.get(1).replace(MinioUtils.STATIC_DOMAIN, ""));
		}
		// 敏感词过滤
		list.set(0, SensitiveWordHelper.replace(list.get(0), wordReplaceUtils));
		return JSON.toJSONString(list);
	}
	
}
