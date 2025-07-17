package cn.yshujia.mapper;

import cn.yshujia.admin.vo.AdminUserVO;
import cn.yshujia.domain.entity.User;
import cn.yshujia.ui.vo.UserVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.List;

/**
 * @author yshujia
 * @create 2024/4/23
 * @description dao持久层
 */

@Mapper
public interface UserMapper extends BaseMapper<User> {
	
	UserVO one(User user);
	
	List<UserVO> list(User user);
	
	List<UserVO> listByIds(Collection<Long> ids);
	
	List<UserVO> selectCountGroupByRoleId();
	
	List<AdminUserVO> listByAdmin(User user);
	
}
