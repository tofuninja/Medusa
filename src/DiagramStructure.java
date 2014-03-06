import java.util.*;

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
	
}

class ParameterObject {
	/*
	 * Type, Name
	 */
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
	/*
	 * Type, Name, Value, Static
	 *
	 * Value are forced into a String object
	 */
	String type;		// Return Type
	String name;		// Method Name
	boolean isStatic;	// Whether it's static
	Vector<ParameterObject> parameters;	// Parameters
	
	// Constructor - Empty
	public MethodObject() {
		type = null;
		name = null;
		isStatic = false;
		parameters = new Vector<ParameterObject>();
	}
	
	// Constructor - For methods without parameters
	public MethodObject(String type, String name, boolean isStatic) {
		this.type = type;
		this.name = name;
		this.isStatic = isStatic;
		parameters = new Vector<ParameterObject>();
	}
	
	// Constructor - For a standard method
	public MethodObject(String type, String name, boolean isStatic, Vector<ParameterObject> parameters) {
		this.type = type;
		this.name = name;
		this.isStatic = isStatic;
		this.parameters = parameters;
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
}

class VariableObject {
	/*
	 * Type, Name, Value, Static
	 *
	 * Value are forced into a String object
	 */
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
	public static void main(String[] args) {
		
	}
}