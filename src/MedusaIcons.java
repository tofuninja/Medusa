import java.awt.Image;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;


public class MedusaIcons 
{

	public static ImageIcon cIcon;
	public static ImageIcon mIcon;
	public static ImageIcon vIcon;
	public static ImageIcon dIcon;
	public static ImageIcon eIcon;
	public static ImageIcon iIcon;
	public static ImageIcon aIcon;
	public static ImageIcon xIcon;
	
	static
	{

		try {
			Image cImg = ImageIO.read(new File("res/class.png"));
			Image mImg = ImageIO.read(new File("res/method.png"));
			Image vImg = ImageIO.read(new File("res/variable.png"));
			Image dImg = ImageIO.read(new File("res/diagram.png"));
			Image eImg = ImageIO.read(new File("res/extends.png"));
			Image iImg = ImageIO.read(new File("res/interface.png"));
			Image aImg = ImageIO.read(new File("res/abstract.png"));
			Image xImg = ImageIO.read(new File("res/close.png"));
			
			cIcon = new ImageIcon(cImg.getScaledInstance(16, 16, Image.SCALE_SMOOTH));
			mIcon = new ImageIcon(mImg.getScaledInstance(16, 16, Image.SCALE_SMOOTH));
			vIcon = new ImageIcon(vImg.getScaledInstance(16, 16, Image.SCALE_SMOOTH));
			dIcon = new ImageIcon(dImg.getScaledInstance(16, 16, Image.SCALE_SMOOTH));
			eIcon = new ImageIcon(eImg.getScaledInstance(16, 16, Image.SCALE_SMOOTH));
			iIcon = new ImageIcon(iImg.getScaledInstance(16, 16, Image.SCALE_SMOOTH));
			aIcon = new ImageIcon(aImg.getScaledInstance(16, 16, Image.SCALE_SMOOTH));
			xIcon = new ImageIcon(xImg.getScaledInstance(16, 16, Image.SCALE_SMOOTH));
			
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
}
