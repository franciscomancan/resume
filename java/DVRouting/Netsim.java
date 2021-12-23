/**
*	<b>Netsim.java: a network simulator at two levels of the OSI
*	protocol stack (DataLink & Network).  Three primary concepts
*	are demonstrated here:
*		1) Sliding window protocol (1-bit)
*		2) Go-back N error correction
*		3) Distance vector routing
*
*	The entire subnet is  a 3x3 mesh of nodes, connected in a
*	duplex manner between adjacent nodes, either having
*	2(corner), 3(side), or 4(center) adjacent neighbors.</b>
*
*	@author anthony.francis@asu.edu
*	@version 4.29.2003
*/

import java.io.*;
import java.util.*;

//////////////////////////////////////////////////////////////////////////////////
/** Netsim: the class that acts as the driver for the backend of
*	simulation.  All gui elements are maintained elsewhere.
*/
public class Netsim extends Object {

		/** some constants that define the topology of the network,
		*	according to specs.
		*/
	public static final int ROWS = 3, COLS = 3;
	public static final int TOTAL_NODES = ROWS*COLS;
	public static final int POSSIBLE_ADJACENCIES = 5;

		/** a constant that determines the width of
		*	the display of integers within ascii outputs
		*	to std.out
		*/
	public static final int INT_FORMAT = 3;

		/** two values that represent different cases in the
		*	calling of the route-computation method
		*/
	public static final boolean BASE = true, ELSE = false;

	public static final PrintStream o = System.out;

		/** creates an instance of a network */
	private Netnode[][] network;
	private int iterations;

	public Netsim() {
		network = init();
		iterations = 0;
	}

	//**************************************************
	/** getNode: returns one of the routers from
	*	the network (given index) at hand, for
	*	manipulation of properties (tables, etc...)
	*/
	public Netnode getNode(int ind) {
		int[] tmp = getRowandCol(ind);
		return network[(tmp[0])][(tmp[1])];
	}

	//**************************************************
	/** getRowandCol: returns the row and col
	*	of a node given that nodes index.
	*	(used by getNode(int))
	*/
	public int[] getRowandCol(int ind) {
		int[] tmp = new int[2];
		for(int i=0; i<ROWS; i++)
			for(int j=0; j<COLS; j++)
				if(network[i][j].index == ind) {
					tmp[0] = i;
					tmp[1] = j;
				}
		return tmp;
	}


	//**************************************************
	/** getRoutes: given the index of a node, it
	*	allows for the retrieval of that nodes
	*	routing table (gets a neighbor's routes
	*	to "updateVector").
	*
	*	(used only within the class)
	*/
	private int[] getRoutes(int neighbor) {
		int[] result = new int[TOTAL_NODES];
		Netnode temp = getNode(neighbor);
		for(int node=0; node<TOTAL_NODES; node++) {
			result[node] = temp.dv.routes[node][DVTable.DELAY];
		}
		return result;
	}

	//**************************************************
	/** updateVector: the first portion of the algorithm
	*	used to update information from neighbors.
	*/
	public void updateVectors(int ind) {
		Netnode current = getNode(ind);
		int[] neighbor;
		for(int vector=0; vector<current.dv.adjacencies.length; vector++) {
			neighbor = getRoutes(current.dv.adjacencies[vector]);
			for(int dest=0; dest<TOTAL_NODES; dest++) {
				current.dv.entries[dest][vector] = neighbor[dest];
			}
		}
	}

	/** iteration shorthand */
	public void updateAllVectors() {
		for(int i=0; i<TOTAL_NODES; i++)
			updateVectors(i);
	}

	//**************************************************
	/** computeRoutes: the second portion of the
	*	dv-algorithm used to compute the best path
	*	for the parent of "this" DVTable
	*/
	public void computeRoutes(int ind) {
		Netnode thisNode = getNode(ind);		// route being computed for this node
		int adjAdder;	//adds 1 for any adjacent node, since 1 hop ==> 1
		int [][] theseRoutes, theseEntries;

			//compute index on node-by-node basis, "for each node"
		for(int destination=0; destination<DVTable.indeces.length; destination++) {
			adjAdder = 1;
			theseRoutes = thisNode.dv.routes;
			theseEntries = thisNode.dv.entries;
			//if(thisNode.isAdjacent(destination)) adjAdder += 1;

			if(destination == thisNode.index) {		// self, no delay and route is null
				theseRoutes[destination][DVTable.DELAY] = 0;
				theseRoutes[destination][DVTable.LINK] = DVTable.NULL_ROUTE;
			}
			else if(iterations == 0 /*&& adjAdder == 0*/) {
				theseRoutes[destination][DVTable.DELAY] = DVTable.INFINITY;
				theseRoutes[destination][DVTable.LINK] = DVTable.NULL_ROUTE;
			}
			else {	//otherwise, delay = min from neighbor + neighbor's queue (buffers)

				int tmpIndex = 0, min = theseEntries[destination][0];	//default, first entry
				int[] adjacs = thisNode.dv.adjacencies;

				//find min of entries and enter to "routes"
				for(int vector=1; vector<adjacs.length; vector++) {
					if(theseEntries[destination][vector] < min) {	//min changes
						min = theseEntries[destination][vector];
						tmpIndex = vector;
					}
				}
				if(min != DVTable.INFINITY) {
					theseRoutes[destination][DVTable.DELAY] = min + adjAdder +
							(min != DVTable.INFINITY?(getNode(adjacs[tmpIndex]).frames.size()):0);
					theseRoutes[destination][DVTable.LINK] = adjacs[tmpIndex];
				}
			}

		}
	}

	/* iteration shorthand */
	public void computeAllRoutes() {
		for(int i=0; i<TOTAL_NODES; i++)
			computeRoutes(i);
	}

	//**************************************************
	/** updateAllTables: updated all vectors and
	*	subsequently computes routes
	*/
	public void updateAllTables() {
		updateAllVectors();
		computeAllRoutes();
		iterations++;
	}

	/**************************************************
	/** displayAllTables: displays all ascii tables
	*	currently in the network (at current state)
	*/
	public void displayAllTables() {
		for(int i=0; i<9; i++) {
			getNode(i).dv.displayTable();
		}
	}

	//**************************************************
	/** init: instantiates all the routers in the network,
	*	including DVTables and send&rec windows
	*/
	public Netnode[][] init() {
		o.print("\tinitializing");
		Netnode[][] network = new Netnode[ROWS][COLS];
		for(int i=0; i<ROWS; i++) {
			for(int j=0; j<COLS; j++) {
				network[i][j] = new Netnode(i,j);
			}
		}
		Netnode tmp;
		for(int i=0; i<ROWS; i++) {
			for(int j=0; j<COLS; j++) {
				tmp = network[i][j];
				tmp.getAdjacencies(network);
				tmp.dv.initTable();
				tmp.setBuffers();
				o.print(".......");
			}
		}
		o.print("\n\n");
		return network;
	}


	//**************************************************
	/** makeTransfers: moves data from sWin of one
	*	node to the rWin of the adjacent nodes.
	*	Once the data is transferred, the nodes
	*	compute their current state for generation
	*/
	public void makeTransfers(int thisNode) {
		Netnode current = getNode(thisNode);
		Netnode nabor;
		Frame[][] sending = current.sWin;

			/* for each adjacent, exchange front of sWin with their rWin */
		Frame tmp;
		int tmpDex = -1;
		for(int adj=0; adj<current.dv.adjacencies.length; adj++) {
			if((tmp = sending[adj][0]) == null) continue;
			nabor = getNode(current.dv.adjacencies[adj]);
			for(int i=0; i<nabor.dv.adjacencies.length; i++) {		// extract corresp. index from neighbor
				if(nabor.dv.adjacencies[i] == current.index) tmpDex = i;
			}
			if(nabor.rWin[tmpDex] == null && tmp.state == Frame.READY) {
				nabor.rWin[tmpDex] = new Frame(tmp.seq,tmp.dest,tmp.type,current.index);
				tmp.state = Frame.WAITING;
			}
		}
	}

	//**************************************************
	/** computeState: makes the necessary computations
	*	to move a given node from one state to the next
	*	and is to be performed for every unit of time.
	*/
	public void computeState(int thisNode) {
		Netnode currentNode = getNode(thisNode);
		Queue theseFrames = currentNode.frames;
		Frame[][] sending = currentNode.sWin;
		Frame[] receiving = currentNode.rWin;
		int[] adjacents = currentNode.dv.adjacencies;

		// iterate through receive windows and make adjustments
		// according to those received frames
		Frame cur;
		int otherNodeIndex;
		for(int win=0; win<adjacents.length; win++) {
			cur = receiving[win];
			otherNodeIndex = adjacents[win];		// convert to usable index

			if(cur == null) continue;					// implies nothing received
			if(cur.type == Frame.ACK) {
				if(cur.dest == currentNode.index) {		// change sWin and rWin
					for(int swin=0; swin<currentNode.dv.adjacencies.length; swin++) {
						for(int spot=0; spot<Netnode.SENDERS_WIN_SIZE; spot++) {
							if((sending[swin][spot].seq) == cur.seq &&
								sending[swin][spot].dest == cur.dest) {
								sending[swin][spot] = null;
								currentNode.successes++;
								o.println("SUCCESS");
								break;
							}
						}
					}
				}
				else {			// must forward
					theseFrames.enqueue(cur);
				}
				receiving[win] = null;
			}
			else if(cur.type == Frame.DATA) {
				if(cur.dest == currentNode.index) {		// gen. ack
					cur.state = Frame.WAITING;
					currentNode.generateAck(cur.seq);
					o.println("Ack generated");
					continue;
				}
				else if(cur.dest == currentNode.index) {
					continue;
				}
				else {		// must forward, priority over frame queue
					if(currentNode.getTotalSize() < Netnode.MAX_SIZE_BUFFER)  {
						currentNode.insertToSend(cur);
					}
					else {
						theseFrames.enqueue(cur);
					}
				}
				receiving[win] = null;
			}
			else {			// must be an error

			}
		} // end rWin iteration

		//trimWindows();

		// iterate through main buffer and dispatch frames
		// into appropriate window, if possible
		Frame temp;
		for(int frame=0; frame<theseFrames.size(); frame++) {
			if(currentNode.getTotalSize() >= Netnode.MAX_SIZE_BUFFER)
				break;
			else {
				temp = (Frame)theseFrames.front();
				if (temp != null) {
					if(currentNode.insertToSend(temp) == 0)
					theseFrames.dequeue();
				}
			}
		}
		//trimWindows();

	}


	/** trimWIndows: consolidates the contents of
	*	all sending windows within the network,
	*	one node at a time.  This allows for the correct
	*	representation of "sliding" buffers.
	*/
	public void trimWindows() {
		Netnode curNode;
		Frame[][] sending;
		int adjs;

		/*for each node */
		for(int node=0; node<TOTAL_NODES; node++) {
			curNode = getNode(node);
			sending = curNode.sWin;
			adjs = curNode.dv.adjacencies.length;

			/* for each neighbor of node*/
			for(int nbor=0; nbor<adjs; nbor++) {
				for(int spot=0; spot<Netnode.SENDERS_WIN_SIZE-1; spot++) {
					if(sending[nbor][spot] == null & spot+1 < Netnode.SENDERS_WIN_SIZE &
										sending[nbor][spot+1] != null)
						sending[nbor][spot] = sending[nbor][spot+1];
				}
			}
		}
	}

	/**********************************************
	/** print utility */
	public void printWindow(int node) {
		Netnode n = getNode(node);

		o.print("sWindows - node " + node + ": (dest:seq:source:type)\n");
		for(int i=0; i<n.dv.adjacencies.length; i++) {
			o.print("To Node:" + n.dv.adjacencies[i] + " ");
			for(int j=0; j<Netnode.SENDERS_WIN_SIZE; j++) {
				o.print(n.sWin[i][j] + ", ");
			}
			o.print("\n");
		}
		o.print("rWin: ");
		for(int i=0; i<n.dv.adjacencies.length; i++) {
			o.print(n.rWin[i] + " - ");
		}
		o.println("");
	}


	//**************************************************
	/** step: performs the operations of the network
	*	in one procedural step so that the events that
	*	occur may be evaluated state-by-state.
	*/
	public void step() {

		updateAllTables();

			/** sending step, transfer buffer info if approp. */
		for(int i=0; i<TOTAL_NODES; i++) {
			makeTransfers(i);
		}

			/** frame generation, if approp. */
		if((iterations % 10) == 0 && iterations > 0) {
			for(int i=0; i<TOTAL_NODES; i++)
			getNode(i).generateFrames();
		}
			/** state change, adjust info per buffer according
			*	to all transfers that occurred in prev. step
			*/
		for(int i=0; i<TOTAL_NODES; i++) {
			computeState(i);
		}


	}


	//**************************************************

	public static void main(String[] argv) {
		Netsim sim = new Netsim();


		BufferedReader stdin =
		      new BufferedReader(
		        new InputStreamReader(System.in));

		char[] buff = new char[2];
		int steps = 10;
		while(true) {
			o.print("Press f key to step * " + steps + ", q to quit:");
			try {
				stdin.read(buff, 0, buff.length);
			} catch(IOException e) { e.printStackTrace(); }
			if(buff[0] == 'q') break;
			if(buff[0] == 'f') {
				for(int i=0; i<steps; i++) {
					sim.step();
				}
				Netnode disp = sim.getNode(8);
				disp.dv.displayTable();
				sim.printWindow(disp.index);
			}
			else o.println("Bad char, try again...");
		}




	} // end main

} // end Netsim
