import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

import javax.swing.*;
import javax.swing.filechooser.*;
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
	JMenuItem saveInteractive;
	JMenuItem openInteractive;
	JTabbedPane tabbedPane;
	
	JButton webOk;
	JButton webCancel;
	JTextField webText;
	JFrame webOpenFrame;
	
	JCheckBoxMenuItem viewRefCheck;
	JCheckBoxMenuItem viewExtendsCheck;
	JCheckBoxMenuItem viewImplimentsCheck;
	
	Vector<ArrayList<String>> arrVector = new Vector<ArrayList<String>>();
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
		tabbedPane.add(tf);
		tabbedPane.setTabComponentAt(tabbedPane.getTabCount()-1, createTabPanel(tabbedPane, tf, "diag"+diagCount));
		
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
		save = new JMenuItem("Save Image");
		openLocal = new JMenuItem("Add");
		openOnline = new JMenuItem("Add web zip");
		saveInteractive = new JMenuItem("Save Diagram");
		openInteractive = new JMenuItem("Open Diagram");
		
		menu.add(newMenuItem);
		menu.add(save);
		menu.add(openLocal);
		menu.add(openOnline);
		menu.add(new JSeparator());
		menu.add(saveInteractive);
		menu.add(openInteractive);
		
		JMenu viewMenu = new JMenu("View");
		menuBar.add(viewMenu);
		
		viewRefCheck = new JCheckBoxMenuItem("View Reference Connections");
		viewExtendsCheck = new JCheckBoxMenuItem("View Extends Connections");
		viewImplimentsCheck = new JCheckBoxMenuItem("View Implements Connections");
		
		viewRefCheck.setState(true);
		viewExtendsCheck.setState(true);
		viewImplimentsCheck.setState(true);
		
		viewMenu.add(viewRefCheck);
		viewMenu.add(viewExtendsCheck);
		viewMenu.add(viewImplimentsCheck);
		
		save.addActionListener(this);
		openLocal.addActionListener(this);
		openOnline.addActionListener(this);
		newMenuItem.addActionListener(this);
		saveInteractive.addActionListener(this);
		openInteractive.addActionListener(this);
		viewRefCheck.addActionListener(this);
		viewExtendsCheck.addActionListener(this);
		viewImplimentsCheck.addActionListener(this);
		
		return menuBar;
	}
	
	/// Actions listener for the file dialog 
	public void actionPerformed(ActionEvent e) 
	{
		if(e.getSource() == newMenuItem)
		{
			diagCount++;
			tabFrame tf = new tabFrame();
			tabbedPane.add(tf);
			tabbedPane.setTabComponentAt(tabbedPane.getTabCount()-1, createTabPanel(tabbedPane, tf, "diag"+diagCount));
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
				diagCount++;
				tabFrame tf = new tabFrame();
				tabbedPane.add(tf);
				tabbedPane.setTabComponentAt(tabbedPane.getTabCount()-1, createTabPanel(tabbedPane, tf, "diag"+diagCount));
				tb = (tabFrame)tabbedPane.getSelectedComponent();
			}
			arrVector.add(arr);
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
		else if (e.getSource() == saveInteractive)
		{
			int index = tabbedPane.getSelectedIndex();
			ArrayList<String> arr = arrVector.get(index);
			tabFrame tb = (tabFrame)tabbedPane.getSelectedComponent();
			if(tb == null)
			{
				infoBox("Nothing to save.","Error");
				return;
			}
					
			fc = new JFileChooser();
			fc.setSelectedFile(new File("diagram.json"));
			int returnVal = fc.showSaveDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				if(!file.getPath().toLowerCase().endsWith(".json")) {
					file = new File(file.getPath() + ".json");
				}
						
				try {
					JSONArray list = new JSONArray();
					for (String s : arr)
					    list.add(s); 
					FileWriter fw = new FileWriter(file);
					fw.write(list.toJSONString());
					fw.flush();
					fw.close();
				} catch (IOException ex) {
					infoBox("An I/O error has occurred", "Failed");
				} catch (Exception ex) {
					infoBox("An unknown error has occurred", "Failed");
					ex.printStackTrace();
				}
			} else {
				return;
			}
		}
		else if (e.getSource() == openInteractive)
		{
			JFileChooser fc = new JFileChooser();
			fc.setAcceptAllFileFilterUsed(false);
			fc.setFileFilter(new FileNameExtensionFilter(".json", "json"));
			fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			int returnVal = fc.showOpenDialog(this);
			String m_folder_path;

			if (returnVal == JFileChooser.APPROVE_OPTION)  {
				File file = fc.getSelectedFile();
				m_folder_path = file.getPath();
			} else {
				return;
			}
						
			UrlResolver resolv = new UrlResolver(m_folder_path);
			ArrayList<String> arr = new ArrayList<String>();
			
			JSONParser parser = new JSONParser();
			try {
				Object obj = parser.parse(new FileReader("c:\\test.json"));
				JSONArray jsonArr = (JSONArray)obj;
				Iterator<String> iterator = jsonArr.iterator();
				while (iterator.hasNext()) {
					String str = iterator.next();
					arr.add(str);
				}
			} catch (FileNotFoundException ex) {
				infoBox("FileNotFound Exception", "Failed");
			} catch (IOException ex) {
				infoBox("IOException Exception", "Failed");
			} catch (Exception ex) {
				infoBox("Unknown Exception", "Failed");
			}
			
			if(arr == null) 
			{
				infoBox("Unable to open file or directory", "Error");
				return;
			}
						
			tabFrame tb = (tabFrame)tabbedPane.getSelectedComponent();
			if(tb == null) 
			{
				diagCount++;
				tabFrame tf = new tabFrame();
				tabbedPane.add(tf);
				tabbedPane.setTabComponentAt(diagCount-1, createTabPanel(tabbedPane, tf, "diag"+diagCount));
				tb = (tabFrame)tabbedPane.getSelectedComponent();
			}
			arrVector.add(arr);
			tb.pan.diag.addFiles(arr, 0, 0);
		}
		else if (e.getSource() == webOk)
		{
			UrlResolver resolv = new UrlResolver(webText.getText());
			webOpenFrame.setVisible(false);
			ArrayList<String> arr = resolv.resolve();
			if (arr == null) 
			{
				infoBox("Unable to open file or directory", "Error");
				return;
			}
			
			tabFrame tb = (tabFrame)tabbedPane.getSelectedComponent();
			if (tb == null) 
			{
				diagCount ++;
				tabFrame tf = new tabFrame();
				tabbedPane.add(tf);
				tabbedPane.setTabComponentAt(tabbedPane.getTabCount()-1, createTabPanel(tabbedPane, tf, "diag"+diagCount));
				tb = (tabFrame)tabbedPane.getSelectedComponent();
			}
			arrVector.add(arr);
			tb.pan.diag.addFiles(arr, 0, 0);
			//textField.addActionListener(this);
		}
		else if (e.getSource() == webCancel)
		{
			webOpenFrame.setVisible(false);
			//textField.addActionListener(this);
		}
		else if(e.getSource() == viewRefCheck)
		{
			javaRef.enabled[0] = viewRefCheck.getState();
		}
		else if(e.getSource() == viewExtendsCheck)
		{
			javaRef.enabled[1] = viewExtendsCheck.getState();
		}
		else if(e.getSource() == viewImplimentsCheck)
		{
			javaRef.enabled[2] = viewImplimentsCheck.getState();
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
	        	e.printStackTrace();
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
	
	private JPanel createTabPanel(final JTabbedPane tabbedPane, final tabFrame tf, String title) 
	{
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		JLabel label = new JLabel(title);
		JButton button = new JButton(MedusaIcons.xIcon);
		button.setBackground(new Color(0,0,0,0));
		button.setOpaque(false);
		button.setPreferredSize(new Dimension(16, 16));
		button.setBorder(null);

		button.addMouseListener(new MouseAdapter() 
		{
			public void mouseClicked(MouseEvent e) 
			{
				int index = tabbedPane.indexOfComponent(tf);
				arrVector.removeElementAt(index);
				tabbedPane.remove(tf);
			}
		});
		
		panel.add(label);
		panel.add(button);
		return panel;
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

