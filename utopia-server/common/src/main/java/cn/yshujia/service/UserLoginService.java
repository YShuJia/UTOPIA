package cn.yshujia.service;

import cn.yshujia.domain.LoginUser;
import cn.yshujia.domain.entity.User;
import cn.yshujia.encryption.AESEncrypt;
import cn.yshujia.mapper.UserMapper;
import cn.yshujia.ui.vo.UserVO;
import cn.yshujia.utils.RegexUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


/**
 * @author yshujia
 * @create 2024/4/23
 * @description 用户登录
 */

@Service
public class UserLoginService implements UserDetailsService {
	
	@Resource
	UserMapper userMapper;
	
	@Override
	public UserDetails loadUserByUsername(String username) {
		if (RegexUtils.isValidEmail(username)) {
			LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
			queryWrapper.eq(User::getEmail, AESEncrypt.encrypt(username));
			UserVO user = userMapper.one(new User(AESEncrypt.encrypt(username), true));
			if (user != null) {
				return new LoginUser(user.getId(), username, user.getPassword(), user.getRoleId(), user.getRoleAdmin(), user.getPermission());
			}
		}
		throw new UsernameNotFoundException("账号不存在！");
	}
	
}
