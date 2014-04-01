import japa.parser.*;
import japa.parser.ast.*;
import japa.parser.ast.body.*;
import japa.parser.ast.expr.*;
import japa.parser.ast.stmt.*;
import japa.parser.ast.type.*;
import japa.parser.ast.visitor.*;
import java.io.*;
import java.util.*;

public class MethodPrinter {

    public static ArrayList<JavaClass> getClasses(String file) throws Exception {
        // creates an input stream for the file to be parsed
        FileInputStream in = new FileInputStream(file);

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
        
        return mv.classes;
    }

    
    private static class MethodVisitor extends VoidVisitorAdapter {

		public ArrayList<JavaClass> classes = new ArrayList<JavaClass>();		
		
        @Override
        public void visit(ClassOrInterfaceDeclaration n, Object arg) {
        
        	JavaClass jc = new JavaClass();
        	
        	jc.className = n.getName();
        	
            for(Iterator<BodyDeclaration> i = n.getMembers().iterator(); i.hasNext(); ) {
        	    BodyDeclaration item = i.next();
        	    if (item instanceof japa.parser.ast.body.FieldDeclaration) {
        	    	japa.parser.ast.body.FieldDeclaration fd = (japa.parser.ast.body.FieldDeclaration)item;
        	    	for(Iterator<VariableDeclarator> j = fd.getVariables().iterator(); j.hasNext(); ) {
        	    		VariableDeclarator vd = j.next();
        	    		jc.variableNames.add(fd.getType()+" "+vd.toString());
        	    	}
        	    }
        	    else if (item instanceof japa.parser.ast.body.MethodDeclaration) {
        	    	japa.parser.ast.body.MethodDeclaration md = (japa.parser.ast.body.MethodDeclaration)item;  
        	    	String umlstr = md.getType().toString();
        	    	umlstr += " "+md.getName()+"(";        	    
        	    	if(md.getParameters() != null) {
		    	    	for(Iterator<Parameter> k = md.getParameters().iterator(); k.hasNext(); ) {
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
}
