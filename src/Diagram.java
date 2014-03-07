import java.util.*;

class DiagramBlock {
	private String name;
	private float x;
	private float y;
	
	public DiagramBlock(String name, float x, float y) {
		this.name = name;
		this.x = x;
		this.y = y;
	} 
	
	// Getter and setter for name
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
		return name + ": (" + x + ", " + y + ")";
	}
}

class Diagram {
	public ArrayList<DiagramBlock> JavaBlocks = new ArrayList<DiagramBlock>();
	
	public Diagram(ArrayList<String> JavaClasses) {
		for( int i=0; i<JavaClasses.size(); i++ ) {
			DiagramBlock b = new DiagramBlock(JavaClasses.get(i), 200*(i%5), (i/5)*100);
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
			if( b.equals(name) ) {
				return b.getX() + "," + b.getY();
			}
		}
		return null;
	}
}