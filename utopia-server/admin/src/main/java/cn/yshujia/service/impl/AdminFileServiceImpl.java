package cn.yshujia.service.impl;


import cn.yshujia.admin.vo.AdminFileVO;
import cn.yshujia.constant.RedisKeys;
import cn.yshujia.domain.dto.FileDTO;
import cn.yshujia.domain.entity.File;
import cn.yshujia.domain.vo.PageVO;
import cn.yshujia.enums.MinioFolder;
import cn.yshujia.ex.CustomException;
import cn.yshujia.ex.ServiceException;
import cn.yshujia.mapper.FileMapper;
import cn.yshujia.transform.FileTransform;
import cn.yshujia.ui.vo.FileVO;
import cn.yshujia.utils.IDUtils;
import cn.yshujia.utils.MinioUtils;
import cn.yshujia.utils.PageUtils;
import cn.yshujia.utils.SecurityUtils;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.apache.ibatis.executor.BatchResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author: yshujia
 * @create: 2025/6/4 15:58
 * @description: AdminFileServiceImpl
 */

@Service
public class AdminFileServiceImpl extends ServiceImpl<FileMapper, File> {

	@Resource
	RedisServiceImpl<FileVO> redis;

	@Resource
	private FileMapper mapper;

	public PageVO<AdminFileVO> pageAdmin(FileDTO dto) {
		File file = FileTransform.dto2Entity(dto);
		List<AdminFileVO> list = mapper.listByAdmin(file);
		return PageUtils.page(list);
	}

	public PageVO<FileVO> pageSelect(FileDTO dto) {
		File file = FileTransform.dto2Entity(dto);
		List<FileVO> list = mapper.list(file);
		return PageUtils.page(list);
	}

	@Transactional(rollbackFor = {Exception.class})
	public void insert(MultipartFile[] files, FileDTO dto) {
		List<File> resourcesList = new ArrayList<>();
		Map<String, BigDecimal> map = MinioUtils.uploadUrlKb(files, MinioFolder.files);
		List<String> urls = new ArrayList<>(map.keySet());
		for (int i = 0; i < files.length; i++) {
			File file = new File();
			file.setId(IDUtils.getId());
			file.setUrl(urls.get(i));
			file.setSize(map.get(urls.get(i)));
			String name = files[i].getOriginalFilename();
			file.setName(Optional.ofNullable(name).map(str -> str.substring(0, name.lastIndexOf("."))).orElse(dto.getName()));
			file.setLabelId(dto.getLabelId());
			file.setTags(dto.getTags());
			resourcesList.add(file);
		}
		try {
			List<BatchResult> n = mapper.insert(resourcesList);
			if (n.isEmpty()) {
				throw new ServiceException("文件添加失败，请稍后重试！");
			}
			redis.delKeysByPrefix(RedisKeys.FILE_LABEL_ID);
		} catch (Exception e) {
			MinioUtils.delete(urls);
			throw new CustomException(e.getMessage());
		}
	}

	@Transactional(rollbackFor = {Exception.class})
	public void update(MultipartFile files, FileDTO dto) {
		File old = mapper.selectById(dto.getId());
		if (old == null) {
			throw new ServiceException("文件不存在，无法更新！");
		}
		File file = FileTransform.dto2Entity(dto);
		if (files != null) {
			Map<String, BigDecimal> map = MinioUtils.uploadUrlKb(files, MinioFolder.files);
			Set<String> urls = map.keySet();
			for (String url : urls) {
				file.setUrl(url);
				file.setSize(map.get(url));
			}
		}
		try {
			int n = mapper.update(file, SecurityUtils.createUpdateWrapper(file.getId()));
			if (n <= 0) {
				throw new ServiceException("文件不存在，无法更新！");
			}
			redis.delKeysByPrefix(RedisKeys.FILE_LABEL_ID);
			if (files != null) {
				MinioUtils.delete(old.getUrl());
			}
		} catch (Exception e) {
			throw new CustomException(e.getMessage());
		}
	}

	@Transactional(rollbackFor = {Exception.class})
	public void remove(List<Long> ids) {
		Wrapper<File> qw = SecurityUtils.createDeleteWrapper(ids);
		List<File> files = mapper.selectList(qw);
		try {
			int n = mapper.delete(qw);
			if (n < ids.size()) {
				throw new ServiceException("文件不存在，无法删除！");
			}
			MinioUtils.delete(files.stream().map(File::getUrl).toList());
			redis.delKeysByPrefix(RedisKeys.FILE_LABEL_ID);
		} catch (Exception e) {
			throw new CustomException(e.getMessage());
		}
	}

}
