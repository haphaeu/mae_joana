import java.util.*;

public class DotCom {
	private ArrayList<String> locationCells;
	private String name;
	
	public void setLocationCells(ArrayList<String> locs) {
		locationCells = locs;
	}
	
	public void setName(String nm) {
		name = nm;
	}
	
	public String checkYourself(String stringGuess) {
		String result = "miss";
		int index = locationCells.indexOf(stringGuess);
		if (index >= 0) {
			locationCells.remove(index);
			
			if (locationCells.isEmpty()) {
				result = "kill";
				System.out.println("Cool, you sunk " + name + "!");
			} else {
			    result = "hit";
			}
		}
		// System.out.println(result);
		return result;
	}
}