/**
*	StateDP.java: a class that demonstrates the
*	State design pattern.  It simulates the operation
*	of a coffe machine that deals with accepting
*	a particular amount of change, all the while giving
*	usage messages and vending refunds.
*
*	The program provides the functionality to specs
*	defined in cse445 - Distributed Computing with Corba&Java
*
*	@author anthony.francis@asu.edu
*	@version 10.6.2002
*/

public class StateDP extends Object {

	public static void main(String[] argv) {

		String[] testSequences =  { "dnqc", "qqnc", "qndqq" };

		for(int i=0; i<testSequences.length; i++) {
			System.out.println("\nTest Transaction" + (i+1));
			System.out.println("-------------");
			Driver driver = new Driver();
			String current = testSequences[i];
			int len = current.length();
			for(int j=0; j<len; j++) {
				driver.scan(current.charAt(j));
			}
			driver = null;
		}
	
		System.out.println("");
		System.exit(0);		
	}

} //end StateDP



//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%*
/**
*	Driver: the container of all other classes
*	that are related to the coffee machine.  Once
*	called, the class will operate the machine
*	as specified.
*
*	*NOTE: a new driver is to be instantiated for each transaction!
*/

class Driver extends Object {

	private MachineState state;

	public Driver() {  state = new State0();  }  //initialized each transaction
												 //to the initial state (0)
	public void scan(char coin) {
		state = state.nextState(state.action(coin));
	}

} //end Driver


//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%*
/**
*	MachineState: is the interface that describes
*	any given state in a transaction.  The two methods
*	are implemented individually to provide the 
*	funcionality described in the specs.
*/


interface MachineState {

		/** all usage messages provided by coffee machine */

	static final String MESSAGE1 = "Collect refund and add quarters only",
				MESSAGE2 = "Add quarters only or press Cancel",
				MESSAGE3 = "Add 25 cents or press Cancel",
				MESSAGE4 = "Remove Coffee",
				MESSAGE5 = "Collect Refund",
				MESSAGE6 = "Add 50 cents or press Cancel";

	static final int QUARTER_ACCEPTED = 0;	// a quarter has been accepted
	static final int CANCEL_PRESSED = 1;  // the cancel button has been pushed
	static final int COIN_REJECTED = 2;  // a coin other than quarter has been rejected

	static final char QUARTER = 'q',
					DIME = 'd',
					NICKEL = 'n',
					CANCEL = 'c';


		/** action: defines the actions taken in a given state
		*
		*	input: character representing a coin
		*	output: an integer representing an event
		*		and produces usage messages
		*/

	public int action(char coin);

		/** nextState: determines the next state according
		*	to the return value of action.
		*
		*	input: integer describing previous event
		*	output: returns another MachineState according
		*		to the next appropriate step.
		*/

	public MachineState nextState(int event);

} //end MachineState



//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%*
/**
*	State0: An implementation class of MachineState which 
*	represents the vending machine state when no quarter 
*	has been accepted. The vending machine will return to 
*	this state whenever a cancel button is pushed or a sale 
*	has been made. It must define the methods specified in 
*	MachineState interface according to the required behavior 
*	of the vending machine in this state.
*/

class State0 implements MachineState {

		/** method's functionality provided in MachineState interface */

	public int action(char coin) {
		if(coin == QUARTER) {
			System.out.println(MESSAGE6);
			return QUARTER_ACCEPTED;
		}
		else if(coin == DIME || coin == NICKEL) {
			System.out.println(MESSAGE1);
			return COIN_REJECTED;
		}
		else if(coin == CANCEL) {
			return CANCEL_PRESSED;
		}
		else return -1;

	}

		/** method's functionality provided in MachineState interface */

	public MachineState nextState(int event) {
		if(event == QUARTER_ACCEPTED) return new State1();
		else if(event == COIN_REJECTED) return new State0();
		else if(event == CANCEL_PRESSED) return new State0();
		return null;
	}

} //end State0

//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%*
/**
*	State1: An implementation class of MachineState which 
*	represents the vending machine state when one and only 
*	one quarter has been accepted. It must define the methods 
*	specified in MachineState interface according to the 
*	required behavior of the vending machine in this state.
*/

class State1 implements MachineState {

		/** method's functionality provided in MachineState interface */

	public int action(char coin) {
		if(coin == QUARTER) {
			System.out.println(MESSAGE3);
			return QUARTER_ACCEPTED;
		}
		else if(coin == DIME || coin == NICKEL) {
			System.out.println(MESSAGE2);
			return COIN_REJECTED;
		}
		else if(coin == CANCEL) {
			System.out.println(MESSAGE5);
			return CANCEL_PRESSED;
		}
		return -1;
	}


		/** method's functionality provided in MachineState interface */

	public MachineState nextState(int event) {
		if(event == QUARTER_ACCEPTED) return new State2();
		else if(event == COIN_REJECTED) return new State1();
		else if(event == CANCEL_PRESSED) return new State0();
		return null;
	}


} //end State1

//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%*
/**
*	State2: An implementation class of MachineState which represents 
*	the vending machine state when exactly two quarters have been 
*	accepted. It must define the methods specified in MachineState 
*	interface according to the required behavior of the vending machine 
*	in this state.
*/

class State2 implements MachineState {

		/** method's functionality provided in MachineState interface */

	public int action(char coin) {
		if(coin == QUARTER) {
			System.out.println(MESSAGE4);
			return QUARTER_ACCEPTED;
		}
		else if(coin == DIME || coin == NICKEL) {
			System.out.println(MESSAGE2);
			return COIN_REJECTED;
		}
		else if(coin == CANCEL) {
			System.out.println(MESSAGE5);
			return CANCEL_PRESSED;
		}
		return -1;
	}

		/** method's functionality provided in MachineState interface */

	public MachineState nextState(int event) {
		if(event == QUARTER_ACCEPTED) return new State0();
		else if(event == COIN_REJECTED) return new State2();
		else if(event == CANCEL_PRESSED) return new State0();
		return null;
	}


} //end State2
