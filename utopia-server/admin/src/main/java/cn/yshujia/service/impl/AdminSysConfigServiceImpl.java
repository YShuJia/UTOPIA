package cn.yshujia.service.impl;

import cn.yshujia.domain.dto.SysConfigDTO;
import cn.yshujia.domain.entity.SysConfig;
import cn.yshujia.domain.vo.PageVO;
import cn.yshujia.ex.CustomException;
import cn.yshujia.ex.ServiceException;
import cn.yshujia.mapper.SysConfigMapper;
import cn.yshujia.repository.SysRepository;
import cn.yshujia.transform.SysConfigTransform;
import cn.yshujia.ui.vo.AlbumVO;
import cn.yshujia.utils.IDUtils;
import cn.yshujia.utils.PageUtils;
import cn.yshujia.utils.SecurityUtils;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author YShuJia
 * @since 2024/4/23
 */

@Service
public class AdminSysConfigServiceImpl extends ServiceImpl<SysConfigMapper, SysConfig> implements IService<SysConfig> {

	@Resource
	public SysConfigMapper mapper;

	@Resource
	RedisServiceImpl<AlbumVO> redis;

	@Resource(name = "dataSource")
	private DataSource dataSource;

	@Value("${spring.datasource.url}")
	private String url;

	@Resource
	private SysRepository sysRepository;

	public List<String> listTable() {
		// 正则：匹配 jdbc:mysql://host:port/dbname
		String regex = "jdbc:mysql://[^/]+/([^/?]+)";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(url);
		String dbName = "";
		try (Connection conn = dataSource.getConnection()) {
			if (!matcher.find()) {
				throw new CustomException("获取数据库名称失败！");
			}
			dbName = matcher.group(1);
			List<String> tableNames = new ArrayList<>();
			DatabaseMetaData metaData = conn.getMetaData();
			String[] types = {"TABLE"};
			ResultSet rs = metaData.getTables(dbName, null, "%", types);
			while (rs.next()) {
				String tableName = rs.getString("TABLE_NAME").replaceAll("t_", "");
				tableNames.add(tableName);
			}
			return tableNames;
		} catch (SQLException e) {
			throw new CustomException(e.getMessage());
		}
	}

	public PageVO<SysConfig> pageAdmin(SysConfigDTO dto) {
		List<SysConfig> list = mapper.listByAdmin(SysConfigTransform.dto2Entity(dto));
		return PageUtils.page(list);
	}

	@Transactional(rollbackFor = Exception.class)
	public void insert(SysConfigDTO dto) {
		SysConfig sysConfig = SysConfigTransform.dto2Entity(dto);
		sysConfig.setId(IDUtils.getTimeId());
		try {
			int n = mapper.insert(sysConfig);
			if (n <= 0) {
				throw new ServiceException("配置添加失败, 请稍后重试！");
			}
		} catch (Exception e) {
			throw new CustomException(e.getMessage());
		}
	}

	@Transactional(rollbackFor = Exception.class)
	public void update(SysConfigDTO dto) {
		SysConfig sysConfig = SysConfigTransform.dto2Entity(dto);
		// 禁用其他配置
		if (dto.getEnabled()) {
			mapper.update(new LambdaUpdateWrapper<SysConfig>().set(SysConfig::getEnabled, false));
		}
		// 更新数据
		try {
			int n = mapper.updateById(sysConfig);
			if (n <= 0) {
				throw new ServiceException("配置不存在，无法更新！");
			}
			sysRepository.removeSysConfig();
		} catch (Exception e) {
			throw new CustomException(e.getMessage());
		}
	}

	@Transactional(rollbackFor = Exception.class)
	public void remove(List<Long> ids) {
		Wrapper<SysConfig> qw = SecurityUtils.createDeleteWrapper(ids);
		List<SysConfig> list = mapper.selectList(qw);
		try {
			int n = mapper.delete(qw);
			if (n < ids.size()) {
				throw new ServiceException("配置不存在，无法删除！");
			}
			sysRepository.removeSysConfig();
		} catch (Exception e) {
			throw new CustomException(e.getMessage());
		}
	}
}
