package cn.yshujia.service.impl;

import cn.yshujia.domain.dto.WebConfigDTO;
import cn.yshujia.domain.entity.WebConfig;
import cn.yshujia.domain.vo.PageVO;
import cn.yshujia.ex.CustomException;
import cn.yshujia.ex.ServiceException;
import cn.yshujia.mapper.WebConfigMapper;
import cn.yshujia.repository.WebRepository;
import cn.yshujia.transform.WebConfigTransform;
import cn.yshujia.utils.IDUtils;
import cn.yshujia.utils.PageUtils;
import cn.yshujia.utils.SecurityUtils;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author YShuJia
 * @since 2024/4/23
 */
@Service
public class AdminWebConfigServiceImpl extends ServiceImpl<WebConfigMapper, WebConfig> implements IService<WebConfig> {

	@Resource
	public WebConfigMapper mapper;

	@Resource
	private WebRepository webRepository;

	public WebConfig oneById(Long id) {
		return mapper.selectOne(new LambdaQueryWrapper<WebConfig>()
				.eq(WebConfig::getId, id));
	}

	public PageVO<WebConfig> pageAdmin(WebConfigDTO dto) {
		List<WebConfig> list = mapper.listByAdmin(WebConfigTransform.dto2Entity(dto));
		return PageUtils.page(list);
	}

	@Transactional(rollbackFor = Exception.class)
	public void insert(WebConfigDTO dto) {
		WebConfig webConfig = WebConfigTransform.dto2Entity(dto);
		webConfig.setId(IDUtils.getTimeId());
		try {
			int n = mapper.insert(webConfig);
			if (n <= 0) {
				throw new ServiceException("配置添加失败, 请稍后重试！");
			}
		} catch (Exception e) {
			throw new CustomException(e.getMessage());
		}
	}

	@Transactional(rollbackFor = Exception.class)
	public void update(WebConfigDTO dto) {
		WebConfig old = oneById(dto.getId());
		WebConfig webConfig = WebConfigTransform.dto2Entity(dto);
		// 禁用其他配置
		if (dto.getEnabled()) {
			mapper.update(new LambdaUpdateWrapper<WebConfig>().set(WebConfig::getEnabled, false));
		}
		// 确保有一个配置打开
		if (old.getEnabled() && !dto.getEnabled()) {
			dto.setEnabled(true);
		}
		// 更新数据
		try {
			int n = mapper.updateById(webConfig);
			if (n <= 0) {
				throw new ServiceException("配置不存在，无法更新！");
			}
			webRepository.removeWebConfig();
		} catch (Exception e) {
			throw new CustomException(e.getMessage());
		}
	}

	@Transactional(rollbackFor = Exception.class)
	public void remove(List<Long> ids) {
		Wrapper<WebConfig> qw = SecurityUtils.createDeleteWrapper(ids);
		try {
			int n = mapper.delete(qw);
			if (n < ids.size()) {
				throw new ServiceException("配置不存在，无法删除！");
			}
			webRepository.removeWebConfig();
		} catch (Exception e) {
			throw new CustomException(e.getMessage());
		}
	}
}
