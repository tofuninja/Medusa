import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class UrlResolver 
{
	
	static
	{
		
		File f = new File("downloads");
		if(!f.exists())
		{
			f.mkdir();
		}
		
	}

	private String url;
	private String type;
	private String filename;

	public UrlResolver(String p_url) 
	{
		System.out.println(p_url);
		type = "couldNotResolve";
		if(p_url == null) return;

		File f = new File(p_url);
		if (f.exists()) 
		{
			if(p_url.endsWith(".zip") || p_url.endsWith(".ZIP"))
			{
				type = "localZip";
			}
			else if(p_url.endsWith(".java") || p_url.endsWith(".JAVA"))
			{
				type = "java";
			}
			else if(p_url.endsWith(".url"))
			{
				type = "urlFile";
			}
			else
			{
				type = "localDir";
			}
			
			filename = p_url;
		} 
		else if(p_url.endsWith(".zip") || p_url.endsWith(".ZIP")) 
		{
			try 
			{
				new URL(p_url);
			} 
			catch (MalformedURLException e) 
			{
				return; // not proper url
			}
			
			type = "webzip";
			UUID uniq = UUID.randomUUID();
			filename = "Downloads/" + uniq.toString();
		}

		url = p_url;

	}

	public ArrayList<String> resolve() {
		switch (type) {
		// case "github":
		// command = "git clone "+url+" "+filename ;
		// output = executeCommand(command);
		// break;
		case "webzip":
			try {
				saveUrl(url);
			} 
			catch (Exception e) 
			{
				return null;
			}
			Unzip uz = new Unzip(filename + ".zip", filename);
			uz.unZipIt();
			// output = executeCommand ("rm "+filename+".zip") ;
			break;
		case "urlFile":
			
			try {
				List<String> lines = Files.readAllLines(Paths.get(url), Charset.defaultCharset());
				
				for(String line:lines)
				{
					if(line.startsWith("URL="))
					{
						String nurl = line.replaceFirst("URL=", "");
						UrlResolver resolv = new UrlResolver(nurl);
						return resolv.resolve();
					}
				}
			} catch (IOException e) {
				return null;
			}
			
			
			break;
		case "localZip":
			String localDir = "Downloads/" + UUID.randomUUID().toString();
			Unzip unzp = new Unzip(filename, localDir);
			unzp.unZipIt();
			filename = localDir;
			break;
		case "java":
			ArrayList<String> a = new ArrayList<String>();
			a.add(url);
			return a;
		case "couldNotResolve":
			filename = "couldNotResolve";
			return null;
		case "localDir":
			break;
		default:
			return null;
		}
		return DirCrawler.getFlatJavaFilesList(filename);
	}

	/*
	 * private static String executeCommand(String command) { StringBuffer
	 * output = new StringBuffer(); Process p; try { p =
	 * Runtime.getRuntime().exec(command); p.waitFor(); BufferedReader reader =
	 * new BufferedReader(new InputStreamReader(p.getInputStream())); String
	 * line = ""; while ((line = reader.readLine())!= null) { output.append(line
	 * + "\n"); } } catch (Exception e) { e.printStackTrace(); } return
	 * output.toString(); }
	 */

	public void saveUrl(final String urlString) throws MalformedURLException,IOException 
	{
		BufferedInputStream in = null;
		FileOutputStream fout = null;
		try 
		{
			in = new BufferedInputStream(new URL(urlString).openStream());
			fout = new FileOutputStream(filename + ".zip");
			final byte data[] = new byte[1024];
			int count;
			while ((count = in.read(data, 0, 1024)) != -1) 
			{
				fout.write(data, 0, count);
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		} 
		finally 
		{
			if (in != null) 
			{
				in.close();
			}
			if (fout != null) 
			{
				fout.close();
			}
		}
	}

}
