import java.awt.Color;

import org.json.simple.JSONObject;

class DiagramBlock {
	JavaClass Class;
	public Block block;
	
	public double accelx = 0.0f;
	public double accely = 0.0f;
	
	
	public DiagramBlock(JavaClass c, int x, int y) 
	{	
		Class = c;
		c.diagBlock = this;
		String str = c.className;/* + "\n\n"; 
		
		for(int j = 0; j < c.methodNames.size(); j++)
		{
			str += c.methodNames.get(j) + "\n";
		}
		
		str += "\n";
		
		for(int j = 0; j < c.variableNames.size(); j++)
		{
			str += c.variableNames.get(j) + "\n";
		}
		*/
		
		Color col;
		
		if(c.isInterface)
			col = new Color(214, 162, 88);
		else if(c.isAbstract)
			col = new Color(139, 144, 217);
		else if(c.isError)
			col = new Color(252, 91, 91);
		else
			col = new Color(196, 121, 126);
		
		block = new Block(str, x, y, col);
		
	} 
	
	
	
	public DiagramBlock(JSONObject j) 
	{	
		Class = new JavaClass((JSONObject)j.get("class"));
		block = new Block((JSONObject)j.get("block"));
		Class.diagBlock = this;
	} 
	
	
	
	@SuppressWarnings("unchecked")
	public JSONObject toJSON()
	{
		JSONObject j = new JSONObject();
		j.put("block", block.toJSON());
		j.put("class", Class.toJSON());
		return j;
	}
	
}