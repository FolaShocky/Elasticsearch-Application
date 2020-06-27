package Utility;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.*;
import java.util.List;
import java.util.stream.*;
public class SearchOptionsDialog extends JDialog {
    private static final Dimension SCREEN_DIMENSION = Toolkit.getDefaultToolkit().getScreenSize();
    private JPanel jPanelId;//Has 2 components per row: A label and a JTextField
    private JPanel jPanelContent;
    private JPanel jPanelMediaType;
    private JPanel jPanelSource;
    private JPanel jPanelButton;
    private JPanel jPanelCentre;
    private JTextField jTextFieldId;
    private JTextField jTextFieldContent;
    private JTextField jTextFieldMediaType;
    private JTextField jTextFieldSource;
    private List<JTextField>jTextFieldList;

    private JButton jButtonSearch;
    private JButton jButtonClear;
    private static SearchOptionsDialog searchOptionsDialogInstance;
    private JTextField[] jTextFieldArr;

    /*Used to make compliance with singleton design pattern possible.
     * Because the this method provides the sole entry point for accessing the contents of this class
     * from a different file without a NullPointerException being thrown
     * */
    public static SearchOptionsDialog getInstance(){
        if(searchOptionsDialogInstance == null){
            searchOptionsDialogInstance = new SearchOptionsDialog();
        }
        return searchOptionsDialogInstance;
    }

    //Constructor
    private SearchOptionsDialog(){
        setTitle("Search for Documents");
        setSize(SCREEN_DIMENSION.width/2,SCREEN_DIMENSION.height/2);
        setLayout(new BorderLayout());
        jTextFieldList = new ArrayList<>();

        jButtonSearch = new JButton("Initiate Search");

        jButtonClear = new JButton("Clear fields");

        jTextFieldId = new JTextField();

        jTextFieldContent = new JTextField();

        jTextFieldMediaType = new JTextField();

        jTextFieldSource = new JTextField();

        jPanelId = new JPanel(new GridLayout(1,2));

        jPanelContent = new JPanel(new GridLayout(1,2));

        jPanelMediaType = new JPanel(new GridLayout(1,2));

        jPanelSource = new JPanel(new GridLayout(1,2));

        jPanelCentre = new JPanel(new GridLayout(4,1));

        jPanelButton = new JPanel(new GridLayout(2,1));

        jPanelButton.add(jButtonClear);
        jPanelButton.add(jButtonSearch);
        Arrays.asList(new JLabel("Id:"),jTextFieldId).forEach(jPanelId::add);
        Arrays.asList(new JLabel("Content:"),jTextFieldContent).forEach(jPanelContent::add);
        Arrays.asList(new JLabel("Media Type:"),jTextFieldMediaType).forEach(jPanelMediaType::add);
        Arrays.asList(new JLabel("Source:"),jTextFieldSource).forEach(jPanelSource::add);
        jPanelCentre.add(jPanelId);
        jPanelCentre.add(jPanelContent);
        jPanelCentre.add(jPanelMediaType);
        jPanelCentre.add(jPanelSource);
        add(jPanelCentre,BorderLayout.CENTER);
        add(jPanelButton,BorderLayout.SOUTH);
        revalidate();
        repaint();
        jTextFieldArr = new JTextField[]{ jTextFieldId,jTextFieldContent,jTextFieldMediaType,jTextFieldSource};
        initListeners();
    }

    /*Provides code determining the events
     executed upon pressing a button
    * */
    private void initListeners(){
        jButtonSearch.addActionListener(event ->{
            SwingUtilities.invokeLater(()->{
                try{
                    DateTimeFormatterBuilder dateTimeFormatterBuilder = new DateTimeFormatterBuilder();
                    System.out.println(
                            ApplicationLogic.retrieveDocumentsByField(
                            new String[]{
                                ArticleDocument.ID_FIELD,
                                ArticleDocument.CONTENT_FIELD,
                                ArticleDocument.MEDIA_TYPE_FIELD,
                                ArticleDocument.SOURCE_FIELD
                            },
                            new String[]{
                                jTextFieldId.getText().trim(),
                                jTextFieldContent.getText().trim(),
                                jTextFieldMediaType.getText().trim(),
                                jTextFieldSource.getText().trim()
                            }));
                }
                catch(Exception ex) {
                    System.out.println("An exception occurred: " + ex.getMessage());
                    ex.printStackTrace();
                }
            });
        });
        jButtonClear.addActionListener(event ->
            Arrays.asList(jTextFieldId,jTextFieldContent,jTextFieldSource,jTextFieldMediaType)
                    .forEach(jTextField -> jTextField.setText("")));
    }
}
