/* CountingSort: the sorting algorithm implemented
*	to operate on int[].  The sorting f(n) = O(n),
*	a linear order of growth, done by taking into
*	account the value of the infomation to be
*	sorted (numerical).
*/

import anf.*;
import java.util.*;

public class CountingSort extends Object {
	private final static int MAX_GENERATED_NUM = 100;
	public int steps;

	public CountingSort() {				//constructed with a variable to track running time
	  this.steps = 0;
	}

	public int[] sort(int[]a, int k) {
		int[] b = new int[a.length];
		int[] c = new int[++k];
		int tmp=0;

		for(int i=0; i<k; i++) {
		  for (int j=0; j<a.length; j++) {
			if ((i)==a[j]) c[i]++;
			steps++;
		  }
		}
		for (int i=0; i<k; i++){
			c[i]=c[i]+tmp;
			tmp=c[i];
			steps+= 2;
		}
		for (int i=a.length-1; i>=0; i--){
			b[--c[a[i]]]=a[i];
			steps++;
		}
		return b;		
	}

	public static int max(int[] s) {
	  int max = 0;
	  for(int i=0; i<s.length; i++)
		if(s[i] > max)
		  max = s[i];
	  return max;
	}

//-----------------

	/*  main: a driver for the application.  It demonstrates
	 *	the use of the counting sort on a short array.
	*/

	public static void main(String[] argv) {
	  Random numFactory = new Random();
	  int[] arra = new int[10];
	  P.p("Pre-: ");
	  for(int i=0; i<arra.length; i++) {
	    arra[i] = Math.abs((numFactory.nextInt() % MAX_GENERATED_NUM));
	    P.p(arra[i] + " ");
	  }
	  P.pl("");

	  CountingSort count = new CountingSort();
	  try {
	    arra = count.sort(arra,max(arra));
	  } catch(Exception error) {  error.printStackTrace();  }
	  P.p("Post: ");
	  P.pl(arra);
	  P.pl("...sorted " + arra.length + " elements in " + count.steps
+
" steps");
	  System.exit(0);
	}

} //end CountingSort
