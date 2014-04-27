import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Image;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;


@SuppressWarnings("serial")
public class tabFrame extends JPanel
{
	public Pan pan;
	public tabFrame()
	{
		// adr.setCaretPosition(adr.getDocument().getLength())
		DefaultMutableTreeNode top = new DefaultMutableTreeNode(new nodeType("Medusa", "d"));
		JTree tree = new JTree(top);
		
		Diagram diag = new Diagram(tree);
		
		Pan p = new Pan();
		p.setDiag(diag);
		pan = p;
		
		JSplitPane pane = new JSplitPane();
		pane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		
		tree.setCellRenderer(new MyRenderer());
		JScrollPane treeView = new JScrollPane(tree);
		pane.setLeftComponent(treeView);
		pane.setRightComponent(p);
		pane.setDividerLocation(150);
		this.setLayout(new BorderLayout());
		this.add(pane, BorderLayout.CENTER);
		
	}

}


@SuppressWarnings("serial")
class MyRenderer extends DefaultTreeCellRenderer 
{
	static ImageIcon cIcon;
	static ImageIcon mIcon;
	static ImageIcon vIcon;
	static ImageIcon dIcon;
	
	static
	{

		try {
			Image cImg = ImageIO.read(new File("res/class.png"));
			Image mImg = ImageIO.read(new File("res/method.png"));
			Image vImg = ImageIO.read(new File("res/variable.png"));
			Image dImg = ImageIO.read(new File("res/diagram.png"));
			
			cIcon = new ImageIcon(cImg.getScaledInstance(16, 16, Image.SCALE_SMOOTH));
			mIcon = new ImageIcon(mImg.getScaledInstance(16, 16, Image.SCALE_SMOOTH));
			vIcon = new ImageIcon(vImg.getScaledInstance(16, 16, Image.SCALE_SMOOTH));
			dIcon = new ImageIcon(dImg.getScaledInstance(16, 16, Image.SCALE_SMOOTH));
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		
		
		
	}

    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) 
    {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        DefaultMutableTreeNode node =(DefaultMutableTreeNode)value;
        nodeType s = (nodeType)node.getUserObject();
        if (s != null && s.type == "c") 
        {
            setIcon(cIcon);
        } 
        else if (s != null && s.type == "m") 
        {
            setIcon(mIcon);
        }
        else if (s != null && s.type == "v") 
        {
            setIcon(vIcon);
        }
        else if (s != null && s.type == "d") 
        {
            setIcon(dIcon);
        }
        
        setToolTipText(null); //no tool tip
        return this;
    }
}

class nodeType
{
	public String text;
	public String type;
	public nodeType(String txt,String typ) 
	{
		text = txt;
		type = typ;
	}
	
	@Override
	public String toString() 
	{
		return text;
	}
}
