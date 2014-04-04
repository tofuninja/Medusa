import java.awt.*;
import java.awt.image.BufferedImage;

class Block 
{
	public String text;
	public float x;
	public float y;
	public Color color;
	public Font font;
	public int width;
	public int height;
	public BufferedImage img;
	
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
		
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
	    GraphicsDevice device = env.getDefaultScreenDevice();
	    GraphicsConfiguration config = device.getDefaultConfiguration();
	    img = config.createCompatibleImage(width + 10, height + 10, Transparency.TRANSLUCENT);
	    
		Graphics g = img.getGraphics();
		
		//Draw box
		g.setColor(color);
		g.fillRoundRect(0,0,width,height,15,15);
		
		g.setColor(Color.black);
		g.drawRoundRect(0,0,width,height,15,15);
		
		//Draw out each line
		g.setFont(font);
		int cx = border;
		int cy = ascent + border;
		for(int i = 0; i < lines.length; i++)
		{
			g.drawString(lines[i],cx ,cy + i*lineHeight);
		}
		
	}
	


}
