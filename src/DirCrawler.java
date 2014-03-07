import java.io.File;
import java.util.ArrayList;
import org.json.simple.*;
 
public class DirCrawler {
 	 
	public static JSONObject getDirStruct(String path) { 
		String files;
		int count=0;
		JSONObject dirStruct = new JSONObject();
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles(); 
		
		for (int i = 0; i < listOfFiles.length; i++) {
			files = listOfFiles[i].getName();
			if (listOfFiles[i].isDirectory())
				  dirStruct.put(files, getDirStruct(path+'/'+files));
			else 
		          dirStruct.put(""+count++, files);
		}
		return dirStruct;
	}

	public static ArrayList<String> getFlatJavaFilesList(String path) {
		String files;
		ArrayList<String> dirStruct = new ArrayList<String>();
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles(); 
		
		for (int i = 0; i < listOfFiles.length; i++) {
			files = listOfFiles[i].getName();
			if (listOfFiles[i].isDirectory())
				  dirStruct.addAll(getFlatJavaFilesList(path+'/'+files));
			else 
				if (files.matches("\.java"))
		          dirStruct.add(files);
		}
		return dirStruct;
	}
	
}
