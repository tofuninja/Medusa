import java.awt.Color;
import java.util.*;

class DiagramBlock {
	JavaClass Class;
	public Block block;
	
	public float accelx = 0.0f;
	public float accely = 0.0f;
	
	public DiagramBlock(JavaClass c, int x, int y) 
	{	
		Class = c;
		c.diagBlock = this;
		String str = c.className + "\n\n"; 
		
		for(int j = 0; j < c.methodNames.size(); j++)
		{
			str += c.methodNames.get(j) + "\n";
		}
		
		str += "\n";
		
		for(int j = 0; j < c.variableNames.size(); j++)
		{
			str += c.variableNames.get(j) + "\n";
		}
		
		block = new Block(str, x, y,new Color(196, 121, 126), Pan.font);
		
	} 
}

class Diagram {
	public ArrayList<DiagramBlock> JavaBlocks = new ArrayList<DiagramBlock>();
	public String Folder;
	
	public Diagram(ArrayList<JavaClass> JavaClasses, String folder) 
	{
		Folder = folder;
		for(int i = 0; i < JavaClasses.size(); i++ ) 
		{
			JavaClass jc = JavaClasses.get(i);
			
			// Find all connections
			for(int j = 0; j < JavaClasses.size(); j++ ) 
			{
				for(int k = 0; k < jc.referenceNames.size(); k++)
				{
					if(jc.referenceNames.get(k).equals(JavaClasses.get(j).className))
					{
						jc.referenceClasses.add(JavaClasses.get(j));
					}
				}	
			}
			
			//System.out.println(jc.referenceClasses.size());
			
			
			DiagramBlock b = new DiagramBlock(jc, 50 + 800*(i%5), 100 + (i/5)*600);
			JavaBlocks.add(b);
		}
		
		
		physThread pt = new physThread();
		pt.start();
		
	}
	
	
	class physThread extends Thread
	{
		
		public void run() 
		{
			// arranging blocks, could take a while
			for(int i = 0; i < 10000; i++)
			{
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				for(int j = 0; j < JavaBlocks.size(); j++)
				{
					DiagramBlock db = JavaBlocks.get(j);
					for(int k = 0; k < JavaBlocks.size(); k++)
					{
						DiagramBlock db2 = JavaBlocks.get(k);
						
						double dist = (db.block.x-db2.block.x)*(db.block.x-db2.block.x) + (db.block.y-db2.block.y)*(db.block.y-db2.block.y);
						dist = Math.sqrt(dist) + 0.01f;
						

						double normx = (db.block.x-db2.block.x)/dist;
						double normy = (db.block.y-db2.block.y)/dist;
						
						double force = -1000.0/(dist*dist);
						
						double r1 = Math.sqrt(db.block.width*db.block.width + db.block.height*db.block.height)/2;
						force*=r1;
						//double r2 = Math.sqrt(db2.block.width*db2.block.width + db2.block.height*db2.block.height)/2;
						
						//if(dist < r1+r2)
						//{
							//force += 10;
							
						//}
						

						
						db2.accelx += (float)(normx*force);
						db2.accely += (float)(normy*force);
						
					}
				}
				
				/*
				for(int j = 0; j < JavaBlocks.size(); j++)
				{
					DiagramBlock db = JavaBlocks.get(j);
					
					double dist = (db.block.x)*(db.block.x) + (db.block.y)*(db.block.y);
					dist = Math.sqrt(dist) + 1.0;
					
					double normx = (db.block.x)/dist;
					double normy = (db.block.y)/dist;
					
					double force = -0.000001*(dist*dist);
					
					
					db.accelx += (float)(normx*force);
					db.accely += (float)(normy*force);
						
					
				}
				*/
				
				for(int j = 0; j < JavaBlocks.size(); j++)
				{
					DiagramBlock db = JavaBlocks.get(j);
					for(int k = 0; k < db.Class.referenceClasses.size(); k++)
					{
						DiagramBlock db2 = db.Class.referenceClasses.get(k).diagBlock;
						
						double dist = (db.block.x-db2.block.x)*(db.block.x-db2.block.x) + (db.block.y-db2.block.y)*(db.block.y-db2.block.y);
						dist = Math.sqrt(dist) + 0.0001f;
						double normx = (db.block.x-db2.block.x)/dist;
						double normy = (db.block.y-db2.block.y)/dist;
						double force = 0.000000005*(dist*dist);
						db2.accelx += (float)(normx*force);
						db2.accely += (float)(normy*force);
						
					}
				}
				
				
				
				
				
				//Friction & apply accel
				for(int j = 0; j < JavaBlocks.size(); j++)
				{
					DiagramBlock db = JavaBlocks.get(j);
					db.accelx *= 0.6f;
					db.accely *= 0.6f;
					
					if(db.accelx > 100000.0f) db.accelx = 1000;
					if(db.accelx < -100000.0f) db.accelx = -1000;
					if(db.accely > 100000.0f) db.accely = 1000;
					if(db.accely < -100000.0f) db.accely = -1000;
					
					db.block.x += db.accelx;
					db.block.y += db.accely;
				}
			}
		}
	}
}
