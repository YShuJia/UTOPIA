package cn.yshujia.service.impl;

import cn.yshujia.admin.vo.AdminAlbumVO;
import cn.yshujia.constant.RedisKeys;
import cn.yshujia.domain.dto.AlbumDTO;
import cn.yshujia.domain.entity.Album;
import cn.yshujia.domain.vo.PageVO;
import cn.yshujia.enums.MinioFolder;
import cn.yshujia.ex.CustomException;
import cn.yshujia.ex.ServiceException;
import cn.yshujia.mapper.AlbumMapper;
import cn.yshujia.transform.AlbumTransform;
import cn.yshujia.ui.vo.AlbumVO;
import cn.yshujia.utils.*;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author yshujia
 * @create 2025/3/4
 * @description AdminAlbumServiceImpl
 */

@Service
public class AdminAlbumServiceImpl extends ServiceImpl<AlbumMapper, Album> {
	
	@Resource
	public AlbumMapper mapper;
	
	@Resource
	RedisServiceImpl<AlbumVO> redis;
	
	
	public PageVO<AdminAlbumVO> pageAdmin(AlbumDTO dto) {
		List<AdminAlbumVO> list = mapper.listByAdmin(AlbumTransform.dto2Entity(dto));
		return PageUtils.page(list);
	}
	
	@Transactional(rollbackFor = Exception.class)
	public void insert(MultipartFile[] files, AlbumDTO dto) {
		Album album = AlbumTransform.dto2Entity(dto);
		List<String> urls = MinioUtils.upload(files, MinioFolder.ALBUM);
		album.setUrls(urls);
		album.setId(IDUtils.getTimeId());
		try {
			int n = mapper.insert(album);
			if (n <= 0) {
				throw new ServiceException("相册添加失败, 请稍后重试！");
			}
			redis.delKeysByPrefix(RedisKeys.ALBUM);
		} catch (ServiceException e) {
			MinioUtils.delete(urls);
			throw e;
		} catch (Exception e) {
			MinioUtils.delete(urls);
			throw new CustomException(e.getMessage());
		}
	}
	
	/**
	 * @author: yshujia
	 * @create: 2025/4/12 16:30
	 * @description: update 更新相册
	 * @param: files 新添加的图片
	 * @param: album urls中的图片为更新时未删除的图片，为空时全删除
	 * @return: Boolean
	 */
	@Transactional(rollbackFor = Exception.class)
	public void update(MultipartFile[] files, AlbumDTO dto) {
		Album album = AlbumTransform.dto2Entity(dto);
		// 只单独更新状态
		if (dto.getUpdateStatus()) {
			update(dto);
			return;
		}
		// 获取旧日记
		AlbumVO oldAlbum = mapper.one(new Album(album.getId(), null, null, null));
		if (oldAlbum == null) {
			throw new ServiceException("相册不存在，无法更新！");
		}
		
		// 获取未删除的图片 为空表示全删除或者本身没有图片（包含域名）
		Set<String> remainUrls = new HashSet<>();
		if (album.getUrls() != null) {
			remainUrls = new HashSet<>(album.getUrls());
		}
		// 从原来的图片中筛选遗弃的图片（包含域名）
		Set<String> deletedUrls = CollectionUtils.removeHas(remainUrls, oldAlbum.getUrls());
		// 如果有图片，则上传图片
		List<String> newUrls = new ArrayList<>();
		if (!ObjectUtils.isEmpty(files)) {
			newUrls = MinioUtils.upload(files, MinioFolder.ALBUM);
		}
		// 重新设置 urls
		remainUrls.addAll(newUrls);
		album.setUrls(remainUrls.stream().toList());
		// 更新数据
		try {
			int n = mapper.update(album, SecurityUtils.createUpdateWrapper(album.getId()));
			if (n <= 0) {
				throw new ServiceException("相册不存在，无法更新！");
			}
			redis.delKeysByPrefix(RedisKeys.DIARY);
			// 删除 oldList 中有但 newList 中没有的图片
			MinioUtils.delete(deletedUrls);
		} catch (Exception e) {
			// 更新失败，删除新上传的图片
			MinioUtils.delete(newUrls);
			throw new CustomException(e.getMessage());
		}
	}
	
	@Transactional(rollbackFor = {Exception.class})
	public void update(AlbumDTO dto) {
		Album album = AlbumTransform.dto2Entity(dto);
		try {
			int r = mapper.update(
					album, SecurityUtils.createUpdateWrapper(album.getId()));
			
			if (r <= 0) {
				throw new ServiceException("相册不存在，无法更新！");
			}
			redis.delKeysByPrefix(RedisKeys.DIARY);
		} catch (Exception e) {
			throw new CustomException(e.getMessage());
		}
	}
	
	@Transactional(rollbackFor = Exception.class)
	public void remove(List<Long> ids) {
		Wrapper<Album> qw = SecurityUtils.createDeleteWrapper(ids);
		List<Album> list = mapper.selectList(qw);
		try {
			int n = mapper.delete(qw);
			if (n < ids.size()) {
				throw new ServiceException("相册不存在，无法删除！");
			}
			MinioUtils.delete(list.stream().map(Album::getUrls).flatMap(List::stream).toList());
			redis.delKeysByPrefix(RedisKeys.ALBUM);
		} catch (Exception e) {
			throw new CustomException(e.getMessage());
		}
	}
	
}
