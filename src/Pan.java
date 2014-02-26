import java.awt.*;
import javax.swing.JPanel;
import java.awt.font.*;
import java.awt.geom.*;

/// Will be the basic render, will prob replace later down the road. 
public class Pan extends JPanel
{
	public int x = 1000, y =900;
	public Color backgroundColor = Color.white;
	public Font font = new Font("Courier New",Font.PLAIN,12);
	
	private Graphics g;
	
	private Block b1 = new Block("Hello World this is a test\n:D\nAlso.......arthur is a noob :P",100,100,new Color(186,196,227), font);
	private Block b2 = new Block("Class:someClass\nDoes some cool things that classes do\nI dont know..",200,200,new Color(196, 121, 126), font);
	
	public Pan()
	{
		setBackground(Color.BLACK);
		setPreferredSize(new Dimension(x, y));
	}
	
	public Pan(int x, int y)
	{
		this.x = x;
		this.y = y;
		setBackground(Color.BLACK);
		setPreferredSize(new Dimension(x, y));
	}
	
	int t = 0;
	public void paintComponent(Graphics page)
	{
		t++;
		super.paintComponents(page);
		g = page;
		
		((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		((Graphics2D)g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		
		//Clears the background
		g.setColor(backgroundColor);
		g.fillRect(0,0,x,y);
		
		renderLine(150,150,250,250,new Color(255, 229, 153));
		b1.draw(g);
		b2.draw(g);
	}
	
	
	private void renderLine(int x1, int y1, int x2, int y2,Color c)
	{
		int dot = 8;
		int space = 20;
		double time = t/4.0;
		double length = Math.sqrt((x2-x1)*(x2 - x1) + (y2-y1)*(y2-y1));
		int count = (int)(length/space);
		double normx = (x2-x1)/length;
		double normy = (y2-y1)/length;
		double offset = time%space;
		
		g.setColor(c);
		
		for(int i = 0; i < count; i ++)
		{
			int x = x1 + (int)((space*i + offset)*normx);
			int y = y1 + (int)((space*i + offset)*normy);
			g.fillOval(x - dot/2,y - dot/2, dot, dot);
		}
	}
	
}
