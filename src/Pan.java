import java.awt.*;

import javax.swing.JPanel;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

/// Will be the basic render, will prob replace later down the road. 
public class Pan extends JPanel 
{
	public int x = 1000, y = 900;
	public Color backgroundColor = Color.white;
	public static Font font = new Font("Courier New", Font.BOLD, 24);
	public static Font font2 = new Font("Courier New", Font.PLAIN, 12);
	
	public float zoomx = 0.0f;
	public float zoomy = 0.0f;
	public float zoom = 1.0f;

	private Graphics g;
	Diagram diag;
	Block title;
	
	int startX = 0;
	int startY = 0;
	
	Mouse mouse;

	public Pan() 
	{
		setBackground(Color.BLACK);
		setPreferredSize(new Dimension(x, y));
		
		mouse = new Mouse(this);
		addMouseListener(mouse);
		addMouseMotionListener(mouse);
		addMouseWheelListener(mouse);
		
		renderThread t = new renderThread(this);
		t.start();
	}

	public void setDiag(Diagram d) 
	{
	    title = new Block("Folder: " + d.Folder + "\nClass Count: " + d.JavaBlocks.size(), 50, 0, new Color(193, 255, 194), font);
		diag = d;
	}
	int t = 0;

	public void paintComponent(Graphics page) 
	{
		x = this.getWidth();
		y = this.getHeight();
		
		t++;
		super.paintComponents(page);
		g = page;

		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

		// Clears the background
		g.setColor(backgroundColor);
		g.fillRect(0, 0, x + 100, y + 100);
		
		float prezoom = zoom;
		zoom = (float)Math.pow(1.05f, mouse.wheel);
		
		if(mouse.Mpress)
		{
			mouse.wheel = 0;
			zoom = 1.0f;
		}
		
		zoomx -= (mouse.x/zoom - mouse.x/prezoom);
		zoomy -= (mouse.y/zoom - mouse.y/prezoom);
		
		
		if(mouse.Lpress)
		{
			zoomx += (startX - mouse.x)/zoom;
			zoomy += (startY - mouse.y)/zoom;
			
			startX = mouse.x;
			startY = mouse.y;
		}
		
		//if(img != null) g.drawImage(img, (int)(zoomx*zoom), (int)(zoomy*zoom), (int)(img.getWidth()*zoom), (int)(img.getHeight()*zoom), null);
		
		if(diag != null)
		{
			
			int dx;
			int dy;
			
			
			for (int i = 0; i < diag.JavaBlocks.size(); i++) 
			{
				DiagramBlock db = diag.JavaBlocks.get(i);
				Block b = db.block;
				dx = (int)(b.x*zoom - zoomx*zoom);
				dy = (int)(b.y*zoom - zoomy*zoom);
				dx += (int)(b.width*zoom) /2;
				dy += (int)(b.height*zoom) /2;
				
				// Draw Connection lines 
				for(int j = 0; j < db.Class.referenceClasses.size();j++)
				{
					Block b2 = db.Class.referenceClasses.get(j).diagBlock.block;
					int dx2 = (int)(b2.x*zoom - zoomx*zoom) + (int)(b2.width*zoom) /2;
					int dy2 = (int)(b2.y*zoom - zoomy*zoom) + (int)(b2.height*zoom) /2;
					
					g.setColor(Color.blue);
					g.drawLine(dx, dy, dx2, dy2);
				}
				
			}
			
			
			
			dx = (int)(title.x*zoom - zoomx*zoom);
			dy = (int)(title.y*zoom - zoomy*zoom );
			if(dx >= -title.width*zoom && dx < x && dy >= -title.height*zoom && dy < y)//on screen
				g.drawImage(title.img, dx, dy, (int)(title.img.getWidth()*zoom), (int)(title.img.getHeight()*zoom), null);
			
			for (int i = 0; i < diag.JavaBlocks.size(); i++) 
			{
				DiagramBlock db = diag.JavaBlocks.get(i);
				Block b = db.block;
				dx = (int)(b.x*zoom - zoomx*zoom);
				dy = (int)(b.y*zoom - zoomy*zoom);
				if(dx >= -b.width*zoom && dx < x && dy >= -b.height*zoom && dy < y)//on screen
					g.drawImage(b.img, dx, dy, (int)(b.img.getWidth()*zoom), (int)(b.img.getHeight()*zoom), null);
			}
			
		}
		
		
	}
	
	
	
	public BufferedImage renderToImage()
	{
		int minX = 100000000;
		int maxX = -100000000;
		int minY = 100000000;
		int maxY = -100000000;
		
		for (int i = 0; i < diag.JavaBlocks.size(); i++) 
		{
			DiagramBlock db = diag.JavaBlocks.get(i);
			
			if((int)db.block.x < minX) minX = (int)db.block.x;
			if(((int)db.block.x + db.block.width) > maxX) maxX = (int)db.block.x + db.block.width;
			
			if((int)db.block.y < minY) minY = (int)db.block.y;
			if(((int)db.block.y + db.block.height) > maxY) maxY = (int)db.block.y + db.block.height;
			
			//System.out.println("X:" + db.block.x + "Y:" + db.block.y);
		}
		
		System.out.println("minX " + minX);
		System.out.println("miny " + minX);
		System.out.println("maxX " + maxX);
		System.out.println("maxY " + maxY);
		
		// Render the image
		int img_width = maxX - minX + 200;
		int img_height = maxY - minY + 200;
		
		System.out.println("img_width " + img_width);
		System.out.println("img_height " + img_height);
		
	    BufferedImage img = new BufferedImage(img_width,img_height,BufferedImage.TYPE_INT_ARGB);
	    
		Graphics imgG = img.getGraphics();
		
		// Clears the background
		imgG.setColor(backgroundColor);
		imgG.fillRect(0, 0, img_width, img_height);
		
		int dx;
		int dy; 
		
		for (int i = 0; i < diag.JavaBlocks.size(); i++) 
		{
			DiagramBlock db = diag.JavaBlocks.get(i);
			Block b = db.block;
			dx = (int)(b.x) - minX;
			dy = (int)(b.y) - minY;
			dx += (int)(b.width) /2;
			dy += (int)(b.height) /2;
			
			for(int j = 0; j < db.Class.referenceClasses.size();j++)
			{
				Block b2 = db.Class.referenceClasses.get(j).diagBlock.block;
				int dx2 = (int)(b2.x) + (int)(b2.width) /2 - minX;
				int dy2 = (int)(b2.y) + (int)(b2.height) /2 - minY;
				
				imgG.setColor(Color.blue);
				imgG.drawLine(dx, dy, dx2, dy2);
			}
			
		}
		
		
		dx = (int)(title.x) - minX;
		dy = (int)(title.y) - minY;
		imgG.drawImage(title.img, dx, dy, title.img.getWidth(), title.img.getHeight(), null);
		
		for (int i = 0; i < diag.JavaBlocks.size(); i++) 
		{
			DiagramBlock db = diag.JavaBlocks.get(i);
			Block b = db.block;
			dx = (int)(b.x) - minX;
			dy = (int)(b.y) - minY;
			imgG.drawImage(b.img, dx, dy, b.img.getWidth(), b.img.getHeight(), null);
			
		}
		return img;
	}

	/*
	private void renderLine(int x1, int y1, int x2, int y2, Color c) {
		int dot = 8;
		int space = 20;
		double time = t / 4.0;
		double length = Math
				.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
		int count = (int) (length / space);
		double normx = (x2 - x1) / length;
		double normy = (y2 - y1) / length;
		double offset = time % space;

		g.setColor(c);

		for (int i = 0; i < count; i++) {
			int x = x1 + (int) ((space * i + offset) * normx);
			int y = y1 + (int) ((space * i + offset) * normy);
			g.fillOval(x - dot / 2, y - dot / 2, dot, dot);
		}
	}
	*/
	
	
	
	public void click(int x, int y)
	{
		if(mouse.Lpress)
		{
			startX = x;
			startY = y;
		}
	}

}




class renderThread extends Thread 
{
	Pan currentPan;

	public renderThread(Pan p) 
	{
		currentPan = p;
	}

	public void run() 
	{
		while (true) 
		{
			try 
			{
				currentPan.repaint();
				Thread.sleep(10);
			} 
			catch (Exception e) 
			{
				break;
			}

		}
	}
}
