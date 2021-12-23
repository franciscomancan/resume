//////////////////////////////////////////////////////////////////////////////////
/** Frame: represents the smallest unit of transmission (same
*	as packet) and is generated dynamically as the network runs.
*	Once the frame has been placed into a node's background queue,
*	they may be transmitted at a rate of just 1 per cycle
*	(having a free line to transmit).
*
*	@author anthony.francis@asu.edu
*	@version 4.29.2003
*/
class Frame {

		/** each frame has one of three purposes in sim. */
	public static final int DATA = 1, ACK = 2, ERROR = 3;

		/** states used in logic of transfers of frames to and fro */
	public static final int READY = 0, WAITING = 1, SENT = 2;

	//sequence num.
	public int seq;

	//index so that intermed.
	//nodes know where to forward
	public int dest;

	// indicates purpose
	public int type;

	//indicates state
	public int state;

	//indicates node index
	public int source;

	public Frame(int s, int d, int t, int so) {
		seq = s;
		dest = d;
		type = t;
		state = READY;
		source = so;
	}

	/*********************************
	/*	toString: std. override
	*/
	public String toString() {
		String buf = new String(dest + ":" + seq + ":" + source + ":" + type);
		return buf;
	}
}
