import java.io.*;

class TextFile {
	String[] fileContents;
	String fileName;
	
	TextFile(String fn) {
		fileName = fn;
	}
	TextFile() {
		this("TextFile.txt");
	}
	public static void main(String[] args) {
		TextFile txt = new TextFile();
		txt.createContents();
		txt.save();
		txt.fileContents = null;
		txt.load();
		txt.show();
		//txt.delete();
	}
	void createContents() {
		String symbols = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		char[] chars = symbols.toCharArray();
		int len = chars.length;
		int nlines = 80;
		int ncols = 72;
		char[] buffer;
		fileContents = new String[nlines];
		for (int i=0; i < nlines; i++) { 
			buffer = new char[ncols];
			for (int j=0; j < ncols; j++) {
				buffer[j] = chars[(int) (Math.random()*len)];
			}
			fileContents[i] = new String(buffer);
		}
	}
	void save() {
		try {
			File f = new File(fileName);
			BufferedWriter bw = new BufferedWriter(new FileWriter(f));
			for (String l: fileContents) {
				bw.write(l);
				bw.newLine();
			}
			bw.close(); // this will also close FileWriter and File.
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	void load() {
		try  {
			File f = new File(fileName);
			BufferedReader br = new BufferedReader(new FileReader(f));
			String tmp = "";
			String line;
			while ((line = br.readLine()) != null) {
				tmp += line + "\n";
			}
			br.close();
			fileContents = tmp.split("\n");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	void show() {
		System.out.println("Contents of " + fileName);
		for (String l: fileContents) {
			System.out.println(l);
		}
	}
	void delete() {
		try {
			File f = new File(fileName);
			if (f.delete()) {
				System.out.println("File " + fileName + " deleted.");
			} else {
				System.out.println("File " + fileName + " *not* deleted.");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}