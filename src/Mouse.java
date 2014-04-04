import java.awt.*;
import java.awt.event.*;


public class Mouse implements MouseListener , MouseMotionListener , MouseWheelListener
{
	public static Mouse me;
	public int x = 0;
	public int y = 0;
	public boolean Lpress = false;
	public boolean Rpress = false;
	public boolean hover = true;
	public int wheel = 0;

	public Mouse()
	{
		me = this;
	}

	
	public void mouseClicked(MouseEvent e) 
	{
		//fill later
	}

	public void mousePressed(MouseEvent e) {
		if(e.getButton()==1)
			Lpress = true;
		if(e.getButton()==2)
			wheel = 0;
		if(e.getButton()==3)
			Rpress = true;
		
		
		Pan.me.click(x, y);
	}

	public void mouseReleased(MouseEvent e) {
		if(e.getButton()==1)
			Lpress = false;
		if(e.getButton()==3)
			Rpress = false;

	}

	public void mouseEntered(MouseEvent e) {
		hover = true;
		
	}

	public void mouseExited(MouseEvent e) {
		hover = false;
		
	}


	public void mouseDragged(MouseEvent e) {
		x = e.getX();
		y = e.getY();

	}


	public void mouseMoved(MouseEvent e) {
		x = e.getX();
		y = e.getY();

	}
	

	public void mouseWheelMoved(MouseWheelEvent e) {
		wheel -= e.getWheelRotation();
	}
	
	
	
}
