package cn.yshujia.aop;

import cn.yshujia.annotation.Experience;
import cn.yshujia.constant.RedisKeys;
import cn.yshujia.constant.SystemConst;
import cn.yshujia.domain.LoginUser;
import cn.yshujia.domain.entity.Role;
import cn.yshujia.domain.entity.User;
import cn.yshujia.mapper.RoleMapper;
import cn.yshujia.mapper.UserMapper;
import cn.yshujia.service.impl.RedisServiceImpl;
import cn.yshujia.ui.vo.UserVO;
import cn.yshujia.utils.RequestUtils;
import cn.yshujia.utils.SecurityUtils;
import cn.yshujia.utils.TimeUtils;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;
import java.util.Optional;

/**
 * @author yshujia
 * @create 2025/3/30
 * @description SettlementExperience
 */

@Aspect
@Slf4j
@Component
public class SettlementExperience {
	
	@Resource
	public UserMapper mapper;
	
	@Resource
	public RoleMapper roleMapper;
	
	@Resource
	RedisServiceImpl<Object> redis;
	
	@Before("@annotation(experience)")
	public void doBefore(Experience experience) {
		int value = experience.value();
		// 更新用户经验值
		try {
			LoginUser loginUser = SecurityUtils.getLoginUser();
			Long id = loginUser.getUserId();
			String key = RedisKeys.EXPERIENCE + id + ":" + TimeUtils.getDay(new Date());
		    Integer experienceValue = (Integer) Optional.ofNullable(redis.get(key)).orElse(0);
			// 如果今日已获取经验值达到上限则不处理
			if (experienceValue > SystemConst.MAX_EXPERIENCE) {
				return;
			}
			Role role = roleMapper.selectOne(new LambdaUpdateWrapper<Role>()
					                                 .gt(Role::getId, loginUser.getRoleId())
					                                 .eq(Role::getAdmin, loginUser.getAdmin())
					.last("limit 1"));
			UserVO vo = (UserVO) redis.get(RedisKeys.USER + id);
			Integer total = vo.getExperience() + value;
			Long roleId = vo.getRoleId();
			if (role != null) {
				roleId = total >= role.getExperience() ? role.getId() : vo.getRoleId();
			}
			mapper.update(new LambdaUpdateWrapper<User>()
					              .eq(User::getId, vo.getId())
					              .set(User::getExperience, total)
					              .set(User::getRoleId, roleId));
			vo.setExperience(total);
			redis.set(RedisKeys.USER + id, vo, RedisKeys.THREE_DAYS);
			// 缓存今日获取的经验值
			redis.set(key, experienceValue + value, Duration.ofHours(24));
		} catch (Exception ignore) {
			log.info("{}: 用户未登录，不计算经验值！", RequestUtils.getIp());
		}
	}
	
}
