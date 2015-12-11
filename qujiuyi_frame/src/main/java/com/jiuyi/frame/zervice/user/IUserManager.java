package com.jiuyi.frame.zervice.user;

import com.jiuyi.frame.zervice.user.model.User;

public interface IUserManager {
	public User getUserByToken(String token);

}
