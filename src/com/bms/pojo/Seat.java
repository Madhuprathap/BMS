package com.bms.pojo;

import com.bms.enums.SeatStatus;
import com.bms.enums.SeatType;

public class Seat {
	private String no;
	private SeatStatus status;
	private SeatType seatType;
	
	public Seat(String no, SeatStatus status, SeatType seatType) {
		super();
		this.no = no;
		this.status = status;
		this.seatType = seatType;
	}
	
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public SeatStatus getStatus() {
		return status;
	}
	public void setStatus(SeatStatus status) {
		this.status = status;
	}
	public SeatType getSeatType() {
		return seatType;
	}
	public void setSeatType(SeatType seatType) {
		this.seatType = seatType;
	}
}
