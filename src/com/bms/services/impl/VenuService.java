package com.bms.services.impl;

import com.bms.pojo.Venue;
import com.bms.services.IVenuService;

public class VenuService implements IVenuService {

	@Override
	public Venue add(int id, String name, String address) {
		return new Venue(id, name, address);
	}
}
