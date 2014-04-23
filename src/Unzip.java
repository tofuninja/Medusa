import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
 
public class Unzip
{
    List<String> fileList;
    private static String INPUT_ZIP_FILE;
    private static String OUTPUT_FOLDER;
 
    public Unzip (String in, String out) {
        INPUT_ZIP_FILE = in;
        OUTPUT_FOLDER = out;
    }
 
    /**
     * Unzip it
     * @param zipFile input zip file
     * @param output zip file output folder
     */
    public void unZipIt(){
    
     String zipFile = INPUT_ZIP_FILE;
     String output = OUTPUT_FOLDER;

     byte[] buffer = new byte[1024];
 
     try{
 
    	//create output directory is not exists
    	File folder = new File(OUTPUT_FOLDER);
    	if(!folder.exists()){
    		folder.mkdir();
    	}
 
    	//get the zip file content
    	ZipInputStream zis = 
    		new ZipInputStream(new FileInputStream(zipFile));
    	//get the zipped file list entry
    	ZipEntry ze = zis.getNextEntry();
 
    	while(ze!=null){
 
    	   String fileName = ze.getName();
           File newFile = new File(output + File.separator + fileName);
           
            //create all non exists folders
            //else you will hit FileNotFoundException for compressed folder
            if (ze.isDirectory()) {
                newFile.mkdirs();
                System.out.println(fileName+": DIRECTORY!");
            }
            else {
                System.out.println(fileName+": FILE!");
                FileOutputStream fos = new FileOutputStream(newFile);             
                int len;
                while ((len = zis.read(buffer)) > 0) {
           		   fos.write(buffer, 0, len);
                }
                fos.close();   
            } 
            ze = zis.getNextEntry();
    	}
 
        zis.closeEntry();
    	zis.close();
 
    	System.out.println("Done");
 
    }catch(IOException ex){
       ex.printStackTrace(); 
    }
   }    
}