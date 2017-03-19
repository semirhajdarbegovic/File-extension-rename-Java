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
public class ReplaceForm extends JFrame {
    private JTextField folder_path_txt;
    private JButton search_btn;
    private JPanel rootPanel;
    private JScrollPane scrollpane;
    private JList list1;
    private JButton add_btn;
    private JButton remove_btn;
    private JButton change_btn;
    private JComboBox extensions_cmb;
    private JList list2;
    private JCheckBox include_subloder_chb;
    private JButton refresh_btn;
    private JButton add_all_btn;
    private JButton remove_all;
    public final ArrayList listaItema = new ArrayList();
    private DefaultListModel model1 = new DefaultListModel();
    private DefaultListModel model2 = new DefaultListModel();


    public ReplaceForm() {

        setBackground(Color.WHITE);
        setContentPane(rootPanel);

        search_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc;
                fc = new JFileChooser();
                if (Util.currentPath.length() > 0){
                    File f = new File(Util.currentPath);
                    fc.setCurrentDirectory(f);
                }
                fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                fc.isDirectorySelectionEnabled();
                //  fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                fc.showOpenDialog(rootPanel);

                Util.currentPath = fc.getSelectedFile().getAbsolutePath().toString();
                folder_path_txt.setText(Util.currentPath);
                //  folderPath = folder_path_txt.getText();

                if (!include_subloder_chb.isSelected()){
                    getFileList();
                }
                else {
                    if (Util.fileLists.size() > 0){
                        Util.fileLists.clear();
                    }
                    getFileSubFolderList(Util.currentPath);
                }

                if (!model1.isEmpty())
                    model1.clear();

                for(int i = 0; i <  Util.fileLists.size(); i++){
                    model1.addElement(Util.fileLists.get(i).getFileName());
                }

                list1.setModel(model1);
                list1.setVisibleRowCount(10);
                list1.revalidate();

            }
        });
        refresh_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Util.currentPath.length() > 0){
                    refresh_list();
                }
            }
        });

        extensions_cmb.setModel(new DefaultComboBoxModel(exeList()));
        folder_path_txt.setText(Util.currentPath);

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
                    for(int j = 0; j < Util.fileLists.size(); j++) {
                        if (model2.get(i) == Util.fileLists.get(j).getFileName()) {
                            listaItema.add(Util.fileLists.get(j).getfullFilePath());
                            break;
                        }
                    }
                }

                renameFileList(Util.currentPath, extensions_cmb.getSelectedItem().toString(), listaItema);

                refresh_list();
            }
        });

        add_all_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addAllListItems();
            }
        });

        remove_all.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeAllList2Items();
            }
        });

        setSize(900, 600);
        setResizable(false);
        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        setVisible(true);
    }

    public void refresh_list() {
        model1.removeAllElements();
        model2.removeAllElements();
        list1.removeAll();
        if (!include_subloder_chb.isSelected()){
            getFileList();
        }
        else {
            if (Util.fileLists.size() > 0){
                Util.fileLists.clear();
            }
            getFileSubFolderList(Util.currentPath);
        }
        for(int i = 0; i <  Util.fileLists.size(); i++){
            model1.addElement(Util.fileLists.get(i).getFileName());
        }

        list1.setModel(model1);
        list1.setVisibleRowCount(10);
        list1.revalidate();
    }

    public void addAllListItems(){
        model2.removeAllElements();
        for(int i = 0; i <  Util.fileLists.size(); i++){
            model2.addElement(Util.fileLists.get(i).getFileName());
        }
        list1.revalidate();
        list2.setModel(model2);
        list2.revalidate();
    }
    public void removeAllList2Items(){
        model2.removeAllElements();
        list2.removeAll();
        list2.revalidate();
    }

    private static void getFileList() {
        int i = 0;
        if (Util.fileLists.size() > 0){
            Util.fileLists.clear();
        }
        final FileNameExtensionFilter extensionFilter = new FileNameExtensionFilter("N/A", "html", "php", "txt", "jpg", "jpeg");
        final File file = new File(Util.currentPath);
        for (final File child : file.listFiles()) {
            if(!child.getAbsolutePath().isEmpty()){
                Util.fileLists.add(new FileList(i, child.getName(), child.getAbsolutePath()));
                i++;
            }

        }
    }
    private static void getFileSubFolderList(String path) {
        File directory = new File(path);
        int i = 0;
        // get all the files from a directory
        File[] fList = directory.listFiles();
        for (File file : fList) {
            if (file.isFile()) {
                Util.fileLists.add(new FileList(i, file.getName(), file.getAbsolutePath()));

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
        String[] eList = {"Choose Extension", "html", "php", "pdf", "jpg", "jpeg", "png", "gif", "psd", "mp4", "wmv", "mac"};
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
