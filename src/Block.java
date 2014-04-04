import java.awt.*;
import java.awt.font.*;

class Block extends Entity
{
	public String text;
	public int x;
	public int y;
	public Color color;
	public Font font;
	public int width;
	public int height;
	
	private String[] lines;
	private int[] widths;
	private int border = 5;
	private int lineHeight;
	private int maxLineWidth;
	private int ascent;
	
	public Block(String text, int x, int y, Color color, Font font)
	{
		this.text = text;
		this.x = x;
		this.y = y;
		this.color = color;
		this.font = font;
		

		lines = text.split("\n");
		widths = new int[lines.length];
		
		//Get box size from text
		Canvas c = new Canvas();//This feels like such a hack just to get a FontMetrics :/
		FontMetrics metrics = c.getFontMetrics(font);
		lineHeight = metrics.getHeight();
		height = lineHeight*lines.length + border*2;
		maxLineWidth = 0;
		for(int i = 0; i < lines.length; i++)
		{
			widths[i] = metrics.stringWidth(lines[i]);
			if(widths[i] > maxLineWidth) maxLineWidth = widths[i];
		}
		
		width = maxLineWidth + border*2;
		ascent = metrics.getAscent();
		
		
		
		
	}
	
	public void draw(Graphics g)
	{
		//Draw box
		g.setColor(color);
		g.fillRoundRect(x,y,width,height,15,15);
		
		g.setColor(Color.black);
		g.drawRoundRect(x,y,width,height,15,15);
		
		//Draw out each line
		g.setFont(font);
		int cx = x + border;
		int cy = y + ascent + border;
		for(int i = 0; i < lines.length; i++)
		{
			g.drawString(lines[i],cx ,cy + i*lineHeight);
		}
	}

}
