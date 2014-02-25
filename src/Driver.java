import javax.swing.JFrame;


public class Driver {
	public static Pan currentPan;
	public static JFrame currentFrame;
	
	public static void main(String[] args) 
	{
		System.out.println("+----------+ MADUSA +----------+");
		intalize();// runs all the start up stuff
		
		while(true)
		{
			try {
				currentPan.repaint();
				Thread.sleep(30);
			} 
			catch (InterruptedException e) 
			{
				break;
			}
			
		}
	}
	
	
	public static void intalize()
	{
		System.out.println("starting Frame...");
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
