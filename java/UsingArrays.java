public class UsingArrays {

	public static void main(String[] args) {
		int [] myArray = new int[10];
		// fill in array
		for(int i=0; i<10; i++) {
			myArray[i] = i;
		}
		// print array
		for(int i: myArray ) {
			System.out.println(i);
		}
	}
}