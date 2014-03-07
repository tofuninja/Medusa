import java.io.*;
import org.json.simple.*;
import java.util.ArrayList;
import java.util.regex.*;
 
public class ClassFinder {
	public static ArrayList<String> getClassList(String fileName) {
		ArrayList<String> classNames = new ArrayList<String>();
		try {
			FileReader file = new FileReader(fileName);
			BufferedReader reader = new BufferedReader(file);
			String line = "";
			while ( (line = reader.readLine()) != null ) {
				Pattern classDeclaration = Pattern.compile("class\\s+([^\\s\\{]*)"); 
				Matcher classMatches = classDeclaration.matcher(line);
				if (classMatches.find()) {
					classNames.add((classMatches.group(1)));
				}	
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return classNames;
	}

/*
	public static void main( String args[] ) {
		ArrayList<String> list = getClassList("test.java");
		for (String s : list)
		    System.out.println(s);
	}
*/
}