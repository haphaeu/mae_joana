/* Test of types
a "smaller" var can go into a "large" var but not the other way around.
bits types
1     boolean
16    char

8      byte
16    short
32    int
64    long

32    float
64    double */
public class CupSizes {
    public static void main(String[] args) {
	    // int x = 34.5;  // wont compile int <- double
		// boolean boo = x;  // wont compile bool <- int
		int g = 17;
		int y = g;
		short s;
		// s = y;  //wont compile short <- int
		byte b = 3;
		byte v = b;
		short n = 12;
		// v = n;  // wont compile byte <- short
		// byte k = 128;  //wont compile -128 <= byte <= 127
		byte k1 = -127;
		// float pi1 = 3.1415;  //wont compile   float <- double 
		float pi2 = 3.1415f;  // need the f to mark float
		float pi3 = (float) 3.1415;  // or use use cast
	}
}
