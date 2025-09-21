package cn.yshujia.handler.sensitive;

import cn.yshujia.encryption.AESEncrypt;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Component;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author yshujia
 * @create 2024/4/23
 * @description 数据加解密 处理器
 */

@Component
public class EncryptionHandler extends BaseTypeHandler<String> {
	
	@Override
	public void setNonNullParameter(PreparedStatement preparedStatement, int i, String s, JdbcType jdbcType) throws
			SQLException {
		preparedStatement.setString(i, AESEncrypt.encrypt(s));
	}
	
	@Override
	public String getNullableResult(ResultSet resultSet, String s) throws SQLException {
		return AESEncrypt.decrypt(resultSet.getString(s));
	}
	
	@Override
	public String getNullableResult(ResultSet resultSet, int i) throws SQLException {
		return AESEncrypt.decrypt(resultSet.getString(i));
	}
	
	@Override
	public String getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
		return AESEncrypt.decrypt(callableStatement.getString(i));
	}
	
}
