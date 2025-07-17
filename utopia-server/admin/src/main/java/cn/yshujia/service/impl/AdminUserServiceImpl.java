package cn.yshujia.service.impl;

import cn.yshujia.admin.vo.AdminUserVO;
import cn.yshujia.constant.DefaultConst;
import cn.yshujia.constant.RedisKeys;
import cn.yshujia.domain.dto.UserDTO;
import cn.yshujia.domain.entity.User;
import cn.yshujia.domain.vo.PageVO;
import cn.yshujia.encryption.AESEncrypt;
import cn.yshujia.ex.CustomException;
import cn.yshujia.ex.ServiceException;
import cn.yshujia.mapper.UserMapper;
import cn.yshujia.transform.UserTransform;
import cn.yshujia.ui.vo.UserVO;
import cn.yshujia.utils.IDUtils;
import cn.yshujia.utils.PageUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yshujia
 * @create 2024/4/23
 * @description 服务层
 */

@Service
@Transactional(rollbackFor = {Exception.class})
public class AdminUserServiceImpl extends ServiceImpl<UserMapper, User> {
	
	@Resource
	public UserMapper mapper;
	
	@Resource
	RedisServiceImpl<UserVO> redis;
	
	@Resource
	BCryptPasswordEncoder passwordEncoder;
	
	public UserVO oneById(Long userId) {
		UserVO userVO = redis.get(RedisKeys.USER + userId);
		if (null != userVO) {
			return userVO;
		}
		userVO = mapper.one(new User(userId, true));
		redis.set(RedisKeys.USER + userId, userVO, RedisKeys.ONE_DAYS);
		return userVO;
	}
	
	public PageVO<AdminUserVO> pageAdmin(UserDTO dto) {
		User user = UserTransform.dtoToEntity(dto);
		List<AdminUserVO> list = mapper.listByAdmin(user);
		return PageUtils.page(list);
	}
	
	public Map<String, List<String>> selectCountGroupByRoleId() {
		List<UserVO> list = mapper.selectCountGroupByRoleId();
		List<String> keys = new ArrayList<>();
		List<String> values = new ArrayList<>();
		Map<String, List<String>> map = new HashMap<>();
		list.forEach(item -> {
			keys.add(item.getRoleName());
			values.add(item.getUserCount().toString());
		});
		map.put("keys", keys);
		map.put("values", values);
		return map;
	}
	
	@Transactional(rollbackFor = {Exception.class})
	public void insert(UserDTO dto) throws ServiceException {
		User user = UserTransform.dtoToEntity(dto);
		// 判断邮箱是否重复
		User old = mapper.selectOne(new LambdaQueryWrapper<User>()
				                            .eq(User::getEmail, AESEncrypt.encrypt(user.getEmail())));
		if (old != null) {
			throw new ServiceException("该账号已被注册！");
		}
		user.setId(IDUtils.getId());
		// 加密密码
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setAvatar(DefaultConst.ICON);
		try {
			int n = mapper.insert(user);
			if (n <= 0) {
				throw new ServiceException("用户添加失败，请稍后重试！");
			}
		} catch (Exception e) {
			throw new CustomException(e.getMessage());
		}
	}
	
	@Transactional(rollbackFor = {Exception.class})
	public void update(User user) {
		if (user.getPassword() != null) {
			user.setPassword(passwordEncoder.encode(user.getPassword()));
		}
		try {
			int n = mapper.updateById(user);
			if (n <= 0) {
				throw new ServiceException("用户不存在，无法更新！");
			}
			redis.delKey(RedisKeys.USER + user.getId());
		} catch (Exception e) {
			throw new CustomException(e.getMessage());
		}
	}
	
}