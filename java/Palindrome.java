/**
* class Palindrome: receives one String argument and determines
* 			if that string is in fact a Palindrome.
*
* Palindrome = a sequence of characters that reads the same both
*		forwards and reversed.  (i.e. radar)
*
* @author a.francis
* @version 11.23.99
*/


public class Palindrome {

	private static boolean assumption = true;

    public static void main(String[] argv) {
    	if(argv.length != 1) {
		System.out.println("error: requires a String arg");
		System.exit(1);
	}
	if(isPalindrome(argv[0]))
		System.out.println(argv[0] + " is a palindrome.");
	else
		System.out.println(argv[0] + " is NOT a palindrome.");
	

	System.exit(0);
    }

//-----------------------------------------------

	//method that recursively tests for symmetry within
	//a sequence of characters. 

    public static boolean isPalindrome(String s) {
	int length = s.length();
	
	if(length <= 1)
		return assumption;

	char beginning = s.charAt(0); 
	char end = s.charAt(length-1);
	if(beginning == end) {
		return isPalindrome(shave(s));
	}

	return false;
    }

//-----------------------------------------------

	//method that will shave off the first and last
	//characters of a string, used by isPalindrome().

    public static String shave(String s) {
	int size = s.length();
	if(size == 3 || size == 2)
		return String.valueOf(s.charAt(1));
	else
		return s.substring(1, (size-1));
    }

//------------------------------------------------

	// a process method which will prompt a user for a string
	// and return a boolean indicator.(used from within another app.)

    public static boolean process() {
	String s = DumbConsole.getString("enter a string:", System.out);
	if(isPalindrome(s))
		System.out.println(s + " is a palindrome.");
	else
		System.out.println(s + " is NOT a palindrome.");
	return true;
    }

} //end Palindrome
