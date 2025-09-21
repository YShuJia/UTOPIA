package cn.yshujia.utils;


import cn.yshujia.repository.SysRepository;
import com.github.houbb.sensitive.word.api.IWordContext;
import com.github.houbb.sensitive.word.api.IWordReplace;
import com.github.houbb.sensitive.word.api.IWordResult;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;


/**
 * @author yshujia
 * @create 2025/3/8
 * @description WordReplaceUtils 自定义替换字符
 */

@Component
public class WordReplaceUtils implements IWordReplace {

	@Resource
	private SysRepository sysRepository;

	@Override
	public void replace(StringBuilder stringBuilder, char[] chars, IWordResult iWordResult, IWordContext iWordContext) {
		stringBuilder.append(sysRepository.getSysConfig().getSysReplaceChar());
	}

}

