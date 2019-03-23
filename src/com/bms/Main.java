package com.bms;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.bms.exceptions.BMSException;
import com.bms.pojo.Screen;
import com.bms.pojo.Seat;
import com.bms.pojo.Show;
import com.bms.pojo.Theatre;
import com.bms.pojo.User;
import com.bms.services.IThreatreService;
import com.bms.services.ITicketingService;
import com.bms.services.impl.TheatreService;
import com.bms.services.impl.TicketingService;
import com.bms.sessionManagment.IUserSessionMangmentService;
import com.bms.sessionManagment.UserSessionMangmentService;
import com.bms.sessionManagment.Executors.SessionTimeOutExecutor;
import com.bms.userManagment.IUserManagementService;
import com.bms.userManagment.UserManagementService;

public class Main {


	/**
	 * Should use JUnit with JMock to test cases
	 * 
	 * @param args
	 * @throws NumberFormatException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws NumberFormatException, IOException, InterruptedException {
		ExecutorService executorService = null;
		try{
			//Start SessionTimeOutExecutor
			executorService = Executors.newSingleThreadExecutor();
			executorService.submit(new SessionTimeOutExecutor());

			// User DataSeed
			IUserManagementService iUserManagementService = UserManagementService.getInsance();
			for (int i = 1; i <= 10; i++) {
				iUserManagementService.createUser("User"+i, "Admin123");
			}

			// Theaters/Screens/Shows/Seats DataSeed
			// Can implement Factory methods using factory pattern
			int noOfSeats = 100;
			IThreatreService threatreService = new TheatreService();
			Theatre theatre1 = threatreService.add(1,"CinePolis", "BannergattaRoad");
			Screen t1s1 = new Screen(1,noOfSeats);
			t1s1.addShows(new Show("EMovie1", "Plot", 4));
			t1s1.addShows(new Show("EMovie2", "Plot", 4));
			// Testing purpose made maxNoOfShows to 2. When added more than maxNoOfShows throws RuntimeException
			//			t1s1.addShows(new Movie("EMovie3", "Plot", 4));
			theatre1.addScreen(t1s1);
			Screen t1s2 = new Screen(2,noOfSeats);
			t1s2.addShows(new Show("HMovie1", "Plot", 4));
			t1s2.addShows(new Show("HMovie2", "Plot", 4));
			theatre1.addScreen(t1s2);

			Theatre theatre2 = threatreService.add(2,"Gopalan", "BannergattaRoad");
			t1s1 = new Screen(1,noOfSeats);
			t1s1.addShows(new Show("EMovie1", "Plot", 4));
			t1s1.addShows(new Show("EMovie2", "Plot", 4));
			theatre2.addScreen(t1s1);
			t1s2 = new Screen(2,noOfSeats);
			t1s2.addShows(new Show("HMovie1", "Plot", 4));
			t1s2.addShows(new Show("HMovie2", "Plot", 4));
			theatre2.addScreen(t1s2);

			Theatre theatre3 = threatreService.add(3,"PVR", "Koramangala");
			t1s1 = new Screen(1,noOfSeats);
			t1s1.addShows(new Show("EMovie1", "Plot", 4));
			t1s1.addShows(new Show("EMovie2", "Plot", 4));
			theatre3.addScreen(t1s1);
			t1s2 = new Screen(2,noOfSeats);
			t1s2.addShows(new Show("HMovie1", "Plot", 4));
			t1s2.addShows(new Show("HMovie2", "Plot", 4));
			theatre3.addScreen(t1s2);

			Map<Integer, Theatre> theatres = new HashMap<>();
			theatres.put(theatre1.getId(), theatre1);
			theatres.put(theatre2.getId(), theatre2);
			theatres.put(theatre3.getId(), theatre3);

			//login user1 an user2
			IUserSessionMangmentService iUserSessionMangmentService = UserSessionMangmentService.getInstance();
			User login1 = null;
			User login2 = null;
			if (iUserSessionMangmentService.login("User1", "Admin123")) {
				login1 = iUserManagementService.getUser("User1");
			}
			if (iUserSessionMangmentService.login("User2", "Admin123")) {
				login2 = iUserManagementService.getUser("User2");
			}
			iUserSessionMangmentService.login("User3", "Admin123");

			ITicketingService ticketingService = new TicketingService(theatres);
			Seat[][] seats = ticketingService.getSeatsStatus(1, 1, "EMovie1");			
			Arrays.stream(seats).flatMap(x -> Arrays.stream(x)).forEach(n -> System.out.println("Seat: "+n.getNo() + " Status: " + n.getStatus().toString()));
			System.out.println("===============================");

			// Booking a ticket with out concurrent attempt
			String seatNo = ticketingService.book(login1, 1, 1, "EMovie1", seats[1][1]);
			System.out.println("Seat: "+ seatNo + " Status: "+ticketingService.getSeatStatus(1, 1, "EMovie1", seatNo));

			// trying already booked ticket with another user/same user
			try {
				seatNo = ticketingService.book(login2, 1, 1, "EMovie1", seats[1][1]);
			} catch (BMSException e) {
				System.out.println(e.getMessage());
			}


			// Concurrency test
			Thread t1 = new Thread( new Main().new Runner(ticketingService, login1, seats));
			t1.setName(login1.getLogin());
			t1.start();
			Thread t2 = new Thread( new Main().new Runner(ticketingService, login2, seats));
			t2.setName(login1.getLogin());
			t2.start();
			
			//Wait so that Concurrent running thread book the ticket, before we logout
			Thread.sleep(1000);
			
			// logout
			iUserSessionMangmentService.logout("User1");
			iUserSessionMangmentService.logout("User2");
			
			// try booking ticket after logout/session timeout
			try{
				seatNo = ticketingService.book(login2, 1, 1, "EMovie1", seats[2][1]);
			} catch (BMSException e) {
				System.out.println(e.getMessage());
			}
			

			// waiting maxSessionTimeOut+1 time so that session is invalidated by SessionTimeOutExecutor
			Thread.sleep(2*60*1000);
			System.out.println("Active Sessions: "+iUserSessionMangmentService.getSessions().size()); // size should be 0 as User3 should have logout by executor

		} finally {
			executorService.shutdownNow();
		}

	}

	class Runner implements Runnable {
		private ITicketingService ticketingService;
		private User login;
		private Seat[][] seats;

		public Runner(ITicketingService ticketingService, User login, Seat[][] seats) {
			this.ticketingService = ticketingService;
			this.login = login;
			this.seats = seats;
		}
		public void run() {
			try{
				String seatNo = ticketingService.book(login, 1, 1, "EMovie1", seats[0][1]);
				System.out.println("User: "+ login.getLogin() + " booked Seat: "+ seatNo);
			} catch (BMSException e) {
				System.out.println(e.getMessage());
			}
		}
	}
}
