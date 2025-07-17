package cn.yshujia.transform;

import cn.yshujia.admin.vo.AdminArticleVO;
import cn.yshujia.domain.dto.ArticleDTO;
import cn.yshujia.domain.entity.Article;

/**
 * @author yshujia
 * @create 2024/11/28
 * @description ArticleTransform
 */

public class ArticleTransform {
	
	public static AdminArticleVO entity2VO(Article article) {
		if (article == null) {
			return null;
		}
		AdminArticleVO adminArticleVO = new AdminArticleVO();
		adminArticleVO.setLabelId(article.getLabelId());
		adminArticleVO.setTitle(article.getTitle());
		adminArticleVO.setCover(article.getCover());
		adminArticleVO.setContent(article.getContent());
		adminArticleVO.setCopyright(article.getCopyright());
		adminArticleVO.setLikeCount(article.getLikeCount());
		adminArticleVO.setViewCount(article.getViewCount());
		adminArticleVO.setCommentCount(article.getCommentCount());
		adminArticleVO.setHasVideo(article.getHasVideo());
		adminArticleVO.setPassword(article.getPassword());
		adminArticleVO.setPasswordTip(article.getPasswordTip());
		adminArticleVO.setLikeable(article.getLikeable());
		adminArticleVO.setRecommend(article.getRecommend());
		adminArticleVO.setCommentable(article.getCommentable());
		adminArticleVO.setReviewed(article.getReviewed());
		adminArticleVO.setId(article.getId());
		adminArticleVO.setCreateTime(article.getCreateTime());
		adminArticleVO.setUpdateTime(article.getUpdateTime());
		adminArticleVO.setCreateBy(article.getCreateBy());
		adminArticleVO.setUpdateBy(article.getUpdateBy());
		adminArticleVO.setEnabled(article.getEnabled());
		return adminArticleVO;
	}
	
	
	public static Article dto2Entity(ArticleDTO dto) {
		if (dto == null) {
			return null;
		}
		Article article = new Article();
		article.setId(dto.getId());
		article.setLabelId(dto.getLabelId());
		article.setTitle(dto.getTitle());
		article.setCover(dto.getCover());
		article.setContent(dto.getContent());
		article.setCopyright(dto.getCopyright());
		article.setHasVideo(dto.getHasVideo());
		article.setPassword(dto.getPassword());
		article.setPasswordTip(dto.getPasswordTip());
		article.setRecommend(dto.getRecommend());
		article.setLikeable(dto.getLikeable());
		article.setCommentable(dto.getCommentable());
		article.setEnabled(dto.getEnabled());
		article.setReviewed(dto.getReviewed());
		article.setCreateBy(dto.getCreateBy());
		return article;
	}
	
}
