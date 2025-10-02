package cn.yshujia.service.impl;

import cn.yshujia.admin.vo.AdminSelectRoleVO;
import cn.yshujia.constant.RedisKeys;
import cn.yshujia.domain.dto.RoleDTO;
import cn.yshujia.domain.entity.Role;
import cn.yshujia.domain.vo.PageVO;
import cn.yshujia.ex.CustomException;
import cn.yshujia.ex.ServiceException;
import cn.yshujia.mapper.RoleMapper;
import cn.yshujia.repository.SysRepository;
import cn.yshujia.transform.RoleTransform;
import cn.yshujia.ui.vo.RoleVO;
import cn.yshujia.utils.PageUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author yshujia
 * @create 2024/4/23
 * @description 服务层
 */

@Service
public class AdminRoleServiceImpl extends ServiceImpl<RoleMapper, Role> {

	@Resource
	public RoleMapper mapper;

	@Resource
	RedisServiceImpl<RoleVO> redis;

	@Resource
	SysRepository sysRepository;

	public PageVO<RoleVO> pageByAdmin(RoleDTO dto) {
		Role role = RoleTransform.dto2entity(dto);
		List<RoleVO> list = mapper.listByAdmin(role);
		return PageUtils.page(list);
	}

	public List<AdminSelectRoleVO> listSelectData() {
		return mapper.listSelectByAdmin();
	}

	public List<String> table() {
		return sysRepository.getSysConfig().getSysRoleTable();
	}

	@Transactional(rollbackFor = Exception.class)
	public void update(RoleDTO dto) {
		Role role = RoleTransform.dto2entity(dto);
		try {
			int n = mapper.updateById(role);
			if (n <= 0) {
				throw new ServiceException("角色不存在，无法更新！");
			}
			redis.delKeysByPrefix(RedisKeys.ROLE);
		} catch (Exception e) {
			throw new CustomException(e.getMessage());
		}
	}

}