package com.bms.pojo;

import java.util.ArrayList;
import java.util.List;

public class User {
	private String login;
	private String password;
	private boolean isLoggedIn;
	private Long loginTimeInMilliSec;
	private List<Ticket> bookedTickets = new ArrayList<>();
	
	public User(String login, String password) {
		super();
		this.login = login;
		this.password = password;
	}

	public List<Ticket> getBookedTickets() {
		return bookedTickets;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public boolean isLoggedIn() {
		return isLoggedIn;
	}
	public void setLoggedIn(boolean isLoggedIn) {
		this.isLoggedIn = isLoggedIn;
	}
	public Long getLoginTimeInMilliSec() {
		return loginTimeInMilliSec;
	}
	public void setLoginTimeInMilliSec(Long loginTimeInMilliSec) {
		this.loginTimeInMilliSec = loginTimeInMilliSec;
	}
	
	public void addTicket(Ticket ticket) {
		bookedTickets.add(ticket);
	}
}
