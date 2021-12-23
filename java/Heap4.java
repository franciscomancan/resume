/* Heap4.java:  an implementation of a 4-ary heap data structure.
*	Most operations do not differ from a binary heap; the key
*	alteration being the change access of D (4) child nodes.
*
* @author anthony.francis
* @version 6.13.01
*/ 

import java.util.*;
import anf.*;
import QuickSort.*;

public class Heap4 extends Object {

	private static final int SENTINEL = 0, D = 4, TESTLENGTH = 20, ROOT_NODE = 1;
	private static final int[] TESTSET = { 1,3,86,2,5,8,12,5,18,52,14,55,78,92,2,7,99,64 },
								INCREASE_KEYS = { 3,7,9,11,15,88 },
								 INSERT_KEYS = { 22,44,66 };
	private int[] a;

	/* constructor */
	public Heap4(int[] ara) {
		a = new int[ara.length+1];
		a[0] = 0;
		System.arraycopy(ara,0,a,1,ara.length);
		this.a = a;
		build();
	}

		/* instance methods */
		//==================//
		// contains the primary accessor methods of the class 
		// (root, parent, child..)

	public int root() {  return a[1];  }

	public int getParent(int i) {  return (int)Math.floor((double)(i-2)/D+1);  }

	public int getChild(int i, int nth) {  
		i = (D*(i-1)+nth+1);  
		if(i > size())
		  return 0;
		else
		  return i;
	}

	/*  size: returns the current length of the array.
	 *	Subtract 1 for a[0] element, cannot multiply by 0
	 *	within the class's heap operations
	*/	
	public int  size() {  return a.length-1;  }

	public void swap(int u, int d) {
		int tmp = a[u];
		a[u] = a[d];
		a[d] = tmp;
	}
											
		/* supporting operations */
		//=======================//
		// contains the those operations characteristic to the
		// heap data structure (heapify, build, insert, extractmax..)

	public void heapify(int i) {
		int[] children = new int[D];
		for(int r=0; r<D; r++)
		  children[r] = getChild(i,r+1);
		int finalIndex = size();

		int largest = i;
		for(int r=0; r<D; r++)
		  if((children[r]<=finalIndex) && (children[r]!=0) && (a[children[r]] > a[largest]))
		    largest = children[r];

		printHeap();
		if(largest != i) {
		  swap(i,largest);
		  heapify(largest);
		}
	}

	/* build: the basis of the data structure.  It builds
	*	the array into a heap in a bottom-up manner.
	*	The complexity of the method = O(dlogd(n)), d=base 4.
	*/ 

	public void build() {
		int len = size();
		for(int i=(int)Math.floor((double)len/2); i>0; i--)
		  heapify(i);
	}

	/* extractMax: this method will find and extract the current maximum value
	*	of the given heap.  The method operates in Theta(dlogd (n)),
	*	where d is base 4.  The actual time is dependent upon the java 
	*	library (arraycopy).
	*/

	public int extractMax() {
		int[] newone = new int[a.length-1];
		int max = root();
		a[1] = a[size()];
		System.arraycopy(this.a,0,newone,0,size());
		heapify(ROOT_NODE);
		return max;
	}

	/* insert: takes an individual key value and places
	*	the node in the appropriate position of the heap.
	*/

	public void insert(int key) {
		int[] another = new int[a.length+1];
		System.arraycopy(a,0,another,0,a.length);
		a = another;
		int i = size();
		while((i > 1) && (a[getParent(i)] < key)) {
		  a[i] = a[getParent(i)];
		  i = getParent(i);
		}
		a[i] = key;
	}

	/* increaseKey will replace the ith element of the heap
	*	with the maximum of the existing value and the given
	*	key.  The worst-case running time for the method is
	*	Theta(logd(n)), where d is base 4.
	*/

	public void increaseKey(int i, int key) {
		if(a[i] >= key)
		  return;
		while((i > 1) && (a[getParent(i)] < key)) {
		  a[i] = a[getParent(i)];
		  i = getParent(i);
		}
		a[i] = key;
	}

	/* printHeap: displays the heap as an array */

	public void printHeap() {
		for(int i=1; i<a.length; i++)
		  P.p(a[i] + " ");
		P.p("\n");
	}

	/* sort: performs the operation on the array,
	*	"quickly".
	*/

	public int[] sort() {
		QuickSort quick = new QuickSort();
		int[] temp = new int[a.length];
		System.arraycopy(a,0,temp,0,a.length);
		try {  quick.sort(temp);  
		} catch(Exception e) {  e.printStackTrace();  }
		return temp;
	}

	/* function overrides */
	public String toString() {
		return new String("integer heap[" + size() + "]");	
	}

//----------------

	/* The test driver of the app., it performs general operations
	*	and outputs the results in a procedural manner.
	*/

	public static void main(String[] argv) {
		P.pl("Heap4.java, heap operations on a Quad-rary data structure.");
		P.pl("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n");
		P.pl("TEST array:");
		P.pl(TESTSET);

		P.pl("\nThe building process..");
		Heap4 h = new Heap4(TESTSET);
		P.pl("heaped arra:");
		h.printHeap();

		for(int i=0; i<3; i++)
		  P.pl("ExtractMax(): " + h.extractMax());
		h.printHeap();

		for(int i=0; i<INSERT_KEYS.length; i++) {
		  P.pl("Insert(" + INSERT_KEYS[i] + "): ");
		  h.insert(INSERT_KEYS[i]);
		}
		h.printHeap(); 

		for(int i=0; i<INCREASE_KEYS.length;) {
		  P.pl("IncreaseKey(" + INCREASE_KEYS[i] + "," + INCREASE_KEYS[i+1] + "):");
		  h.increaseKey(INCREASE_KEYS[i++],INCREASE_KEYS[i++]);
		  h.printHeap();
		}
		System.exit(0);
	}

} //end Heap4
