package thn.vn.web.miav.shop.services;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class StorageService {
    public static final String uploadDir = System.getProperty("user.dir") + "/uploadDir/";
    public static final String uploadTemp = System.getProperty("user.dir") + "/tempImage/";
    public static final String uploadProductImage = System.getProperty("user.dir") + "/productImage/";
    private final Path rootLocation = (Path) Paths.get(String.valueOf(Paths.get(uploadDir)));
    private final Path rootTemp = (Path) Paths.get(String.valueOf(Paths.get(uploadTemp)));
    private final Path rootProductImage = (Path) Paths.get(String.valueOf(Paths.get(uploadProductImage)));
    public String storeTempImageProduct(MultipartFile file,int userId, String name) {

        String ext = "";
        try {
            String uploadTempUser = System.getProperty("user.dir") + "/tempImage/"+userId+"/";
            Path rootTempUser = (Path) Paths.get(String.valueOf(Paths.get(uploadTempUser)));
            if (!Files.exists(rootTempUser)){
                Files.createDirectory(rootTempUser);
            }
            if (name != null ){
                if (!name.isEmpty()){
                    String[] dot= file.getOriginalFilename().split("\\.");
                    ext = dot[dot.length-1];
                    name = userId+"_"+name+"."+ext;
                }
            } else {
                name = file.getOriginalFilename();
            }
            byte[] bytes = file.getBytes();
            Files.write(rootTempUser.resolve(name), bytes);
            return  name;
        } catch (Exception e) {
            throw new RuntimeException("FAIL!");
        }
    }
    public void moveFile(String path,String toPath){
        if (!path.startsWith("/")){
            path = "/"+path;
        }
        String pathFile = System.getProperty("user.dir") + path;
        String pathTo = System.getProperty("user.dir") + toPath;
        File f = new File(pathFile);
        if (f.renameTo(new File(pathTo))){
            f.delete();
        }

    }
    public Resource loadFileProduct(String filename) {
        if (!filename.startsWith("/")){
            filename = "/"+filename;
        }
        String path = System.getProperty("user.dir")+"/productImage"+filename;
        try {
            Path fullPath = (Path) Paths.get(String.valueOf(Paths.get(path)));
            Resource resource = new UrlResource(fullPath.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("FAIL!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("FAIL!");
        }
    }
    public String store(MultipartFile file, String name) {
        String ext = "";
        try {

            if (name != null ){
                if (!name.isEmpty()){
                    String[] dot= file.getOriginalFilename().split("\\.");
                    ext = dot[dot.length-1];
                    name = name+"."+ext;
                }
            } else {
                name = file.getOriginalFilename();
            }
            byte[] bytes = file.getBytes();
            Files.write(this.rootLocation.resolve(name), bytes);
//            Files.write(file.getInputStream(), this.rootLocation.resolve(name));
            return  name;
        } catch (Exception e) {
            throw new RuntimeException("FAIL!");
        }
    }
    public void readFile(String fileName){
        try {
            Path file = rootLocation.resolve(fileName);

//            byte[] bytes = file.toFile().getBytes();
        } catch (Exception e){

        }

    }
    public Resource loadFile(String filename) {
        try {
            Path file = rootLocation.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("FAIL!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("FAIL!");
        }
    }

    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }
    public void deleteAllFile(String path){
        if (!path.startsWith("/")){
            path = "/"+path;
        }
        String pathBase = System.getProperty("user.dir")+path;
        FileSystemUtils.deleteRecursively(Paths.get(String.valueOf(Paths.get(pathBase))).toFile());
    }
    public void init() {
        try {

            if (!Files.exists(rootLocation)){
                Files.createDirectory(rootLocation);
            }
            if (!Files.exists(rootProductImage)){
                Files.createDirectory(rootProductImage);
            }
            if (!Files.exists(rootTemp)){
                Files.createDirectory(rootTemp);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage!");
        }
    }
}
