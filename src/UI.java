import java.io.*;
import java.util.ArrayList;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.filechooser.*;


public class UI extends JPanel implements ActionListener {
 public static Pan currentPan;
 static private final String newline = "\n";
 JTextArea adr;
 JFileChooser fc;
 String m_folder_path = "";
 JScrollPane sp;
 JMenuBar mb;
 JMenu menu;
 JMenuItem mi;
 static JFrame frame;
 
 
 public UI(){
   fc = new JFileChooser();
   fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
   //mi.addActionListener(this);
 }
 
 public JMenuBar menu(){
   mb = new JMenuBar();
   menu = new JMenu("File");
   mb.add(menu);
   mi = new JMenuItem("Open");
   mi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.ALT_MASK));
   menu.add(mi);
   mi.addActionListener(this);
   return mb;
 }
 
 public Container create() {
   JPanel cp = new JPanel(new BorderLayout());
   cp.setOpaque(true);
   adr = new JTextArea(5, 30);
   adr.setEditable(false);
   sp = new JScrollPane(adr);
   cp.add(sp, BorderLayout.CENTER);
   return cp;
   }

 public void actionPerformed(ActionEvent e) {
  if (e.getSource() == mi) {
   int returnVal = fc.showOpenDialog(UI.this);
   if (returnVal == JFileChooser.APPROVE_OPTION) {
    File file = fc.getSelectedFile();
    //adr.append("Browsing: " + file.getPath() + "." + newline);
    m_folder_path = file.getPath();
   }
   adr.setCaretPosition(adr.getDocument().getLength());
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
   //Driver.renderDiagram(diag);
   currentPan.setDiag(diag);
   
   
  }
 }

 private static void showWindow() {
   frame = new JFrame("Medusa");
   frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
   //frame.add(new UI());
   UI ui = new UI();
   frame.setJMenuBar(ui.menu());
   frame.setContentPane(ui.create());
   frame.setSize(450, 260);
   frame.setVisible(true);
   
   
   Pan p = new Pan();
   currentPan = p;
   
   frame.getContentPane().add(p);
   frame.setResizable(false);
   frame.pack();
   
   renderThread t = new renderThread(p);
   t.start();
   
   
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