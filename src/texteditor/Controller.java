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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import views.MyFrame;

public class Controller {

    private final MyFrame frame = new MyFrame();
    private Path path; //the path to the file that holds the text the user is editing
    private JTextField editedTextField;
    private JTextArea textArea;
    private boolean modified; //marks whether the file is modified so that a dialogue can warn user if unsaved changes

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
        modified = false;

        newMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (!shouldContinue()) { //if the user does not want to continue
                    return;
                }
                
                //these next two are neccessary for if the user does not save and then opens a new file
                editedTextField.setText(""); //remove the edited mark
                modified = false; //uncheck the modified tag
                
                //clear the text from the textArea
                textArea.setText("");

                //replace the display name
                fileNameTextField.setText("<NEW FILE>");

                //disable the save menu item (if neccessary)
                saveMenuItem.setEnabled(false);

                //enable the save as menu item (if neccessary)
                saveAsMenuItem.setEnabled(true);

                //enable the text area (if neccessary)
                textArea.setEditable(true); //enable the textArea (if neccessary)

            }
        });

        openMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (!shouldContinue()) { //if the user does not want to continue
                    return;
                }
                
                //these next two are neccessary for if the user does not save and then opens a file
                editedTextField.setText(""); //remove the edited mark
                modified = false; //uncheck the modified tag

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

                        textArea.setEditable(true); //enable the textArea (if neccessary)

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

                modified = false; //the file is no longer modified

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

                    modified = false; //the file is no longer modified

                    saveMenuItem.setEnabled(true); //enable the save menu item (if necessary)

                }

            }
        });

        //add key listenner to text area
        textArea.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

                //first things first, add * to edited area
                if (textArea.isEditable()) { //if the text area is editable
                    editedTextField.setText("*"); //show that edits have been made
                    modified = true; //mark the file as modified
                }

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

        frame.addWindowListener(new WindowAdapter() {
            
            @Override
            public void windowClosing(WindowEvent e) {
                if (!shouldContinue()) { //if the user does not want to close the window without saving
                    return; //stop clising the window
                } else { //if the user does want to close the window
                    super.windowClosing(e); //call the super method to close the window
                }
            }
            
        });

    }

    /**
     * Convenience method that asks a user if they want to continue if they try
     * to 1) open a file without saving 2) create a new file without saving 3)
     * close the program without saving
     *
     * @return true if the process should continue, false if you should return
     * out of the method
     */
    private boolean shouldContinue() {
        if (modified) { //if the file has been modified, but not saved

            int selection = JOptionPane.showConfirmDialog(frame, "OK to overwrite existing file?"); //ask the user if they want to continue

            if (selection != JOptionPane.YES_OPTION) { //if the user did not choose "yes"
                return false; //cancel the operation
            }

            //if the user did choose yes, then we should continue the operation
        }

        //if the file has been saved, then we can just return true
        return true;

    }

    /**
     * Convenience function that saves the text within textArea to the current
     * path (and removes the edited mark)
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
