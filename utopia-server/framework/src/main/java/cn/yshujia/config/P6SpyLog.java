package cn.yshujia.config;

import com.baomidou.mybatisplus.extension.p6spy.StdoutLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.ObjectUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
		if (!ObjectUtils.isEmpty(text)) {
			log(text);
		}
	}
	
	@Async("Task")
	protected void log(String sql) {
		if (sql == null || sql.trim().isEmpty()) {
			return;
		}
		// 合并空白符（空格、制表符、换行等）为单个空格
		sql = sql.replaceAll("\\s+", " ").trim();
		// 定义需要处理的 SQL 关键字：格式为 {匹配正则, 替换内容, 是否换行前缀}
		Object[][] rules = {
				// --- JOIN 类型（必须放在 LEFT、RIGHT、JOIN 单独项之前）---
				{"(?i)\\bFULL OUTER JOIN\\b", "FULL OUTER JOIN", true},
				{"(?i)\\bLEFT OUTER JOIN\\b", "LEFT OUTER JOIN", true},
				{"(?i)\\bRIGHT OUTER JOIN\\b", "RIGHT OUTER JOIN", true},
				{"(?i)\\bINNER JOIN\\b", "INNER JOIN", true},
				{"(?i)\\bFULL JOIN\\b", "FULL JOIN", true},
				{"(?i)\\bLEFT JOIN\\b", "LEFT JOIN", true},
				{"(?i)\\bRIGHT JOIN\\b", "RIGHT JOIN", true},
				{"(?i)\\bOUTER JOIN\\b", "OUTER JOIN", true},
				{"(?i)\\bCROSS JOIN\\b", "CROSS JOIN", true},

				// --- 其他关键字 ---
				{"(?i)\\bSELECT\\b", "SELECT", false},
				{"(?i)\\bFROM\\b", "FROM", true},
				{"(?i)\\bWHERE\\b", "WHERE", true},
				{"(?i)\\bGROUP BY\\b", "GROUP BY", true},
				{"(?i)\\bHAVING\\b", "HAVING", true},
				{"(?i)\\bORDER BY\\b", "ORDER BY", true},
				{"(?i)\\bSET\\b", "SET", true},
				{"(?i)\\bVALUES\\b", "VALUES", true},
				{"(?i)\\bON\\b", "ON", false},
				{"(?i)\\bINSERT\\b", "INSERT", true},
				{"(?i)\\bUPDATE\\b", "UPDATE", true},
				{"(?i)\\bDELETE\\b", "DELETE", true},
		};
		// 逐个应用规则（使用单词边界 \\b，忽略大小写）
		for (Object[] rule : rules) {
			String patternStr = (String) rule[0];
			String replacement = (String) rule[1];
			boolean newlineBefore = (boolean) rule[2];
			Pattern pattern = Pattern.compile(patternStr);
			Matcher matcher = pattern.matcher(sql);
			StringBuilder sb = new StringBuilder();
			while (matcher.find()) {
				String replacementWithLine = newlineBefore ? "\n" + replacement : replacement;
				matcher.appendReplacement(sb, replacementWithLine);
			}
			matcher.appendTail(sb);
			sql = sb.toString();
		}
		// 清理多余的换行
		sql = sql.trim().replaceAll("\n+", "\n").replaceAll("\n +", "\n");
		log.debug(sql);
	}
	
}