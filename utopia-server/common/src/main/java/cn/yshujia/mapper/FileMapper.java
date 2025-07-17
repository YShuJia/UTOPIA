package cn.yshujia.mapper;

import cn.yshujia.admin.vo.AdminFileVO;
import cn.yshujia.domain.entity.File;
import cn.yshujia.ui.vo.FileVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author yshujia
 * @since 2024/4/23
 */
@Mapper
public interface FileMapper extends BaseMapper<File> {
	
	List<FileVO> list(File file);
	
	List<AdminFileVO> listByAdmin(File file);
	
}
