/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Scott
 */

package texteditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import views.MyFrame;

public class Controller {

  private final MyFrame frame = new MyFrame();
  
  public Controller() {
    frame.setTitle( getClass().getSimpleName() );
    frame.setLocationRelativeTo(null);
    // you can adjust the size with something like this:
    // frame.setSize(600, 500);
    JFileChooser chooser = new JFileChooser(System.getProperty("user.dir"));
    
    //Get the varius graphical elements
    JMenuItem openMenuItem = frame.getOpenMenuItem();
    JMenuItem saveMenuItem = frame.getSaveMenuItem();
    JMenuItem saveAsMenuItem = frame.getSaveAsMenuItem();
    
    openMenuItem.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            
            //use a file chooser to open the file the user wants
            chooser.setDialogTitle("Open");
            if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) { //if the user chose a valid file
                
                System.out.println(chooser.getSelectedFile().getAbsolutePath());
                
                //If the user opens a file, then the other two options (save and save as) should be enabled
                saveMenuItem.setEnabled(true);
                saveAsMenuItem.setEnabled(true);
                
            }
            
        }
    });
	
    // event handlers
  }

  public static void main(String[] args) {
    Controller app = new Controller();
    app.frame.setVisible(true);
  }
}
