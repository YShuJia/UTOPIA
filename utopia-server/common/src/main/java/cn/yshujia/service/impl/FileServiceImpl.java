package cn.yshujia.service.impl;

import cn.yshujia.constant.DefaultConst;
import cn.yshujia.constant.RedisKeys;
import cn.yshujia.domain.dto.FileDTO;
import cn.yshujia.domain.entity.File;
import cn.yshujia.domain.vo.PageVO;
import cn.yshujia.mapper.FileMapper;
import cn.yshujia.ui.vo.FileVO;
import cn.yshujia.utils.CollectionUtils;
import cn.yshujia.utils.PageUtils;
import cn.yshujia.utils.RandomUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author yshujia
 * @since 2024/4/23
 */
@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, File> {

	@Resource
	public FileMapper mapper;

	@Resource
	RedisServiceImpl<FileVO> redis;

	public PageVO<FileVO> pageAvatar(FileDTO dto) {
		Long labelId = dto.getLabelId();
		if (labelId == null) {
			labelId = DefaultConst.ICON_LABEL_ID;
		}
		return listCache(labelId);
	}

	public PageVO<FileVO> pageWall(FileDTO dto) {
		Long labelId = dto.getLabelId();
		if (labelId == null) {
			labelId = DefaultConst.WALL_LABEL_ID;
		}
		return listCache(labelId);
	}

	public PageVO<FileVO> listCache(Long labelId) {
		List<FileVO> voList = redis.range(RedisKeys.FILE_LABEL_ID + labelId, 0, -1);
		if (CollectionUtils.isEmpty(voList)) {
			voList = mapper.list(new File(labelId, true, 1));
			redis.rightPushAll(RedisKeys.FILE_LABEL_ID + labelId, voList, RedisKeys.ONE_DAYS);
		}
		return PageUtils.page(voList);
	}

	public String selectRandomIcon() {
		List<FileVO> list = redis.range(RedisKeys.FILE_LABEL_ID + DefaultConst.ICON_LABEL_ID, 0, -1);
		if (!CollectionUtils.isEmpty(list)) {
			return list.get(RandomUtils.random(list.size())).getUrl();
		}
		list = mapper.list(new File(DefaultConst.ICON_LABEL_ID, true, 1));
		if (!CollectionUtils.isEmpty(list)) {
			return list.get(RandomUtils.random(list.size())).getUrl();
		}
		return null;
	}

	public String selectRandomWall() {
		List<FileVO> list = redis.range(RedisKeys.FILE_LABEL_ID + DefaultConst.WALL_LABEL_ID, 0, -1);
		if (!CollectionUtils.isEmpty(list)) {
			return list.get(RandomUtils.random(list.size())).getUrl();
		}
		list = mapper.list(new File(DefaultConst.WALL_LABEL_ID, true, 1));
		if (!CollectionUtils.isEmpty(list)) {
			return list.get(RandomUtils.random(list.size())).getUrl();
		}
		return null;
	}

}
