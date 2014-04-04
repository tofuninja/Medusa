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
	public static Pan me;
	
	public static float zoomx = 0.0f;
	public static float zoomy = 0.0f;
	public static float zoom = 1.0f;

	private Graphics g;
	ArrayList<Block> block_list = new ArrayList<Block>();
	
	public BufferedImage img;
	Graphics imgG;
	
	BufferedImage[][] subImage;
	int subSize = 100;
	int subW;
	int subH;
	
	int startX = 0;
	int startY = 0;

	public Pan() 
	{
		setBackground(Color.BLACK);
		setPreferredSize(new Dimension(x, y));
		me = this;
		
		Mouse m = new Mouse();
		addMouseListener(m);
		addMouseMotionListener(m);
		addMouseWheelListener(m);
	}

	public void setDiag(Diagram d) 
	{
		block_list.clear();
		Block a = new Block("Folder: " + d.Folder + "\nClass Count: " + d.JavaBlocks.size(), 50, 0, new Color(193, 255, 194), font);
		block_list.add(a);
		
		int minX = 0;
		int maxX = 0;
		int minY = 0;
		int maxY = 0;
		
		for (int i = 0; i < d.JavaBlocks.size(); i++) 
		{
			DiagramBlock db = d.JavaBlocks.get(i);
			
			if(db.getX() < minX) minX = (int)db.getX();
			if(db.getX()+db.block.width > maxX) maxX = (int)db.getX()+db.block.width;
			
			if(db.getY() < minY) minY = (int)db.getY();
			if(db.getY()+db.block.height > maxY) maxY = (int)db.getY()+db.block.height;
			
			block_list.add(db.block);
		}
		
		
		
		// Render the image
		int img_width = maxX - minX;
		int img_height = maxY - minY;
		
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
	    GraphicsDevice device = env.getDefaultScreenDevice();
	    GraphicsConfiguration config = device.getDefaultConfiguration();
	    img = config.createCompatibleImage(img_width, img_height, Transparency.TRANSLUCENT);
	    
		imgG = img.getGraphics();
		
		// Clears the background
		imgG.setColor(backgroundColor);
		imgG.fillRect(0, 0, img_width, img_height);
		for (int i = 0; i < block_list.size(); i++) 
		{
			block_list.get(i).draw(imgG);
		}
		
		//Cut up into subimages for performance 
		
		subW = img_width/subSize + 1;
		subH = img_height/subSize + 1;
		
		subImage = new BufferedImage[subW][subH];
		
		for(int i = 0; i < subW; i ++)
		{
			for(int j = 0; j < subH; j++)
			{
				subImage[i][j] = new BufferedImage(subSize,subSize,img.getType());
				Graphics tempg = subImage[i][j].getGraphics();
				tempg.drawImage(img, -i*subSize, -j*subSize, img.getWidth(), img.getHeight(), null);
			}
		}
		
	}

	public Pan(int x, int y) {
		this.x = x;
		this.y = y;
		setBackground(Color.BLACK);
		setPreferredSize(new Dimension(x, y));
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

		// Clears the background
		g.setColor(backgroundColor);
		g.fillRect(0, 0, x + 100, y + 100);
		
		zoom = (float)Math.pow(1.05f, Mouse.me.wheel);
		
		if(Mouse.me.Lpress)
		{
			zoomx += (Mouse.me.x - startX)/zoom;
			zoomy += (Mouse.me.y - startY)/zoom;
			
			startX = Mouse.me.x;
			startY = Mouse.me.y;
		}
		
		//if(img != null) g.drawImage(img, (int)(zoomx*zoom), (int)(zoomy*zoom), (int)(img.getWidth()*zoom), (int)(img.getHeight()*zoom), null);
		
		if(img != null)
		{
			for(int i = 0; i < subW; i ++)
			{
				for(int j = 0; j < subH; j++)
				{
					int dx = (int)(zoomx*zoom + i*(int)(subSize*zoom));
					int dy = (int)(zoomy*zoom + j*(int)(subSize*zoom));
					
					if(dx >= -subSize*zoom && dx < x && dy >= -subSize*zoom && dy < y)//on screen
					{
						g.drawImage(subImage[i][j], dx, dy, (int)(subSize*zoom), (int)(subSize*zoom), null);
					}
				}
			}
		}
		
		g.setColor(backgroundColor);
		g.fillRect(0, y-15, 120, 15);
		g.setFont(font2);
		g.setColor(Color.black);
		g.drawString("Zoom:"+zoom, 0, y);
		
		
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
		if(Mouse.me.Lpress)
		{
			startX = x;
			startY = y;
		}
	}

}


