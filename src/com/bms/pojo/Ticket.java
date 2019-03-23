package com.bms.pojo;

public class Ticket {
	private String theatreName;
	private int screenId;
	private String ticketNo;

	public Ticket(String theatreName, int screenId, String ticketNo) {
		super();
		this.theatreName = theatreName;
		this.screenId = screenId;
		this.ticketNo = ticketNo;
	}
	public String getTheatreName() {
		return theatreName;
	}
	public void setTheatreName(String theatreName) {
		this.theatreName = theatreName;
	}
	public int getScreenId() {
		return screenId;
	}
	public void setScreenId(int screenId) {
		this.screenId = screenId;
	}
	public String getTicketNo() {
		return ticketNo;
	}
	public void setTicketNo(String ticketNo) {
		this.ticketNo = ticketNo;
	}
}
