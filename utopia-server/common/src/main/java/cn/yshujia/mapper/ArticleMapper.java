package cn.yshujia.mapper;

import cn.yshujia.admin.vo.AdminArticleVO;
import cn.yshujia.domain.entity.Article;
import cn.yshujia.ui.vo.ArticleLabelVO;
import cn.yshujia.ui.vo.ArticleVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 文章 Mapper 接口
 * </p>
 *
 * @author yshujia
 * @since 2024/4/23
 */
@Mapper
public interface ArticleMapper extends BaseMapper<Article> {
	
	ArticleVO one(Article article);
	
	List<ArticleVO> list(Article article);
	
	List<ArticleVO> listByLabelIds(@Param("ids") List<Long> ids);
	
	List<ArticleLabelVO> listArticleLabelVO(@Param("id") Long id);
	
	List<AdminArticleVO> listByAdmin(Article article);
	
	AdminArticleVO oneByAdmin(Article article);
	
}
