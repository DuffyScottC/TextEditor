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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JTextArea;
import views.MyFrame;

public class Controller {

    private final MyFrame frame = new MyFrame();
    private File file; //the file that holds the text the user is editing

    public Controller() {
        frame.setTitle(getClass().getSimpleName());
        frame.setLocationRelativeTo(null);
        // you can adjust the size with something like this:
        // frame.setSize(600, 500);
        JFileChooser chooser = new JFileChooser(System.getProperty("user.dir"));

        //Get the varius graphical elements
        JMenuItem openMenuItem = frame.getOpenMenuItem();
        JMenuItem saveMenuItem = frame.getSaveMenuItem();
        JMenuItem saveAsMenuItem = frame.getSaveAsMenuItem();
        JTextArea textArea = frame.getTextArea();

        openMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //use a file chooser to open the file the user wants
                chooser.setDialogTitle("Open");
                if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) { //if the user chose a valid file
                    
                    try {
                        //Create a path object from chooser
                        Path path = chooser.getSelectedFile().toPath();
                        System.out.println(path); //print the path for debugging purposes
                        //create a string that contains the file body
                        String fileBody = new String(Files.readAllBytes(path));
                        
                        textArea.setText(fileBody); //set the body of the text area to the body of the file
                        
                        //If the user opens a file, then the other two options (save and save as) should be enabled
                        saveMenuItem.setEnabled(true);
                        saveAsMenuItem.setEnabled(true);
                        
                        //Also enable the text area for editing
                        textArea.setEditable(true);
                        
                    } catch (IOException ex) { //catch any potential errors
                        
                        System.out.println("Unable to read selected file");
                        System.err.println(ex);
                        
                    }

                }

            }
        });
        
        //add key listenner to text area
        textArea.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void keyPressed(KeyEvent e) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void keyReleased(KeyEvent e) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });

        // event handlers
    }

    public static void main(String[] args) {
        Controller app = new Controller();
        app.frame.setVisible(true);
    }
}
