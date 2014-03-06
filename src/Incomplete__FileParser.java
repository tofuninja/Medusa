import java.io.*;
import org.json.simple.*;
import java.util.ArrayList;
import java.util.regex.*;
 
public class FileParser {
	public static JSONObject getFileStruct(String fileName) { 
		
		JSONObject fileStruct = new JSONObject();
		JSONArray members = new JSONArray();
		JSONArray methods = new JSONArray();

		try {
			FileReader file = new FileReader(fileName);
			BufferedReader reader = new BufferedReader(file);
			String line = "";
			while ( (line = reader.readLine()) != null ) {
				ArrayList<String> evalResult = new ArrayList<String>();
				evalResult = evaluate(line);
				if (evalResult.get(0) == "function") 
					methods.add(evalResult.get(1));				
				if (evalResult.get(0) == "variable") 
					for (int i=1; i<evalResult.size(); i++)
						members.add(evalResult.get(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} /*finally {
			if (file) {
				try {
					file.close();
				} catch (IOException e) {}
			}				
		}*/
		
		
		fileStruct.put("members", members);
		fileStruct.put("methods", methods);
		return fileStruct;
	}
	
	public static ArrayList<String> evaluate(String line){
		ArrayList<String> evalResult = new ArrayList<String>();
	
		// If FUNCTION
		Pattern funcDeclaration = Pattern.compile(".*\\s+(.+)\\s*\\(.*\\)\n*\\s*\\{"); 
		Matcher funcMatches = funcDeclaration.matcher(line);
		if (funcMatches.find()) {
			evalResult.add("function");
			evalResult.add((funcMatches.group(1)));
			return evalResult;
		}	
		
		// If VARIABLE
		// -------- TO DO -------- //	
		Pattern varDeclaration = Pattern.compile("[^\\s]*\\s+([^;]+)"); 
		Matcher varMatches = varDeclaration.matcher(line);
		if (varMatches.find()) {
			evalResult.add("variable");
			line.replaceAll( "=[^,]+" , "");
			String[] varList = line.split("\\s*,\\s*");
			for (String i : varList)
				evalResult.add(i);
			return evalResult;
		}	

		evalResult.add("neither");		
		return evalResult;
	}

	/* Diagnostic code */
	public static void main( String args[] ) {
		System.out.println(getFileStruct("test.java").toString());
	}
	
}
