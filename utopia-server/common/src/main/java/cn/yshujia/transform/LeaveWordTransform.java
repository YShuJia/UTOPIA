package cn.yshujia.transform;


import cn.yshujia.domain.dto.LeaveWordDTO;
import cn.yshujia.domain.entity.LeaveWord;

/**
 * @author: yshujia
 * @create: 2025/6/14 20:39
 * @description: LeaveWordTransform
 */
public class LeaveWordTransform {
	
	public static LeaveWord dto2entity(LeaveWordDTO dto) {
		if (dto == null) {
			return null;
		}
		LeaveWord leaveWord = new LeaveWord();
		leaveWord.setId(dto.getId());
		leaveWord.setIp(dto.getIp());
		leaveWord.setAvatar(dto.getAvatar());
		leaveWord.setContent(dto.getContent());
		return leaveWord;
	}
	
}
