import javax.swing.JFrame;


public class Driver {
	public static Pan currentPan;
	public static JFrame currentFrame;
	
	public static void renderDiagram(Diagram d)
	{
		//Frame
		JFrame f =  new JFrame("Madusa");
		currentFrame = f;
		//pan
		Pan p = new Pan(d);
		currentPan = p;
		
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.getContentPane().add(p);
		f.setResizable(false);
		f.pack();
		f.setVisible(true);
		
		renderThread t = new renderThread(p);
		t.start();
	}

}

class renderThread extends Thread {
	Pan currentPan;
	public renderThread(Pan p) {
        currentPan = p;
    }

    public void run() 
    {
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
}
