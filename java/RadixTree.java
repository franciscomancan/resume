/*  RadixTree: a representation of bit strings through the use
*	of a binary tree.  The class is implemented with an int array.
*	Each element's(node's) key can be determined by traversing the
*	path from the root to that node.  Keys are not actually stored
*	in the individual nodes, only a boolean value that indicates
*	whether or not there is an active bit string associated with it.
*	The class also supports basic binary tree operations.
*
*	@author a.francis
*	@version 7.4.01
*/

import anf.*;
import java.util.*;

public class RadixTree extends Object {

	private static final String[] TESTSET1 = { "0", "011", "100", "1011", "010", "11", "00" },
								  TESTSET2 = { "1", "100", "011", "0100", "101", "00", "11" };
	private static final int ROOT = 1;
	private boolean[] a;
	private int height = 0;

	public RadixTree() {
	  this.a = new boolean[1];
	  this.height = height;
	}

//------------------------------------

		/* generaly supported actions for a binary tree, public */

	public boolean getRoot() {  return a[1];  }

	public int size() {  return a.length-1;  }

	public int getHeight() {  return height;  }

	public int getParent(int i) {  return (int)Math.floor((double)i/2);  }

	public int getLeft(int i) {  return 2*i;  }

	public int getRight(int i) {  return (2*i)+1;  }

		/* public methods for the three standard traversals, 
		 * each one of which will print out the active nodes as 
		 * it walks.
		*/
	
	public void inorder() {
	  P.p("Inorder: ");
	  inorder(ROOT);
	  P.pl("");
	}

	public void preorder() {
	  P.p("Preorder: ");
	  preorder(ROOT);
	  P.pl("");
	}

	public void postorder() {
	  P.p("Postorder: ");
	  postorder(ROOT);
	  P.pl("");
	}

		/* generalized sort method, prints the keys as it walks (preorder) */

	public void sort() {
	  StringBuffer accum = new StringBuffer(getHeight());
	  sort(ROOT, accum);
	}

		/* a method to check the state of a given index, also used
		 * within other class methods
		*/

	public boolean isActive(int i) {
	  boolean s = a[i];
	  return s;
	}

		/* the method inserts keys, assuming the passed key 
		 * is in fact a bit string */

	public void insert(String key) {
	  boolean[] bits = parseBits(key);
	  int current = ROOT;
	  for(int i=0; i<bits.length; i++) {
		if(bits[i] == false) {
		  current = getLeft(current);
		  continue;
		}
		if(bits[i] == true) {
		  current = getRight(current);
		  continue;
		}
	  }
	  if(current > size()) {
	    lengthen(current);
	  }
	  activate(current);
	}

		/* toString override */

	public String toString() {
	  StringBuffer s = new StringBuffer();
	  int j = 0;
	  s.append("Active nodes: ");
	  for(int i=1; i<a.length; i++) {
		if(isActive(i)) {
		  s.append(i + ", ");
		  j++;
		  if(j%6 == 0)
		    s.append("\n");
		}
	  }
	  return (new String(s));
	}

//--------------------------------

		/* private class methods, which are used
		 * only within RadixTree operations */

	private void inorder(int i) {
	  if(i <= size()) {
        inorder(getLeft(i));
		if(isActive(i))
          P.p(i + ", ");
        inorder(getRight(i));
  	  }
	}

	private void preorder(int i) {
	  if(i <= size()) {
		if(isActive(i))
          P.p(i + ", ");
        preorder(getLeft(i));
        preorder(getRight(i));
  	  }
	}

	private void postorder(int i) {
  	  if(i <= size()) {
        postorder(getLeft(i));
        postorder(getRight(i));
		if(isActive(i))
          P.p(i + ", ");
  	  }
	}

		/* a method that will parse a given string into 
		 * a bitset that can be used by insert() */

	private boolean[] parseBits(String bitstring) {
	  int len = bitstring.length();
	  if(len > height)  this.height = len;
	  boolean[] bitset = new boolean[len];
	  for(int i=0; i<len; i++) {
		if(bitstring.charAt(i) == '1')
		  bitset[i] = true;
		else
		  bitset[i] = false;
	  }
	  return bitset;
	}

		/* a method used to grow an array */

	private void lengthen(int i) {
	  boolean[] newone = new boolean[(i+1)];
	  System.arraycopy(a,0,newone,0,a.length);
	  for(int j=a.length; j<newone.length; j++)
		newone[i] = false;
	  a = newone;
	}

		/* activate: sets a particular index of
		 * the tree to "true", which means there is
		 * a bit string associated with that particular
		 * node */

	private void activate(int index) {
	  if(index <= size())
	    a[index] = true;
	}

		/* private sort, used by general public method */

 	private void sort(int i, StringBuffer accumulator) {
	  StringBuffer another = new StringBuffer(accumulator.toString());
	  if(isActive(i))
		P.pl(accumulator);
      if(getLeft(i) <= size())
		sort(getLeft(i),accumulator.append("0"));
	  if(getRight(i) <= size())
    	sort(getRight(i),another.append("1"));
	}

//-----------------------------

		/* the driver that tests the basic operations
		 * of the class */

	public static void main(String[] argv) {
	  P.pl("Testset 1");
	  RadixTree tree = new RadixTree();
	  for(int j=0; j<TESTSET1.length; j++)
		tree.insert(TESTSET1[j]);
	  tree.sort();

	  P.pl("Testset 2");
	  tree = new RadixTree();
	  for(int j=0; j<TESTSET2.length; j++)
		tree.insert(TESTSET1[j]);
	  tree.sort();
	  P.pl(tree);
	  System.exit(0);
	}

} //end RadixTree
