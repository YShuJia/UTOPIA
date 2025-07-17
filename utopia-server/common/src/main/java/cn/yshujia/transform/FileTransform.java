package cn.yshujia.transform;

import cn.yshujia.admin.vo.AdminFileVO;
import cn.yshujia.domain.dto.FileDTO;
import cn.yshujia.domain.entity.File;

/**
 * @author yshujia
 * @create 2025/2/24
 * @description FileTransform
 */

public class FileTransform {
	
	public static AdminFileVO entity2VO(File dto) {
		if (dto == null) {
			return null;
		}
		AdminFileVO adminFileVO = new AdminFileVO();
		adminFileVO.setLabelId(dto.getLabelId());
		adminFileVO.setName(dto.getName());
		adminFileVO.setUrl(dto.getUrl());
		adminFileVO.setSize(dto.getSize());
		adminFileVO.setTags(dto.getTags());
		adminFileVO.setReviewed(dto.getReviewed());
		adminFileVO.setId(dto.getId());
		adminFileVO.setCreateTime(dto.getCreateTime());
		adminFileVO.setUpdateTime(dto.getUpdateTime());
		adminFileVO.setCreateBy(dto.getCreateBy());
		adminFileVO.setUpdateBy(dto.getUpdateBy());
		adminFileVO.setEnabled(dto.getEnabled());
		return adminFileVO;
	}
	
	public static File dto2Entity(FileDTO dto) {
		if (dto == null) {
			return null;
		}
		File file = new File();
		file.setId(dto.getId());
		file.setLabelId(dto.getLabelId());
		file.setName(dto.getName());
		file.setUrl(dto.getUrl());
		file.setSize(dto.getSize());
		file.setTags(dto.getTags());
		file.setEnabled(dto.getEnabled());
		file.setReviewed(dto.getReviewed());
		file.setCreateBy(dto.getCreateBy());
		return file;
	}
	
}
