import anf.*;

/**
*  class NumberSystems: a program which will make conversions(base change) to any
*  	of the four primary systems used in computing.
*
* @author a.francis
*/

public class NumberSystems extends Object {

	private static final int OCTAL_BASE = 8, HEX_BASE = 16, BINARY_BASE = 2,
				 BINARY_MAX = 20, DECIMAL_BASE = 10, TOT_SYSTS = 4;
	private static final short DFLT_BASE = 10;
	private static final String HEX_PREFIX = "0x", DFLT_NUM = "100";
	private static final int A = 10, B = 11, C = 12, D = 13, E = 14, F = 15;

	private String num;
	private short base;
	private String[] conversions = new String[TOT_SYSTS];

    public NumberSystems(String n, short b) {
	this.num = n.trim();
	this.base= b;
	this.conversions = conversions;
    }

    public NumberSystems() {  this(DFLT_NUM,DFLT_BASE);  } 

//-------------------------------------------------------------------------
	/* acceser-mutator methods */

    public void setNum(String n) {  num = n.trim();  }
    public void setBase(short s) {  base = s;  }
    public String getNum() {  return num;  }
    public short getBase() {  return base;  }

	/* will return a specific conversion value from the instance array(of conversions) */

    public String getConversion(int index) {
	if(index < 0 || index > conversions.length -1)
	   return new String("Error");
	return conversions[index];  
    }

//------------------------------------------------------------------------

	/** Conversion methods, which will make calculations according to
	  * beginning state of num (into other 3 primary number systems).
	  * The data is then stored into the conversions array
	 */

    public void convertBinary() {
	long repeat = Long.parseLong(binToDecimal(num));

	conversions[0] = "octal: " + Long.toOctalString(repeat);
	conversions[1] = "decimal: " + binToDecimal(num);
	conversions[2] = "hex: " + HEX_PREFIX + Long.toHexString(repeat);
    }

    public void convertHex() {
	String repeat = binToDecimal(hexToBinary(num));
	
	conversions[0] = "binary: " + hexToBinary(num);
	conversions[1] = "octal: " + Long.toOctalString(Long.parseLong(repeat));
	conversions[2] = "decimal: " + repeat;
    }

    public void convertDecimal() {
	long repeat = Long.parseLong(num);

	conversions[0] = "binary: " + Long.toBinaryString(repeat);
	conversions[1] = "octal: " + Long.toOctalString(repeat);
	conversions[2] = "hex: " + HEX_PREFIX + Long.toHexString(Long.parseLong(num));
    }

    public void convertOctal() {
	long repeat = Long.parseLong(octalToDecimal(num));

	conversions[0] = "binary: " + Long.toBinaryString(repeat);
	conversions[1] = "decimal: " + repeat;
	conversions[2] = "hex: " + HEX_PREFIX + Long.toHexString(repeat);
    }

//-------------------------------------------------------------

	/* toString, overridden

    public String toString() {
	StringBuffer s = new StringBuffer();
	s = num + ", base " + base + " is equivalent to:";
	int len = conversions.length;
	for(int i = 0; i < len; i++) {
	   if(conversions[i] != null)
		s += conversions[i];
	}
	return new String(s);

    }

//---------------------------------------------------------------------------

	/** static method to convert a hex String into a binary
	  * one, a method needed for other class functins as well.
	 */

    public static String hexToBinary(String hex) {
	hex = hex.trim();
	if(hex.startsWith(HEX_PREFIX))
	   hex = hex.substring(HEX_PREFIX.length());

	StringBuffer buff = new StringBuffer();
	int len = hex.length();
	int c = 0, offset = 0;		//inc. in 4's, format length
	
	for(int i = len-1; i >= 0; i--) {
	   if(Character.isLetter(hex.charAt(i))) {
		switch(Character.toLowerCase(hex.charAt(i))) {
	   	  case 'a': c=A; break; case 'b': c=B; break;
		  case 'c': c=C; break; case 'd': c=D; break;
		  case 'e': c=E; break; case 'f': c=F; break;
		}
		buff.insert(0,Integer.toBinaryString(c));
	   }
	   else
		buff.insert(0,formatted(hex.charAt(i)));
	}
	return new String(buff);
    }

//-----------------------------------------------

	/* octal string to decimal, also utilized within class */

    public static String octalToDecimal(String octal) {
	octal = octal.trim();
	long total = 0;
	int len = octal.length(), power = len-1;
	for(int a = 0; a < len; a++) {				//lesson*
	   if(octal.charAt(a) != '0') {
	      total += ((long)Math.pow((double)8,(double)power) * 
					Long.parseLong(String.valueOf(octal.charAt(a))));
	   }
	   power--;
	}
	return String.valueOf(total);
    }

//------------------------------------------------------

	/** binary String to decimal format, an iterative method,
	  * also used by other methods
	 */

    public static String binToDecimal(String binary) {
	binary = binary.trim();
	long total = 0;
	int len = binary.length(), power = len-1;
	for(int a = 0; a < len; a++) {
	   if(binary.charAt(a) == '1') {
	      total += Math.pow((double)BINARY_BASE,(double)power);
	   }
	   power--;
	}
	return String.valueOf(total);
    }

//--------------------------------------------------------------

    public static void main(String[] argv) {
	NumberSystems converter = new NumberSystems();

	converter.setNum("47");
	converter.setBase((short)16);

	converter.convertHex();
	for(int i = 0; i < converter.conversions.length; i++) {
	   P.pl(converter.conversions[i]);
	}
	
	converter.setNum("50");

	converter.convertHex();
	for(int i = 0; i < converter.conversions.length; i++) {
	   P.pl(converter.conversions[i]);
	}

	converter.setNum("8b");

	converter.convertHex();
	for(int i = 0; i < converter.conversions.length; i++) {
	   P.pl(converter.conversions[i]);
	}
	System.exit(0);
    }

//------------------------------------------------------------

	/** a method which will format integers 0-9 into an exactly 4-digit
	  * binary string(buffer 0's).  It is practical in converting hex
	  * values, therefore private.
	 */

    private static String formatted(char c) {
	int formatLength = 4;
	String binary = Long.toBinaryString(Long.parseLong(String.valueOf(c)));
	StringBuffer temp = new StringBuffer(String.valueOf(binary));

	int index = 0;
	while(temp.length() != formatLength)
		temp.insert(0,"0");
	
	return new String(temp);
    }

} //end NumberSystems.java
