package com.bms.services.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.bms.enums.SeatStatus;
import com.bms.exceptions.BMSException;
import com.bms.pojo.Seat;
import com.bms.pojo.Theatre;
import com.bms.pojo.Ticket;
import com.bms.pojo.User;
import com.bms.services.ITicketingService;

public class TicketingService implements ITicketingService {
	
	private Map<Integer, Theatre> theaters = new HashMap<>();
	private Pattern p = Pattern.compile("R([0-9]+)S([0-9]+)");
	
	public TicketingService(Map<Integer, Theatre> theaters) {
		this.theaters = theaters;
	}

	@Override
	public String book(User user, int theatreId, int screenId, String show, Seat seatNo) throws BMSException {
		if (user.isLoggedIn()) {
			Matcher m = p.matcher(seatNo.getNo());
			if (m.find()) {
				int row = Integer.parseInt(m.group(1));
				int col = Integer.parseInt(m.group(1));
				Seat[][] seats = theaters.get(theatreId).getScreens().get(screenId).getMoviesPlaying().get(show);
				if (seats[row-1][col-1].getStatus().equals(SeatStatus.IN_PROGRESS) || 
						seats[row-1][col-1].getStatus().equals(SeatStatus.PERMANENTLY_UNAVAILABLE)) {
					throw new BMSException("Please select a different seat");
				} else {
					// handling concurrent transactions
					synchronized(seatNo) {
						if (seats[row-1][col-1].getStatus().equals(SeatStatus.IN_PROGRESS) || 
								seats[row-1][col-1].getStatus().equals(SeatStatus.PERMANENTLY_UNAVAILABLE)) {
							throw new BMSException("Please select a different seat");
						} else {
							seats[row-1][col-1].setStatus(SeatStatus.IN_PROGRESS);
							Boolean status = pay(seats[row-1][col-1].getSeatType().isCost());
							if (status) {
								seats[row-1][col-1].setStatus(SeatStatus.PERMANENTLY_UNAVAILABLE);
								user.addTicket(new Ticket(theaters.get(theatreId).getName(), screenId, seatNo.getNo()));
							} else {
								// Releasing seat
								seats[row-1][col-1].setStatus(SeatStatus.AVAILABLE);
							}
						}
					}
					return seatNo.getNo();
				}
			}
		} else {
			throw new BMSException("User: " + user.getLogin() +" is not logged in! Please login!");
		}
		return null;
	}

	private boolean pay(double amountTopay) {
		// TODO: payment gateway integration
		return true;
	}
	
	@Override
	public Seat[][] getSeatsStatus(int theatreId,  int screenId, String show) {
		return theaters.get(theatreId).getScreens().get(screenId).getMoviesPlaying().get(show);
	}

	@Override
	public SeatStatus getSeatStatus(int theatreId, int screenId, String show, String seat) {
		Matcher m = p.matcher(seat);
		if (m.find()) {
			int row = Integer.parseInt(m.group(1));
			int col = Integer.parseInt(m.group(1));
			Seat[][] seats = theaters.get(theatreId).getScreens().get(screenId).getMoviesPlaying().get(show);
			return seats[row-1][col-1].getStatus();
		}
		throw new BMSException("Seat no is wrong!");
	}
	
	

}
