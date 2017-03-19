/**
 * Created by Master-PC on 9.6.2016.
 */
public class FileList {
    private String fullFilePath;
    private String fileName;
    private int Id;

    public FileList(Integer i, String name, String absolutePath) {
        fullFilePath = absolutePath;
        fileName = name;
        Id = i;
    }

    public String getfullFilePath(){
        return fullFilePath;
    }
    public void setFullFilePath(String path){
        fullFilePath = path;
    }
    public String getFileName(){
        return fileName;
    }

    public void setFileName(String fileName) {
        fileName = fileName;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }
}
