package cn.yshujia.mapper;


import cn.yshujia.admin.vo.AdminDiaryVO;
import cn.yshujia.domain.entity.Diary;
import cn.yshujia.ui.vo.DiaryVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author：yshujia
 * @create: 2025/5/1 21:20
 * @description: DiaryMapper
 */

@Mapper
public interface DiaryMapper extends BaseMapper<Diary> {
	
	DiaryVO now();
	
	DiaryVO one(Diary diary);
	
	List<DiaryVO> list(Diary diary);
	
	List<AdminDiaryVO> listByAdmin(Diary diary);
	
}
