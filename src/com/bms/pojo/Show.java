package com.bms.pojo;

public class Show {
	private String title;
	private String plot;
	private int rating;
	
	public Show(String title, String plot, int rating) {
		super();
		this.title = title;
		this.plot = plot;
		this.rating = rating;
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPlot() {
		return plot;
	}
	public void setPlot(String plot) {
		this.plot = plot;
	}
	public int getRating() {
		return rating;
	}
	public void setRating(int rating) {
		this.rating = rating;
	}
}
