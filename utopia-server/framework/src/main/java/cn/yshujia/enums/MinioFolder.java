package cn.yshujia.enums;

import lombok.Getter;

/**
 * @author yshujia
 * @create 2025/3/4
 * @description MinioFolder minio文件上传文件夹枚举
 */

@Getter
public enum MinioFolder {
	
	// 系统资源文件夹
	files("/files"),
	// 评论图片文件夹
	COMMENT("/comment"),
	// 文章图片文件夹
	ARTICLE("/article"),
	// 相册图片文件夹
	ALBUM("/album"),
	// 日记图片文件夹
	DIARY("/diary");
	
	private final String folder;
	
	MinioFolder(String folder) {
		this.folder = folder;
	}
}
