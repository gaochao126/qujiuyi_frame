package com.jiuyi.frame.sms;

import java.util.Date;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import com.jiuyi.frame.helper.Loggers;

public class SmsCheckService {

	private static SmsCheckService instance = new SmsCheckService();
	private static final int SUCCESS_CODE = 0;
	private static final int FAIL_CODE = 1;
	private static final int FAIL_LIMIT = 10;// 最大失败次数
	private static final int EXPIRE_TIME = 5 * 60 * 1000;// 验证码有效时间，5分钟
	private final ScheduledExecutorService executor;

	private ConcurrentHashMap<String, CheckInfo> phone_info = new ConcurrentHashMap<>();

	public SmsCheckService() {
		executor = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {

			@Override
			public Thread newThread(Runnable r) {
				Thread thread = new Thread(r);
				thread.setDaemon(true);
				return thread;
			}
		});
		executor.scheduleWithFixedDelay(new ClearRunnable(), 5, 5, TimeUnit.SECONDS);
	}

	public static void main(String[] args) {
		SmsCheckService.instance().setCode("18100863330", "1234");

		CheckResult res = SmsCheckService.instance().checkCode("18100863330", "123");
		System.out.println(res.getDesc());

		CheckResult res1 = SmsCheckService.instance().checkCode("18100863330", "1234");
		System.out.println(res1.getDesc());

		System.out.println("end");
	}

	public static SmsCheckService instance() {
		return instance;
	}

	/** 设置验证码 */
	public void setCode(String phone, String code) {
		CheckInfo info = new CheckInfo(code, 0);
		this.phone_info.put(phone, info);
	}

	/**
	 * 验证过程
	 * 
	 * @param phone
	 *            电话
	 * @param code
	 *            验证码
	 * @return
	 */
	public CheckResult checkCode(String phone, String code) {
		CheckInfo info = this.phone_info.get(phone);
		if (info == null) {
			return new CheckResult(FAIL_CODE, "还未发送验证码或者验证码已经失效，请点击重新发送");
		}
		if (!info.codeEquals(code)) {
			info.incFailCount();
			if (info.failLimit()) {// 错误次数达到上限，则让验证码失效，因为发送短信有次数限制，所以这里可以不用做次数限制
				this.phone_info.remove(phone);
			}
			return new CheckResult(FAIL_CODE, "验证码错误，请从新输入");
		}
		info = null;
		this.phone_info.remove(phone);
		return new CheckResult(SUCCESS_CODE, "成功");
	}

	public static class CheckInfo {
		private String code;
		private int failCount;// 失败计数
		private Date createTime;

		public CheckInfo(String code, int failCount) {
			this.code = code;
			this.failCount = failCount;
			this.createTime = new Date();
		}

		public int incFailCount() {
			return ++this.failCount;
		}

		public boolean codeEquals(String code) {
			return this.code.equals(code);
		}

		public boolean failLimit() {
			return this.failCount > FAIL_LIMIT;
		}

		/** 验证码是否过期 */
		public boolean expired() {
			return System.currentTimeMillis() - this.createTime.getTime() > EXPIRE_TIME;
		}

		public void resetCreateTime() {
			this.createTime = new Date();
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public int getFailCount() {
			return failCount;
		}

		public void setFailCount(int failCount) {
			this.failCount = failCount;
		}

	}

	/**
	 * 验证结果，r返回码，desc结果描述
	 * 
	 * @author xutaoyang
	 *
	 */
	public static class CheckResult {
		private int r;
		private String desc;

		public CheckResult(int r, String desc) {
			this.r = r;
			this.desc = desc;
		}

		public boolean isSuccess() {
			return this.r == SUCCESS_CODE;
		}

		public int getR() {
			return r;
		}

		public void setR(int r) {
			this.r = r;
		}

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}
	}

	private class ClearRunnable implements Runnable {
		@Override
		public void run() {
			try {
				for (Iterator<Entry<String, CheckInfo>> iter = phone_info.entrySet().iterator(); iter.hasNext();) {
					Entry<String, CheckInfo> entry = iter.next();
					CheckInfo checkInfo = entry.getValue();
					if (checkInfo.expired()) {
						iter.remove();
					}
				}
			} catch (Exception e) {
				Loggers.err("clear sms check info err: ", e);
			}
		}
	}
}
