package cn.yshujia.mapper;

import cn.yshujia.admin.vo.AdminSelectRoleVO;
import cn.yshujia.domain.entity.Role;
import cn.yshujia.ui.vo.RoleVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author yshujia
 * @create 2024/4/23
 * @description dao持久层
 */

@Mapper
public interface RoleMapper extends BaseMapper<Role> {
	
	RoleVO oneGtId(@Param("id") Long id);
	
	List<RoleVO> list(Role role);
	
	List<RoleVO> listByAdmin(Role role);
	
	List<AdminSelectRoleVO> listSelectByAdmin();
	
}
