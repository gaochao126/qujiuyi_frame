package com.jiuyi.frame.sms;

public class SmsResp {

	private String reason;
	private int error_code;
	private SmsResult result;

	public SmsResp(String reason, int error_code) {
		this.reason = reason;
		this.error_code = error_code;
	}

	public boolean isSuccess() {
		return this.error_code == 0;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public int getError_code() {
		return error_code;
	}

	public void setError_code(int error_code) {
		this.error_code = error_code;
	}

	public SmsResult getResult() {
		return result;
	}

	public void setResult(SmsResult result) {
		this.result = result;
	}

	public class SmsResult {
		private String sid;
		private int fee;
		private int count;

		public int getFee() {
			return fee;
		}

		public void setFee(int fee) {
			this.fee = fee;
		}

		public int getCount() {
			return count;
		}

		public void setCount(int count) {
			this.count = count;
		}

		public String getSid() {
			return sid;
		}

		public void setSid(String sid) {
			this.sid = sid;
		}
	}

	@Override
	public String toString() {
		return "SmsResp [reason=" + reason + ", error_code=" + error_code + "]";
	}

}
