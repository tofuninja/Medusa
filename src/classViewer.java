import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.border.EtchedBorder;
import javax.swing.tree.DefaultMutableTreeNode;


@SuppressWarnings("serial")
public class classViewer extends JDialog
{
	JTree tree;
	
	public classViewer(Diagram d, JavaClass jc)
	{
		super(UI.frame,jc.className);
		this.setSize(700, 700);
		this.setLayout(new BorderLayout());
		
		
		Pan pan1 = new Pan(false);
		Pan pan2 = new Pan(false);
		Pan pan3 = new Pan(false);
		
		
		
		ArrayList<JavaClass> classList1 = new ArrayList<JavaClass>();
		ArrayList<JavaClass> classList2 = new ArrayList<JavaClass>();
		ArrayList<JavaClass> classList3 = new ArrayList<JavaClass>();
		
		classList1.add(jc.dup());
		classList2.add(jc.dup());
		classList3.add(jc.dup());
		
		
		for(javaRef ref: jc.referenceClasses)
		{
			classList1.add(ref.end.dup());
			classList3.add(ref.end.dup());
		}
		
		for(DiagramBlock db: d.JavaBlocks)
		{
			if(db.Class == jc) continue;
			for(javaRef ref: db.Class.referenceClasses)
			{
				if(ref.end == jc)
				{
					classList2.add(db.Class.dup());
					classList3.add(db.Class.dup());
				}
			}
		}
		
		
		Diagram diag1 = new Diagram(null, null);
		diag1.addJavaClasses(classList1, 0, 0);
		pan1.setDiag(diag1);
		
		
		Diagram diag2 = new Diagram(null, null);
		diag2.addJavaClasses(classList2, 0, 0);
		pan2.setDiag(diag2);
		
		
		Diagram diag3 = new Diagram(null, null);
		diag3.addJavaClasses(classList3, 0, 0);
		pan3.setDiag(diag3);
		
		pan1.addMouseListener(new PopClickListener(diag1, jc.className + " in"));
		pan2.addMouseListener(new PopClickListener(diag2, jc.className + " out"));
		pan3.addMouseListener(new PopClickListener(diag3, jc.className + " in out"));
		
		
		JTabbedPane tabs = new JTabbedPane();
		tabs.addTab("in", pan1);
		tabs.addTab("out", pan2);
		tabs.addTab("in/out", pan3);
		
		JSplitPane pane = new JSplitPane();
		pane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		
		tree = new JTree(genNodes(jc));
		tree.setCellRenderer(new MyRenderer());
		JScrollPane treeView = new JScrollPane(tree);
		
		if(!jc.javaDoc.equals(""))
		{
			JSplitPane vpane = new JSplitPane();
			vpane.setOrientation(JSplitPane.VERTICAL_SPLIT);
			
			vpane.setTopComponent(tabs);
			vpane.setBottomComponent(new JScrollPane(new classInfoPanel(jc)));
			vpane.setDividerLocation(550);
			pane.setRightComponent(vpane);
		}
		else
			pane.setRightComponent(tabs);
		
		pane.setLeftComponent(treeView);
		

		pane.setDividerLocation(150);
		
		this.add(pane, BorderLayout.CENTER);
		
	}
	
	
	private DefaultMutableTreeNode genNodes(JavaClass jc)
	{
		DefaultMutableTreeNode jClass;
		if(jc.isInterface)
		{
			jClass = new DefaultMutableTreeNode(new nodeType(jc.className, "i"));
			this.setIconImage(MedusaIcons.iImg);
		}
		else if(jc.isAbstract)
		{
			jClass = new DefaultMutableTreeNode(new nodeType(jc.className, "a"));
			this.setIconImage(MedusaIcons.aImg);
		}
		else
		{
			jClass = new DefaultMutableTreeNode(new nodeType(jc.className, "c"));
			this.setIconImage(MedusaIcons.cImg);
		}
		
		if(!jc.extendsClass.equals(""))
		{
			DefaultMutableTreeNode jExtends = new DefaultMutableTreeNode(new nodeType(jc.extendsClass,"e"));
			jClass.add(jExtends);
		}
		
		for(String inter: jc.implementsInterfaces)
		{
			DefaultMutableTreeNode jInterface = new DefaultMutableTreeNode(new nodeType(inter,"i"));
			jClass.add(jInterface);
		}
		
		ArrayList<String> msorted = jc.methodNames;
		Collections.sort(msorted);
		ArrayList<String> vsorted = jc.variableNames;
		Collections.sort(vsorted);
		for (int j = 0; j < jc.methodNames.size(); j++)
		{
			DefaultMutableTreeNode jMethod = new DefaultMutableTreeNode(new nodeType(msorted.get(j),"m"));
			jClass.add(jMethod);
		}
		
		for (int j = 0; j < jc.variableNames.size(); j++)
		{
			DefaultMutableTreeNode jVar = new DefaultMutableTreeNode(new nodeType(vsorted.get(j), "v"));
			jClass.add(jVar);
		}	
		return jClass;
	}
	
	
}

class PopClickListener extends MouseAdapter 
{
	private Diagram diag;
	private String title;
	
	public PopClickListener(Diagram d, String t)
	{
		diag = d;
		title = t;
	}
	
    public void mousePressed(MouseEvent e){
        if (e.isPopupTrigger())
            doPop(e);
    }

    public void mouseReleased(MouseEvent e){
        if (e.isPopupTrigger())
            doPop(e);
    }

    private void doPop(MouseEvent e){
    	JPopupMenu pop = new JPopupMenu();
		JMenuItem add = new JMenuItem("Add to new diagram");
		add.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				tabFrame tf = new tabFrame(title);
				ArrayList<JavaClass> arr = new ArrayList<JavaClass>();
				for(DiagramBlock db: diag.JavaBlocks) arr.add(db.Class.dup());
				tf.pan.diag.addJavaClasses(arr, 0, 0);
				tabLabelPane tl = new tabLabelPane(UI.me.tabbedPane, tf);
				UI.me.tabbedPane.add(tf);
				UI.me.tabbedPane.setTabComponentAt(UI.me.tabbedPane.getTabCount()-1, tl);
				UI.me.tabbedPane.setSelectedComponent(tf);
				
			}
		});
		pop.add(add);
		pop.show(e.getComponent(), e.getX(), e.getY());
    }
}




@SuppressWarnings("serial")
class classInfoPanel extends JPanel
{
	public classInfoPanel(JavaClass jc)
	{
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		//this.setBackground(Color.white);
		
		String doc = jc.javaDoc.replaceAll("/\\*\\*", "");
		doc = doc.replaceAll("\\*/", "");
		doc = doc.replaceAll("\n\\*", "");
		doc = doc.replaceAll("\n \\*", "");
		JPanel p = new JPanel(new BorderLayout());
		JLabel docL = new JLabel("<html><body align=\"left\">Documentation:<br>" + doc + "</body></html>", 0);
		docL.setVerticalAlignment(0);
		p.add(docL,BorderLayout.WEST);
		p.setBackground(Color.white);
		p.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		this.add(p);
		
	}
}
