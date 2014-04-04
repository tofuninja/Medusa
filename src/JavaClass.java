import java.util.*;

public class JavaClass {
	String className;
	String extendsClass;
	ArrayList<String> implementsInterfaces;
	ArrayList<String> methodNames;
	ArrayList<String> variableNames;
	ArrayList<String> referenceNames;
	
	public JavaClass() {
		className = "";
		extendsClass = "";
		implementsInterfaces = new ArrayList<String>();
		methodNames = new ArrayList<String>();
		variableNames = new ArrayList<String>();
	}
	public String toString() {
		 return "\nClassName: "+className+"\n"
		 			+"extends: "+extendsClass+"\n"
		 			+"implements: "+implementsInterfaces+"\n"
					+"methodNames: "+methodNames+"\n"
					+"variables: "+variableNames;
	}
}
