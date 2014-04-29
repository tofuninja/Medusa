import java.util.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class JavaClass {
	boolean isInterface = false;
	boolean isAbstract = false;
	String className;
	String extendsClass;
	ArrayList<String> implementsInterfaces;
	ArrayList<String> methodNames;
	ArrayList<String> variableNames;
	ArrayList<String> referenceNames;
	
	ArrayList<javaRef> referenceClasses;
	
	DiagramBlock diagBlock = null;
	
	public JavaClass() {
		className = "";
		extendsClass = "";
		implementsInterfaces = new ArrayList<String>();
		methodNames = new ArrayList<String>();
		variableNames = new ArrayList<String>();
		referenceClasses = new ArrayList<javaRef>();
	}
	
	
	
	public JavaClass(JSONObject j) {
		this();
		referenceNames = new ArrayList<String>();
		
		
		className = (String)j.get("name");
		extendsClass = (String)j.get("extends");
		isInterface = (Boolean)j.get("isInterface");
		isAbstract = (Boolean)j.get("isAbstract");
		
		JSONArray inames = (JSONArray)j.get("inames");
		for(Object o: inames) implementsInterfaces.add((String)o);
		
		JSONArray mnames = (JSONArray)j.get("mnames");
		for(Object o: mnames) methodNames.add((String)o);
		
		JSONArray vnames = (JSONArray)j.get("vnames");
		for(Object o: vnames) variableNames.add((String)o);
		
		JSONArray rnames = (JSONArray)j.get("rnames");
		for(Object o: rnames) referenceNames.add((String)o);
	}
	
	
	
	
	
	
	public String toString() {
		 return "\nClassName: "+className+"\n"
		 			+"extends: "+extendsClass+"\n"
		 			+"implements: "+implementsInterfaces+"\n"
					+"methodNames: "+methodNames+"\n"
					+"variables: "+variableNames;
	}
	
	/**
	 * Make a simple copy of the JavaClass, does not copy referenceClasses or diagBlock
	 */
	public JavaClass dup()
	{
		JavaClass jc = new JavaClass();
		jc.className = className;
		jc.extendsClass = extendsClass;
		jc.isAbstract = isAbstract;
		jc.isInterface = isInterface;
		for(String s: implementsInterfaces) jc.implementsInterfaces.add(s);
		for(String s: methodNames) jc.methodNames.add(s);
		for(String s: variableNames) jc.variableNames.add(s);
		for(String s: referenceNames) jc.referenceNames.add(s);
		return jc;
	}
	
	
	@SuppressWarnings("unchecked")
	public JSONObject toJSON()
	{
		JSONObject j = new JSONObject();
		j.put("name", className);
		j.put("extends", extendsClass);
		j.put("isInterface", isInterface);
		j.put("isAbstract", isAbstract);
		
		JSONArray inames = new JSONArray();
		inames.addAll(implementsInterfaces);
		j.put("inames",inames);
		
		JSONArray mnames = new JSONArray();
		mnames.addAll(methodNames);
		j.put("mnames",mnames);
		
		JSONArray vnames = new JSONArray();
		vnames.addAll(variableNames);
		j.put("vnames",vnames);
		
		JSONArray rnames = new JSONArray();
		rnames.addAll(referenceNames);
		j.put("rnames",rnames);
		return j;
	}
}
