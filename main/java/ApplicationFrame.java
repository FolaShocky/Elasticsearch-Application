import Utility.ApplicationLogic;
import Utility.SearchOptionsDialog;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.*;
public class ApplicationFrame extends JFrame {

    //C:\\Users\\folas\\IdeaProjects\\IRAssignmentProject\\items.jsonl

    //The sole instance of this class
    private static ApplicationFrame applicationFrameInstance;
    //A constant representing the dimensions of the screen
    private static final Dimension SCREEN_DIMENSION = Toolkit.getDefaultToolkit().getScreenSize();
    /*A JButton used to display a dialog for loading a file's contents into ElasticSearch
    * The results of this operation are printed in the console*/
    private JButton jButtonLoadFile;
    /*A JButton used to display a dialog for retrieving data from ElasticSearch
    * The results of this operation are printed in the console*/
    private JButton jButtonRetrieveDocuments;

    //A container for holding several JComponent widgets
    private JPanel jPanelButtons;

    //Constructor
    private ApplicationFrame(String title){
        super(title);
        setLayout(new BorderLayout());
        setSize(SCREEN_DIMENSION);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initProps();
        initListeners();
        jPanelButtons.add(jButtonLoadFile);
        jPanelButtons.add(jButtonRetrieveDocuments);
        add(jPanelButtons,BorderLayout.CENTER);
    }

    //A method for initialising objects and their respective attributes
    private void initProps(){
        jButtonLoadFile = new JButton("Load File into ElasticSearch");
        jButtonLoadFile.setToolTipText("Load file into ElasticSearch");
        jButtonRetrieveDocuments = new JButton("Retrieve Documents from ElasticSearch");
        jButtonRetrieveDocuments.setToolTipText("Retrieve Documents from ElasticSearch");
        jPanelButtons = new JPanel(new GridLayout(2,1));
    }

    /*Used to make compliance with singleton design pattern possible.
    * Because the this method provides the sole entry point for accessing the contents of this class
    * from a different file without a NullPointerException being thrown
    * */
    public static ApplicationFrame getInstance(){
        if(applicationFrameInstance == null)
            applicationFrameInstance = new ApplicationFrame("CE306 Assignment 2");
        return applicationFrameInstance;
    }


    /*Provides code determining the events
     executed upon pressing a button
    * */
    private void initListeners(){
        jButtonLoadFile.addActionListener(listener ->{
            try{
                String pathAsString =JOptionPane.showInputDialog(this,"Please enter the path of the file").trim();
                SwingUtilities.invokeLater(()->{
                    try{
                        if(!pathAsString.equals("")) {
                            Path chosenPath = Paths.get(pathAsString).toRealPath();
                            System.out.println(ApplicationLogic.insertArticleDocument(chosenPath.toString()));
                        }
                    }
                    catch (IOException ex){
                        System.out.println("An exception occurred: " + ex.getMessage());
                        ex.printStackTrace();
                    }
                });
            }
            catch (NullPointerException ex){
                System.out.println("A null pointer exception occurred: " + ex.getMessage());
            }
        });
        jButtonRetrieveDocuments.addActionListener(listener -> {
            SearchOptionsDialog searchOptionsDialog = SearchOptionsDialog.getInstance();
            searchOptionsDialog.setVisible(true);
        });
    }

    /* The entry point for this application
     * */
    public static void main(String[] args){
        ApplicationFrame applicationFrame = ApplicationFrame.getInstance();
        applicationFrame.setVisible(true);
    }
}
