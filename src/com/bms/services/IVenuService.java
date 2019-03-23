package com.bms.services;

import com.bms.pojo.Venue;

public interface IVenuService {
	Venue add(int id, String name, String address);
}
