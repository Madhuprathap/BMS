package com.bms.userManagment;

import java.util.Map;

import com.bms.pojo.User;

public interface IUserManagementService {
	User createUser(String login, String password);
	Map<String, User> getAllAvailableUsers();
	User getUser(String login);
}
