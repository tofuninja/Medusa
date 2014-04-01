import java.util.*;

public class JavaClass {
	String className;
	ArrayList<String> methodNames;
	ArrayList<String> variableNames;
	
	public JavaClass() {
		className = "";
		methodNames = new ArrayList<String>();
		variableNames = new ArrayList<String>();
	}
	public String toString() {
		 return "\nClassName: "+className+"\n"
					+"methodNames: "+methodNames+"\n"
					+"variables: "+variableNames;
	}
}
