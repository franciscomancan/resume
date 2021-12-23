/**
*	Cache.java: three classy objects that simulate the effects
*	of different types of microcomputer caches:
*		1) directly mapped
*		2) 2-way set associative
*		3) 4-way set associative
*
*		Requirements:
*	Each cache consists of 8 one-word entries (8*32bits)
*	Each block will hold its own memory address as contents
*	Replacement policy is LEAST RECENTLY USED
*
*	The Cache class serves as the driver that compares
*	the performance of the others by using 1000 randomly
*	generated entries.
*
*	@author anthony.francis@asu.edu
*	@version 1.0
*	@date 4.20.2002
*/


import java.io.*;
import java.util.*;

//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%


	/** class Block: this object represents one of the
	*	eight blocks that stores data in each type of
	*	cache.
	*/

class Block {

	/** characters that represent both states of a bit */
	protected static final char ASSERTED = '1', DEASSERTED = '0';

	/** each of these fields
	*	substitute for their
	*	corresponging bit strings
	*	in a cache (int values used)
	*/
	private boolean valid;
	private int tag;
	private int data;

	/** instantiates a Block
	*	object with zeros
	*/

	public Block() {
		valid = false;
		tag = 0;
		data = 0;
	}

	/** Modifier method */
	public void setData(int a) { data = a; }

	/** Data-access method */
	public int getData() { return data; }

	/** Data-access method */
	public boolean isValid() { return valid; }

	/** Modifier method */
	public void setValid(boolean v) { valid = v; }

	/** Modifier method */
	public void setTag(int t) { tag = t; }

	/** Data-access method */
	public int getTag() { return tag; }

	/** Overriden method for string representation */
	public String toString() {
		StringBuffer s = new StringBuffer();
		s.append("Valid: " + isValid());
		s.append(" Tag: " + getTag());
		s.append(" Data: " + getData());

		return new String(s);
	}

} //end Block


//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%



	/** class DirectMap: any given word of data is stored
	*	in and accessed from the cache using the lower-order
	*	bits of the address. (exactly one spot for each block)
	*
	*	Tags and the valid bit are then used, after cache
	*	has been filled, to retrieve recently used data.
	*/

class DirectMap {

	/** a handle to the blocks that represent the cache */
	private Block[] entries;

	/** constructor initializes 8 new blocks for storage */
	public DirectMap() {
		entries = new Block[Cache.TOTAL_BLOCKS];
		for(int i=0; i<Cache.TOTAL_BLOCKS; i++)
			entries[i] = new Block();
	}


	/** A method to return a particular block.
	*	NOTE: uses array indexing (starts @ zero)
	*/
	public Block getBlock(int index) { return entries[index]; }

	/** Used to find data in the cache.
	*	If the data isn't in one of the
	*	blocks then a replacement will
	*	be made at the given position.
	*
	*	@returns String that indicates hit or miss
	*/

	public char access(int curAddress) {
		double total = 0, binary = 2.0;

		// get block position
		int offset = curAddress % Cache.TOTAL_BLOCKS;

		// get tag value for target data (copies appropriate portion of array)
		char[] tmp = (Integer.toBinaryString(curAddress)).toCharArray();
		if(tmp.length != 7) {
			char[] another = new char[7];
			int dest = Cache.MAX_ADDRESS_LENGTH-tmp.length;
			System.arraycopy(tmp,0,another,dest,tmp.length);
			int g=0;
			while(!(another[g]==Block.ASSERTED || another[g]==Block.DEASSERTED)) {
				another[g++] = Block.DEASSERTED;
			}
			tmp = another;
		}

		// converts (binary) tag value into int
		char[] curTag = new char[4];
		System.arraycopy(tmp,0,curTag,0,curTag.length);
		for(int j=curTag.length-1,r=0; j>=0; j--,r++) {
			if(curTag[j] == Block.ASSERTED) {
				double exp = r;
				total += Math.pow(binary,exp);
			}
		}
		Double d = new Double(total);
		int newTag = d.intValue();

		// survey says?
		Block block = entries[offset];
		if(block.isValid() && block.getTag() == newTag) {
			return Cache.HIT;
		}
		else {							// replaces in case of a miss
			block.setValid(true);
			block.setTag(newTag);
			block.setData(curAddress);
			return Cache.MISS;
		}
	}

} //end DirectMap

//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

	/**	class TwoWaySet: a 2-way set associative cache.
	*	The total amount of cache blocks is divided by
	*	2, leaving n/2 sets of blocks.
	*/

class TwoWaySet {

	/** this constant represents the 2-way association */
	private static final int SET_FACTOR = 2;

	/** constants that serve as tags that indicate the least
	*	recently used block of the two
	*/
	private static final int FIRST_BLOCK = 0, SECOND_BLOCK = 1;

	//instance variables
	private Block[][] entries;
	private int[] lru;


	/*	A 2-dimensional array used to
	*	represent this cache.
	*/

	public TwoWaySet() {
		entries = new Block[4][2];
		for(int i=0; i<4; i++) {
			for(int j=0; j<2; j++)
				entries[i][j] = new Block();
		}
		lru = new int[4];
		for(int j=0; j<lru.length; j++) lru[j] = 1;

	}

	/** Access, the method used by this class to access
	*	the data of the given address.
	*	A multidimensional array used
	*	as data structure to iterate through
	*	various blocks.
	*
	*	@returns String that indicates a hit or miss(& replace)
	*/

	public char access(int curAddress) {
		double total = 0, binary = 2.0;

		// get block position (top or bottom half)
		int offset = curAddress % 4;

		// pad binary strings to get correct tag value for target data
		// (String representations will only used enouch characters
		//	necessary for magnitude)

		char[] tmp = (Integer.toBinaryString(curAddress)).toCharArray();
		if(tmp.length != 7) {
			char[] another = new char[7];
			int dest = Cache.MAX_ADDRESS_LENGTH-tmp.length;
			System.arraycopy(tmp,0,another,dest,tmp.length);
			int g=0;
			while(!(another[g]==Block.ASSERTED || another[g]==Block.DEASSERTED)) {
				another[g++] = Block.DEASSERTED;
			}
			tmp = another;
		}

		// get integr value from a string of 1's & 0's
		char[] curTag = new char[5];
		System.arraycopy(tmp,0,curTag,0,curTag.length);
		for(int j=curTag.length-1,r=0; j>=0; j--,r++) {
			if(curTag[j] == Block.ASSERTED) {
				double exp = r;
				total += Math.pow(binary,exp);
			}
		}
		Double d = new Double(total);
		int newTag = d.intValue();

		Block block1 = entries[offset][0], block2 = entries[offset][1];
		if(block1.isValid() && block1.getTag() == newTag) {
			lru[offset] = SECOND_BLOCK;
			return Cache.HIT;
		}
		else if(block2.isValid() && block2.getTag() == newTag) {
			lru[offset] = FIRST_BLOCK;
			return Cache.HIT;
		}
		else if(lru[offset] == SECOND_BLOCK) {	// => replace second entry
			block2.setValid(true);
			block2.setTag(newTag);
			block2.setData(curAddress);
			lru[offset] = FIRST_BLOCK;
			return Cache.MISS;
		}
		else {									// => replace second entry
			block1.setValid(true);
			block1.setTag(newTag);
			block1.setData(curAddress);
			lru[offset] = SECOND_BLOCK;
			return Cache.MISS;
		}

	}


} //end TwoWaySet

//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

class FourWaySet {

	/** there will be only two main blocks in this cache */
	private static final int SET_FACTOR = 2;

	/** integers representing each of the different blocks */
	private static final int FIRSTBLOCK = 0, SECONDBLOCK = 1, THIRDBLOCK = 2,
								FORTHBLOCK = 3;

	/** instance fields */
	private Block[][] entries;
	private int[][] lru;


	/** a 2*4 dimensional array
	*	represents this cache and
	*	a set of tags to keep track
	*	of replacement policy.
	*/

	public FourWaySet() {
		entries = new Block[2][4];
		lru = new int[2][4];
		for(int i=0; i<2; i++) {
		   for(int e=0; e<4; e++) {
			entries[i][e] = new Block();
			lru[i][e] = 0;
		   }
		}
	}


	/**	This method is used to increment
	*	index values that represent a
	*	word's time since last replacement.
	*	(incremented every cycle or cache access)
	*
	*	The indexes are then used to provide
	*	the least frequently used entry
	*	of a block of cache.
	*
	*	@returns void
	*/

	private void bump(int block, int priority) {
		lru[block][priority] = 0;

		for(int w=0; w<4; w++) {			// Increment the elapsed time of
			if(w != priority) {				// all word spaces other than the
				lru[block][w]++;			// one being replaced or accessed.
			}								// The cache that was accessed is
		}									// restarted at 0 (no elapsed time).
	}

	/** This method is used to find the
	*	least recently used word of one
	*	of the two possible blocks in this
	*	cache.  It determines which wordspace
	*	has accumulated the most cycles (max) since
	*	use, therefore the least frequently used.
	*	It is called when the
	*	information is required for a replacement.
	*
	*	@returns int index of the 0-3 words that should be replaced
	*/

	private int lru(int offset) {
		int max = 0;
		int most = 0;
		for(int i=0; i<4; i++) {
			if(lru[offset][i] > max) {
				max = lru[offset][i];
				most = i;
			}
		}

		return most;	// returns the index of the least recently used
	}


	/** Access, has the same function as its
	*	counterpart in the prior classes.
	*	Slight alterations have been made
	*	to make its associativity work.
	*	(array of different dimensions)
	*/

	public char access(int curAddress) {
		double total = 0, binary = 2.0;

		// get block position
		int offset = curAddress % 2;

		// get tag value for target data
		char[] tmp = (Integer.toBinaryString(curAddress)).toCharArray();
		if(tmp.length != 7) {
			char[] another = new char[7];
			int dest = Cache.MAX_ADDRESS_LENGTH-tmp.length;
			System.arraycopy(tmp,0,another,dest,tmp.length);
			int g=0;
			while(!(another[g]==Block.ASSERTED || another[g]==Block.DEASSERTED)) {
				another[g++] = Block.DEASSERTED;
			}
			tmp = another;
		}

		//get tag value from a binary string (char[])
		//tag size also increases with less associativity
		char[] curTag = new char[6];
		System.arraycopy(tmp,0,curTag,0,curTag.length);
		for(int j=curTag.length-1,r=0; j>=0; j--,r++) {
			if(curTag[j] == Block.ASSERTED) {
				double exp = r;
				total += Math.pow(binary,exp);
			}
		}
		Double d = new Double(total);
		int newTag = d.intValue();			// tag value extracted


		/** each of the entries of the block are searched */
		Block[] blocks = new Block[4];
		for(int s=0; s<4; s++)		//get block of entries
			blocks[s] = entries[offset][s];

		if(blocks[FIRSTBLOCK].isValid() && blocks[FIRSTBLOCK].getTag() == newTag) {
			bump(offset,FIRSTBLOCK);
			return Cache.HIT;
		}
		else if(blocks[SECONDBLOCK].isValid() && blocks[SECONDBLOCK].getTag() == newTag) {
			bump(offset,SECONDBLOCK);
			return Cache.HIT;
		}
		else if(blocks[THIRDBLOCK].isValid() && blocks[THIRDBLOCK].getTag() == newTag) {
			bump(offset,THIRDBLOCK);
			return Cache.HIT;
		}
		else if(blocks[FORTHBLOCK].isValid() && blocks[FORTHBLOCK].getTag() == newTag) {
			bump(offset,FORTHBLOCK);
			return Cache.HIT;
		}
		else {
			int index = lru(offset);
			blocks[index].setValid(true);
			blocks[index].setTag(newTag);
			blocks[index].setData(curAddress);
			bump(offset,index);
			return Cache.MISS;
		}

	}

} //end FourWaySet

//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%


public class Cache {

	/** requirements of test data used to compare cache performance */
	private static final int TEST_SET_SIZE = 1000, TEST_SET_RANGE = 100;

	/** maximum length of binary string given address space (0-99) */
	protected static final int MAX_ADDRESS_LENGTH = 7;

	/** each of the caches consists of 8 blocks of storage */
	protected static final int TOTAL_BLOCKS = 8;

	/** strings used to analyze resluts */
	protected static final char HIT = 'h', MISS = 'm';

	/** handle to minimize typing */
	private static final PrintStream o = System.out;

	/** The main method is the driver that will
	*	generate the test data and plug it into
	*	each of the cache simulators.
	*
	*	The data for each is displayed along
	*	with their miss rates
	*/

	public static void main(String[] argv) {

		int totalMisses = 0;

		Random rand = new Random();
		int[] input = new int[TEST_SET_SIZE];

		o.println("The test set.");
		for(int i=0; i<TEST_SET_SIZE; i++) {
			o.print((input[i] = Math.abs(rand.nextInt() % TEST_SET_RANGE)) + " ");
			if((i+1)%20 == 0) o.print("\n");
		}

		//test first one

		DirectMap cache1 = new DirectMap();
		char[] results1 = new char[TEST_SET_SIZE];
		for(int i=0; i<TEST_SET_SIZE; i++) {
			o.print((results1[i] = cache1.access(input[i])) + " ");
			if((i+1)%30 == 0) o.print("\n");
			if(results1[i] == MISS) totalMisses++;
		}
		double rate = totalMisses; totalMisses = 0;
		o.println("\nDirectly Mapped cache's miss rate = " + rate/10f + "%");

		//test second

		TwoWaySet cache2 = new TwoWaySet();
		char[] results2 = new char[TEST_SET_SIZE];
		for(int i=0; i<TEST_SET_SIZE; i++) {
			o.print((results2[i] = cache2.access(input[i])) + " ");
			if((i+1)%30 == 0) o.print("\n");
			if(results2[i] == MISS) totalMisses++;
		}

		rate = totalMisses; totalMisses = 0;
		o.println("\nTwo-way set associative cache's miss rate = " + rate/10f + "%");

		//test third

		FourWaySet cache3 = new FourWaySet();
		char[] results3 = new char[TEST_SET_SIZE];
		for(int i=0; i<TEST_SET_SIZE; i++) {
			o.print((results3[i] = cache3.access(input[i])) + " ");
			if((i+1)%30 == 0) o.print("\n");
			if(results3[i] == MISS) totalMisses++;
		}
		rate = totalMisses;
		o.println("\nFour-way set associative cache's miss rate = " + rate/10f + "%");

		System.gc();
		System.exit(0);
	}

} //end Cache