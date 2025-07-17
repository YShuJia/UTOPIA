package cn.yshujia.transform;

import cn.yshujia.admin.vo.AdminAlbumVO;
import cn.yshujia.domain.dto.AlbumDTO;
import cn.yshujia.domain.entity.Album;

/**
 * @author yshujia
 * @create 2025/3/4
 * @description AlbumTransform
 */

public class AlbumTransform {
	
	public static AdminAlbumVO entity2VO(Album album) {
		if (album == null) {
			return null;
		}
		AdminAlbumVO adminAlbumVO = new AdminAlbumVO();
		adminAlbumVO.setLabelId(album.getLabelId());
		adminAlbumVO.setTitle(album.getTitle());
		adminAlbumVO.setUrls(album.getUrls());
		adminAlbumVO.setIntroduction(album.getIntroduction());
		adminAlbumVO.setLikeable(album.getLikeable());
		adminAlbumVO.setRecommend(album.getRecommend());
		adminAlbumVO.setCommentable(album.getCommentable());
		adminAlbumVO.setReviewed(album.getReviewed());
		adminAlbumVO.setId(album.getId());
		adminAlbumVO.setCreateTime(album.getCreateTime());
		adminAlbumVO.setUpdateTime(album.getUpdateTime());
		adminAlbumVO.setCreateBy(album.getCreateBy());
		adminAlbumVO.setUpdateBy(album.getUpdateBy());
		adminAlbumVO.setEnabled(album.getEnabled());
		return adminAlbumVO;
	}
	
	public static Album dto2Entity(AlbumDTO dto) {
		if (dto == null) {
			return null;
		}
		Album album = new Album();
		album.setId(dto.getId());
		album.setLabelId(dto.getLabelId());
		album.setTitle(dto.getTitle());
		album.setUrls(dto.getUrls());
		album.setIntroduction(dto.getIntroduction());
		album.setRecommend(dto.getRecommend());
		album.setLikeable(dto.getLikeable());
		album.setCommentable(dto.getCommentable());
		album.setEnabled(dto.getEnabled());
		album.setReviewed(dto.getReviewed());
		album.setCreateBy(dto.getCreateBy());
		return album;
	}
	
}
