package cn.yshujia.utils;

import cn.yshujia.constant.SystemConst;
import com.github.houbb.sensitive.word.api.IWordContext;
import com.github.houbb.sensitive.word.api.IWordReplace;
import com.github.houbb.sensitive.word.api.IWordResult;

/**
 * @author yshujia
 * @create 2025/3/8
 * @description WordReplaceUtils 自定义替换字符
 */

public class WordReplaceUtils implements IWordReplace {
	
	@Override
	public void replace(StringBuilder stringBuilder, char[] chars, IWordResult iWordResult, IWordContext iWordContext) {
		stringBuilder.append(SystemConst.SENSITIVE_REPLACE);
	}
	
}
