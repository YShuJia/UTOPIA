package cn.yshujia.handler.sensitive;

import cn.yshujia.utils.StringUtils;
import cn.yshujia.utils.WordReplaceUtils;
import com.github.houbb.sensitive.word.core.SensitiveWordHelper;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author yshujia
 * @create 2024/12/18
 * @description SensitiveHandler
 */

public class SensitiveHandler extends BaseTypeHandler<String> {
	
	WordReplaceUtils wordReplaceUtils = new WordReplaceUtils();
	
	@Override
	public void setNonNullParameter(PreparedStatement preparedStatement, int i, String s, JdbcType jdbcType) throws SQLException {
		if (StringUtils.isEmpty(s)) {
			return;
		}
		// 敏感词过滤
		String str = SensitiveWordHelper.replace(s, wordReplaceUtils);
		preparedStatement.setString(i, str);
	}
	
	
	@Override
	public String getNullableResult(ResultSet resultSet, String s) throws SQLException {
		return resultSet.getString(s);
	}
	
	@Override
	public String getNullableResult(ResultSet resultSet, int i) throws SQLException {
		return resultSet.getString(i);
	}
	
	@Override
	public String getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
		return callableStatement.getString(i);
	}
	
}


