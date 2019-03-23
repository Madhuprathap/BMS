package com.bms.sessionManagment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.bms.constants.BMSConstants;
import com.bms.pojo.User;
import com.bms.userManagment.IUserManagementService;
import com.bms.userManagment.UserManagementService;
import com.bms.utils.ConfigUtil;

public class UserSessionMangmentService implements IUserSessionMangmentService {
	// Singleton so that we have only one Instances of this class, as it handles session which should be only one instance
	private UserSessionMangmentService(){
	}
	
	private static UserSessionMangmentService INSTANCE = new UserSessionMangmentService();
	// default timeout
	private static int sessionTimeOut = 10*60*1000;
	private static IUserManagementService iUserManagementService;
	private Map<String, User> activeUsers = new HashMap<>();
	
	public static UserSessionMangmentService getInstance() {
		try {
			int mins = Integer.parseInt(ConfigUtil.getInstance().getConfigValue(BMSConstants.SESSSION_TIMEOUT_TIME));
			sessionTimeOut = mins * 60 * 1000;
		} catch (NumberFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		iUserManagementService = UserManagementService.getInsance();
		return INSTANCE;
	}
	
	@Override
	public boolean login(String login, String password) {
		User user = iUserManagementService.getUser(login);
		if (user.getPassword().equals(password)) {
			user.setLoginTimeInMilliSec(System.currentTimeMillis());
			user.setLoggedIn(true);
			activeUsers.put(user.getLogin(), user);
			return true;
		}
		return false;
	}

	@Override
	public boolean logout(String login) {
		if (activeUsers.containsKey(login)) {
			activeUsers.get(login).setLoggedIn(false);
			activeUsers.get(login).setLoginTimeInMilliSec(null);
			activeUsers.remove(login);
			return true;
		}
		return false;
	}

	public List<User> getExpiredSessions() {
		Long currentSysTime = System.currentTimeMillis();
		List<User> expiredSessions = new ArrayList<>();
		
		Iterator<Entry<String, User>> iterator = activeUsers.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<java.lang.String, com.bms.pojo.User> entry = (Map.Entry<java.lang.String, com.bms.pojo.User>) iterator
					.next();
			User user = entry.getValue();
			if (currentSysTime - user.getLoginTimeInMilliSec() >= sessionTimeOut) {
				expiredSessions.add(user);
			}
		}
		return expiredSessions;
	}

	@Override
	public Collection<User> getSessions() {
		return activeUsers.values();
	}

}
