import java.awt.Color;
import java.util.*;

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
			
			
			DiagramBlock b = new DiagramBlock(jc, 50 + 50*(i%5), 100 + (i/5)*50);
			JavaBlocks.add(b);
		}
		
		
		physThread pt = new physThread();
		pt.start();
		
	}
	
	
	public void addFile(String url)
	{
		/*
		 * 
		 * Change to use the url resolver
		 * 
		 */
	}
	
	
	class physThread extends Thread
	{
		int runTime;
		
		public physThread()
		{
			runTime = 100000;
		}
		
		public physThread(int time)
		{
			runTime = time;
		}
		
		public void run() 
		{
			// arranging blocks, could take a while
			for(int i = 0; i < runTime; i++)
			{
				/*
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
				
				//push from nodes
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
						double force = -(100.0 + 15*(db.block.width + db2.block.width))/(dist*dist);
						
						db2.accelx += (normx*force);
						db2.accely += (normy*force);
						
						
						
					}
				}
				
				//pull to connected nodes
				for(int j = 0; j < JavaBlocks.size(); j++)
				{
					DiagramBlock db = JavaBlocks.get(j);
					int refCount = db.Class.referenceClasses.size();
					double refMod = Math.pow(refCount, 2);
					for(int k = 0; k < refCount; k++)
					{
						DiagramBlock db2 = db.Class.referenceClasses.get(k).diagBlock;
						
						double dist = (db.block.x-db2.block.x)*(db.block.x-db2.block.x) + (db.block.y-db2.block.y)*(db.block.y-db2.block.y);
						dist = Math.sqrt(dist) + 0.0001f;
						double normx = (db.block.x-db2.block.x)/dist;
						double normy = (db.block.y-db2.block.y)/dist;
						double force = 0.00000003*Math.pow(dist, 2.5)/(refMod);
						db2.accelx += (normx*force);
						db2.accely += (normy*force);
						db.accelx -= (normx*force);
						db.accely -= (normy*force);
					}
				}
				
				//pull to center of screen
				for(int j = 0; j < JavaBlocks.size(); j++)
				{
					DiagramBlock db = JavaBlocks.get(j);
					int refCount = db.Class.referenceClasses.size()+1;
					double refMod = Math.pow(refCount, 2.3);
					
					double dist = (db.block.x)*(db.block.x) + (db.block.y)*(db.block.y);
					dist = Math.sqrt(dist) + 0.0001f;
					double normx = (db.block.x)/dist;
					double normy = (db.block.y)/dist;
					double force = 0.00000008*(dist*dist)/(refMod);
					db.accelx -= (normx*force);
					db.accely -= (normy*force);
					
				}
				
				
				
				//Friction & apply accel
				for(int j = 0; j < JavaBlocks.size(); j++)
				{
					DiagramBlock db = JavaBlocks.get(j);
					db.accelx *= 0.95f;
					db.accely *= 0.95f;
					
					db.block.x += db.accelx;
					db.block.y += db.accely;
				}
			}
		}
	}
}
