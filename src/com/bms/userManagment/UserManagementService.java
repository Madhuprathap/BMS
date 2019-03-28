package com.bms.userManagment;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.bms.pojo.User;

public class UserManagementService implements IUserManagementService {
	final static Logger logger = Logger.getLogger(UserManagementService.class);
	//Singleton
	private UserManagementService() {

	}

	private Map<String, User> users = new HashMap<>();
	private static UserManagementService INSTANCE = new UserManagementService();

	public static UserManagementService getInsance() {
		return INSTANCE;
	}

	@Override
	public User createUser(String login, String password) {
		User user = new User(login, password);
		users.put(user.getLogin(), user);
		logger.info("user " + login + " created!");
		return user;
	}

	@Override
	public Map<String, User> getAllAvailableUsers() {
		return users;
	}

	@Override
	public User getUser(String login) {
		if (users.containsKey(login)) {
			return users.get(login);
		}
		return null;
	}

}
