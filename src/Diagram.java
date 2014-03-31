import java.util.*;

class DiagramBlock {
	private String className;
	private ArrayList<String> methodNames;
	private ArrayList<String> variableNames;
	private float x;
	private float y;
	
	public DiagramBlock(String className, ArrayList<String> methodNames, ArrayList<String> variableNames, float x, float y) {
		this.className = className;
		this.methodNames = methodNames;
		this.variableNames = variableNames;
		this.x = x;
		this.y = y;
	} 
	
	// Getter and setter for class name
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}

	public ArrayList<String> getMethodNames() {
		return methodNames;
	}

	public void setMethodNames(ArrayList<String> methodNames) {
		this.methodNames = methodNames;
	}

	public ArrayList<String> getVariableNames() {
		return variableNames;
	}

	public void setVariableNames(ArrayList<String> variableNames) {
		this.variableNames = variableNames;
	}
	
	// Getter and setter for x
	public float getX() {
		return x;
	}
	public void setX(float x) {
		this.x = x;
	}
	
	// Getter and setter for y
	public float getY() {
		return y;
	}
	public void setY(float y) {
		this.y = y;
	}
	
	@Override
	public String toString() {
		return className + ": (" + x + ", " + y + ")";
	}
}

class Diagram {
	public ArrayList<DiagramBlock> JavaBlocks = new ArrayList<DiagramBlock>();
	public String Folder;
	
	public Diagram(ArrayList<JavaClass> JavaClasses, String folder) {
		Folder = folder;
		for( int i=0; i<JavaClasses.size(); i++ ) {
			JavaClass jc = JavaClasses.get(i);
			DiagramBlock b = new DiagramBlock(jc.className, jc.methodNames, jc.variableNames, 50 + 200*(i%5), 100 + (i/5)*100);
			JavaBlocks.add(b);
		}
	}
	
	public void printBlockCoordinates() {
		for( int i=0; i<JavaBlocks.size(); i++ ) {
			DiagramBlock b = JavaBlocks.get(i);
			System.out.println(b);
		}
	}
	
	public String getCoordinateForJavaClass(String name) {
		for( int i=0; i<JavaBlocks.size(); i++ ) {
			DiagramBlock b = JavaBlocks.get(i);
			if( b.getClassName().equals(name) ) {
				return b.getX() + "," + b.getY();
			}
		}
		return null;
	}
}
