package com.jiuyi.frame.front;

import com.jiuyi.frame.util.StringUtil;

/**
 * @author xutaoyang @Date 2015年3月23日
 * 
 * @Description 所有接口返回的结果集。
 *
 * @Copyright 2015 重庆玖壹健康管理有限公司
 */
public enum ResultConst {

	/** 成功 */
	SUCCESS(0, "成功"),
	/** 失败 */
	FAIL(1, "失败"),
	/** 没有该用户名 */
	NO_USERNAME(2, "没有该用户名"),
	/** 用户名已经存在 */
	USERNAME_EXIST(3, "用户名已经存在"),
	/** 密码错误 */
	PASSWORD_ERR(4, "密码错误"),
	/** 格式不正确 */
	FORMAT_ERR(5, "格式不正确"),
	/** 条件不满足 */
	NOT_SATISFY(6, "条件不满足"),
	/** 参数错误 */
	PARAM_ERROR(7, "参数错误"),
	/** 还未登录 */
	NOT_LOGIN(8, "还未登录"),
	/** 没有权限 */
	NO_AUTH(9, "没有权限"),
	/** 参数不能为空 */
	PARAM_NULL(10, "缺少参数"),
	/** 键值不存在 */
	KEY_NOT_EXIST(11, "键值不存在"),
	/** 找不到对应数据 */
	DATA_NOT_FOUND(12, "找不到对应数据"),
	/** 身份证号已经存在 */
	UID_EXSIT(13, "身份证号已经存在"),
	/** 服务器错误 */
	SERVER_ERR(14, "请联系客服~"),
	/** 手机号已经注册过 */
	PHONE_NUM_EXIST(15, "手机号已经注册过"),
	/** 没有找到结果 */
	NOT_FOUND(16, "没有找到结果"),
	/** 已经在患者群中 */
	HAVE_IN_PATIENTS(17, "已经在患者群中"),
	/** 没有token参数 */
	NEED_PARAM_TOKEN(18, "没有token参数"),
	/** url 错误 */
	URL_ERROR(19, "url 错误"),
	/** 该患者不存在 */
	PATIENT_NOT_EXIST(20, "该患者不存在"),
	/** 该患者已经在该分组中 */
	PATIENT_EXIST_IN_GROUP(21, "该患者已经在该分组中"),
	/** 还未付费 */
	HAS_NOT_PAY(22, "还未付费"),
	/** 新注册用户 */
	NEW_USER(23, "新注册用户"),
	/** 还没设置密码 */
	HAVE_NOT_SET_PASSWORD(24, "还没设置密码"),
	/** 暂时不支持该银行 */
	BANK_NOT_SUPPORT(25, "暂时不支持该银行"),
	/** 每周只能提现一次 */
	WITHDRAW_TIME_LIMIT(26, "每周只能提现一次，请下周再来"),
	/** 提现金额需要大于10元 */
	WITHDRAW_MONEY_LT_TEN(27, "提现金额需要大于10元"),
	/** salt已经过期，请从新获取 */
	SALT_EXPIRE(28, "令牌已经过期，请从新进入此界面"),
	/** 图文咨询设置为免费，将自动关闭一元义诊服务 */
	YIYUAN_CLOSED(29, "图文咨询设置为免费，将自动关闭一元义诊服务"),
	/** 服务未开启 */
	SERVICE_NOT_OPEN(30, "服务未开启"),
	/** 参数太长 */
	DATA_TOO_LONG(31, "参数太长"),
	/** 名称已经存在 */
	NAME_EXIST(32, "名称已经存在"),
	/** 标签不存在 */
	TAG_NOT_EXIST(33, "标签不存在"),
	/** 私人患者不能进行该项操作 */
	PERSONAL_PATIENT(34, "私人患者不能进行该项操作"),
	/** 用户名和密码不能为空*/
	NAME_PASSWD_NULL(35, "用户名和密码不能为空"),

	// ===== 手机验证结果 =====
	/** 发送短信成功 */
	VERIFY_SUCCESS(200, "发送短信成功"),
	/** 服务器拒绝访问，或者拒绝操作 */
	SERVER_DENIED(512, "服务器拒绝访问，或者拒绝操作"),
	/** Appkey不存在或被禁用 */
	APPKEY_ERR(513, "求Appkey不存在或被禁用"),
	/** 权限不足 */
	NO_PERMISSIONS(514, "权限不足"),
	/** 服务器内部错误 */
	SERVER_ERROR(515, "服务器内部错误"),
	/** 缺少必要的请求参数 */
	PARAM_LACK(517, "缺少必要的请求参数"),
	/** 请求中用户的手机号格式不正确（包括手机的区号） */
	PHONE_FORMAT_ERR(518, "请求中用户的手机号格式不正确（包括手机的区号）"),
	/** 请求发送验证码次数超出限制 */
	TIME_LIMIT(519, "请求发送验证码次数超出限制"),
	/** 无效验证码 */
	CODE_INVALID(520, "无效验证码"),
	/** 余额不足 */
	MONEY_NOT_ENOUGH(526, "余额不足"), ;

	public final int id;
	public final String desc;

	private ResultConst(int id, String desc) {
		this.id = id;
		this.desc = desc;
	}

	public boolean isSuccess() {
		return this.id == SUCCESS.id;
	}

	public static boolean checkPhoneSuccess(String verifyRes) {
		if (StringUtil.isNullOrEmpty(verifyRes)) {
			return false;
		}
		return Integer.parseInt(verifyRes) == VERIFY_SUCCESS.id;
	}

	public static ResultConst getResById(int result) {
		for (ResultConst rc : values()) {
			if (rc.id == result) {
				return rc;
			}
		}
		return null;
	}

	public static ResultConst codeCheck2Res(Integer res) {
		if (res == VERIFY_SUCCESS.id) {
			return SUCCESS;
		}
		return getResById(res);
	}
}
