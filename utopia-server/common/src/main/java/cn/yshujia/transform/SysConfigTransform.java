package cn.yshujia.transform;


import cn.yshujia.domain.dto.SysConfigDTO;
import cn.yshujia.domain.entity.SysConfig;

/**
 * @author: yshujia
 * @create: 2025/9/14 10:06
 * @description: SysConfigTransform 系统配置转换类
 */
public class SysConfigTransform {

	public static SysConfig dto2Entity(SysConfigDTO dto) {
		if (dto == null) {
			return null;
		}
		SysConfig sysConfig = new SysConfig();
		sysConfig.setId(dto.getId());
		sysConfig.setName(dto.getName());
		sysConfig.setSysPasswordCount(dto.getSysPasswordCount());
		sysConfig.setSysPasswordTime(dto.getSysPasswordTime());
		sysConfig.setSysPasswordBan(dto.getSysPasswordBan());
		sysConfig.setSysLimitCount(dto.getSysLimitCount());
		sysConfig.setSysLimitTime(dto.getSysLimitTime());
		sysConfig.setSysLimitBan(dto.getSysLimitBan());
		sysConfig.setSysReplaceChar(dto.getSysReplaceChar());
		sysConfig.setSysMaxExp(dto.getSysMaxExp());
		sysConfig.setSysRoleTable(dto.getSysRoleTable());
		sysConfig.setEnabled(dto.getEnabled());
		return sysConfig;
	}
}
