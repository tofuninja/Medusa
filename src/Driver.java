import javax.swing.JFrame;


public class Driver {
	public static Pan currentPan;
	public static JFrame currentFrame;
	
	public static void main(String[] args) 
	{
		intalize();// runs all the start up stuff
		
		while(true)
		{
			try {
				currentPan.repaint();
				Thread.sleep(10);
			} 
			catch (InterruptedException e) 
			{
				break;
			}
			
		}
	}
	
	
	public static void intalize()
	{
		//Frame
		JFrame f =  new JFrame("Madusa");
		currentFrame = f;
		//pan
		Pan p = new Pan();
		currentPan = p;
		
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.getContentPane().add(p);
		f.setResizable(false);
		f.pack();
		f.setVisible(true);
	}

}
