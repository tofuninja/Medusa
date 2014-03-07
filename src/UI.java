import java.io.*;
import java.util.ArrayList;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.filechooser.*;


public class UI extends JPanel implements ActionListener {
	static private final String newline = "\n";
	JButton browseButton, generateButton;
	JTextArea adr;
	JFileChooser fc;
	String m_folder_path = "";

	public UI() {
		super(new BorderLayout());
		adr = new JTextArea(5, 25);
		adr.setMargin(new Insets(5, 5, 5, 5));
		adr.setEditable(false);
		JScrollPane logScrollPane = new JScrollPane(adr);
		fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		browseButton = new JButton("Browse");
		browseButton.addActionListener(this);
		generateButton = new JButton("Generate");
		generateButton.addActionListener(this);
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(browseButton);
		buttonPanel.add(generateButton);
		add(buttonPanel, BorderLayout.PAGE_START);
		add(logScrollPane, BorderLayout.CENTER);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == browseButton) {
			int returnVal = fc.showOpenDialog(UI.this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				adr.append("Browsing: " + file.getPath() + "." + newline);
				m_folder_path = file.getPath();
			}
			adr.setCaretPosition(adr.getDocument().getLength());
		} else if (e.getSource() == generateButton) {
			
			ArrayList<String> arr = DirCrawler.getFlatJavaFilesList(m_folder_path);
			ArrayList<String> class_list = new ArrayList<String>();
			for(int i = 0; i < arr.size(); i++)
			{
				ArrayList<String> classes = ClassFinder.getClassList(arr.get(i));
				for(int j = 0; j < classes.size(); j ++)
				{
					class_list.add(classes.get(j));
				}
						
			}
			
			Diagram diag = new Diagram(class_list, m_folder_path);
			Driver.renderDiagram(diag);
		}
	}

	private static void showWindow() {
		JFrame frame = new JFrame("Medusa");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(new UI());
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				UIManager.put("swing.boldMetal", Boolean.FALSE);
				showWindow();
			}
		});
	}
}