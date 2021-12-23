/**
*	3dMAC.java: a simulation of a typical MAC
*		protocol.  It is the second assignment of CSE434.
*		Requirements of the protocol are listed below.
*
***	STEP A:  The time axis is slotted, and nodes only report
*	data arrival (if any) at slot boundaries.  When a node
*	has data to transmit, it seeks for an idle channel.  If
*	the channel is found busy, then the node enters into a
*	random wait phase:
*
*	duration = 2^(nodeid# *
*		number of unsuccessful consecutive trials by that node)
*			mod (7)
*
*	At the end of the wait duration, it will be trying the channel
*	again to find out the free/busy status.  This process continues
*	until the channel is found free, i.e., idle.
*
***	STEP B:  SImilar to the p-persistent MAC protocol, the node upon
*	finding a free channel, will attempt to transmit its data with
*	probability = p.  With probability 1-p, it will wait until the
*	next time slot.  If the channel is free at the next time slot
*	also, the node will retry transmission with probability = p, and
*	defer transmission with probability = 1-p.  This process continues
*	until the node transmits its datap, or the channel gets occupied by
*	some other node.
*
***	STEP C:  Immediately after a transmission, the node listens for a
*	potential collision.  If a collision occurs, the node immediately
*	withdraws its transmission (i.e., discontinues the packet), and
*	enters into a random wait phase of the same duration as listed
*	above, except mod (9).  At the end of the wait, it proceeds,
*	to step A.
*
*
*	The network is broadcast based; therefore,
*	most of the data is left protected.
*
*	@author anthony.francis@asu.edu
*	@version 2.28.2003
*	CSE434 Computer Networking Spring 2003
*
*/

import java.io.*;
import java.util.*;

public class Macsim {

		/** magic numbers for the basic stats of the system **/
	protected static final int TOTAL_NODES = 100;
	protected static final int BANDWIDTH = 100000000; //(100 mbps)
	protected static final int NUMBER_BASE = 2;

		/** utility handle */
	protected static final PrintStream o = System.out;
	protected static Random rand = new Random();  // only one created per sim

		/** average values for traffic loads transmitted by nodes,
		*	each of which will be used to produce one set of data **/

	protected static final int SLOT = 1;
	protected static final int LOAD1 = 100000; //(100 kbps)
	protected static final int LOAD2 = 500000; //(500 kbps)
	protected static final int LOAD3 = 1000000; //(1 mbps)
	protected static final int DEFAULT_LOAD = LOAD2;

		/** first and last names, used in the gernation of random vals **/
	protected static final String FNAME = "anthony";
	protected static final String LNAME = "francisco";

		/** constants associtated with frames **/
	protected static final int FRAME_SIZE = 1000;	//actual binary trimmed for
	protected static final int IDLE = 0;				//simplification (not 1024)
	protected static final int BUSY = 1;
	protected static final int CONTENTION = 2;
	protected static final float P_LEVEL = 0.5f;		//prob. level per specs.

		/** Constant that represents whether or not a slot
		*	from any given frame has yet to be transmitted
		*/
	protected static final boolean SENT = false;
	protected static final boolean ACTIVE = true;

		/** message in case of badly formatted command */
	protected static final String USAGE_MESSAGE =
			"\nUsage: java Macsim load\n" +
			"\n\tload = 1, 2, or 3  " +
			"(100kbps, 500kbps, or 1mbps, respectively)\n";

//------------------------------------------------------------------------------------

/** a "radom" number generator used in producing a wait phase,
*	(the first degree of randomness, in the specs).
*/

	protected static long r_1(int id, int failures) {
		long result =
			(long)Math.pow(NUMBER_BASE,(id * failures % FNAME.length()));
		return result;
	}

//------------------------------------------------------------------------------------

/** another "radom" number generator used in producing a wait phase,
*	(the second degree of randomness, in the specs).
*/

	protected static long r_2(int id, int failures) {
		long result =
			(long)Math.pow(NUMBER_BASE,(id * failures % LNAME.length()));
		return result;
	}

//------------------------------------------------------------------------------------

/** another random number generator, this time to produce the
*	effects of a probablistic determination of node interaction
*	(i.e. transmit or defer for antoher slot)
*/

	protected static float getProbability() {
		return rand.nextFloat();
	}

//------------------------------------------------------------------------------------

	public static void main(String[] argv) {
		int loadArg = DEFAULT_LOAD;
		if(argv.length != 1 || argv[0].length() > 1) {	//give usage message
			o.println(USAGE_MESSAGE);
			System.exit(1);
		}
		else {										//assign correct num frames
			int tmp = Integer.parseInt(argv[0]);	//parse argument
			switch(tmp) {
				case 1: { loadArg = LOAD1; break; }
				case 2: { loadArg = LOAD2; break; }
				case 3: { loadArg = LOAD3; break; }
				default: { loadArg = DEFAULT_LOAD; }
			}
			o.println("Load: " + loadArg + "\n");
		}

		Node[] network = new Node[TOTAL_NODES];		//initialize the network structure
		int numFrames = loadArg/FRAME_SIZE;
		o.print("Network initializing");
		for(int init=0; init<TOTAL_NODES; init++) {		//initial node group
			network[init] = new Node(init, numFrames);
			network[init].nextSend = Math.abs(rand.nextInt() % TOTAL_NODES/10);
			if(init % 7 == 0) o.print(" .");
		}
		Channel channel = new Channel();			//instantiate channel obj.
		o.println("Done\n");

		long clock = 0;								//re-initialize clock for "1 sec"

		// Primary loop begins here, where the operations
		// of each loop are assumed to occur within 10ns,
		// which is also the slot delimitation; therefore,
		// one bit may potentially be transmitted every
		// cycle.

		o.println("Begin communications...");
		while(clock < BANDWIDTH) {			// begin communications,
			int concurrentSends = 0;		// increment for each contending node

			long tmp;
			for(int i=0; i<TOTAL_NODES; i++) {
				Node current = network[i];
				if((tmp = current.frames[current.curFrame].startContend) ==
													Frame.NULL_TIME)
					{ tmp = clock; }

				//Determine if node needs to start a NEW send
				//Use randomization to initiate sends. Sends that
				//were delayed from p-persistent or collision doesn't
				//need to do this.

				// Step A begins; however, only if:
				//			1) data is ready
				//			2) channel is in contention (not won)

				if(current.nextSend == clock && current.transmitting == false) {

					if(channel.state == BUSY) {
						current.nextSend = clock + r_1(current.id, current.failures);
					}

//------------------------------------------------------------------------------------
//Step B
					else { // channel not busy, in contention

					    if(getProbability() < P_LEVEL) {	// p, enter into contention
					    	current.transmitting = true;
					       	current.nextSend = clock + r_1(current.id, current.failures);
					       	concurrentSends++;
					    }
					    else {			// 1-p, wait
							current.nextSend = clock + SLOT;
					    }

					}
				}
			} // end first while

//------------------------------------------------------------------------------------
//Step C

			for(int i=0; i<TOTAL_NODES; i++) {
				Node current = network[i];
				if(current.transmitting) {
					if(concurrentSends <= 1) {	//channel has been won
						channel.send(clock, current); //equiv. of sending frame
						for(int j=0; j<TOTAL_NODES; j++) {	//inc. all times
							network[j].nextSend+=FRAME_SIZE;
						}
						current.nextSend = clock +
										FRAME_SIZE +
										r_1(current.id, current.failures);
						clock+= FRAME_SIZE;	// forward clock
					}
					else {		// collision has occured
						current.transmitting = false;
						current.nextSend = clock + r_2(current.id, current.failures++);
									//faiure increment should possibly go upstairs
					}
				}
			}

//------------------------------------------------------------------------------------
//Channel Statistics

			// channel efficiency is calculated and outputs to screen
			// as it proceeds

			if((clock % 10000) == 0) {
				channel.cntavg++;
				float tmpeffic = (float)channel.successes/(float)clock;
				if(clock > 0) o.println(channel.efficiency =
									(channel.efficiency + tmpeffic)/2);
				//if(clock > 0) o.println("Efficiency: " + chaneff);



				//channel.runavg =
				//		((channel.runavg * channel.cntavg) + (float)chaneff) /
				//							(channel.cntavg + 1);
				//o.println(channel.runavg);


				//runavg = ((runavg * cntavg) + chaneff) / (cntavg + 1)
			}

			clock++; //increment time
		}
		System.exit(0);

	} // end Main

} // end Macsim


class Channel {

	protected int state;
	protected int successes;
	protected int cntavg;
	protected long runavg;
	protected float efficiency;

	Channel() {
		state = Macsim.IDLE;
		successes = 0;
		cntavg = 0;
		runavg = 0;
		efficiency = Macsim.P_LEVEL;
	}

//------------------------------------------------------------------------------------

	public void send(long clock, Node curr) {
		long tmp = curr.frames[curr.curFrame].startSend;
		if(tmp == Frame.NULL_TIME) tmp = clock;

		if(curr.curFrame < curr.frames.length-1) {
			curr.curFrame++;					// implies not on last frame yet
		}
		else {
			curr.done = true;
			System.out.println(curr + " is done transmitting all packets.\n");
		}
		this.state = Macsim.IDLE;
		curr.transmitting = false;
		successes+= Macsim.FRAME_SIZE;
	}

} // end Channel


/******************************************************************
* class Node: represents any one node on the network that may
*	acquire service.  Each node will have data packets as required
*	and attempt to transmit on random intervals after creation.
*/

class Node extends Object {

	protected int id;
	protected int failures;
	protected long nextSend;
	protected boolean transmitting;		//communicates node's status slot-by-slot
	protected Frame[] frames;
	protected int curFrame;
	protected boolean done = false;

    Node(int ident, int totFrames) {		//constructor that uniquely defines each node
		id = ident;
		transmitting = false;
		frames = new Frame[totFrames];
		for(int i=0; i<totFrames; i++) {  frames[i] = new Frame(ident);  }
		curFrame = 0;
		failures = 0;
	}

//------------------------------------------------------------------------------------

	/** typical string generator **/

	public String toString() {
		return new String(	"Node id: " + id + "\n" +
										"Transmission Failures: " + failures + "\n" +
										"Total Frames: " + frames.length + "\n");
	}


}  // end Node


/******************************************************************
* class Frame: holds typical info used to identify
*	slots and such
*/

class Frame extends Object {

	protected static final int NULL_TIME = -1;

	protected int id = 0;			//all instance vars.
	protected int slots = 0;
	protected long startContend = 0, startSend = 0;

//------------------------------------------------------------------------------------

		/** Filter constructor automatically creates a frame
		*	with the correct amount of slots (determined by
		*	const. in driver class), data may be identified
		*	down to the slot level (in order to collect collison).
		*/

	protected Frame(int ident, int numSlots) {
		id = ident;
		startContend = startSend = NULL_TIME;
		slots = numSlots;
	}
	protected Frame(int ident)
		{  this(ident,Macsim.FRAME_SIZE);  }


//--------------------

		/** A simple method that aids in deriving
		*	statistical information about the node.
		*/

	protected long getUserResponse() {
		long result = startSend - startContend;
		return result;
	}

} // end Frame
