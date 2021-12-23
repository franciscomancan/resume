import java.util.*;

//////////////////////////////////////////////////////////////////////////////////
/** Queue: a vector-based implementation of a queue.
*	The should grow as needed, retaining all the properties
*	of a vecor
*
*	@author anthony.francis@asu.edu
*	@version 4.30.2003
*/
public class Queue  {

   private static final int DFLT_NELEMENTS = 56;

   private Vector elems;

   public Queue() { this(DFLT_NELEMENTS); }
   public Queue(int len) { elems = new Vector(len); }


	/** standard override */
   public String toString() {
		StringBuffer buf = new StringBuffer();
		for(int i=0; i<size(); i++)
			buf.append(elementAt(i) + ", ");
		return new String(buf);
   }

	/** Placed here are some of the general operations
	*	one might assume of a collection.
	*/
    public int capacity() { return elems.capacity(); }
    public int size() { return elems.size(); }
    public boolean isEmpty() { return elems.size() == 0; }
    public boolean isFull() { return elems.size() == elems.capacity(); }
	public void clear()  { elems.removeAllElements(); }
	public void removeElement(Object e) { elems.removeElement(e); }
	public Object elementAt(int i) { return elems.elementAt(i); }

	public void enqueue(Object elem)  { elems.addElement(elem); }
	public Object dequeue() {
		Object obj = front();
    	if (obj != null) elems.removeElementAt(0);
    	return obj;
	}
	public Object front() {
      return elems.size() == 0 ? null : elems.elementAt(0);
	}

	public static void main(String[] argv) {
		Queue q = new Queue();
		for (int i = 0; i < argv.length; i++) q.enqueue(argv[i]);
			System.out.println(q);
		if (!q.isEmpty()) {
			System.out.println("\ndequeue: " + q.dequeue());
      		System.out.println("front: " + q.front());
      		System.out.println("size: " + q.size());
      		System.out.println("capacity: " + q.capacity());
      	}
      	q.clear();
      	System.out.println("\n*** queue cleared\n\n" + q);
    }
}
