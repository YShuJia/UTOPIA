package cn.yshujia.handler;

import cn.yshujia.utils.SecurityUtils;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;

import java.util.Date;

/**
 * @author yshujia
 * @create 2024/4/23
 * @description mybatis-plus 自动填充字段
 */

@Slf4j
public class MetaHandler implements MetaObjectHandler {
	
	@Override
	public void insertFill(MetaObject metaObject) {
		strictInsertFill(metaObject, "createTime", Date.class, new Date());
		strictInsertFill(metaObject, "updateTime", Date.class, new Date());
		try {
			Long id = SecurityUtils.getUserId();
			if (id != null) {
				strictInsertFill(metaObject, "createBy", Long.class, id);
				strictInsertFill(metaObject, "updateBy", Long.class, id);
			}
		} catch (Exception ignored) {
		
		}
	}
	
	@Override
	public void updateFill(MetaObject metaObject) {
		try {
			Long id = SecurityUtils.getUserId();
			if (id != null) {
				strictUpdateFill(metaObject, "updateTime", Date.class, new Date());
				strictInsertFill(metaObject, "updateBy", Long.class, SecurityUtils.getUserId());
			}
		} catch (Exception ignored) {
		
		}
	}
	
}