package com.jiuyi.frame.front;

import java.util.Collection;
import java.util.List;

import com.jiuyi.frame.helper.ConstKeys;

/**
 * @author xutaoyang @Date 2015年3月23日
 * 
 * @Description 后端controller返回值的封装类。方便返回结果的一些管理以及序列化等功能。
 * 
 *              记住这个类是继承的HashMap，所以不是线程安全的，因为绝大多数情况下，都是一个线程处理一次请求，
 * 
 *              一个ServerResult只会被同一线程处理
 *
 * @Copyright 2015 重庆玖壹健康管理有限公司
 */
public class ServerResult extends MapObject {

	protected static final String RESULT = "resultCode";
	protected static final String RESULT_DESC = "resultDesc";
	protected static final String KEY_LIST = "list";

	private static final long serialVersionUID = 6486177456150293050L;

	public ServerResult() {
		putResult(ResultConst.SUCCESS);
	}

	/** res 等于0表示成功，非0表示失败 */
	public ServerResult(int res, String desc) {
		putRes(res, desc);
	}

	public ServerResult(ISerializableObj obj) {
		this();
		putObject(obj);
	}

	public ServerResult(String key, Object obj) {
		this();
		put(key, obj);
	}

	public ServerResult(List<? extends Object> list) {
		this(KEY_LIST, list);
	}

	public ServerResult(String key, ISerializableObj obj) {
		this();
		putObject(key, obj);
	}

	public ServerResult(String key, Collection<? extends ISerializableObj> objList) {
		this(key, objList, true);
	}

	public ServerResult(String key, Collection<? extends ISerializableObj> objList, boolean list) {
		this();
		if (list) {
			putObjects(key, objList);
		} else {
			put(key, objList);
		}
	}

	public ServerResult(int result) {
		putResult(result);
	}

	public ServerResult(ResultConst resultConst) {
		putResult(resultConst);
	}

	public boolean isSuccess() {
		return (Integer) this.get(RESULT) == ResultConst.SUCCESS.id;
	}

	public boolean isCheckPhoneCodeSuccess() {
		return (Integer) this.get(RESULT) == ResultConst.VERIFY_SUCCESS.id;
	}

	public void putResult(int result) {
		putResult(ResultConst.getResById(result));
	}

	public void putResult(ResultConst resultConst) {
		put(RESULT, resultConst.id);
		put(RESULT_DESC, resultConst.desc);
	}

	public void putSuccessDesc(String desc) {
		putRes(ResultConst.SUCCESS.id, desc);
	}

	public void putFailDesc(String desc) {
		putRes(ResultConst.FAIL.id, desc);
	}

	public void putRes(int id, String desc) {
		put(RESULT, id);
		put(RESULT_DESC, desc);
	}

	/** 若果没有put id则和putSuccessDesc一样的效果 */
	public void putDesc(String desc) {
		put(RESULT_DESC, desc);
	}

	public void putToken(String token) {
		put(ConstKeys.TOKEN, token);
	}

	public void putDescf(String format, Object... args) {
		String desc = String.format(format, args);
		this.putDesc(desc);
	}

	public ResultConst getResultConst() {
		Object r = get(RESULT);
		return ResultConst.getResById(r != null ? (Integer) r : 0);
	}
}
