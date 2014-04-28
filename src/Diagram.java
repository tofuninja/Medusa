import java.awt.Color;
import java.util.*;

import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

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

class Diagram 
{
	public ArrayList<DiagramBlock> JavaBlocks = new ArrayList<DiagramBlock>();
	private physThread phys = null;
	JTree node;
	JLabel statusLabel;
	
	public Diagram(JTree top, JLabel status) 
	{
		node = top;
		statusLabel = status;
	}
	
	
	public void addFiles(List<String> files, int x, int y)
	{
		
		ArrayList<JavaClass> class_list = new ArrayList<JavaClass>();

		for (int i = 0; i < files.size(); i++) 
		{
			ArrayList<JavaClass> classes;
			try 
			{
				classes = FileDetails.getClasses(files.get(i));
			} 
			catch (Exception e1) 
			{
				continue;// error in file
			}

			for (int j = 0; j < classes.size(); j++) 
			{
				class_list.add(classes.get(j));
			}
		}
		
		
		ArrayList<DiagramBlock> newBlocks = new ArrayList<DiagramBlock>();
		
		for(int i = 0; i < class_list.size(); i++ ) 
		{
			JavaClass jc = class_list.get(i);
			
			// Find all connections between current classes
			for(int j = 0; j < class_list.size(); j++ ) 
			{
				for(int k = 0; k < jc.referenceNames.size(); k++)
				{
					if(jc.referenceNames.get(k).equals(class_list.get(j).className))
					{
						jc.referenceClasses.add(new javaRef(jc,class_list.get(j)));
					}
				}	
			}
			
			// Find all connections between classes already added
			for(DiagramBlock db: JavaBlocks)
			{
				// if jc ref db
				for(String ref: jc.referenceNames)
				{
					if(ref.equals(db.Class.className))
					{
						jc.referenceClasses.add(new javaRef(jc, db.Class));
					}
				}
				
				// if db ref jc
				for(String ref: db.Class.referenceNames)
				{
					if(ref.equals(jc.className))
					{
						db.Class.referenceClasses.add(new javaRef(db.Class, jc));
					}
				}
			}
			
			
			DiagramBlock b = new DiagramBlock(jc, x + 50 + 50*(i%5), y + 100 + (i/5)*50);
			newBlocks.add(b);
		}
		
		JavaBlocks.addAll(newBlocks);
		
		createNodes(class_list);
		runPhys(100000);
	}
	
	
	public void genStatus()
	{
		String status = "Node Count:" + JavaBlocks.size();
		
		if(phys != null && phys.runTime > 0)
			status += "            Phys Time:" + phys.runTime;
		
		statusLabel.setText(status);
	}
	
	
	private void createNodes(ArrayList<JavaClass> class_list) 
	{
		DefaultTreeModel model = (DefaultTreeModel)node.getModel();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode)model.getRoot();
		
		for (int i = 0; i < class_list.size(); i++ )
		{
			DefaultMutableTreeNode jClass = new DefaultMutableTreeNode(new nodeType(class_list.get(i).className, "c"));
			root.add(jClass);
			
			for (int j = 0; j < class_list.get(i).methodNames.size(); j++)
			{
				DefaultMutableTreeNode jMethod = new DefaultMutableTreeNode(new nodeType(class_list.get(i).methodNames.get(j),"m"));
				jClass.add(jMethod);
			}
			
			for (int j = 0; j < class_list.get(i).variableNames.size(); j++)
			{
				DefaultMutableTreeNode jVar = new DefaultMutableTreeNode(new nodeType(class_list.get(i).variableNames.get(j), "v"));
				jClass.add(jVar);
			}	
		}
		
		model.reload();
	}
	
	
	private void runPhys(int time)
	{
		
		if(phys != null && phys.isAlive())
		{
			phys.shouldRun = false;
			try {
				phys.join();
				phys.runTime = 0;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
			
		phys = new physThread();
		phys.runTime = time;
		phys.start();
	}
	
	
	class physThread extends Thread
	{
		public int runTime = 0;
		public boolean shouldRun = true;
		
		public void run() 
		{
			// arranging blocks, could take a while
			for(;runTime > 0; runTime--)
			{
				
				if(!shouldRun) return;
				
				
				if(JavaBlocks.size() < 50 && runTime%5 == 0)
				{
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
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
						DiagramBlock db2 = db.Class.referenceClasses.get(k).end.diagBlock;
						
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
					db.accelx *= 0.9f;
					db.accely *= 0.9f;
					
					db.block.x += db.accelx;
					db.block.y += db.accely;
				}
			}
		}
	}
}
