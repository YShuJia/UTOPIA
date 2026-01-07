package cn.yshujia.service.impl;


import cn.yshujia.constant.ClassifyKeys;
import cn.yshujia.constant.RedisKeys;
import cn.yshujia.domain.entity.Article;
import cn.yshujia.mapper.ArticleMapper;
import cn.yshujia.ui.vo.ArchiveVO;
import cn.yshujia.ui.vo.ArticleLabelVO;
import cn.yshujia.ui.vo.ClassifyVO;
import cn.yshujia.utils.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: yshujia
 * @create: 2025/5/26 13:13
 * @description: ArchiveServiceImpl
 */

@Service
public class ArchiveServiceImpl extends ServiceImpl<ArticleMapper, Article> {

	@Resource
	RedisServiceImpl<ArchiveVO> redis;

	@Resource
	private ClassifyServiceImpl classifyService;

	@Resource
	private FileServiceImpl fileService;

	@Resource
	private ArticleMapper mapper;

	public List<ArchiveVO> selectArchiveList() {
		List<ArchiveVO> voList = redis.range(RedisKeys.ARTICLE_ARCHIVE, 0L, -1L);
		if (!CollectionUtils.isEmpty(voList)) {
			return voList;
		}
		voList = new ArrayList<>();
		List<ClassifyVO> list = classifyService.listByLikeKey(ClassifyKeys.ARTICLE);
		if (CollectionUtils.isEmpty(list)) {
			return new ArrayList<>();
		}
		for (ClassifyVO classifyVO : list) {
			ArchiveVO archiveVO = new ArchiveVO();
			archiveVO.setClassifyId(classifyVO.getId());
			archiveVO.setClassifyName(classifyVO.getName());
			List<ArticleLabelVO> labelList = mapper.listArticleLabelVO(classifyVO.getId());
			archiveVO.setLabelList(labelList);
			voList.add(archiveVO);
		}
		redis.rightPushAll(RedisKeys.ARTICLE_ARCHIVE, voList, RedisKeys.ONE_DAYS);
		return voList;
	}

}
