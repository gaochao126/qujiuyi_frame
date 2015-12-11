package com.jiuyi.frame.helper.simplesearch;

import org.springframework.stereotype.Service;

import com.jiuyi.frame.util.StringUtil;

/**
 * 
 * Author: xutaoyang Date: 下午5:58:25
 * 
 * Copyright @ 2015 重庆玖壹健康管理有限公司
 * 
 */
@Service("StrMatch")
public class StrMatchDefaultService implements IStrMatch {

	@Override
	public boolean match(String src, String input) {
		if (StringUtil.isNullOrEmpty(src) || input == null) {
			return false;
		}
		return src.contains(input);
	}

}
