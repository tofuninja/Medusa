import java.awt.*;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
	
	private boolean isFullPan = true;
	public float zoomx = -300f;
	public float zoomy = -300f;
	public float zoom = 1.0f;
	private Graphics g;
	public Diagram diag;
	
	private static double arrowAng = Math.PI/6.0;

	
	int startX = 0;
	int startY = 0;
	
	Mouse mouse;
	
	int clickStartX = 0;
	int clickStartY = 0;
	int clickTime = -1;

	public Pan(boolean fullPan) 
	{
		isFullPan = fullPan;
		setBackground(Color.BLACK);
		setPreferredSize(new Dimension(x, y));
		
		mouse = new Mouse(this);
		addMouseListener(mouse);
		addMouseMotionListener(mouse);
		addMouseWheelListener(mouse);
		
		if(isFullPan) 
		{
			this.setDropTarget(new dragCatcher(this));
			this.addMouseListener(new panRightClick(this));
		}
		
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
		
		if(diag == null)
		{
			g.setColor(backgroundColor);
			g.fillRect(0, 0, x, y);
			return;
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
	
	
	
	private static BasicStroke normStroke = new BasicStroke(1);
	private static BasicStroke thickStroke = new BasicStroke(2);
	
	public void render(Graphics g, float zoom_l, float zoomx_l, float zoomy_l, int width, int height)
	{
		// Clears the background
		g.setColor(backgroundColor);
		g.fillRect(0, 0, width, height);
		Graphics2D g2d = (Graphics2D)g;
		
		if(diag != null)
		{
			
			double dx;
			double dy;
			
			
			for (int i = 0; i < diag.JavaBlocks.size(); i++) 
			{
				DiagramBlock db = diag.JavaBlocks.get(i);
				Block b = db.block;
				dx = (b.x*zoom_l - zoomx_l*zoom_l);
				dy = (b.y*zoom_l - zoomy_l*zoom_l);
				dx += (b.width*zoom_l) /2;
				dy += (b.height*zoom_l) /2;
				
				// Draw Connection lines 
				for(int j = 0; j < db.Class.referenceClasses.size();j++)
				{
					javaRef ref = db.Class.referenceClasses.get(j);
					
					if(!javaRef.enabled[ref.type]) continue;
					
					if(ref.thick)
					{
						g2d.setStroke(thickStroke);
					}
					
					Block b2 = ref.end.diagBlock.block;
					double dx2 = (b2.x*zoom_l - zoomx_l*zoom_l) + (b2.width*zoom_l) /2;
					double dy2 = (b2.y*zoom_l - zoomy_l*zoom_l) + (b2.height*zoom_l) /2;
					
					g.setColor(ref.color);
					g.drawLine((int)dx, (int)dy, (int)dx2, (int)dy2);
					
					double angle = Math.atan2(dy2 - dy, dx2 - dx);
					double boxangle1 = Math.atan2(-b.height/2.0, -b.width/2.0);
					double boxangle2 = Math.atan2(-b.height/2.0,  b.width/2.0);
					double boxangle3 = Math.atan2( b.height/2.0,  b.width/2.0);
					double boxangle4 = Math.atan2( b.height/2.0, -b.width/2.0);
					PointD p;
					
					if(angle >= boxangle1 && angle <= boxangle2)//intersects top
					{
						p = lineIntersect(dx, dy, dx2, dy2, 
								((b.x)*zoom_l - zoomx_l*zoom_l), 			//Line 2 x1
								((b.y)*zoom_l - zoomy_l*zoom_l),	 		//Line 2 y1
								((b.x+b.width)*zoom_l - zoomx_l*zoom_l), 	//Line 2 x2
								((b.y)*zoom_l - zoomy_l*zoom_l));			//Line 2 y2
					}
					else if(angle >= boxangle2 && angle <= boxangle3)//intersects right
					{
						p = lineIntersect(dx, dy, dx2, dy2, 
								((b.x+b.width)*zoom_l - zoomx_l*zoom_l), 	//Line 2 x1
								((b.y)*zoom_l - zoomy_l*zoom_l),	 		//Line 2 y1
								((b.x+b.width)*zoom_l - zoomx_l*zoom_l), 	//Line 2 x2
								((b.y+b.height)*zoom_l - zoomy_l*zoom_l));	//Line 2 y2
					}
					else if(angle >= boxangle3 && angle <= boxangle4)//intersects bottom
					{
						p = lineIntersect(dx, dy, dx2, dy2, 
								((b.x)*zoom_l - zoomx_l*zoom_l), 			//Line 2 x1
								((b.y+b.height)*zoom_l - zoomy_l*zoom_l),	//Line 2 y1
								((b.x+b.width)*zoom_l - zoomx_l*zoom_l), 	//Line 2 x2
								((b.y+b.height)*zoom_l - zoomy_l*zoom_l));	//Line 2 y2
					}
					else//intersects left
					{
						p = lineIntersect(dx, dy, dx2, dy2, 
								((b.x)*zoom_l - zoomx_l*zoom_l), 			//Line 2 x1
								((b.y)*zoom_l - zoomy_l*zoom_l),	 		//Line 2 y1
								((b.x)*zoom_l - zoomx_l*zoom_l), 			//Line 2 x2
								((b.y+b.height)*zoom_l - zoomy_l*zoom_l));	//Line 2 y2
					}
					
					
					g.drawLine((int)p.x, (int)p.y, (int)(p.x+Math.cos(angle-arrowAng)*10), (int)(p.y+Math.sin(angle-arrowAng)*10));
					g.drawLine((int)p.x, (int)p.y, (int)(p.x+Math.cos(angle+arrowAng)*10), (int)(p.y+Math.sin(angle+arrowAng)*10));
					
					
					if(ref.thick)
					{
						g2d.setStroke(normStroke);
					}
				}
				
			}
			
			
			
			for (int i = 0; i < diag.JavaBlocks.size(); i++) 
			{
				DiagramBlock db = diag.JavaBlocks.get(i);
				Block b = db.block;
				dx = (b.x*zoom_l - zoomx_l*zoom_l);
				dy = (b.y*zoom_l - zoomy_l*zoom_l);
				if(dx >= -b.width*zoom_l && dx < width && dy >= -b.height*zoom_l && dy < height)//on screen
					g.drawImage(b.img, (int)dx, (int)dy, (int)(b.img.getWidth()*zoom_l), (int)(b.img.getHeight()*zoom_l), null);
			}
			
		}
		
		
		if(UI.me.dipLegend && isFullPan)
		{
			renderLegend(g);
		}
	}
	
	
	private Block legClassBlock = new Block("Class",0,0,new Color(196, 121, 126));
	private Block legInterfaceBlock = new Block("Interface",0,0,new Color(214, 162, 88));
	private Block legAbstractBlock = new Block("Abstract Class",0,0,new Color(139, 144, 217));
	
	private void renderLegend(Graphics g)
	{
		int w = 125;
		int h = 160;
		int x = 10;
		int y = 10;
		
		g.setColor(Color.white);
		g.fillRoundRect(x, y, w, h, 15, 15);
		
		int tempy = y + 10;
		g.drawImage(legClassBlock.img, x + 10, tempy,legClassBlock.img.getWidth()/2,legClassBlock.img.getHeight()/2, null);
		tempy += legClassBlock.img.getHeight()/2 + 5;
		g.drawImage(legInterfaceBlock.img, x + 10, tempy ,legInterfaceBlock.img.getWidth()/2,legInterfaceBlock.img.getHeight()/2, null);
		tempy += legInterfaceBlock.img.getHeight()/2 + 5;
		g.drawImage(legAbstractBlock.img, x + 10, tempy ,legAbstractBlock.img.getWidth()/2,legAbstractBlock.img.getHeight()/2, null);
		tempy += legAbstractBlock.img.getHeight()/2 + 10;
		
		g.setFont(font2);
		
		double dx = x + 10;
		double dy = tempy;
		double dx2 = dx + 30;
		double dy2 = tempy;
		g.setColor(javaRef.normalCol);
		g.drawLine((int)dx, (int)dy, (int)dx2, (int)dy2);
		double angle = Math.atan2(dy2 - dy, dx2 - dx);
		g.drawLine((int)dx, (int)dy2, (int)(dx+Math.cos(angle-arrowAng)*10), (int)(dy+Math.sin(angle-arrowAng)*10));
		g.drawLine((int)dx, (int)dy2, (int)(dx+Math.cos(angle+arrowAng)*10), (int)(dy+Math.sin(angle+arrowAng)*10));
		g.setColor(Color.black);
		g.drawString("References", x + 43, tempy+4);
		
		tempy += 20;
		dx = x + 10;
		dy = tempy;
		dx2 = dx + 30;
		dy2 = tempy;
		g.setColor(javaRef.extCol);
		g.drawLine((int)dx, (int)dy, (int)dx2, (int)dy2);
		angle = Math.atan2(dy2 - dy, dx2 - dx);
		g.drawLine((int)dx, (int)dy2, (int)(dx+Math.cos(angle-arrowAng)*10), (int)(dy+Math.sin(angle-arrowAng)*10));
		g.drawLine((int)dx, (int)dy2, (int)(dx+Math.cos(angle+arrowAng)*10), (int)(dy+Math.sin(angle+arrowAng)*10));
		g.setColor(Color.black);
		g.drawString("Extends", x + 43, tempy+4);
		
		tempy += 20;
		dx = x + 10;
		dy = tempy;
		dx2 = dx + 30;
		dy2 = tempy;
		g.setColor(javaRef.interCol);
		g.drawLine((int)dx, (int)dy, (int)dx2, (int)dy2);
		angle = Math.atan2(dy2 - dy, dx2 - dx);
		g.drawLine((int)dx, (int)dy2, (int)(dx+Math.cos(angle-arrowAng)*10), (int)(dy+Math.sin(angle-arrowAng)*10));
		g.drawLine((int)dx, (int)dy2, (int)(dx+Math.cos(angle+arrowAng)*10), (int)(dy+Math.sin(angle+arrowAng)*10));
		g.setColor(Color.black);
		g.drawString("Implements", x + 43, tempy+4);
		
		g.setColor(Color.black);
		g.drawRoundRect(x, y, w, h, 15, 15);
	}
	
	
	PointD lineIntersect(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4)
	{
		double m1, c1, m2, c2;
		double dx, dy;
		double intersection_X, intersection_Y;
	 

	    dx = x2 - x1;
	    dy = y2 - y1;
	 
	    m1 = dy / dx;
	    if(Double.isInfinite(m1)) m1 = 99999999.0;
	    // y = mx + c
	    // intercept c = y - mx
	    c1 = y1 - m1 * x1; // which is same as y2 - slope * x2

	    dx = x4 - x3;
	    dy = y4 - y3;
	 
	    m2 = dy / dx;
	    if(Double.isInfinite(m2)) m2 = 99999999.0;
	    // y = mx + c
	    // intercept c = y - mx
	    c2 = y3 - m2 * x3; // which is same as y2 - slope * x2

	    if( (m1 - m2) == 0)
	    {
	    	System.out.println("didnt intersect...");
	        return new PointD(x1,x2);// they didn't intersect but at least i can return something in the area... 
	    }
	    else
	    {
	        intersection_X = (c2 - c1) / (m1 - m2);
	        intersection_Y = m1 * intersection_X + c1;
	        return new PointD(intersection_X, intersection_Y);
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
		if(isFullPan == false) return;
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
				classViewer cv = new classViewer(diag,clickBlock.Class);
				cv.setVisible(true);
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


class PointD
{
	public double x;
	public double y;
	public PointD(double X, double Y)
	{
		x = X;
		y = Y;
	}
}



class panRightClick extends MouseAdapter 
{
	private Pan p;
	
	public panRightClick(Pan pan)
	{
		p = pan;
	}
	
    public void mousePressed(MouseEvent e){
        if (e.isPopupTrigger())
            doPop(e);
    }

    public void mouseReleased(MouseEvent e){
        if (e.isPopupTrigger())
            doPop(e);
    }

    private void doPop(MouseEvent e)
    {
    	
    	if(p.diag == null) return;
    	if(p.diag.JavaBlocks.size() == 0) return;
    	
    	double x = e.getX();
    	double y = e.getY();
    	
    	double rx = p.zoomx + x/p.zoom;
		double ry = p.zoomy + y/p.zoom;
		
		DiagramBlock clickBlock = null;
		
		for(DiagramBlock b: p.diag.JavaBlocks)
		{
			if(rx > b.block.x && ry > b.block.y && rx < b.block.x + b.block.width && ry < b.block.y + b.block.height)
				clickBlock = b;
		}
		
		final DiagramBlock db = clickBlock;
		if(clickBlock != null)
		{
			JPopupMenu pop = new JPopupMenu();
			JMenuItem delete = new JMenuItem("Delete Node");
			delete.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) 
				{
					p.diag.deleteClass(db.Class);
				}
			});
			pop.add(delete);
			pop.show(e.getComponent(), e.getX(), e.getY());
		}
    }
}



