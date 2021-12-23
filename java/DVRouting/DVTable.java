import java.io.*;
import java.text.*;

//////////////////////////////////////////////////////////////////////////////////
/** DVTable: represents the table maintained by each node
*	and indicates the delay on each path to adjacent routers.
*	All operations on the tables are also implemented here.
*
*	@author anthony.francis@asu.edu
*	@version 4.29.2003
*/
public class DVTable {

		/** indeces array, ties column data in the dvtable to particular nodes,
		*	format ==> { node row, node col, dv row index }
		*/
	public  static final int[][] indeces = {	{0,0},{0,1},{0,2},
															{1,0},{1,1},{1,2},
															{2,0},{2,1},{2,2} };

		/** a large number used as the bound of infinity  (route dist.)*/
	public static final int INFINITY = 998;

		/** a number representing "null route" and "null index",
		*	(values other than zero that carry no weight)
		*/
	public static final int NULL_ROUTE = 999;

		/** magic number representing the width of the routing
		*	element of the dvtable
		*/
	public static final int RTABLE_WIDTH = 2;

		/** some magical numbers that indicate the array
		*	index of the corresponding values in a DVTable
		*/
	public static final int LINK = 0, DELAY = 1;

	public static final PrintStream o = System.out;

	public Netnode parent; //---------------------------
	// delays from adjacencies (neighbor vectors)
	//	each col = a vector from neighbor, order of iteration, corresponding
	// to the destination (row index)
	public int[][] entries; //---------------------------
	// delays for routing (computed),
	// first col = intermediate route if any, sec col = cost (correspond to index)
	public int[][] routes; //---------------------------
	public int[] adjacencies;	// holds indeces of adjacent routers

	public DVTable(Netnode p) {
		parent = p;
	}

	//**************************************************
	/** initTable: the method is called to initialize
	*	 a node's vector and path tables (the getAdjacencies
	*	 method must be called first, since the data is needed
	*	for correct table sizes).
	*/
	public void initTable() {
		entries = new int[Netsim.TOTAL_NODES][adjacencies.length];
		routes = new int[Netsim.TOTAL_NODES][RTABLE_WIDTH];
				/** init all vectors for adjacent nodes,
				*	routing decisions are then computed
				*	from this (don't touch "routes") */
		for(int i=0; i<Netsim.TOTAL_NODES; i++) {
			for(int j=0; j<adjacencies.length; j++) {
				if(i == adjacencies[j])
					entries[i][j] = 0;				// distance to self is nill
				else
					entries[i][j] = INFINITY;		//init all others to "INFINITY"
			}
		}
	}

	//**************************************************
	/** getIndex: static method that will associate a row and
	*	column number of a node within the topology with the
	*	correct index, later to be used by the routes table
	*/
	public int getIndex() {
		int row = parent.row, col = parent.col;
		int result = -1;
		for(int i=0; i<indeces.length; i++) {
			if(indeces[i][0] == row && indeces[i][1] == col) {
				result = i;
			}
		}
		return result;
	}

	//**************************************************
	/** displayTable: a ascii-graphical representation to view
	*	one state of a table; aids in the process of debugging
	*	and establishing fucntionable routing.
	*/
	public void displayTable() {
		NumberFormat num = NumberFormat.getInstance();
		num.setMinimumIntegerDigits(Netsim.INT_FORMAT);
		o.println("        -------------------------");
		o.println("        Vector Entries - (" + parent.row + "," + parent.col + ")");
		o.println("        -------------------------");

			/**rest of table built in buffer */
		StringBuffer vectorBuff = new StringBuffer();
			/** append header */
		vectorBuff.append("         ");
		for(int j=0; j<adjacencies.length; j++) {
			vectorBuff.append(" " + adjacencies[j] + "  | ");
		}
		vectorBuff.append("line  | delay |");
		vectorBuff.append("\n        -------------------------\n");
		vectorBuff.append("\n");
			/** iterate through entries and append,
			*	outer loop includes 1 row of data */
		for(int i=0; i<indeces.length; i++) {
			vectorBuff.append("Node[" + i + "]: ");
			for(int j=0; j<adjacencies.length; j++) {
				vectorBuff.append(num.format(entries[i][j]) + " | ");
			}
			for(int j=0; j<RTABLE_WIDTH; j++) {
				vectorBuff.append(" " + num.format(routes[i][j]) + "  | ");
			}
			vectorBuff.append("\n");
		}
		String result = new String(vectorBuff);
		o.println(result);
		o.println("        -------------------------");
	}

} // end DVTable
