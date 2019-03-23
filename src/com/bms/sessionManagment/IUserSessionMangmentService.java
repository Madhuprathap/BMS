package com.bms.sessionManagment;

import java.util.Collection;

import com.bms.pojo.User;

public interface IUserSessionMangmentService {
	boolean login(String login, String password);
	boolean logout(String login);
	Collection<User> getSessions();
}
