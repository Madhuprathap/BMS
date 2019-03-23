package com.bms.services.impl;

import com.bms.pojo.Theatre;
import com.bms.services.IThreatreService;

public class TheatreService implements IThreatreService {

	@Override
	public Theatre add(int id, String name, String address) {
		return new Theatre(id, name, address);
	}

}
