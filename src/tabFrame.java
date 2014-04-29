import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
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
		JLabel statusLabel = new JLabel("");
		
		Diagram diag = new Diagram(tree, statusLabel);
		
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
		
		JPanel statusPanel = new JPanel();
		statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
		this.add(statusPanel, BorderLayout.SOUTH);
		statusPanel.setPreferredSize(new Dimension(this.getWidth(), 16));
		statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
		statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
		statusPanel.add(statusLabel);
		
	}

}


@SuppressWarnings("serial")
class MyRenderer extends DefaultTreeCellRenderer 
{
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) 
    {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        DefaultMutableTreeNode node =(DefaultMutableTreeNode)value;
        nodeType s = (nodeType)node.getUserObject();
        if (s != null && s.type == "c") 
        {
            setIcon(MedusaIcons.cIcon);
            setToolTipText("Class"); 
        } 
        else if (s != null && s.type == "m") 
        {
            setIcon(MedusaIcons.mIcon);
            setToolTipText("Method"); 
        }
        else if (s != null && s.type == "v") 
        {
            setIcon(MedusaIcons.vIcon);
            setToolTipText("Field"); 
        }
        else if (s != null && s.type == "d") 
        {
            setIcon(MedusaIcons.dIcon);
            setToolTipText(null); 
        }
        else if (s != null && s.type == "e") 
        {
            setIcon(MedusaIcons.eIcon);
            setToolTipText("Extends"); 
        }
        else if (s != null && s.type == "i") 
        {
            setIcon(MedusaIcons.iIcon);
            setToolTipText("Interface"); 
        }
        else if (s != null && s.type == "a") 
        {
            setIcon(MedusaIcons.aIcon);
            setToolTipText("Abstract Class"); 
        }
        
        
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
