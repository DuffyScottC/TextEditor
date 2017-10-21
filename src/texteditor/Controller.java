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
    
    JMenuItem openMenuItem = frame.getOpenMenuItem();
    openMenuItem.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            
            chooser.setDialogTitle("Open");
            if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) { //if the user chose a valid file
                
                System.out.println(chooser.getSelectedFile().getAbsolutePath());
                
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
