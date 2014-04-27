import java.io.*;
import java.util.ArrayList;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.TextField;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.imageio.*;

public class UI extends JPanel implements ActionListener 
{
	static JFrame frame;
	//static private final String newline = "\n";
	//JTextArea adr;
	UrlResolver urlResolver;
	JFileChooser fc;
	String m_folder_path = "";
	//JScrollPane sp;
	JMenuBar menuBar;
	JMenu menu;
	JMenu subMenu;
	JMenuItem save;
	JMenuItem openLocal;
	JMenuItem openOnline;
	JTabbedPane tabbedPane;
	
	
	public UI() 
	{

		setPreferredSize(new Dimension(500, 500));
		BorderLayout bl = new BorderLayout();
		this.setLayout(bl);
		tabbedPane = new JTabbedPane();
		this.add(tabbedPane,BorderLayout.CENTER);
	}
	

	public JMenuBar menu() 
	{
		menuBar = new JMenuBar();
		
		
		menu = new JMenu("File");
		menuBar.add(menu);
		
		save = new JMenuItem("Save");
		subMenu = new JMenu("Open");
		menu.add(save);
		menu.add(subMenu);
		save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1,ActionEvent.ALT_MASK));
		save.addActionListener(this);
		
		openLocal = new JMenuItem("Open Local Resource");
		openOnline = new JMenuItem("Open Online Resource");
		subMenu.add(openLocal);
		subMenu.add(openOnline);
		openLocal.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1,ActionEvent.ALT_MASK));
		openOnline.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1,ActionEvent.ALT_MASK));
		openLocal.addActionListener(this);
		openOnline.addActionListener(this);
		
		return menuBar;
	}

	
	/// Actions listener for the file dialog 
	public void actionPerformed(ActionEvent e) 
	{
		if (e.getSource() == openLocal) 
		{
			JFileChooser fc = new JFileChooser();
			fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			int returnVal = fc.showOpenDialog(this);
			
			String m_folder_path;

			if (returnVal == JFileChooser.APPROVE_OPTION) 
			{
				File file = fc.getSelectedFile();
				// adr.append("Browsing: " + file.getPath() + "." + newline);
				m_folder_path = file.getPath();
			} 
			else 
			{
				// Did not openLocal a file
				// Return and do nothing
				return;
			}
			
			tabFrame tf = new tabFrame(m_folder_path);
			tabbedPane.addTab(m_folder_path, tf);

		} 
		else if (e.getSource() == save) 
		{
			SaveImage();
			
		}
		else if (e.getSource() == openOnline)
		{
			JFrame JF = new JFrame();
			JF.setLayout(new BorderLayout(10,10));
			JF.setTitle("Open Online Resource");
			JF.setResizable(false);
			
			JLabel lable = new JLabel();
			lable.setText("Paste link of Online zip file in the box");
			JF.add(lable,BorderLayout.NORTH);
			
			JTextField textField = new JTextField();
			textField.setColumns(30);
			JF.add(textField, BorderLayout.WEST);
			
			JButton jb = new JButton();
			jb.setText("Ok");
			JF.add(jb, BorderLayout.CENTER);
			
			JButton jb2 = new JButton();
			jb2.setText("Cancel");
			JF.add(jb2, BorderLayout.EAST);
			
			JF.pack();
			JF.setVisible(true);
			
			
			//textField.addActionListener(this);
		}
	}
	
	
	
	private  void SaveImage()
	{
		tabFrame tb = (tabFrame)tabbedPane.getSelectedComponent();
		if(tb == null)
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
				ImageIO.write(tb.pan.renderToImage(), "png",file);
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
				
				
				frame = new JFrame("Medusa");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				// frame.add(new UI());
				frame.setLayout(new BorderLayout());
				
				UI ui = new UI();
				frame.setJMenuBar(ui.menu());
				//frame.setContentPane(ui.create());
				frame.setSize(500, 500);
				frame.setVisible(true);

				frame.add(ui, BorderLayout.CENTER);
				
				//frame.setResizable(false);
				frame.pack();
				
			}
		});
	}
}

