package com.bms.enums;

public enum SeatType {
	//For simplicity assumed fare based on Seat type
	Normal(100.00d), Premium(200.00d), Reserved(0.0d);
	
	private double cost;

	private SeatType(double cost) {
		this.cost = cost;
	}

	public double isCost() {
		return cost;
	}

}
