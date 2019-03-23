package com.bms.services;

import com.bms.enums.SeatStatus;
import com.bms.pojo.Seat;
import com.bms.pojo.User;

public interface ITicketingService {
	String book(User user,int theatreId,  int screenId, String show, Seat seat);
	Seat[][] getSeatsStatus(int theatreId,  int screenId, String show);
	SeatStatus getSeatStatus(int theatreId,  int screenId, String show, String seat);
}
