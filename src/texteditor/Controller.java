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
import java.io.File;
import java.io.FileReader;
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
                        file = path.toFile(); //instantiate the file object
                        
                        FileReader fileReader = new FileReader(file); //create a file reader from the file the user selected
                        char[] characters = new char[(int)file.length()]; //create an array of characters to hold all the characters in the file
                        fileReader.read(characters); //use the file reader to put all the characters from the file into the characters array
                        fileReader.close(); //close the file reader, since we don't need it anymore
                        
                        //create a string that contains the file body from the characters input from the file
                        String fileBody = new String(characters);
                        
                        textArea.setText(fileBody); //set the body of the text area to the body of the file
                        
                        //If the user opens a file, then the other two options (save and save as) should be enabled
                        saveMenuItem.setEnabled(true);
                        saveAsMenuItem.setEnabled(true);
                        
                    } catch (IOException ex) { //catch any potential errors
                        
                        System.out.println("Unable to read selected file");
                        System.err.println(ex);
                        
                    }

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
