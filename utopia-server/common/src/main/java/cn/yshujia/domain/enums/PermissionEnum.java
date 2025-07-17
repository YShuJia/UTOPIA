package cn.yshujia.domain.enums;

import lombok.Getter;

/**
 * @author yshujia
 * @create 2025/3/9
 * @description PermissionEnum
 */

@Getter
public enum PermissionEnum {
	
	ALBUM_INSERT("album:insert"),
	ALBUM_UPDATE("album:update"),
	ALBUM_DELETE("album:delete"),
	ARTICLE_INSERT("article:insert"),
	ARTICLE_UPDATE("article:update"),
	ARTICLE_DELETE("article:delete"),
	COMMENT_INSERT("comment:insert"),
	COMMENT_UPDATE("comment:update"),
	COMMENT_DELETE("comment:delete"),
	LIKE_INSERT("like:insert"),
	LIKE_UPDATE("like:update"),
	LIKE_DELETE("like:delete"),
	USER_INSERT("user:insert"),
	USER_UPDATE("user:update"),
	USER_DELETE("user:delete"),
	ROLE_INSERT("role:insert"),
	ROLE_UPDATE("role:update"),
	ROLE_DELETE("role:delete"),
	RESOURCES_INSERT("file:insert"),
	RESOURCES_UPDATE("file:update"),
	RESOURCES_DELETE("file:delete"),
	ROUTER_INSERT("router:insert"),
	ROUTER_UPDATE("router:update"),
	ROUTER_DELETE("router:delete"),
	WEBSITE_INSERT("website:insert"),
	WEBSITE_UPDATE("website:update"),
	WEBSITE_DELETE("website:delete"),
	TREE_HOLE_INSERT("leave_word:insert"),
	TREE_HOLE_UPDATE("leave_word:update"),
	TREE_HOLE_DELETE("leave_word:delete"),
	CLASSIFY_INSERT("classify:insert"),
	CLASSIFY_UPDATE("classify:update"),
	CLASSIFY_DELETE("classify:delete"),
	LABEL_INSERT("label:insert"),
	LABEL_UPDATE("label:update"),
	LABEL_DELETE("label:delete"),
	USER_ALL("user:admin"),
	ARTICLE_ALL("article:admin"),
	ALBUM_ALL("album:admin"),
	COMMENT_ALL("comment:admin"),
	LIKE_ALL("like:admin"),
	ROLE_ALL("role:admin"),
	RESOURCES_ALL("file:admin"),
	ROUTER_ALL("router:admin"),
	TREE_HOLE_ALL("leave_word:admin"),
	WEBSITE_ALL("website:admin"),
	CLASSIFY_ALL("classify:admin"),
	LABEL_ALL("label:admin");
	
	private final String value;
	
	PermissionEnum(String value) {
		this.value = value;
	}
}
