import java.awt.*;

import javax.swing.JPanel;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;


/// Will be the basic render, will prob replace later down the road. 
@SuppressWarnings("serial")
public class Pan extends JPanel 
{
	public int x = 1000, y = 900;
	public Color backgroundColor = Color.white;
	public static Font font = new Font("Courier New", Font.BOLD, 24);
	public static Font font2 = new Font("Courier New", Font.PLAIN, 12);
	
	private float zoomx = -300f;
	private float zoomy = -300f;
	private float zoom = 1.0f;
	private Graphics g;
	public Diagram diag;

	
	int startX = 0;
	int startY = 0;
	
	Mouse mouse;
	
	int clickStartX = 0;
	int clickStartY = 0;
	int clickTime = -1;

	public Pan() 
	{
		setBackground(Color.BLACK);
		setPreferredSize(new Dimension(x, y));
		
		mouse = new Mouse(this);
		addMouseListener(mouse);
		addMouseMotionListener(mouse);
		addMouseWheelListener(mouse);
		
		this.setDropTarget(new dragCatcher(this));
		
		mouse.wheel = -20;
		zoom = (float)Math.pow(1.05f, mouse.wheel);
		
		renderThread t = new renderThread(this);
		t.start();
	}

	public void setDiag(Diagram d) 
	{
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

		//((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		//((Graphics2D) g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON); //  Was causing problems
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

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
			
			clickTime++;
		}
		
		if(diag.JavaBlocks.size() != 0)
		{
			render(g, zoom, zoomx, zoomy, x, y);
		}
		else
		{
			g.setColor(backgroundColor);
			g.fillRect(0, 0, x, y);
			g.setColor(Color.black);
			g.setFont(font);
			g.drawString("add some files", x/2-100, y/2);
			g.drawString("to get started", x/2-100, y/2+25);
		}
		
		diag.genStatus();
	}
	
	
	
	
	
	public void render(Graphics g, float zoom_l, float zoomx_l, float zoomy_l, int width, int height)
	{
		// Clears the background
		g.setColor(backgroundColor);
		g.fillRect(0, 0, width, height);
		
		if(diag != null)
		{
			
			int dx;
			int dy;
			
			
			for (int i = 0; i < diag.JavaBlocks.size(); i++) 
			{
				DiagramBlock db = diag.JavaBlocks.get(i);
				Block b = db.block;
				dx = (int)(b.x*zoom_l - zoomx_l*zoom_l);
				dy = (int)(b.y*zoom_l - zoomy_l*zoom_l);
				dx += (int)(b.width*zoom_l) /2;
				dy += (int)(b.height*zoom_l) /2;
				
				// Draw Connection lines 
				for(int j = 0; j < db.Class.referenceClasses.size();j++)
				{
					Block b2 = db.Class.referenceClasses.get(j).diagBlock.block;
					int dx2 = (int)(b2.x*zoom_l - zoomx_l*zoom_l) + (int)(b2.width*zoom_l) /2;
					int dy2 = (int)(b2.y*zoom_l - zoomy_l*zoom_l) + (int)(b2.height*zoom_l) /2;
					
					g.setColor(Color.blue);
					g.drawLine(dx, dy, dx2, dy2);
				}
				
			}
			
			
			
			for (int i = 0; i < diag.JavaBlocks.size(); i++) 
			{
				DiagramBlock db = diag.JavaBlocks.get(i);
				Block b = db.block;
				dx = (int)(b.x*zoom_l - zoomx_l*zoom_l);
				dy = (int)(b.y*zoom_l - zoomy_l*zoom_l);
				if(dx >= -b.width*zoom_l && dx < width && dy >= -b.height*zoom_l && dy < height)//on screen
					g.drawImage(b.img, dx, dy, (int)(b.img.getWidth()*zoom_l), (int)(b.img.getHeight()*zoom_l), null);
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
		
		// Render the image
		int img_width = maxX - minX + 200;
		int img_height = maxY - minY + 200;
		
	    BufferedImage img = new BufferedImage(img_width,img_height,BufferedImage.TYPE_INT_ARGB);
	    
		Graphics imgG = img.getGraphics();
		((Graphics2D) imgG).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		((Graphics2D) imgG).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		((Graphics2D) imgG).setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		render(imgG,1.0f,minX-100,minY-100,img_width,img_height);
		
		return img;
	}

	
	public void click(int x, int y)
	{
		if(mouse.Lpress)
		{
			startX = x;
			startY = y;
			
			clickStartX = x;
			clickStartY = y;
			clickTime = 0;
		}
	}
	
	
	public void release(int x, int y)
	{
		if(clickTime >= 0)
			leftClick(clickTime, x, y, clickStartX, clickStartY);
		clickTime = -1;
	}
	
	private void leftClick(int clickDur, int x, int y, int sx, int sy)
	{
		double dist = (x-sx)*(x-sx) + (y-sy)*(y-sy);
		dist = Math.sqrt(dist);
		if(dist <= 5.0)// we will count this as a click... longer and its prob panning... 
		{
			double rx = zoomx + x/zoom;
			double ry = zoomy + y/zoom;
			
			DiagramBlock clickBlock = null;
			
			for(DiagramBlock b: diag.JavaBlocks)
			{
				if(rx > b.block.x && ry > b.block.y && rx < b.block.x + b.block.width && ry < b.block.y + b.block.height)
					clickBlock = b;
			}
			
			
			if(clickBlock != null)
			{
				// the block has been clicked... do stuff
			}
			
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


@SuppressWarnings("serial")
class dragCatcher extends DropTarget
{
	
	Pan currentPan;

	public dragCatcher(Pan p) 
	{
		currentPan = p;
	}
	
	@Override
    public synchronized void drop(DropTargetDropEvent dtde) 
	{
		dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
		Transferable t = dtde.getTransferable();
		
		if(t.isDataFlavorSupported(DataFlavor.javaFileListFlavor))
		{
	        try 
	        {
	        	@SuppressWarnings("unchecked")
				java.util.List<File> fileList = (java.util.List<File>)t.getTransferData(DataFlavor.javaFileListFlavor);
	        	
	        	for(File f : fileList)
	        	{
	        		UrlResolver resolv = new UrlResolver(f.getAbsolutePath());
	    			ArrayList<String> arr = resolv.resolve();
	    			if(arr == null) return;
	    			currentPan.diag.addFiles(arr, 0, 0);
	        	}
			} 
	        catch (Exception e) 
	        {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(t.isDataFlavorSupported(DataFlavor.stringFlavor))
		{
	        try 
	        {
	        	String s = (String)t.getTransferData(DataFlavor.stringFlavor);
	        	UrlResolver resolv = new UrlResolver(s);
    			ArrayList<String> arr = resolv.resolve();
    			if(arr == null) return;
    			currentPan.diag.addFiles(arr, 0, 0);
			} 
	        catch (Exception e) 
	        {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
        
        //super.drop(dtde);
    }
}
