import java.io.*;
import java.util.ArrayList;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.*;

import javax.swing.*;
import javax.imageio.*;

@SuppressWarnings("serial")
public class UI extends JPanel implements ActionListener 
{
	public static UI me;
	
	static JFrame frame;
	//static private final String newline = "\n";
	//JTextArea adr;
	UrlResolver urlResolver;
	JFileChooser fc;
	String m_folder_path = "";
	//JScrollPane sp;
	JMenuBar menuBar;
	JMenu menu;
	JMenuItem save;
	JMenuItem newMenuItem;
	JMenuItem openLocal;
	JMenuItem openOnline;
	JTabbedPane tabbedPane;
	
	JButton webOk;
	JButton webCancel;
	JTextField webText;
	JFrame webOpenFrame;
	
	int diagCount = 0;
	
	
	public UI() 
	{
		me = this;
		setPreferredSize(new Dimension(500, 500));
		BorderLayout bl = new BorderLayout();
		this.setLayout(bl);
		tabbedPane = new JTabbedPane();
		this.add(tabbedPane,BorderLayout.CENTER);
		
		diagCount ++;
		tabFrame tf = new tabFrame();
		tabbedPane.addTab("diag"+diagCount, tf);
		
		
		
		webOpenFrame = new JFrame();
		webOpenFrame.setLayout(new BorderLayout(10,10));
		webOpenFrame.setTitle("Open online zip");
		webOpenFrame.setResizable(false);
		
		JLabel lable = new JLabel();
		lable.setText("Open online zip file(such as github)");
		webOpenFrame.add(lable,BorderLayout.NORTH);
		
		webText = new JTextField();
		webText.setColumns(30);
		webOpenFrame.add(webText, BorderLayout.WEST);
		
		webOk = new JButton();
		webOk.setText("Ok");
		webOpenFrame.add(webOk, BorderLayout.CENTER);
		webOk.addActionListener(this);
		
		webCancel = new JButton();
		webCancel.setText("Cancel");
		webOpenFrame.add(webCancel, BorderLayout.EAST);
		webCancel.addActionListener(this);
		
		webOpenFrame.pack();
	}
	

	public JMenuBar menu() 
	{
		menuBar = new JMenuBar();
		
		menu = new JMenu("File");
		menuBar.add(menu);
		newMenuItem = new JMenuItem("New");
		save = new JMenuItem("Save");
		openLocal = new JMenuItem("Add");
		openOnline = new JMenuItem("Add web zip");
		
		menu.add(openLocal);
		menu.add(openOnline);
		menu.add(newMenuItem);
		menu.add(save);
		
		save.addActionListener(this);
		openLocal.addActionListener(this);
		openOnline.addActionListener(this);
		newMenuItem.addActionListener(this);
		
		return menuBar;
	}

	
	/// Actions listener for the file dialog 
	public void actionPerformed(ActionEvent e) 
	{
		if(e.getSource() == newMenuItem)
		{
			diagCount ++;
			tabFrame tf = new tabFrame();
			tabbedPane.addTab("diag"+diagCount, tf);
			tabbedPane.setSelectedComponent(tf);
		}
		else if (e.getSource() == openLocal) 
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
			
			UrlResolver resolv = new UrlResolver(m_folder_path);
			ArrayList<String> arr = resolv.resolve();
			if(arr == null) 
			{
				infoBox("Unable to open file or directory", "Error");
				return;
			}
			
			tabFrame tb = (tabFrame)tabbedPane.getSelectedComponent();
			if(tb == null) 
			{
				diagCount ++;
				tabFrame tf = new tabFrame();
				tabbedPane.addTab("diag"+diagCount, tf);
				tb = (tabFrame)tabbedPane.getSelectedComponent();
			}
			tb.pan.diag.addFiles(arr, 0, 0);

		} 
		else if (e.getSource() == save) 
		{
			SaveImage();
			
		}
		else if (e.getSource() == openOnline)
		{
			webText.setText("");
			webOpenFrame.setVisible(true);
			//textField.addActionListener(this);
		}
		else if (e.getSource() == webOk)
		{
			UrlResolver resolv = new UrlResolver(webText.getText());
			webOpenFrame.setVisible(false);
			ArrayList<String> arr = resolv.resolve();
			if(arr == null) 
			{
				infoBox("Unable to open file or directory", "Error");
				return;
			}
			
			tabFrame tb = (tabFrame)tabbedPane.getSelectedComponent();
			if(tb == null) 
			{
				diagCount ++;
				tabFrame tf = new tabFrame();
				tabbedPane.addTab("diag"+diagCount, tf);
				tb = (tabFrame)tabbedPane.getSelectedComponent();
			}
			tb.pan.diag.addFiles(arr, 0, 0);
			//textField.addActionListener(this);
		}
		else if (e.getSource() == webCancel)
		{
			webOpenFrame.setVisible(false);
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

