package com.jiuyi.frame.helper;

import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * @author xutaoyang @Date 2015年3月23日
 * 
 * @Description 字符串的加密解密辅助方法
 *
 * @Copyright 2015 重庆玖壹健康管理有限公司
 */
@Service("basicEncryptor")
public class TextEncryptor {

	@Autowired
	@Qualifier("encryptor")
	BasicTextEncryptor encryptor;

	@Autowired
	@Qualifier("zoresaltencryptor")
	PooledPBEStringEncryptor poolEncryptor;

	public String encrypt(String input) {
		return encryptor.encrypt(input);
	}

	public String decrypt(String message) {
		return encryptor.decrypt(message);
	}

	public String zeroSaltEncrypt(String input) {
		return poolEncryptor.encrypt(input);
	}

	public String zeroSaltDecrypt(String message) {
		return poolEncryptor.decrypt(message);
	}
}
