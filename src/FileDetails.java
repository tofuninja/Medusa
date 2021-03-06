import japa.parser.*;
import japa.parser.ast.*;
import japa.parser.ast.body.*;
import japa.parser.ast.type.*;
import japa.parser.ast.visitor.*;

import java.io.*;
import java.util.*;

public class FileDetails {

	/**
	 *	Call 
	 *		FileDetails.getClasses("/path/to/file.java");
	 *	It will return an ArrayList of JavaClass objects
	 */

    public static ArrayList<JavaClass> getClasses(String file) throws Exception {
        // creates an input stream for the file to be parsed
        FileInputStream in = new FileInputStream(file);

        CompilationUnit cu = null;
        try {
            // parse the file
            cu = JavaParser.parse(in);
        }
        catch(Exception e)
        {
        	ArrayList<JavaClass> arr = new ArrayList<JavaClass>();
        	JavaClass jc = new JavaClass();
        	jc.className = "File Error:" + file;
        	jc.referenceNames = new ArrayList<String>();
        	jc.isError = true;
        	arr.add(jc);
        	return arr;
        	
        }
        finally 
        {
            in.close();
        }
        
        // visit and print the methods names
		MethodVisitor mv = new MethodVisitor();
		try
		{
			mv.visit(cu, null);
		}
		catch(Exception e)
        {
        	ArrayList<JavaClass> arr = new ArrayList<JavaClass>();
        	JavaClass jc = new JavaClass();
        	jc.className = "File Error:" + file.substring(file.lastIndexOf('/'));
        	jc.referenceNames = new ArrayList<String>();
        	arr.add(jc);
        	return arr;
        }
        return mv.classes;
    }

    /*
	public static void main(String args[]) throws Exception{
	        FileInputStream in = new FileInputStream("test.javla");

        CompilationUnit cu;
        try {
            // parse the file
            cu = JavaParser.parse(in);
        } finally {
            in.close();
        }

        // visit and print the methods names
		MethodVisitor mv = new MethodVisitor();
        mv.visit(cu, null);
  		System.out.println(mv.classes.toString());
	}
	*/
    
    private static class MethodVisitor extends VoidVisitorAdapter <Object>
    {

		public ArrayList<JavaClass> classes = new ArrayList<JavaClass>();		
		
        @Override
        public void visit(ClassOrInterfaceDeclaration n, Object arg) 
        {
        
        	JavaClass jc = new JavaClass();
        	jc.isInterface = n.isInterface();
        	jc.className = n.getName();
        	jc.extendsClass = (n.getExtends() != null) ? n.getExtends().get(0).getName() : "";
        	JavadocComment doc = n.getJavaDoc();
        	jc.javaDoc = (doc == null)? "": doc.toString();
        	
        	if((n.getModifiers() & ModifierSet.ABSTRACT) == ModifierSet.ABSTRACT)
        	{
        		jc.isAbstract = true;
        	}
        	
        	
        	referenceVisit rf = new referenceVisit();
        	rf.visit(n, null);
     
        	jc.referenceNames = rf.references;
        	for(String s: jc.referenceNames)
        	{
        		if(s.equals(jc.className)) 
        		{
        			jc.referenceNames.remove(s);
        			break;
        		}
        	}
        	
        	if (n.getImplements() != null) 
        	{
        		
	        	for(Iterator<ClassOrInterfaceType> h = n.getImplements().iterator(); h.hasNext(); ) 
	        	{
	        		ClassOrInterfaceType imp = h.next();
	        		jc.implementsInterfaces.add(imp.getName());
	        	}
	        	
        	}
            for(Iterator<BodyDeclaration> i = n.getMembers().iterator(); i.hasNext(); ) 
            {
        	    BodyDeclaration item = i.next();
        	    if (item instanceof japa.parser.ast.body.FieldDeclaration) 
        	    {
        	    	
        	    	japa.parser.ast.body.FieldDeclaration fd = (japa.parser.ast.body.FieldDeclaration)item;
        	    	
        	    	for(Iterator<VariableDeclarator> j = fd.getVariables().iterator(); j.hasNext(); ) 
        	    	{
        	    		VariableDeclarator vd = j.next();
        	    		jc.variableNames.add(fd.getType()+" "+vd.getId().getName());
        	    	}
        	    	
        	    }
        	    else if (item instanceof japa.parser.ast.body.MethodDeclaration) 
        	    {
        	    	
        	    	japa.parser.ast.body.MethodDeclaration md = (japa.parser.ast.body.MethodDeclaration)item;  
        	    	String umlstr = "";
        	    	
        	    	if((md.getModifiers() & ModifierSet.ABSTRACT) == ModifierSet.ABSTRACT)
                	{
        	    		umlstr += "abstract ";
                	}
        	    	
        	    	umlstr += md.getType().toString();
        	    	umlstr += " "+md.getName()+"(";  
        	    	
        	    	if(md.getParameters() != null) 
        	    	{
		    	    	for(Iterator<Parameter> k = md.getParameters().iterator(); k.hasNext(); ) 
		    	    	{
							Parameter p = k.next();
							umlstr += p.toString()+((k.hasNext())?", ":"");
				   	    }
				   	}
        	    	
				   	umlstr += ")";
				   	jc.methodNames.add(umlstr);	
				   	
        	    }
        	}
        	
        	
        	classes.add(jc);
        	
        	
			//System.out.println(n.getMembers().toString());
        }
    }
    
    
    
    private static class referenceVisit extends VoidVisitorAdapter <Object>
    {
    	ArrayList<String> references = new ArrayList<String>();
	
        @Override
        public void visit(ClassOrInterfaceType n, Object arg) 
        {
        	if(!references.contains(n.getName()))
        		references.add(n.getName());
        	
        	if( n.getTypeArgs() != null)
        	{
	        	for(Type t: n.getTypeArgs())
	        	{
        			if(!references.contains(t.toString()))
                		references.add(t.toString());
	        	}
        	}
        	
        }
        
       
    }
    
    
}
