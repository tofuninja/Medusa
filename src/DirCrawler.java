import java.io.File;
import java.util.ArrayList;
 
public class DirCrawler {
 

	public static ArrayList<String> getFlatJavaFilesList(String path) {
		String files;
		ArrayList<String> dirStruct = new ArrayList<String>();
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles(); 
		if(listOfFiles == null) return null;
		for (int i = 0; i < listOfFiles.length; i++) {
			files = listOfFiles[i].getName();
			
			if (listOfFiles[i].isDirectory())
				  dirStruct.addAll(getFlatJavaFilesList(path+'/'+files));
			else 
				if (files.matches(".*java"))
		          dirStruct.add(path+'/'+files);
		}
		return dirStruct;
	}
	
}
