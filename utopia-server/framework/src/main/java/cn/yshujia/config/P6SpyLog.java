package cn.yshujia.config;

import com.baomidou.mybatisplus.extension.p6spy.StdoutLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.ObjectUtils;

/**
 * @author yshujia
 * @create 2024/4/23
 * @description sql 日志
 */

@Slf4j
public class P6SpyLog extends StdoutLogger {
	
	@Override
	public void logText(String text) {
		// SQL 语句不为空且不是查询语句
		if (!ObjectUtils.isEmpty(text) && !text.toUpperCase().startsWith("SELECT")) {
			log(text);
		}
	}
	
	@Async("Task")
	protected void log(String sql) {
		sql = sql.replaceAll("\\s+", " ");
		sql = sql.replaceAll("\n", "");
		sql = sql.replaceAll("SET|set", "\nSET");
		sql = sql.replaceAll("VALUES|values", "\nVALUES");
		sql = sql.replaceAll("FROM|from", "\nFROM");
		sql = sql.replaceAll("WHERE|where", "\nWHERE");
		sql = sql.replaceAll("GROUP|group", "\nGROUP");
		sql = sql.replaceAll("ORDER BY|order by", "\nORDER BY");
		sql = sql.replaceAll("JOIN|join", "\nJOIN");
		sql = sql.replaceAll("HAVING|having", "\nHAVING");
		log.debug(sql);
	}
	
}