package thn.vn.web.miav.shop.models.response;

public class ImageUploadResponse {
    public ImageUploadResponse(String fileName){
        this.fileName = fileName;
    }
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

   private String fileName;
}
