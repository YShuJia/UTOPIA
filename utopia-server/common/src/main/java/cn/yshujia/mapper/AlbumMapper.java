package cn.yshujia.mapper;

import cn.yshujia.admin.vo.AdminAlbumVO;
import cn.yshujia.domain.entity.Album;
import cn.yshujia.ui.vo.AlbumVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author yshujia
 * @create 2025/3/4
 * @description AlbumMapper
 */

@Mapper
public interface AlbumMapper extends BaseMapper<Album> {
	
	AlbumVO one(Album diary);
	
	List<AlbumVO> list(Album album);
	
	List<AdminAlbumVO> listByAdmin(Album album);
	
}
