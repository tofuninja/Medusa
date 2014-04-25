import java.awt.BorderLayout;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;


public class tabFrame extends JPanel
{
	public Pan pan;
	public tabFrame(String m_folder_path)
	{

		

		// adr.setCaretPosition(adr.getDocument().getLength());

		ArrayList<String> arr = DirCrawler
				.getFlatJavaFilesList(m_folder_path);
		ArrayList<JavaClass> class_list = new ArrayList<JavaClass>();

		for (int i = 0; i < arr.size(); i++) 
		{
			ArrayList<JavaClass> classes;
			try 
			{
				classes = FileDetails.getClasses(arr.get(i));
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

		Diagram diag = new Diagram(class_list, m_folder_path);
		Pan p = new Pan();
		p.setDiag(diag);
		pan = p;
		
		JSplitPane pane = new JSplitPane();
		pane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		
		
		DefaultMutableTreeNode top = new DefaultMutableTreeNode("Medusa");
		
		JTree tree = new JTree(top);
		JScrollPane treeView = new JScrollPane(tree);
		
		pane.setLeftComponent(treeView);
		pane.setRightComponent(p);
		pane.setDividerLocation(150);
		this.setLayout(new BorderLayout());
		this.add(pane, BorderLayout.CENTER);
		
	}

	
}



