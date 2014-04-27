import java.io.*;
import java.net.*;
import java.util.*;
import java.util.zip.*;
import java.nio.file.Files;
 
public class UrlResolver {

	private String url;
	private String type;
	private String filename;
	
	
	public UrlResolver(String p_url) {
		String last3 = null;
		if (p_url.length() > 3) 
			last3 = p_url.substring(p_url.length()-3);

		type = "couldNotResolve";

		File f = new File(p_url);
		if (f.exists()) {
			type = (last3 != null && last3.equals("zip")) ? "localZip" : "localDir";
			filename = p_url;
		}
		else {
			if (last3 != null && last3.equals("zip")) {
				if ( p_url.contains("://") )
					type = "webzip";
			}
			else if ( p_url.contains("://github.com") || p_url.contains("://www.github.com") ) {
				type = "github";
			}
			UUID uniq = UUID.randomUUID();
      		filename = "Downloads/" + uniq.toString() ;
      	}

		url = p_url;
		
	}

	public  ArrayList<String> resolve () {
		String command = null;
		String output = null;
		switch (type) {
			case "github":
				command = "git clone "+url+" "+filename ;
		 		output = executeCommand(command);
				break;
			case "webzip":
				try {
					saveUrl(url);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				Unzip uz = new Unzip(filename+".zip", filename);
				uz.unZipIt();
				output = executeCommand ("rm "+filename+".zip") ;
				break;
			case "localZip":
				String localDir = "Downloads/"+UUID.randomUUID().toString() ;
				Unzip unzp = new Unzip(filename, localDir);
				unzp.unZipIt();
				filename = localDir;
				break;
			case "couldNotResolve":
				filename = "couldNotResolve";
				break;
			case "localDir":
			default:
				break;
		}
		return DirCrawler.getFlatJavaFilesList(filename);
	}

	private static String executeCommand(String command) {
		StringBuffer output = new StringBuffer();
		Process p;
		try {
			p = Runtime.getRuntime().exec(command);
			p.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = "";			
			while ((line = reader.readLine())!= null) {
				output.append(line + "\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return output.toString();
	}



	public void saveUrl(final String urlString)
      throws MalformedURLException, IOException {
	    BufferedInputStream in = null;
	    FileOutputStream fout = null;
	    try {
	        in = new BufferedInputStream(new URL(urlString).openStream());
	        fout = new FileOutputStream(filename+".zip");
	        final byte data[] = new byte[1024];
	        int count;
	        while ((count = in.read(data, 0, 1024)) != -1) {
	            fout.write(data, 0, count);
	        }
	    } 
	    catch (Exception e) {
	    	e.printStackTrace();
	    }
	    finally {
	        if (in != null) {
	            in.close();
	        }
	        if (fout != null) {
	            fout.close();
	        }
	    }
	}

}
