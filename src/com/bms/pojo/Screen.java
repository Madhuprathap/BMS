package com.bms.pojo;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.bms.constants.BMSConstants;
import com.bms.enums.SeatStatus;
import com.bms.enums.SeatType;
import com.bms.exceptions.BMSException;
import com.bms.utils.ConfigUtil;

public class Screen {
	private int id;
	// TODO : Multiple Days no handled
	private Map<String, Seat[][]> moviesPlaying = new HashMap<>();
	private int noOfSeats;
	private Show currentRunnigMovie;
//	private Map<Integer, Movie> givenDayShowsList;
	
	public Map<String, Seat[][]> getMoviesPlaying() {
		return moviesPlaying;
	}
	public int getNoOfSeats() {
		return noOfSeats;
	}
	public int getId() {
		return id;
	}
	public Screen(int id, int noOfSeats) {
		this.id = id;
		this.noOfSeats = noOfSeats;
	}
	public Show getCurrentRunnigMovie() {
		return currentRunnigMovie;
	}
	public void setCurrentRunnigMovie(Show currentRunnigMovie) {
		this.currentRunnigMovie = currentRunnigMovie;
	}
	
	public Screen addShows(Show movie) throws NumberFormatException, IOException {
		int maxNoOfShows = Integer.parseInt(ConfigUtil.getInstance().getConfigValue(BMSConstants.NOOF_MAX_SHOWS_PER_SCREEN));
		if (moviesPlaying.size() < maxNoOfShows) {
			int rem = noOfSeats%20;
			int rows = (rem > 0) ? (noOfSeats/20) + 1 : (noOfSeats/20);
			// With assumption that each row will have 20 seats
			Seat[][] seats = new Seat[rows][20];
			//For simplicity Made every Seat as Normal for now
			//TODO : Handling of SeatType is pending
			for (int i = 1; i <= seats.length; i++) {
				for (int j = 1; j <= 20; j++) {
					seats[i-1][j-1] = new Seat("R"+(i)+"S"+(j), SeatStatus.AVAILABLE, SeatType.Normal); 
				}
			}
			// last row
			for (int i = 1; i < rem; i++) {
				seats[rows-1][i-1] = new Seat("R"+rows+"S"+(i), SeatStatus.AVAILABLE, SeatType.Normal);
			}
			moviesPlaying.put(movie.getTitle(),seats);
		} else {
			throw new BMSException("max no of screens allowed reached!");
		}
		return this;
	}
}
