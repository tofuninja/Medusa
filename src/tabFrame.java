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
	static ImageIcon cIcon;
	static ImageIcon mIcon;
	static ImageIcon vIcon;
	static ImageIcon dIcon;
	static ImageIcon eIcon;
	static ImageIcon iIcon;
	static ImageIcon aIcon;
	
	static
	{

		try {
			Image cImg = ImageIO.read(new File("res/class.png"));
			Image mImg = ImageIO.read(new File("res/method.png"));
			Image vImg = ImageIO.read(new File("res/variable.png"));
			Image dImg = ImageIO.read(new File("res/diagram.png"));
			Image eImg = ImageIO.read(new File("res/extends.png"));
			Image iImg = ImageIO.read(new File("res/interface.png"));
			Image aImg = ImageIO.read(new File("res/abstract.png"));
			
			cIcon = new ImageIcon(cImg.getScaledInstance(16, 16, Image.SCALE_SMOOTH));
			mIcon = new ImageIcon(mImg.getScaledInstance(16, 16, Image.SCALE_SMOOTH));
			vIcon = new ImageIcon(vImg.getScaledInstance(16, 16, Image.SCALE_SMOOTH));
			dIcon = new ImageIcon(dImg.getScaledInstance(16, 16, Image.SCALE_SMOOTH));
			eIcon = new ImageIcon(eImg.getScaledInstance(16, 16, Image.SCALE_SMOOTH));
			iIcon = new ImageIcon(iImg.getScaledInstance(16, 16, Image.SCALE_SMOOTH));
			aIcon = new ImageIcon(aImg.getScaledInstance(16, 16, Image.SCALE_SMOOTH));
			
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
            setToolTipText("Class"); 
        } 
        else if (s != null && s.type == "m") 
        {
            setIcon(mIcon);
            setToolTipText("Method"); 
        }
        else if (s != null && s.type == "v") 
        {
            setIcon(vIcon);
            setToolTipText("Field"); 
        }
        else if (s != null && s.type == "d") 
        {
            setIcon(dIcon);
            setToolTipText(null); 
        }
        else if (s != null && s.type == "e") 
        {
            setIcon(eIcon);
            setToolTipText("Extends"); 
        }
        else if (s != null && s.type == "i") 
        {
            setIcon(iIcon);
            setToolTipText("Interface"); 
        }
        else if (s != null && s.type == "a") 
        {
            setIcon(aIcon);
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
