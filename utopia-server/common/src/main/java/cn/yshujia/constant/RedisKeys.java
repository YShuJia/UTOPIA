package cn.yshujia.constant;


import java.time.Duration;

/**
 * @author: yshujia
 * @create: 2025/5/26 11:17
 * @description: RedisKeys
 */
public class RedisKeys {
	
	public final static Duration THREE_MINUTES = Duration.ofMinutes(3L);
	
	public final static Duration TEN_MINUTES = Duration.ofMinutes(10L);
	
	public final static Duration ONE_DAYS = Duration.ofDays(1L);
	
	public final static Duration THREE_DAYS = Duration.ofDays(3L);
	
	public final static Duration SEVEN_DAYS = Duration.ofDays(7L);
	
	public final static Duration ONE_HOURS = Duration.ofHours(1L);
	
	public final static Duration TEN_HOURS = Duration.ofHours(10L);
	
	public final static Duration FOREVER = null;
	
	// 相册
	public final static String ALBUM = "album:";
	
	public final static String ALBUM_LABEL = "album:label:";
	
	// 文章
	public final static String ARTICLE = "article:";
	
	public final static String ARTICLE_LABEL = "article:label:";
	
	public final static String ARTICLE_ARCHIVE = "article:archive";
	
	public final static String ARTICLE_DEPLOY = "article:deploy";
	
	// 分类
	public final static String CLASSIFY = "classify:";
	
	public final static String CLASSIFY_TYPE = "classify:type:";
	
	public final static String CLASSIFY_ALL = "classify:admin";
	
	// 评论
	public final static String COMMENT_SOURCE = "comment:sourceId:";
	
	public final static String COMMENT_TREE_HOLE = "comment:leave_word";
	
	// 开发
	public final static String DEVELOP = "develop:";
	
	// 标签
	public final static String LABEL = "label:";
	
	public final static String LABEL_CLASSIFY = "label:classify:";
	
	// 点赞记录
	public final static String LIKE_USER_ID = "like:userId:";
	
	// 媒体
	public final static String FILE_LABEL_ID = "file:label:";
	
	// 角色
	public final static String ROLE = "role:";
	
	public final static String ROLE_LIST = "role:list";
	
	// 路由
	public final static String ROUTER_ROLE_ID = "router:roleId:";
	
	// 经验值
	public final static String EXPERIENCE = "experience:";
	
	// 用户
	public final static String USER = "user:";
	
	public final static String CAPTCHA = "captcha:";
	
	// 登录用户
	public final static String LOGIN_UUID = "login:uuid:";
	
	// 留言
	public final static String LEAVE_WORD_LIST = "leave_word:list";
	
	// 友情链接
	public final static String WEBSITE_LIST = "website:list";
	
	public final static String WEBSITE_LIST_RECOMMEND = "website:list:recommend:";
	
	// 日记
	public final static String DIARY = "diary:";
	
	public final static String DIARY_NOW = "diary:now";
	
	public final static String DIARY_LIST = "diary:list";
	
}