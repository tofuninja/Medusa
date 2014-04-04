import java.io.*;
import java.util.ArrayList;
import java.awt.event.*;


import javax.swing.*;
import javax.imageio.*;

public class UI extends JPanel implements ActionListener 
{
	public static Pan currentPan;
	static JFrame frame;
	//static private final String newline = "\n";
	//JTextArea adr;
	JFileChooser fc;
	String m_folder_path = "";
	//JScrollPane sp;
	JMenuBar mb;
	JMenu menu;
	JMenuItem mi;
	JMenuItem save;
	
	public UI() 
	{
		
		// mi.addActionListener(this);
	}

	public JMenuBar menu() 
	{
		mb = new JMenuBar();
		menu = new JMenu("File");
		mb.add(menu);
		mi = new JMenuItem("Open");

		save = new JMenuItem("save");
		save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1,ActionEvent.ALT_MASK));
		mi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1,ActionEvent.ALT_MASK));
		menu.add(save);
		menu.add(mi);
		save.addActionListener(this);
		mi.addActionListener(this);
		return mb;
	}

	/*
	public Container create() 
	{
		JPanel cp = new JPanel(new BorderLayout());
		cp.setOpaque(true);
		adr = new JTextArea(5, 30);
		adr.setEditable(false);
		sp = new JScrollPane(adr);
		cp.add(sp, BorderLayout.CENTER);
		return cp;
	}*/

	
	/// Actions listener for the file dialog 
	public void actionPerformed(ActionEvent e) 
	{
		if (e.getSource() == mi) 
		{
			fc = new JFileChooser();
			fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int returnVal = fc.showOpenDialog(UI.this);

			if (returnVal == JFileChooser.APPROVE_OPTION) 
			{
				File file = fc.getSelectedFile();
				// adr.append("Browsing: " + file.getPath() + "." + newline);
				m_folder_path = file.getPath();
			} 
			else 
			{
				// Did not open a file
				// Return and do nothing
				return;
			}

			// adr.setCaretPosition(adr.getDocument().getLength());

			ArrayList<String> arr = DirCrawler
					.getFlatJavaFilesList(m_folder_path);
			ArrayList<JavaClass> class_list = new ArrayList<JavaClass>();

			for (int i = 0; i < arr.size(); i++) 
			{
				ArrayList<JavaClass> classes;
				try 
				{
					classes = FileDetails.getClasses(arr.get(i));
				} 
				catch (Exception e1) 
				{
					continue;// error in file
				}

				for (int j = 0; j < classes.size(); j++) 
				{
					class_list.add(classes.get(j));
				}
			}

			Diagram diag = new Diagram(class_list, m_folder_path);
			currentPan.setDiag(diag);

		} 
		else if (e.getSource() == save) 
		{
			SaveImage();
			
		}
	}

	private static void showWindow() 
	{
		frame = new JFrame("Medusa");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// frame.add(new UI());
		UI ui = new UI();
		frame.setJMenuBar(ui.menu());
		//frame.setContentPane(ui.create());
		//frame.setSize(450, 260);
		frame.setVisible(true);

		Pan p = new Pan();
		currentPan = p;

		frame.getContentPane().add(p);
		//frame.setResizable(false);
		frame.pack();

		renderThread t = new renderThread(p);
		t.start();

	}
	
	private  void SaveImage()
	{
		if(Pan.me.diag == null)
		{
			infoBox("Nothing to save.","Error");
			return;
		}
		
		fc = new JFileChooser();
		fc.setSelectedFile(new File("diagram.png"));
		int returnVal = fc.showSaveDialog(this);
		
		if (returnVal == JFileChooser.APPROVE_OPTION) 
		{
			File file = fc.getSelectedFile();
			if(!file.getPath().toLowerCase().endsWith(".png"))
			{
			    file = new File(file.getPath() + ".png");
			}
			
			try 
			{
				ImageIO.write(Pan.me.renderToImage(), "png",file);
			}
			catch (IOException e) 
			{
	        	
	        }
			catch(OutOfMemoryError e)
			{
				infoBox("Diagram too large","Error");
			}
		} 
		else 
		{
			// Did not open a file
			// Return and do nothing
			return;
		}
		
		
	}
	



	public static void infoBox(String infoMessage, String title)
    {
        JOptionPane.showMessageDialog(null, infoMessage, title, JOptionPane.INFORMATION_MESSAGE);
    }
	

	public static void main(String[] args) 
	{
		SwingUtilities.invokeLater(new Runnable() 
		{
			public void run() 
			{
				UIManager.put("swing.boldMetal", Boolean.FALSE);
				showWindow();
			}
		});
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
			catch (InterruptedException e) 
			{
				break;
			}

		}
	}
}