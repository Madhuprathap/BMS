package com.bms.sessionManagment.Executors;

import java.util.Iterator;
import java.util.List;

import com.bms.pojo.User;
import com.bms.sessionManagment.UserSessionMangmentService;

public class SessionTimeOutExecutor implements Runnable {

	@Override
	public void run() {
		UserSessionMangmentService service = UserSessionMangmentService.getInstance();
		while(true) {
			List<User> expiredSessions = service.getExpiredSessions();
			Iterator<User> iterator = expiredSessions.listIterator();
			while (iterator.hasNext()) {
				User user = (User) iterator.next();
				service.logout(user.getLogin());
			}
			
			// Wait half min before invalidating sessions again
			try {
				Thread.sleep(30*1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
				break;
			}
		}
	}

}
