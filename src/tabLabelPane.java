import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;


@SuppressWarnings("serial")
public class tabLabelPane extends JPanel
{
	JLabel label;
	JButton button;
	
	public tabLabelPane(final JTabbedPane tabbedPane, final tabFrame tf) 
	{
		tf.tab = this;
		this.setOpaque(false);
		label = new JLabel(tf.name);
		button = new JButton(MedusaIcons.xIcon);
		button.setBackground(new Color(0,0,0,0));
		button.setOpaque(false);
		button.setPreferredSize(new Dimension(16, 16));
		button.setBorder(null);

		button.addMouseListener(new MouseAdapter() 
		{
			public void mouseClicked(MouseEvent e) 
			{
				tabbedPane.remove(tf);
			}
		});
		
		this.add(label);
		this.add(button);
	}

}
