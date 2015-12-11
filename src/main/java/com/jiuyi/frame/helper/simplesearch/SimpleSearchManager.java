package com.jiuyi.frame.helper.simplesearch;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * Author: xutaoyang Date: 下午5:48:12
 * 
 * Copyright @ 2015 重庆玖壹健康管理有限公司
 * 
 */
public class SimpleSearchManager {

	IStrMatch strMatch = new StrMatchDefaultService();

	private List<SearchUnit> searchUnits = new ArrayList<SearchUnit>();

	public void addSearchUnit(SearchUnit input) {
		this.searchUnits.add(input);
	}

	public void addSearchUnit(List<SearchUnit> inputs) {
		this.searchUnits.addAll(inputs);
	}

	public SearchUnit searchStr(String search) {
		for (SearchUnit searchUnit : searchUnits) {
			if (strMatch.match(searchUnit.getSearchStr(), search)) {
				return searchUnit;
			}
		}
		return null;
	}

	public List<SearchUnit> searchStrs(String search) {
		List<SearchUnit> result = new ArrayList<SearchUnit>();
		for (SearchUnit searchUnit : searchUnits) {
			if (strMatch.match(searchUnit.getSearchStr(), search)) {
				result.add(searchUnit);
			}
		}
		return result;
	}
}
