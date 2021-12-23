import java.util.*;

/**
*	ThreadedIceCreamShop.java: This is the first of two versions of a simulation
*	of an ice cream shop, representing concurrent operations (threads).
*	The design of the shop allows for maximum independence of the
*	interacting objects (clerks, arrivals, realtime...) by implementing
*	each one as a thread.
*
*	Customers will arrive at 2 persons/minute on a regular interval,
*	and the shop has 2 clerks, each customer requiring 20 full seconds
*	to serve.  The simulator runs for a duration of 10 minutes.
*
*
*	Following is the primary object that instantiates and
*	starts all other concurrent operations of the simulation.
*
*	@author anthony.francis@asu.edu
*	@version 10.18.2002
*/

public class ThreadedIceCreamShop extends Object {

	/** In milliseconds, equiv to 1 sec. simTime */
	public static final int SECOND_CYCLE = 10;

	/** The clock will increment by 'one second' every 10 milli. */
	public static final int SECOND_INTERVAL = 1;

	/** An int representing the 10 minutes of simulation time */
	public static final int TOT_SIM_TIME = 600;

	/** The first arrival occurs 5 seconds after clock starts*/
	public static final int FIRST_ARRIVAL = 5;

	/** The customers arrive on a regular interval, 2 per minute (every 25 sec.) */
	public static final int ARRIVAL_INTERVAL = 30;


	/** Static structures used for bookeeping */

	public static Vector serviced;
	public static int totalService = 0;
	public static int totalWaiting = 0;

	/** Instance fields, used primarily as
	*	handles to "global" objects.
	*/

	public ArrivalQueue que;
	public Clock realtime;
	public ArrivalsOne arrivals;
	public Server server1;
	public Server server2;

	public ThreadedIceCreamShop() {
		que = new ArrivalQueue();
		realtime = new Clock();
		arrivals = new ArrivalsOne(realtime, que);
		serviced = new Vector();
		server1 = new Server(realtime, que, serviced);
		server2 = new Server(realtime, que, serviced);
	}


	/** begin(): will start all threads that have
	*	already been instantiated in the constructor.
	*/

	public void begin() {
		realtime.start();
		arrivals.start();
		server1.start();
		server2.start();
	}

	/** displayResults: shows the results of the simulation,
	*	of course.
	*/

	public static void displayResults() {
		System.out.println("Total customers served: " + serviced.size());
		int len = serviced.size();
		Customer tmp;
		for(int i=0; i<len; i++) {
			tmp = (Customer)serviced.elementAt(i);
			totalService += tmp.serviceTime;
			totalWaiting += tmp.waitTime;
		}
		System.out.println("Total service time: " + totalService);
		System.out.println("Total wait time: " + totalWaiting);
	}

//-------------------------------------------

	/** Main(), where the design is tested. */


	public static void main(String[] argv) {
		ThreadedIceCreamShop sim = new ThreadedIceCreamShop();

		sim.begin();
	}



} // end ThreadedIceCreamShop


/*****************************************************************
/**
*	Customer: Represents an arrival and is used to track
*	various information such as time in queues and in service.
*/


class Customer {

	public static final int TOT_SERVICE_TIME = 20; // (sec.)

	public int waitTime;
	public int serviceTime;

	public Customer() {
		waitTime = 0;
		serviceTime = 0;
	}

	public void incrementWait(int i) {
		waitTime += i;
	}

	public void incrementServTime(int t) {
		serviceTime += t;
	}

	public int getTotalWait() {
		return waitTime + serviceTime;
	}

} // end Customer


/*****************************************************************
/**
*	ArrivalsOne: the first algorithm that represents the concurrent
*	arrival of customers on a particular interval (1 per 30 sec.).
*	The customers are then placed into an event queue so that they
*	may be serviced on a first-come, first-serve basis.
*/

class ArrivalsOne extends Thread {

	public ArrivalQueue qu;
	public Clock clock;
	public int curtime;
	public int lastime;

	public ArrivalsOne(Clock c, ArrivalQueue a) {
		qu = a;
		clock = c;
	}

	/** run: The method that will time the entrance (arrival)
	*	of customers at 2 people/minute and stick each one
	*	into an ArrivalQueue to be distributed appropriately
	*	to one (of the two, in this case) server(s).
	*
	*	Algorithm:
	*	1) if(time has changed and time-FIRST_ARRIVAL%30 == 0)
	*		enqueue a new customer
	*		increment all customer waits approp. time
	*	2) else { just wait (yield) }
	*
	*
	*/

	public void run() {		//Arrivals run

		while(!clock.isDone) {
			if((curtime = clock.getTime()) > lastime) {
				qu.incCustomerWaits(1);
				if((curtime-ThreadedIceCreamShop.FIRST_ARRIVAL) % ThreadedIceCreamShop.ARRIVAL_INTERVAL == 0) {
						qu.enqueue(new Customer());
				}
			}
			lastime = curtime;
			yield();
		}
	}

} // end ArrivalsOne



/*****************************************************************
/**
*	ArrivalsOne: the second algorithm that represents the concurrent
*	arrival of customers on a particular interval (random at 3/60 sec.).
*	The customers are then placed into an event queue so that they
*	may be serviced on a first-come, first-serve basis.
*/


class ArrivalsTwo extends Thread {

	public ArrivalQueue qu;
	public Clock clock;
	public int curtime;
	public int lastime;

	public ArrivalsTwo(Clock c, ArrivalQueue a) {
		qu = a;
		clock = c;
	}

	/** run: The method that will time the entrance (arrival)
	*	of customers at 2 people/minute and stick each one
	*	into an ArrivalQueue to be distributed appropriately
	*	to one (of the two, in this case) server(s).
	*
	*	Algorithm:
	*	1) if(time has changed and time-FIRST_ARRIVAL%30 == 0)
	*		enqueue a new customer
	*		increment all customer waits approp. time
	*	2) else { just wait (yield) }
	*
	*
	*/

	public void run() {		//Arrivals run

		while(!clock.isDone) {
			if((curtime = clock.getTime()) > lastime) {
				qu.incCustomerWaits(1);
				if((curtime-ThreadedIceCreamShop.FIRST_ARRIVAL) % ThreadedIceCreamShop.ARRIVAL_INTERVAL == 0) {
						qu.enqueue(new Customer());
				}
			}
			lastime = curtime;
			yield();
		}
	}

} // end ArrivalsTwo


/*****************************************************************
/**
*	Server: represents one server that works at
*	the shop, in this case an ice-cream server.
*/

class Server extends Thread {

	private Customer currentCustomer;
	private Clock clock;
	private ArrivalQueue queue;
	private int lastime;
	private int curtime;
	public Vector done;


	public Server(Clock c, ArrivalQueue a, Vector serviced) {
		currentCustomer = null;
		clock = c;
		queue = a;
		lastime = 0;
		curtime = 0;
		done = serviced;
	}

	/** run(): Every server will be required to
	*	serve ice-cream nonstop, without breaks,
	*	throughout the duration of this simulation.
	*
	*	Algorithm:
	*	1) if(currentCustomer == null) pop from the ArrivalQueue
	*	2) else
	*		service the currentCustomer by incrementing approp. time
	*	3) yield()
	*/

	public void run() {		//Server run

		while(!clock.isDone) {
			curtime = clock.getTime();
			if(!(currentCustomer == null)) {
				if(curtime > lastime) {
					currentCustomer.incrementServTime(curtime - lastime);
				}
				if(currentCustomer.serviceTime >= Customer.TOT_SERVICE_TIME) {
					done.addElement(currentCustomer);
					currentCustomer = null;
			  	}
			}
			if((currentCustomer == null) && (!queue.isEmpty())) {
				currentCustomer = queue.pop();
			 }
			lastime = curtime;
			yield();
		}
	}

} // end Server

/*****************************************************************
/**
*	ArrivalQueue: represents a LIFO data structure for
*	the queuing of customers in this application.
*
*	This is done simply by using a Vector and its operations
*	as a collection.
*/

class ArrivalQueue  {

	Vector queue;

	public ArrivalQueue() {
		queue = new Vector();
	}

//---------------------

		/** standard operations associated with my LIFO */

	public int size() {  return queue.size(); }

	public int capacity() {  return queue.capacity();  }

	public void enqueue(Customer c) {
		queue.addElement(c);
		//System.out.println("Customer Inserted: #" + size());
	}

	public Customer pop() {
		Customer temp = null;
		if(!isEmpty()) {
			temp = (Customer)queue.elementAt(0);
			queue.removeElementAt(0);
		}
		return temp;
	}

	public void dequeueAll() {  queue.clear();  }

	public boolean isEmpty() {  return queue.isEmpty();  }

//---------------------

	/** allows the Arrivals thread to update the waiting
	*	time of all those customers currently in the
	*	the ArrivalsQueue.
	*/

	public void incCustomerWaits(int j) {
		if(!isEmpty()) {
			int len = size();
			for(int i=0; i<len; i++) {
				((Customer)queue.elementAt(i)).incrementWait(j);
			}
		}
	}

} // end ArrivalQueue



/*****************************************************************
/**
*	Clock: represents the 'real time' of the simulation.
*	It is used as a global object that all other threads
*	will reference and update from.
*
*	This object is written to increment one second every
*	10 milliseconds (alterable by magic number).
*
*	@author anthony.francis@asu.edu
*	@version 10.17.2002
*/


class Clock extends Thread {

	public int time;
	public boolean isDone;

	/** Creates a new clock thread
	*	that may run in synch with
	*	the Server and Arrivals
	*	procedures.
	*/

	public Clock() {
		time = 0;
		isDone = false;
	}

	/** run: Drives the main functionality of
	*	the class, incrementing the second hand
	*	of the clock after 10 millis. cycles.
	*/

	public void run() {
		//System.out.println("Clock: " + time);
		while(time < ThreadedIceCreamShop.TOT_SIM_TIME) {
			yield();
			increment();
		}
		isDone =  true;
		ThreadedIceCreamShop.displayResults();

	}

	/** increment: A simple method that will
	*	add ticks to the clock and should do
	*	so without other threads interrupting.
	*/

	public synchronized void increment() {
		time += ThreadedIceCreamShop.SECOND_INTERVAL;
		notifyAll();
	}

	/** getTime: Allows other classes to acces
	*	the current time at any given time
	*	in the simulation.
	*/

	public synchronized int getTime() {
		return time;
	}

} // end Clock