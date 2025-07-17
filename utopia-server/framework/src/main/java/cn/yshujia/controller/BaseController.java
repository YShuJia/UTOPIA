package cn.yshujia.controller;


import cn.yshujia.constant.HttpCode;
import cn.yshujia.domain.LoginUser;
import cn.yshujia.domain.vo.ApiVO;
import cn.yshujia.utils.PageUtils;
import cn.yshujia.utils.SecurityUtils;

/**
 * @author：yshujia
 * @create: 2025/5/14 18:41
 * @description: BaseController 基类
 */
public class BaseController {
	
	public static <T> ApiVO<T> success(int code, String msg, T obj) {
		return ApiVO.success(code, msg, obj);
	}
	
	public static <T> ApiVO<T> success(T data) {
		return ApiVO.success(HttpCode.SUCCESS, "成功！", data);
	}
	
	public static <T> ApiVO<T> success(T data, String msg) {
		return ApiVO.success(HttpCode.SUCCESS, msg, data);
	}
	
	public static ApiVO<Boolean> message(String msg) {
		return ApiVO.success(HttpCode.SUCCESS, msg, true);
	}
	
	public static ApiVO<Boolean> failure() {
		return ApiVO.failure(HttpCode.SERVER_ERROR, "失败");
	}
	
	public static ApiVO<Boolean> failure(int code, String msg) {
		return ApiVO.failure(code, msg);
	}
	
	protected void startPage() {
		PageUtils.startPage();
	}
	
	protected void clearPage() {
		PageUtils.clearPage();
	}
	
	public LoginUser getLoginUser() {
		return SecurityUtils.getLoginUser();
	}
	
	public Boolean isAdmin() {
		return getLoginUser().getAdmin();
	}
	
	public Long getUserId() {
		return getLoginUser().getUserId();
	}
	
	public Long getRoleId() {
		return getLoginUser().getRoleId();
	}
	
}
