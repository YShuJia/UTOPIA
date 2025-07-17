package cn.yshujia.service.impl;

import cn.yshujia.admin.vo.AdminDiaryVO;
import cn.yshujia.constant.RedisKeys;
import cn.yshujia.domain.dto.DiaryDTO;
import cn.yshujia.domain.entity.Diary;
import cn.yshujia.domain.vo.PageVO;
import cn.yshujia.enums.MinioFolder;
import cn.yshujia.ex.CustomException;
import cn.yshujia.ex.ServiceException;
import cn.yshujia.mapper.DiaryMapper;
import cn.yshujia.transform.DiaryTransform;
import cn.yshujia.ui.vo.DiaryVO;
import cn.yshujia.utils.*;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author yshujia
 * @since 2024/4/23
 */
@Service
public class AdminDiaryServiceImpl extends ServiceImpl<DiaryMapper, Diary> {
	
	@Resource
	public DiaryMapper mapper;
	
	@Resource
	RedisServiceImpl<DiaryVO> redis;
	
	
	public PageVO<AdminDiaryVO> pageAdmin(DiaryDTO dto) {
		List<AdminDiaryVO> list = mapper.listByAdmin(DiaryTransform.dto2Entity(dto));
		return PageUtils.page(list);
	}
	
	@Transactional(rollbackFor = {Exception.class})
	public void insert(MultipartFile[] files, DiaryDTO dto) {
		Diary diary = DiaryTransform.dto2Entity(dto);
		List<String> urls = new ArrayList<>();
		// 上传图片
		if (!ObjectUtils.isEmpty(files)) {
			urls = MinioUtils.upload(files, MinioFolder.DIARY);
		}
		diary.setId(IDUtils.getTimeId());
		diary.setUrls(urls);
		diary.setYear(TimeUtils.getYear(new Date()));
		diary.setMonth(TimeUtils.getMonth(new Date()));
		try {
			int n = mapper.insert(diary);
			if (n <= 0) {
				throw new ServiceException("日记添加失败，请稍后重试！");
			}
			redis.delKeysByPrefix(RedisKeys.DIARY);
		} catch (Exception e) {
			MinioUtils.delete(urls);
			throw new CustomException(e.getMessage());
		}
	}
	
	
	@Transactional(rollbackFor = {Exception.class})
	public void update(MultipartFile[] files, DiaryDTO dto) {
		// 只单独更新状态
		if (dto.getUpdateStatus()) {
			update(dto);
			return;
		}
		
		Diary diary = DiaryTransform.dto2Entity(dto);
		// 获取旧日记
		DiaryVO oldDiary = mapper.one(new Diary(diary.getId(), null, null));
		if (oldDiary == null) {
			throw new ServiceException("日记不存在，无法更新！");
		}
		
		// 获取未删除的图片 为空表示全删除或者本身没有图片（包含域名）
		Set<String> remainUrls = new HashSet<>();
		if (diary.getUrls() != null) {
			remainUrls = new HashSet<>(diary.getUrls());
		}
		// 从原来的图片中筛选遗弃的图片（包含域名）
		Set<String> deletedUrls = CollectionUtils.removeHas(remainUrls, oldDiary.getUrls());
		// 如果有图片，则上传图片
		List<String> newUrls = new ArrayList<>();
		if (!ObjectUtils.isEmpty(files)) {
			newUrls = MinioUtils.upload(files, MinioFolder.DIARY);
		}
		// 重新设置 urls
		remainUrls.addAll(newUrls);
		diary.setUrls(remainUrls.stream().toList());
		// 更新数据
		try {
			int n = mapper.update(diary, SecurityUtils.createUpdateWrapper(diary.getId()));
			if (n <= 0) {
				throw new ServiceException("日记不存在，无法更新！");
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
	public void update(DiaryDTO dto) {
		Diary diary = DiaryTransform.dto2Entity(dto);
		try {
			int r = mapper.update(diary, SecurityUtils.createUpdateWrapper(diary.getId()));
			if (r <= 0) {
				throw new ServiceException("日记不存在，无法更新！");
			}
			redis.delKeysByPrefix(RedisKeys.DIARY);
		} catch (Exception e) {
			throw new CustomException(e.getMessage());
		}
	}
	
	@Transactional(rollbackFor = {Exception.class})
	public void remove(List<Long> ids) {
		Wrapper<Diary> qw = SecurityUtils.createDeleteWrapper(ids);
		List<Diary> list = mapper.selectList(qw);
		try {
			int r = mapper.delete(qw);
			if (r < ids.size()) {
				throw new ServiceException("日记不存在，无法删除！");
			}
			MinioUtils.delete(list.stream().map(Diary::getUrls).flatMap(List::stream).toList());
			redis.delKeysByPrefix(RedisKeys.DIARY);
		} catch (Exception e) {
			throw new CustomException(e.getMessage());
		}
	}
	
}
