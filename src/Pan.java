import java.awt.*;
import javax.swing.JPanel;

public class Pan extends JPanel
{
	public static final int x = 1000, y =900;

	public Pan()
	{
		setBackground(Color.BLACK);
		setPreferredSize(new Dimension(x, y));
	}
	
	int t = 0;
	public void paintComponent(Graphics page)
	{
		t++;
		super.paintComponents(page);
		page.setColor(Color.white);
		page.fillRect(0,0,x,y);
		
		page.setColor(Color.black);
		page.fillRect(20,20,40,40);
	}

}
