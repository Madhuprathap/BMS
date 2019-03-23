package com.bms.pojo;

import java.util.HashMap;
import java.util.Map;

public class Theatre extends Venue {
	private Map<Integer, Screen> screens;
	
	public Theatre(int id, String name, String address) {
		super(id, name, address);
		screens = new HashMap<>();
	}

	public Map<Integer, Screen> getScreens() {
		return screens;
	}
	
	public Theatre addScreen(Screen screen) {
		screens.put(screen.getId(), screen);
		return this;
	}
}
