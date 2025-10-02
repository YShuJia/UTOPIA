package cn.yshujia.domain.dto;


import cn.yshujia.validation.InsertGroup;
import cn.yshujia.validation.UpdateGroup;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;

/**
 * @author: yshujia
 * @create: 2125/9/14 19:51
 * @description: SysConfigDTO 系统配置DTO
 */

@Data
public class SysConfigDTO {

	@Positive(groups = UpdateGroup.class, message = "[路由ID] 不能为空！")
	private Long id;

	@NotBlank(groups = InsertGroup.class, message = "[配置名] 不能为空！")
	@Size(groups = {InsertGroup.class, UpdateGroup.class}, max = 16, message = "[配置名] 长度不能超过 16！")
	private String name;

	@PositiveOrZero(message = "[密码试错次数] 必须为正整数！")
	@Max(value = 255, groups = {InsertGroup.class, UpdateGroup.class}, message = "[密码试错次数] 值范围 [1, 255]！")
	@Min(value = 1, groups = {InsertGroup.class, UpdateGroup.class}, message = "[密码试错次数] 值范围 [1, 255]！")
	private Byte sysPasswordCount;

	@PositiveOrZero(message = "[密码试错间隔时间] 必须为正整数！")
	@Max(value = 255, groups = {InsertGroup.class, UpdateGroup.class}, message = "[密码试错间隔时间] 值范围 [1, 255]！")
	@Min(value = 1, groups = {InsertGroup.class, UpdateGroup.class}, message = "[密码试错间隔时间] 值范围 [1, 255]！")
	private Byte sysPasswordTime;

	@PositiveOrZero(message = "[密码试错封禁账号、IP时间] 必须为正整数！")
	@Max(value = 255, groups = {InsertGroup.class, UpdateGroup.class}, message = "[密码试错封禁账号、IP时间] 值范围 [1, 255]！")
	@Min(value = 1, groups = {InsertGroup.class, UpdateGroup.class}, message = "[密码试错封禁账号、IP时间] 值范围 [1, 255]！")
	private Byte sysPasswordBan;

	@PositiveOrZero(message = "[触发接口限流次数] 必须为正整数！")
	@Max(value = 255, groups = {InsertGroup.class, UpdateGroup.class}, message = "[触发接口限流次数] 值范围 [1, 255]！")
	@Min(value = 1, groups = {InsertGroup.class, UpdateGroup.class}, message = "[触发接口限流次数] 值范围 [1, 255]！")
	private Byte sysLimitCount;

	@PositiveOrZero(message = "[触发接口限流间隔时间] 必须为正整数！")
	@Max(value = 255, groups = {InsertGroup.class, UpdateGroup.class}, message = "[触发接口限流间隔时间] 值范围 [1, 255]！")
	@Min(value = 1, groups = {InsertGroup.class, UpdateGroup.class}, message = "[触发接口限流间隔时间] 值范围 [1, 255]！")
	private Byte sysLimitTime;

	@PositiveOrZero(message = "[触发限流封禁IP时间] 必须为正整数！")
	@Max(value = 255, groups = {InsertGroup.class, UpdateGroup.class}, message = "[触发限流封禁IP时间] 值范围 [1, 255]！")
	@Min(value = 1, groups = {InsertGroup.class, UpdateGroup.class}, message = "[触发限流封禁IP时间] 值范围 [1, 255]！")
	private Byte sysLimitBan;

	@NotNull(groups = InsertGroup.class, message = "[敏感词替换符] 不能为空！")
	@Size(groups = {InsertGroup.class, UpdateGroup.class}, max = 16, message = "[敏感词替换符] 长度不能超过 16！")
	private String sysReplaceChar;

	@PositiveOrZero(message = "[最大经验值] 必须为正整数！")
	@Max(value = 65535, groups = {InsertGroup.class, UpdateGroup.class}, message = "[最大经验值] 值范围 [0, 65535]！")
	@Min(value = 0, groups = {InsertGroup.class, UpdateGroup.class}, message = "[最大经验值] 值范围 [0, 65535]！")
	private Short sysMaxExp;

	@NotEmpty(groups = InsertGroup.class, message = "[生成角色权限的表配置] 不能为空！")
	private List<String> sysRoleTable;

	private Boolean enabled;
}
