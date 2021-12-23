import java.io.*;
import java.util.*;

//////////////////////////////////////////////////////////////////////////////////
/** Netnode: represents a given router within the network,
*	each one containing the properties necessary to track
*	its own routes.
*
*	@author anthony.francis@asu.edu
*	@version 4.29.2003
*/
public class Netnode {

		/* maximum allowed window sizes for both transmission and reception */
	public static final int SENDERS_WIN_SIZE = 8, RECV_WIN_SIZE = 1;
	public static final int MAX_SIZE_BUFFER = 20;

		/** The destination coordinates have been hard coded here
		*	according to specs and correspond to node indeces.
		*/
	public static final int[][] DESTINATION_ROWS =	{ { 2,2,2 }, { 1,1,1 }, { 0,0,0 } };
	public static final int[][] DESTINATION_COLS =	{ { 2,1,0 }, { 2,0,0 }, { 2,1,0 } };

	public static final PrintStream o = System.out;

	public int row, col;
	public DVTable dv;
	public Queue frames;
	public Frame[][] sWin;
	public Frame[] rWin;
	public int destRow, destCol;
	public int index;
	public int sequenceCounter;
	public int successes;

	public Netnode(int r, int c) {
		row = r;
		col = c;
		dv = new DVTable(this);
		frames = new Queue();
		destRow = DESTINATION_ROWS[r][c];
		destCol = DESTINATION_COLS[r][c];
		index = dv.getIndex();
		sequenceCounter = 0;
		successes = 0;
	}

	//**************************************************
	/** setBuffers: used to initialize the
	*	transmitting/receiving windows
	*	for the node.  (adjacencies field
	*	in the "dv" object must be
	*	set before this method is called).
	*/
	public void setBuffers() {
		int adjacentNodes = dv.adjacencies.length;
		sWin = new Frame[adjacentNodes][SENDERS_WIN_SIZE];
		rWin = new Frame[adjacentNodes];
		for(int i=0; i<adjacentNodes; i++) {
			for(int j=0; j<SENDERS_WIN_SIZE; j++) {
				sWin[i][j] = null;
			}
		}
		for(int i=0; i<adjacentNodes; i++) {
			rWin[i] = null;
		}
	}

	/***************************************************
	/** getIndex: returns the appropriate index
	*	for the row and column given.
	*/
	public int getIndex(int row, int col) {
		int result = -1;
		for(int i=0; i<DVTable.indeces.length; i++) {
			if(DVTable.indeces[i][0] == row && DVTable.indeces[i][1] == col) {
				result = i;
			}
		}
		return result;
	}

	//**************************************************
	/** toString: std. method override for this type of object
	*/
	public String toString() {
		String result;
		result = new String("Node position: (" + row + "," + col + ")\n" +
					"Num frames enqueued: " + frames.size() + "\n" +
					"Destination: (" + destRow + "," + destCol + ")");
		return result;
	}

	//**************************************************
	/* Used every 10 units of time to generate p+q+1 frames,
	*	where p and q are the indexes of the node.  This method
	*	only responsible for inserting new frames to queue,
	*	timing is taken care of elsewhere.
	*/
	public void generateFrames() {
		int destin = getIndex(destRow,destCol);
		int numFrames = row+col+1;

		for(int i=0; i<numFrames; i++) {
			frames.enqueue(new Frame(sequenceCounter,destin,Frame.DATA,index));
			sequenceCounter+=1;
		}
	}

	//**************************************************
	/* Initiates the array in a dvtable (of a node) that represents
	*	all other adjacent nodes in the topology.  Minimal routes
	*	can then be identified relative to those adjacencies.
	*/
	public void getAdjacencies(Netnode[][] net) {
		int[] temp = new int[Netsim.POSSIBLE_ADJACENCIES];
		int[] result;
		int adjs = 0;

		for(int i=0; i<temp.length; i++) temp[i] = -1;
		int thisRow = row, thisCol = col;
		Netnode current;

		for(int i=0; i<Netsim.ROWS; i++) {			//for each node
			for(int j=0; j<Netsim.COLS; j++) {
				current = net[i][j];
				int thatRow = current.row, thatCol = current.col;
				if(thisRow == thatRow && thisCol == thatCol)
					continue;
				else if(thisRow == thatRow && Math.abs(thisCol-thatCol) <= 1) {
					temp[adjs++] = current.dv.getIndex();
				}
				else if(thisCol == thatCol && Math.abs(thisRow-thatRow) <= 1) {
					temp[adjs++] = current.dv.getIndex();
				}
			}
		}
		result = new int[adjs];
		System.arraycopy(temp,0,result,0,adjs);
		dv.adjacencies = result;

	}

	/** generates and enqueues another frame with
	*	the purpose of moving to the next state,
	*	(with respect to seq. nums and window sizes)
	*/
	public void generateAck(int seq) {
		Frame tmp;
		int destination = getIndex(destRow,destCol);
		tmp = new Frame(seq,destination,Frame.ACK,index);
		//frames.enqueue(tmp);
		insertToSend(tmp);
	}


	//**************************************************
	/** isAdjacent: indicates whether the argument
	*	(index of node other than this) is indeed
	*	one of the adjacent nodes contained in the
	*	"adjacencies" array.
	*/
	public boolean isAdjacent(int otherIndex) {
		boolean result = false;
		for(int i=0; i<dv.adjacencies.length; i++) {
			if(dv.adjacencies[i] == otherIndex)
				result = true;
		}
		return result;
	}

	/*****************
	/** insertToSend: inserts another frame into the
	*	appropriate sending window if capable.
	*
	*	argument = frame to send, node index of dest.
	*/
	public int insertToSend(Frame f) {
		int destination = f.dest;
		int tmpDex = -1;
		int result = -1;
			/* find correct sender's window */
		for(int i=0; i<dv.adjacencies.length; i++) {
			if(dv.adjacencies[i] == dv.routes[destination][DVTable.LINK])
				tmpDex = i;
		}
			/* insert frame if room */
		if(getSenderSize(tmpDex) < SENDERS_WIN_SIZE) {
			for(int dex=0; dex<SENDERS_WIN_SIZE; dex++) {
				if(sWin[tmpDex][dex] == null) {
					sWin[tmpDex][dex] = f;
					result = 0;
					break;
				}
			}
		}
		return result;
	}

	/*************************
	/** getTotalSize: returns the current size
	*	or vacancy of the buffers for this node.
	*	Should never exceed MAX_SIZE_ BUFFER.
	*/
	public int getTotalSize() {
		int adjacents = dv.adjacencies.length;
		int result = 0;

		for(int i=0; i<adjacents; i++) {
			for(int j=0; j<SENDERS_WIN_SIZE; j++) {
				if(sWin[i][j] != null) result++;
			}
		}
		return result;
	}

	/** returns total num occupants of one adjacent sender's win */
	public int getSenderSize(int index) {
		int result = 0;
		for(int i=0; i<SENDERS_WIN_SIZE; i++) {
			if(sWin[index][i] != null) result++;
		}
		return result;
	}

	/** returns total num of occupants of one adjacent receiver win */
	public int getReceiverSize(int index) {
		int result = 0;
			if(rWin[index] != null) result++;

		return result;
	}
} // end Netnode
