package examples.jsf;

import java.io.Serializable;

import org.apache.myfaces.custom.fileupload.UploadedFile;

/**
 * @author yone
 */
public class TomahawkBean implements Serializable {
    //public static final String uploadedFile_BINDING = "bindingType=none";

    private static final long serialVersionUID = 1L;

    private UploadedFile uploadedFile;
    
    public UploadedFile getUploadedFile() {
        return this.uploadedFile;
    }
    
    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
        System.out.println("#####uploadFileSize["+uploadedFile.getSize()+"]");
    }
}