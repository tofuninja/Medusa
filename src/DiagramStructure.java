/*
import java.io.*;
import java.util.*;
import org.json.simple.*;

class ConnectionObject {
	// Connections - a vector of classobject arrays
	Vector<ClassObject[]> connections;
	
	// Empty constructor
	public ConnectionObject() {
		connections = new Vector<ClassObject[]>();
	}
	
	// Added a connection between two classes
	public void addConnection(ClassObject class1, ClassObject class2) {
		connections.add( new ClassObject[]{ class1, class2 } );
	}
	
	// Check if two classes has a connection
	public boolean hasConnection(ClassObject class1, ClassObject class2) {
		for( int i=0; i<connections.size(); i++ ) {
			// Two-way check because I am not entirely sure how Array Iterator works in Java
			if( connections.elementAt(i).equals(new ClassObject[]{ class1, class2 } ) ||
				connections.elementAt(i).equals(new ClassObject[]{ class2, class1 } ) 
				) {
				return true;
			}
		}
		return false;
	}
}

class ClassObject {
	
	 * constructors - List of constructors
	 * methods - List of methods
	 * variables - List of global variables
	 
	Vector<ConstructorObject> constructors;
	Vector<MethodObject> methods;
	Vector<VariableObject> variables;
	
	// Empty Constructor
	public ClassObject() {
		constructors = new Vector<ConstructorObject>();
		methods = new Vector<MethodObject>();
		variables = new Vector<VariableObject>();
	}
	
	// Constructor - For a standard class
	public ClassObject(Vector<ConstructorObject> constructors, Vector<MethodObject> methods, Vector<VariableObject> variables) {
		this.constructors = constructors;
		this.methods = methods;
		this.variables = variables;
	}
	
	// Getter and Setter for constructors
	public void addConstructor(ConstructorObject object) {
		constructors.add(object);
	}
	public Vector<ConstructorObject> getConstructors() {
		return constructors;
	}
	public void setConstructors(Vector<ConstructorObject> constructors) {
		this.constructors = constructors;
	}
	
	// Getter and Setter for methods
	public void addMethod(MethodObject object) {
		methods.add(object);
	}
	public Vector<MethodObject> getMethods() {
		return methods;
	}
	public void setMethods(Vector<MethodObject> methods) {
		this.methods = methods;
	}
	
	// Getter and Setter for variables
	public void addVariable(VariableObject object) {
		variables.add(object);
	}
	public Vector<VariableObject> getVariables() {
		return variables;
	}
	public void setVariables(Vector<VariableObject> variables) {
		this.variables = variables;
	}
}

class ConstructorObject {
	
	 * Type, Parameters
	 
	String type;		// Return Type
	Vector<ParameterObject> parameters;	// Parameters
	
	// Constructor - Empty
	public ConstructorObject() {
		type = null;
		parameters = new Vector<ParameterObject>();
	}
	
	// Constructor - For a standard Constructor
	public ConstructorObject(String type, Vector<ParameterObject> parameters) {
		this.type = type;
		this.parameters = parameters;
	}
	
	// Getter and Setter for type
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	// Getter and Setter for parameters
	public void addParameter(ParameterObject object) {
		parameters.add(object);
	}
	public Vector<ParameterObject> getParameters() {
		return parameters;
	}
	public void setParameters(Vector<ParameterObject> parameters) {
		this.parameters = parameters;
	}
}

class ParameterObject {
	
	 * Type, Name
	 
	String type;		// Variable Type
	String name;		// Variable Name
	
	// Constructor - For variables without values
	public ParameterObject(String type, String name) {
		this.type = type;
		this.name = name;
	}
	
	// Getter and Setter for type
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	// Getter and Setter for name
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}

class MethodObject {
	
	 * Type, Name, Value, Static, Parameters
	 *
	 * Value are forced into a String object
	 
	String type;		// Return Type
	String name;		// Method Name
	boolean isStatic;	// Whether it's static
	Vector<ParameterObject> parameters;	// Parameters
	Vector<VariableObject> variables;		// Local variables
	
	// Constructor - Empty
	public MethodObject() {
		type = null;
		name = null;
		isStatic = false;
		parameters = new Vector<ParameterObject>();
		Vector<VariableObject> variables;
	}
	
	// Constructor - For methods without parameters
	public MethodObject(String type, String name, boolean isStatic, Vector<VariableObject> variables) {
		this.type = type;
		this.name = name;
		this.isStatic = isStatic;
		parameters = new Vector<ParameterObject>();
		this.variables = variables;
	}
	
	// Constructor - For a standard method
	public MethodObject(String type, String name, boolean isStatic, Vector<ParameterObject> parameters, Vector<VariableObject> variables) {
		this.type = type;
		this.name = name;
		this.isStatic = isStatic;
		this.parameters = parameters;
		this.variables = variables;
	}
	
	// Getter and Setter for type
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	// Getter and Setter for name
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	// Getter and Setter for value
	public boolean getStatic() {
		return isStatic;
	}
	public void setStatic(boolean isStatic) {
		this.isStatic = isStatic;
	}
	
	// Getter and Setter for parameters
	public void addParameter(ParameterObject object) {
		parameters.add(object);
	}
	public Vector<ParameterObject> getParameters() {
		return parameters;
	}
	public void setParameters(Vector<ParameterObject> parameters) {
		this.parameters = parameters;
	}
	
	// Getter and Setter for variables
	public void addVariable(VariableObject object) {
		variables.add(object);
	}
	public Vector<VariableObject> getVariables() {
		return variables;
	}
	public void setVariables(Vector<VariableObject> variables) {
		this.variables = variables;
	}
}

class VariableObject {
	
	 * Type, Name, Value, Static
	 *
	 * Value are forced into a String object
	 
	String type;		// Data Type
	String name;		// Variable Name
	String value;		// Variable Value
	boolean isStatic;	// Whether it's static
	
	// Constructor - Empty
	public VariableObject() {
		type = null;
		name = null;
		value = null;
		isStatic = false;
	}
	
	// Constructor - For variables without values
	public VariableObject(String type, String name, boolean isStatic) {
		this.type = type;
		this.name = name;
		value = null;
		this.isStatic = isStatic;
	}
	
	// Constructor - For a standard variable
	public VariableObject(String type, String name, String value, boolean isStatic) {
		this.type = type;
		this.name = name;
		this.value = value;
		this.isStatic = isStatic;
	}
	
	// Getter and Setter for type
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	// Getter and Setter for name
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	// Getter and Setter for value
	public String getValue() {
		return value;
	}
	public void setValue(Object value) {
		// In case the value is not String
		this.value = value.toString();
	}
	
	// Getter and Setter for value
	public boolean getStatic() {
		return isStatic;
	}
	public void setStatic(boolean isStatic) {
		this.isStatic = isStatic;
	}
}

class DiagramStructure {
	// A diagram structure holds connections and classes
	Vector<ConnectionObject> connections;
	Vector<ClassObject> classes;
	
	// Init
	public DiagramStructure() {
		connections = new Vector<ConnectionObject>();
		classes = new Vector<ClassObject>();
	}
	
	public void parseJavaFile(String fileName) {
		FileReader fr = new FileReader(fileName);
		BufferedReader br = new BufferedReader(fr);

		String currentJSONString = "";
		while( (currentJSONString=br.readLine())!=null ) {
		    JSONObject currentObject = new JSONObject(currentJSONString);
		    if(currentObject.has("id")) {
		        //System.out.println(currentObject.getInt("id"));
		        //System.out.println(currentObject.getString("text"));               
		        //System.out.println(currentObject.getString("created_at"));    
		    }
		}
	}
	
	public Array getClasses() {
		// Not complete yet
		return classes.toArray();
	}
	
	// Waiting for FileParser to be complete...
	// Ready for use - ConnectionObject, ClassObject, ConstructorObject, MethodObject, ParameterObject, VariableObject,
	 Example usage:
	 * DiagramStructure ds = new DiagramStructure();
	 * ds.parse();
	 * Array classArray = ds.getClasses(); // Now you have classes
	 * for (Iterator classi = classArray.iterator(); classi.hasNext(); ) {
	 *   // ClassObject co = (ClassObject)classi;
     *   // Do something with class
	 *	 // Use iterator to loop through methods as well
	 * }
	 
}*/