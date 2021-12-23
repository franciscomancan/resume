/** AlphaTree.java: a tree structure which contains a node for a word and its file of origin.
*	Strings are compared lexagraphically, using the java String class function(s).
*	The tree is searchable, traversable in both directions.
*/

class Node {			/* define node for instance data */

	protected static final String DFLT_FILE = "index.html";

	Node left; 
    	Node right;
    	String word;
    	String file;

    Node(String w, String f) { 
	word = w; 
	file = f;
    }
    Node(String w) {  this(w,DFLT_FILE);  }

    public Node getLeft() {  return left;  }	
    public Node getRight() {  return right;  }
    public String getWord() {  return word;  }
    public String getFile() {  return file;  }

    public String toString() {  
	String s = "<keyword name=\"" + word + "\"/>";
	if(!file.equals(DFLT_FILE)) s+= " found in " + file;
	return s;
    }
}

//---------------------------

public class AlphaTree extends Object {

	private Node root;

	/* constructor defines middle of the alphabet as the root,
	 * resort to a different tree structure for maximum performance
	*/

    AlphaTree() {
	this.root = new Node("m", DFLT_FILE);
    }

	/* insert: places a given node into the tree structure, */

    public void insert(Node n) {
	String key = n.getWord();
	Node temp = findPosition(root, key);
	  if(temp != null && temp.word.equals(key))
	    return;  

      	if(temp == null) { root = n; return; }
      	if((key).compareTo(temp.word) < 0) temp.left = n;
      	else temp.right = n;
    }

	/* clear: will detatch pointers (nullify) of a specified branch */

    public void clear(Node begin) {
	if(begin == null) return;
	if(begin.left != null) 
	   clear(begin.left); 
	if(begin.right != null)
	   clear(begin.right);
	begin = null;

	System.gc();  return;
    }

    public void print(Node n) {
	if(n == null) n = root;
      	if(n.left != null) print(n.left);
      	System.out.println(n); 
      	if(n.right != null) print(n.right);
    }


    //public Node delete(String word) {}


	/* findPosition: used within the class to find appropriate position
	 * to place key
	*/

    protected Node findPosition(Node begin, String key) {
	if(begin == null) return null;   

      	if(key.compareTo(begin.word) > 0) {
          if(begin.right == null) return begin;
            return findPosition(begin.right, key);
      	}

      	if(key.compareTo(begin.word) < 0) {
          if(begin.left == null) return begin;
            return findPosition(begin.left, key);
      	}
      	
	return begin;
    }

//-------------------------------------------------

    public static void main(String[] argv) {
	AlphaTree tree = new AlphaTree();

	tree.insert(new Node("java","one.txt"));
	tree.insert(new Node("c++","two.txt"));
	tree.insert(new Node("perl","three.txt"));
	tree.insert(new Node("smalltalk"));

	tree.print(tree.root);
	System.exit(0);

    }



} //end AlphaTree
