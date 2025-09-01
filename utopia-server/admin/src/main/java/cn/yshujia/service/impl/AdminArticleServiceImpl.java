package cn.yshujia.service.impl;

import cn.yshujia.admin.vo.AdminArticleVO;
import cn.yshujia.constant.RedisKeys;
import cn.yshujia.domain.dto.ArticleDTO;
import cn.yshujia.domain.entity.Article;
import cn.yshujia.domain.vo.PageVO;
import cn.yshujia.ex.CustomException;
import cn.yshujia.ex.ServiceException;
import cn.yshujia.mapper.ArticleMapper;
import cn.yshujia.transform.ArticleTransform;
import cn.yshujia.ui.vo.ArticleVO;
import cn.yshujia.utils.*;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author yshujia
 * @create 2024/4/23
 * @description 服务层
 */

@Service
public class AdminArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> {

	@Resource
	public ArticleMapper mapper;

	@Resource
	RedisServiceImpl<ArticleVO> redis;

	@Resource
	BCryptPasswordEncoder passwordEncoder;

	public AdminArticleVO oneById(Long id, Long userId) {
		Article article = new Article(id, null, null, null);
		article.setCreateBy(userId);
		return mapper.oneByAdmin(article);
	}

	public PageVO<AdminArticleVO> pageAdmin(ArticleDTO dto) {
		Article article = ArticleTransform.dto2Entity(dto);
		List<AdminArticleVO> list = mapper.listByAdmin(article);
		return PageUtils.page(list);
	}

	@Transactional(rollbackFor = {Exception.class})
	public void insert(ArticleDTO dto) {
		dto.setId(IDUtils.getTimeId());
		if (StringUtils.isNotEmpty(dto.getPassword())) {
			dto.setPassword(passwordEncoder.encode(dto.getPassword()));
		}
		Set<String> newUrls = getArticleUrls(dto.getContent());
		List<String> urls = dto.getUrls() == null ? new ArrayList<>() : dto.getUrls();
		// 去除新上传后未使用的图片, 在 urls 中 移除 newUrls 中有的元素
		newUrls.forEach(urls::remove);
		try {
			int n = mapper.insert(ArticleTransform.dto2Entity(dto));
			if (n <= 0) {
				throw new ServiceException("文章不存在，无法更新！");
			}
			redis.delKeysByPrefix(RedisKeys.ARTICLE);
		} catch (Exception e) {
			throw new CustomException(e.getMessage());

		} finally {
			MinioUtils.delete(urls);
		}
	}

	/**
	 * @author: yshujia
	 * @create: 2025/4/12 17:59
	 * @description: update 更新文章
	 * @param: dto 更新后的文章数据，newUrls 可能含有新，旧图片地址
	 * @param: urls 新上传的图片地址集合
	 * @return: Boolean
	 */
	@Transactional(rollbackFor = {Exception.class})
	public void update(ArticleDTO dto) {
		// 如果没有内容，则直接更新文章
		if (dto.getUpdateStatus()) {
			int n = mapper.update(ArticleTransform.dto2Entity(dto), SecurityUtils.createUpdateWrapper(dto.getId()));
			if (n <= 0) {
				throw new ServiceException("文章不存在，无法更新！");
			}
		}
		// 获取旧文章中的图片地址，包含域名前缀
		AdminArticleVO vo = oneById(dto.getId(), dto.getCreateBy());
		Set<String> oldUrls = getArticleUrls(vo.getContent());
		// 新上传的所有图片地址，包含域名前缀
		List<String> urls = dto.getUrls();
		// 获取新文章中的所有图片地址（保留的图片，包含域名前缀）
		Set<String> newUrls = getArticleUrls(dto.getContent());
		// 密码加密
		if (StringUtils.isNotEmpty(dto.getPassword())) {
			dto.setPassword(passwordEncoder.encode(dto.getPassword()));
		}
		try {
			int n = mapper.update(ArticleTransform.dto2Entity(dto), SecurityUtils.createUpdateWrapper(dto.getId()));
			if (n <= 0) {
				throw new ServiceException("文章不存在，无法更新！");
			}
			redis.delKeysByPrefix(RedisKeys.ARTICLE);
			// 在 urls 中 移除 newUrls 中有的元素（筛选出新上传但是又在文章中移除的图片）
			newUrls.forEach(urls::remove);
			MinioUtils.delete(urls);
			// 在 oldUrls中 移除 newUrls 中有的元素（筛选出未保留的旧图片）
			newUrls.forEach(oldUrls::remove);
			MinioUtils.delete(oldUrls);
		} catch (Exception e) {
			throw new CustomException(e.getMessage());
		}
	}

	@Transactional(rollbackFor = Exception.class)
	public void remove(List<Long> ids) {
		Wrapper<Article> qw = SecurityUtils.createDeleteWrapper(ids);
		List<Article> list = mapper.selectList(qw);
		try {
			int n = mapper.delete(qw);
			if (n <= 0) {
				throw new ServiceException("文章不存在，无法删除！");
			}
			for (Article article : list) {
				MinioUtils.delete(getArticleUrls(article.getContent()));
			}
			redis.delKeysByPrefix(RedisKeys.ALBUM);
		} catch (Exception e) {
			throw new CustomException(e.getMessage());
		}
	}

	/**
	 * @author: yshujia
	 * @create: 2025/4/12 18:10
	 * @description: getArticleUrls 去除图片地址的域名
	 * @param: content
	 * @return: Set<String>
	 */
	private Set<String> getArticleUrls(String content) {
		if (StringUtils.isEmpty(content)) {
			return new HashSet<>();
		}
		Pattern pattern = Pattern.compile("(?:src|poster)\\s*=\\s*(['\"])(.*?)\\1", Pattern.DOTALL);
		Matcher matcher = pattern.matcher(content);
		// 用于存储 src 内容的列表
		Set<String> set = new HashSet<>();
		// 查找所有匹配项并提取 src 值
		while (matcher.find()) {
			// 将提取到的 src 添加到列表中
			set.add(matcher.group(2));
		}
		return set;
	}
}
