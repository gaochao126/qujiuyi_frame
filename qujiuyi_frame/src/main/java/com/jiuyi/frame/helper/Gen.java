package com.jiuyi.frame.helper;

import java.util.List;

import com.jiuyi.frame.util.ObjectUtil;
import com.jiuyi.frame.zervice.user.model.User;

public class Gen {

	public static void main(String[] args) {
		List<String> fields = ObjectUtil.getFields(User.class);
		StringBuilder sb = new StringBuilder();
		for (String field : fields) {
			String format = "<div class='form-group'><label for='#name#' class='col-sm-2 control-label'>#name#</label><div class='col-sm-10'><input type='text' class='form-control' id='#name#' name='#name#' value='${#name#}'></div></div>";
			String item = format.replace("#name#", field);
			sb.append(item);
		}
		System.out.println(sb.toString().replaceAll("\'", "\""));
	}

}
