import com.intellij.uiDesigner.core.GridConstraints;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.io.Files.getFileExtension;

/**
 * Created by Master-PC on 5.6.2016.
 */
public class replace_back extends JFrame {
    private JTextField folder_path_txt;
    private JButton search_btn;
    private JList files_list;
    private JPanel rootPanel;
    private JScrollPane scrollpane;
    private JList list1;
    private JButton add_btn;
    private JButton remove_btn;
    private JButton change_btn;
    private JComboBox extensions_cmb;
    private JList list2;
    private JCheckBox include_subloder_chb;
    private String folderPath = "folder";
    private static ArrayList<String> fileList = new ArrayList<>();
    public final ArrayList listaItema = new ArrayList();
    private static final ArrayList<String> fileSubFolderList = new ArrayList<>();

    public replace_back() {
        super("Hello World");

        DefaultListModel model1 = new DefaultListModel();
        DefaultListModel model2 = new DefaultListModel();



        setBackground(Color.WHITE);

        setContentPane(rootPanel);

        search_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc;
                fc = new JFileChooser();
                fc.isDirectorySelectionEnabled();
                //  fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                fc.showOpenDialog(rootPanel);

                folderPath = fc.getCurrentDirectory().toString();
                folder_path_txt.setText(folderPath);
                //  folderPath = folder_path_txt.getText();

                if (!include_subloder_chb.isSelected()){
                    fileList = getFileList(folderPath);
                }
                else {
                    //fileList = getFileSubFolderList(folderPath);
                    getFileSubFolderList(folderPath);
                }

                for(int i = 0; i <  fileList.size(); i++){
                    model1.addElement(fileList.get(i));
                }

                list1.setModel(model1);
                list1.setVisibleRowCount(10);
                list1.revalidate();

            }
        });



        extensions_cmb.setModel(new DefaultComboBoxModel(exeList()));
        folder_path_txt.setText(folderPath);

        list1.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        scrollpane.setViewportView(list1);

        //Add button

        add_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i< list1.getSelectedValuesList().size(); i++){
                    model2.addElement(list1.getSelectedValuesList().get(i));
                }
                list2.setModel(model2);
                list2.revalidate();
            }
        });

        remove_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(list2.getSelectedIndices().length > 0) {
                    int[] selectedIndices = list2.getSelectedIndices();
                    for (int i = selectedIndices.length-1; i >=0; i--) {
                        model2.removeElementAt(selectedIndices[i]);
                    }
                }
            }
        });

        change_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                for (int i = 0; i< model2.size(); i++){
                    listaItema.add(model2.get(i));
                }

                renameFileList(folderPath, extensions_cmb.getSelectedItem().toString(), listaItema);

                model1.removeAllElements();
                model2.removeAllElements();
                list1.removeAll();
                fileList = getFileList(folderPath);
                for(int i = 0; i <  fileList.size(); i++){
                    model1.addElement(fileList.get(i));
                }

                list1.setModel(model1);
                list1.setVisibleRowCount(10);
                list1.revalidate();
            }
        });

        setSize(900, 700);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        setVisible(true);
    }



    private static ArrayList<String> getFileList(String path) {

        ArrayList<String> fileList = new ArrayList<>();
        final FileNameExtensionFilter extensionFilter = new FileNameExtensionFilter("N/A", "html", "php", "txt", "jpg", "jpeg");
        //  final File file = new File("C:\\Users\\Master-PC\\Desktop\\Posao_Brisati\\1");
        final File file = new File(path);
        for (final File child : file.listFiles()) {
            fileList.add(child.getName());
        }

        return fileList;
    }

    private static void getFileSubFolderList(String path) {
        File directory = new File(path);

        // get all the files from a directory
        File[] fList = directory.listFiles();
        for (File file : fList) {
            if (file.isFile()) {
                fileList.add(file.getAbsolutePath());
                fileSubFolderList.add(file.getAbsolutePath());
            } else if (file.isDirectory()) {
                getFileSubFolderList(file.getAbsolutePath());
            }
        }

        // return fileList;
    }
    private static void renameFileList(String path, String extension, ArrayList<Object> listItems) {

        for (int i = 0; i< listItems.size(); i++){
            renameFileExtension(listItems.get(i).toString(), extension);
        }

    }
    private static void renameFileSubFolderList(String path, String extension, ArrayList<Object> listItems) {

        for (int i = 0; i< listItems.size(); i++){
            renameFileExtension(listItems.get(i).toString(), extension);
        }

    }
    public static boolean renameFileExtension
            (String source, String newExtension)
    {
        String target;
        String currentExtension = getFileExtension(source);

        if (currentExtension.equals("")){
            target = source + "." + newExtension;
        }
        else {
            target = source.replaceFirst(Pattern.quote("." +
                    currentExtension) + "$", Matcher.quoteReplacement("." + newExtension));

        }
        return new File(source).renameTo(new File(target));
    }
    private String[] exeList(){
        String[] eList = {"Choose Extension", "html", "php", "pdf", "jpg"};
        return eList;
    }
    private ArrayList<String> getSelectedValues(){
        //listaItema.add(list1.getSelectedValuesList());
        for (Object s : list1.getSelectedValuesList()){
            listaItema.add(s);
        }
        return listaItema;
    }



    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

}
