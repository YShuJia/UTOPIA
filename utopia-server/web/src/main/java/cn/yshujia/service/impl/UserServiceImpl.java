package cn.yshujia.service.impl;

import cn.yshujia.constant.DefaultConst;
import cn.yshujia.constant.HttpCode;
import cn.yshujia.constant.RedisKeys;
import cn.yshujia.domain.dto.UserDTO;
import cn.yshujia.domain.entity.Album;
import cn.yshujia.domain.entity.Article;
import cn.yshujia.domain.entity.Diary;
import cn.yshujia.domain.entity.User;
import cn.yshujia.encryption.AESEncrypt;
import cn.yshujia.ex.CustomException;
import cn.yshujia.ex.ServiceException;
import cn.yshujia.mapper.AlbumMapper;
import cn.yshujia.mapper.ArticleMapper;
import cn.yshujia.mapper.DiaryMapper;
import cn.yshujia.mapper.UserMapper;
import cn.yshujia.repository.SMRepository;
import cn.yshujia.service.EmailService;
import cn.yshujia.transform.UserTransform;
import cn.yshujia.ui.vo.*;
import cn.yshujia.utils.IDUtils;
import cn.yshujia.utils.RandomUtils;
import cn.yshujia.utils.TimeUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;

/**
 * @author yshujia
 * @create 2024/4/23
 * @description 服务层
 */

@Slf4j
@Service
@Transactional(rollbackFor = {Exception.class})
public class UserServiceImpl extends ServiceImpl<UserMapper, User> {

	@Resource
	public AlbumMapper albumMapper;

	@Resource
	public DiaryMapper diaryMapper;

	@Resource
	public ArticleMapper articleMapper;

	@Resource
	UserMapper mapper;

	@Resource
	RedisServiceImpl<Object> redis;

	@Resource
	PasswordEncoder passwordEncoder;

	@Resource
	FileServiceImpl fileService;
	@Resource
	EmailService emailService;
	@Resource(name = "Task")
	private Executor executor;
	@Resource
	private SMRepository smRepository;

	public String exchangeSm2Key(HttpServletRequest request) {
		// 返回后端公钥
		return smRepository.getPublicKey(request);
	}

	public Integer sendCode(String email) {
		String captcha = Optional.ofNullable(redis.get(RedisKeys.CAPTCHA + email))
				.map(Object::toString)
				.orElse(null);
		if (null == captcha) {
			// 生成验证码
			captcha = String.valueOf(RandomUtils.random(100000, 999999));
			redis.set(RedisKeys.CAPTCHA + email, captcha, RedisKeys.THREE_MINUTES);
			emailService.captchaRegister(email, captcha, 3);
		}
		return 180;
	}

	public SearchVO searchByTask(String search) {
		SearchVO searchVO = new SearchVO();
		CompletableFuture<List<AlbumVO>> album = getAlbumVO(search);
		CompletableFuture<List<ArticleVO>> article = getArticleVO(search);
		CompletableFuture<List<DiaryVO>> diary = getDiaryVO(search);
		//等待所有线程结束
		CompletableFuture<Void> result = CompletableFuture.allOf(article, album, diary);
		try {
			result.get(60, TimeUnit.SECONDS);
			searchVO.setArticle(article.get(60, TimeUnit.SECONDS));
			searchVO.setAlbum(album.get(60, TimeUnit.SECONDS));
			searchVO.setDiary(diary.get(60, TimeUnit.SECONDS));
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			log.error("全局搜索失败 {}", e.getMessage());
		}
		return searchVO;
	}

	private CompletableFuture<List<AlbumVO>> getAlbumVO(String search) {
		return CompletableFuture.supplyAsync(() -> albumMapper.list(new Album(search, true, 1)), executor);
	}

	private CompletableFuture<List<ArticleVO>> getArticleVO(String search) {
		return CompletableFuture.supplyAsync(() -> articleMapper.list(new Article(search, true, 1)), executor);
	}

	private CompletableFuture<List<DiaryVO>> getDiaryVO(String search) {
		return CompletableFuture.supplyAsync(() -> diaryMapper.list(new Diary(search, true, 1)), executor);
	}

	public UserVO oneById(Long userId) {
		UserVO userVO = (UserVO) redis.get(RedisKeys.USER + userId);
		if (null != userVO) {
			return userVO;
		}
		userVO = mapper.one(new User(userId, true));
		redis.set(RedisKeys.USER + userId, userVO, RedisKeys.THREE_DAYS);
		return userVO;
	}

	public Integer getExperience(Long id) {
		Integer experience = (Integer) redis.get(RedisKeys.EXPERIENCE + id + ":" + TimeUtils.getDay(new Date()));
		return null != experience ? experience : 0;
	}


	@Transactional(rollbackFor = {Exception.class})
	public void insert(String code, String email, String password) throws ServiceException {
		String captcha = Optional.ofNullable(redis.get(RedisKeys.CAPTCHA + email))
				.map(Object::toString)
				.orElse(null);
		if (null == captcha) {
			throw new ServiceException("验证码已过期！");
		}
		if (!code.equals(captcha)) {
			throw new ServiceException("验证码错误！");
		}
		// 判断邮箱是否重复
		User old = mapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getEmail, AESEncrypt.encrypt(email)));
		if (old != null) {
			throw new ServiceException(HttpCode.HAD_USER, "该账号已被注册！");
		}
		Long id = IDUtils.getId();
		User user = new User(id, "用户" + RandomUtils.randomIncludeMax(10000, 99999), passwordEncoder.encode(password), email, fileService.selectRandomIcon());
		user.setCreateBy(id);
		user.setUpdateBy(id);
		user.setRoleId(DefaultConst.MIN_ROLE_ID);
		try {
			int n = mapper.insert(user);
			if (n <= 0) {
				throw new ServiceException("注册失败，请稍后再试！");
			}
		} catch (Exception e) {
			throw new CustomException(e.getMessage());
		}
	}

	@Transactional(rollbackFor = {Exception.class})
	public void update(UserDTO dto) {
		User user = UserTransform.dtoToEntity(dto);
		try {
			int n = mapper.updateById(user);
			if (n <= 0) {
				throw new ServiceException("更新失败，请稍后再试！");
			}
			redis.delKey(RedisKeys.USER + user.getId());
		} catch (Exception e) {
			throw new CustomException(e.getMessage());
		}
	}

	@Transactional(rollbackFor = {Exception.class})
	public void updatePassword(Long id, String password, String newPassword) {
		User user = mapper.selectById(id);
		if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
			throw new ServiceException("旧密码错误！");
		}
		if (passwordEncoder.matches(newPassword, user.getPassword())) {
			throw new ServiceException("新密码不能与旧密码相同！");
		}
		try {
			int n = mapper.update(new LambdaUpdateWrapper<User>()
					.set(User::getPassword, passwordEncoder.encode(newPassword))
					.eq(User::getId, id));
			if (n <= 0) {
				throw new ServiceException("更新密码失败，请稍后再试！");
			}
		} catch (Exception e) {
			throw new CustomException(e.getMessage());
		}
	}

}