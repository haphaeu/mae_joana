class RefTest {
    public static void main(String[] args) {
	    String s1 = "this is a string";
		String s2 = s1;
		s1 += ", extended";
		s2 += " and copied";
		System.out.println(s1);
		System.out.println(s2);
		
		int[] a = {1, 2, 3};
		int[] b = a;
		b[0] = 9;  // this will update a too
		for (int i: a) 
		    System.out.print(i + " ");
	}
}