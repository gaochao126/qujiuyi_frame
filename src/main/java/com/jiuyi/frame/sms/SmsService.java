package com.jiuyi.frame.sms;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import com.jiuyi.frame.helper.Loggers;
import com.jiuyi.frame.sms.SmsCheckService.CheckResult;
import com.jiuyi.frame.util.JsonUtil;

/**
 * 短信API服务调用示例代码 － 聚合数据 在线接口文档：http://www.juhe.cn/docs/54
 **/

public class SmsService {
	public static final String DEF_CHATSET = "UTF-8";
	public static final int DEF_CONN_TIMEOUT = 30000;
	public static final int DEF_READ_TIMEOUT = 30000;
	public static String userAgent = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.66 Safari/537.36";
	private static final String url = "http://v.juhe.cn/sms/send";// 请求接口地址

	// appkey
	public static final String APPKEY = "049ecdd61e0d11b35db5a1c7812165f1";

	private static SmsService instance = new SmsService();

	public static SmsService instance() {
		return instance;
	}

	/**
	 * 短信验证码
	 * 
	 * @param phone
	 *            电话
	 * @return
	 */
	public SmsResp sendCode(String phone) {
		String code = genRandomCode();
		return sendCode(phone, code);
	}

	/**
	 * 短信验证码
	 * 
	 * @param phone
	 *            电话
	 * @param code
	 *            验证码
	 * @return
	 */
	public SmsResp sendCode(String phone, String code) {
		SmsCheckService.instance().setCode(phone, code);
		return sendSms(phone, "6994", "#code#=" + code);
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
		return SmsCheckService.instance().checkCode(phone, code);
	}

	/**
	 * 发送短信
	 * 
	 * @param phone
	 *            电话
	 * @param templateId
	 *            模板编号
	 * @param args
	 *            #key1#=value1&#key2#=value2
	 * @return
	 */
	public SmsResp sendSms(String phone, String templateId, String args) {
		Map<String, Object> params = new HashMap<String, Object>();// 请求参数
		params.put("mobile", phone);// 接收短信的手机号码
		params.put("tpl_id", templateId);// 短信模板ID，请参考个人中心短信模板设置
		params.put("tpl_value", args);// 变量名和变量值对。如果你的变量名或者变量值中带有#&amp;=中的任意一个特殊符号，请先分别进行urlencode编码后再传递
		params.put("key", APPKEY);// 应用APPKEY(应用详细页查询)
		params.put("dtype", "json");// 返回数据的格式,xml或json，默认json
		SmsResp resp = null;
		String result = null;
		try {
			result = net(url, params, "GET");
			resp = JsonUtil.fromJson(result, SmsResp.class);
		} catch (Exception e) {
			resp = new SmsResp("网络错误", -1);
			Loggers.err("调用juhe 短信api报错", e);
		}
		return resp;
	}

	/**
	 *
	 * @param strUrl
	 *            请求地址
	 * @param params
	 *            请求参数
	 * @param method
	 *            请求方法
	 * @return 网络请求字符串
	 * @throws Exception
	 */
	private static String net(String strUrl, Map<String, Object> params, String method) throws Exception {
		HttpURLConnection conn = null;
		BufferedReader reader = null;
		String rs = null;
		try {
			StringBuffer sb = new StringBuffer();
			if (method == null || method.equals("GET")) {
				strUrl = strUrl + "?" + urlencode(params);
			}
			URL url = new URL(strUrl);
			conn = (HttpURLConnection) url.openConnection();
			if (method == null || method.equals("GET")) {
				conn.setRequestMethod("GET");
			} else {
				conn.setRequestMethod("POST");
				conn.setDoOutput(true);
			}
			conn.setRequestProperty("User-agent", userAgent);
			conn.setUseCaches(false);
			conn.setConnectTimeout(DEF_CONN_TIMEOUT);
			conn.setReadTimeout(DEF_READ_TIMEOUT);
			conn.setInstanceFollowRedirects(false);
			conn.connect();
			if (params != null && method.equals("POST")) {
				try (DataOutputStream out = new DataOutputStream(conn.getOutputStream())) {
					out.writeBytes(urlencode(params));
				}
			}
			InputStream is = conn.getInputStream();
			reader = new BufferedReader(new InputStreamReader(is, DEF_CHATSET));
			String strRead = null;
			while ((strRead = reader.readLine()) != null) {
				sb.append(strRead);
			}
			rs = sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				reader.close();
			}
			if (conn != null) {
				conn.disconnect();
			}
		}
		return rs;
	}

	// 将map型转为请求参数型
	public static String urlencode(Map<String, ?> data) {
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, ?> i : data.entrySet()) {
			try {
				sb.append(i.getKey()).append("=").append(URLEncoder.encode(i.getValue() + "", "UTF-8")).append("&");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	/** 六位随机码 */
	private static String genRandomCode() {
		return String.valueOf((int) (Math.random() * 900000) + 100000);
	}

}
