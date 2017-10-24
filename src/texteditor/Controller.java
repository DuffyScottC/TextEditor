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

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import views.MyFrame;

public class Controller {

    private final MyFrame frame = new MyFrame();
    private Path path; //the path to the file that holds the text the user is editing
    private JTextField editedTextField;
    private JTextArea textArea;

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
        JMenuItem newMenuItem = frame.getNewMenuItem();
        JTextField fileNameTextField = frame.getFileNameTextField();
        textArea = frame.getTextArea(); //instantiate the class member textArea
        editedTextField = frame.getEditedTextField(); //instantiate the class member editedTextField
        editedTextField.setMinimumSize(new Dimension(500, editedTextField.getHeight()));
        
        newMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                //clear the text from the textArea
                textArea.setText("");
                
                //replace the display name
                fileNameTextField.setText("<NEW FILE>");
                
                //disable the save menu item
                saveMenuItem.setEnabled(false);
                
                //enable the text area (if neccessary)
                enableTextArea();
                
            }
        });
        
        openMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //use a file chooser to open the file the user wants
                chooser.setDialogTitle("Open");
                if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) { //if the user chose a valid file

                    try {
                        //Create a path object from chooser
                        path = chooser.getSelectedFile().toPath();
                        System.out.println("Openned file from path: " + path); //print the path for debugging purposes
                        //create a string that contains the file body
                        String fileBody = new String(Files.readAllBytes(path));

                        textArea.setText(fileBody); //set the body of the text area to the body of the file

                        //If the user opens a file, then the other two options (save and save as) should be enabled
                        saveMenuItem.setEnabled(true);
                        saveAsMenuItem.setEnabled(true);

                        enableTextArea();
                        
                        //put the file name into the fileNameTextField
                        
                        String workingDirStr = System.getProperty("user.dir"); //get the working directory
                        Path workingDir = Paths.get(workingDirStr); //convert the working directory to a path object
                        Path relativePath = workingDir.relativize(path); //get the relative path of the file from the working directory
                        fileNameTextField.setText(relativePath.toString());

                    } catch (IOException ex) { //catch any potential errors

                        System.out.println("Unable to read selected file");
                        System.err.println(ex);

                    }

                }

            }
        });

        //save the text to the file the user is editing
        saveMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                saveFile(); //save the file

            }
        });

        saveAsMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                chooser.setDialogTitle("Save");
                int chooserResult = chooser.showSaveDialog(frame); //open the save dialogue and let the user choose where to save the file
                if (chooserResult == JFileChooser.APPROVE_OPTION) { //if the user successfully saved the file

                    path = chooser.getSelectedFile().toPath(); //get the new path to the file save location
                    
                    //check if the file has an extension already
                    String fileName = path.getFileName().toString(); //the name of the file, including the extension
                    if (!fileName.matches(".*\\.\\w+")) { //if the file name does NOT have an extension, then we need to add a .txt
                        
                        //add .txt
                        String fileNameWithExtension = path.getFileName() + ".txt"; //append the .txt extension to the file name
                        //use the resolveSibling method to change the old, extensionless file name to the new filename created above
                        path = path.resolveSibling(fileNameWithExtension); //e.g. this will replace "curdir/sample2" with "curdir/sample2.txt"
                        
                    }
                    
                    //put the file name into the fileNameTextField
                    fileNameTextField.setText(path.getFileName().toString());
                    
                    saveFile(); //save the file
                    
                }

            }
        });

        //add key listenner to text area
        textArea.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

                //first things first, add * to edited area
                editedTextField.setText("*");

            }

            @Override
            public void keyPressed(KeyEvent e) {
                //System.out.println("keyPressed");
            }

            @Override
            public void keyReleased(KeyEvent e) {
                //System.out.println("keyReleased");
            }
        });

        // event handlers
    }
    
    private void enableTextArea() {
        
        if (!textArea.isEnabled()) { //if the textArea is NOT enabled
            
            textArea.setEditable(true); //enable the textArea
            
        } //if it's already enabled, then we don't need to do anything
        
    }
    
    /**
     * Convenience function that saves the text within textArea to the current path (and removes the edited mark)
     */
    private void saveFile() {
        try {
            
            String textAreaBody = textArea.getText(); //get the text stored within the body of the text area
            
            Files.write(path, textAreaBody.getBytes()); //save the body of the text area to the path last selected by the user
            
            System.out.println("file saved to: " + path);
            
            editedTextField.setText(""); //remove the * from the edited text field
            
        } catch (IOException ex) {

            System.out.println("Unable to save file");
            System.err.println(ex);

        }
    }
    
    public static void main(String[] args) {
        Controller app = new Controller();
        app.frame.setVisible(true);
    }
}
