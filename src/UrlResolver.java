import java.io.*;
import java.net.*;
import java.util.*;
import java.util.zip.*;
 
public class UrlResolver {

	private String url;
	private String type;
	private String filename;

	public void resolve () {
		String command;
		switch (type) {
			case "github":
				command = "git clone "+url+" "+filename ;
		 		String output = executeCommand(command);
				break;
			case "zipfile":
				try {
					saveUrl(url);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				Unzip uz = new Unzip(filename+".zip", filename);
				uz.unZipIt();
				break;
		}
		return filename;
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

	public UrlResolver(String p_url) {
		if (p_url.length > 3)
			String last3 = p_url.substring(p_url.length-3);
		if (last3.equals("zip"))
			type = "zipfile";
		if ( p_url.contains("://github.com") || p_url.contains("://www.github.com") )
			type = "github"; 
		url = p_url;
		UUID uniq = UUID.randomUUID();
      	filename = "Downloads/" + uniq.toString() ;
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